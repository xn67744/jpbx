/* JPBX 悬浮软电话 —— 基于 sip.js 0.15，配置保存在浏览器 localStorage */
(function ($) {
    'use strict';

    var CFG_KEY = 'jpbx.sip.config.v1';
    var POS_KEY = 'jpbx.sip.pos.v1';
    var widget, ua, session, pendingSession, timer, startAt;
    var muted = false;
    var ringTimer = null, audioCtx = null, registeredUri = null;

    function defaultConfig() {
        var host = location.hostname || 'localhost';
        return {
            // 默认走 sofia 的明文 ws-binding（5066），不依赖自签证书；
            // 需要加密时在设置页改成 wss://<FS主机IP>:7443
            wsServer: 'ws://' + host + ':5066',
            uri: '1132@' + host,
            password: '',
            displayName: 'JPBX 1132',
            register: true
        };
    }

    function isLoopback(host) {
        return !host || host === 'localhost' || host === '127.0.0.1' || host === '::1' || host === '[::1]';
    }

    /** 7443 是 TLS 端口，历史配置里的 ws://…:7443 自动纠正为 wss://（5066 保持明文 ws） */
    function normalizeServer(url) {
        if (!url) { return url; }
        if (/^ws:\/\/[^\/]+:7443(\/|$)/i.test(url)) {
            return url.replace(/^ws:/i, 'wss:');
        }
        return url;
    }

    function loadConfig() {
        var cfg = defaultConfig();
        try {
            var saved = JSON.parse(localStorage.getItem(CFG_KEY) || '{}');
            $.extend(cfg, saved);
        } catch (e) { /* 配置损坏时使用默认值 */ }
        cfg.wsServer = normalizeServer(cfg.wsServer);
        return cfg;
    }

    function log(msg) {
        var el = $('#pbxSipLog');
        if (!el.length) { return; }
        var time = new Date().toLocaleTimeString('zh-CN', {hour12: false});
        el.append($('<div>').append($('<span class="t">').text('[' + time + '] ')).append($('<span>').text(msg)));
        var rows = el.children();
        if (rows.length > 200) { rows.slice(0, rows.length - 200).remove(); }
        el.scrollTop(el[0].scrollHeight);
        if (!$('#sipViewLog').is(':visible')) { $('#sipTabLog').addClass('has-new'); }
    }

    function setStatus(text, state) {
        $('#pbxSipDot').attr('class', 'pbx-sip-dot' + (state ? ' ' + state : ''));
        $('#pbxSipStatus').html(text);
    }

    function toggleWidget() {
        if (widget.hasClass('hidden')) {
            widget.removeClass('hidden');
            applyPosition();
        } else {
            widget.addClass('hidden');
        }
    }

    function applyPosition() {
        var pos = null;
        try { pos = JSON.parse(localStorage.getItem(POS_KEY) || 'null'); } catch (e) { pos = null; }
        if (pos) {
            var maxX = Math.max(0, $(window).width() - widget.outerWidth() - 8);
            var maxY = Math.max(0, $(window).height() - widget.outerHeight() - 8);
            widget.css({left: Math.min(Math.max(8, pos.left), maxX), top: Math.min(Math.max(8, pos.top), maxY), right: 'auto'});
        }
    }

    function enableDrag() {
        var head = widget.find('.pbx-sip-head');
        var dragging = false, ox = 0, oy = 0;
        head.on('mousedown', function (e) {
            if ($(e.target).closest('button').length) { return; }
            dragging = true;
            var o = widget.offset();
            ox = e.clientX - o.left;
            oy = e.clientY - o.top;
            widget.css('right', 'auto');
            e.preventDefault();
        });
        $(document).on('mousemove', function (e) {
            if (!dragging) { return; }
            var left = Math.min(Math.max(8, e.clientX - ox), $(window).width() - widget.outerWidth() - 8);
            var top = Math.min(Math.max(8, e.clientY - oy), $(window).height() - 40);
            widget.css({left: left, top: top});
        });
        $(document).on('mouseup', function () {
            if (!dragging) { return; }
            dragging = false;
            var o = widget.offset();
            localStorage.setItem(POS_KEY, JSON.stringify({left: o.left, top: o.top}));
        });
    }

    function fillForm(cfg) {
        $('#sipWsServer').val(cfg.wsServer);
        $('#sipUri').val(cfg.uri);
        $('#sipPassword').val(cfg.password);
        $('#sipDisplay').val(cfg.displayName);
        $('#sipRegister').prop('checked', !!cfg.register);
    }

    function saveConfig() {
        var cfg = {
            wsServer: $.trim($('#sipWsServer').val()),
            uri: $.trim($('#sipUri').val()),
            password: $('#sipPassword').val(),
            displayName: $.trim($('#sipDisplay').val()) || 'JPBX',
            register: $('#sipRegister').is(':checked')
        };
        if (!cfg.wsServer || !cfg.uri) { log('WebSocket 地址和 SIP 账号不能为空'); return; }
        localStorage.setItem(CFG_KEY, JSON.stringify(cfg));
        log('配置已保存到本机浏览器');
        disconnect();
        connect();
    }

    function ensureSipLib(done) {
        if (typeof SIP !== 'undefined' && SIP.UA) { done(); return; }
        setStatus('SIP 组件加载中…', 'calling');
        log('SIP 组件未就绪，正在重新加载');
        var s = document.createElement('script');
        s.src = ctx + 'ajax/libs/sip.js/sip.min.js?_ts=' + Date.now();
        s.onload = function () {
            if (typeof SIP !== 'undefined' && SIP.UA) { done(); }
            else { setStatus('SIP 组件不可用', ''); log('sip.min.js 已加载但缺少 SIP.UA，请强制刷新页面'); }
        };
        s.onerror = function () {
            setStatus('SIP 组件加载失败', '');
            log('无法加载 sip.min.js，请检查浏览器缓存或网络');
        };
        document.head.appendChild(s);
    }

    function connect() {
        ensureSipLib(function () { doConnect(); });
    }

    /** 组装 SIP.js 需要的 wsServers 条目（缺 sipUri 时部分分支会取不到 host） */
    function wsServerEntry(url) {
        var host = '';
        try {
            host = new URL(url).hostname;
        } catch (e) {
            host = String(url || '').replace(/^wss?:\/\//i, '').split(':')[0];
        }
        return {
            sipUri: '<sip:' + host + ';transport=ws;lr>',
            weight: 0,
            wsUri: url,
            isError: false
        };
    }

    function doConnect() {
        disconnect();
        var cfg = loadConfig();
        fillForm(cfg);
        try {
            ua = new SIP.UA({
                uri: cfg.uri,
                password: cfg.password,
                displayName: cfg.displayName,
                // SIP.js 0.15.11 只认 transportOptions.wsServers（数组），
                // 传 wsServer 字符串会被忽略并回落到内置的 wss://edge.sip.onsip.com
                transportOptions: {
                    wsServers: [wsServerEntry(cfg.wsServer)]
                },
                register: cfg.register,
                autostart: false,
                media: {
                    constraints: {audio: true, video: false},
                    render: {remote: document.getElementById('pbxSipRemote')}
                }
            });
            ua.on('registered', function () {
                registeredUri = cfg.uri;
                setStatus('已注册：<b>' + $('<span>').text(cfg.uri).html() + '</b>', 'online');
                updateButtons();
                log('SIP 注册成功');
            });
            ua.on('unregistered', function () {
                registeredUri = null;
                setStatus('已注销', '');
                updateButtons();
                log('SIP 已注销');
            });
            ua.on('registrationFailed', function (e) {
                setStatus('注册失败', '');
                // 0.15 里事件负载可能是 {response, cause}，也可能直接是响应对象
                var resp = (e && e.response) ? e.response : e;
                var detail = '';
                if (resp && resp.status) {
                    detail = resp.status + ' ' + (resp.reason_phrase || resp.reasonPhrase || '');
                } else if (e && e.cause) {
                    detail = e.cause;
                } else if (e && e.message) {
                    detail = e.message;
                }
                if (/403|404/.test(detail)) {
                    log('注册失败 [' + detail + ']：FS 在 directory 里找不到该账号，检查「域」是否与 SIP域名 一致');
                } else if (/401|407/.test(detail)) {
                    log('注册失败 [' + detail + ']：账号或密码错误');
                } else {
                    log('注册失败' + (detail ? ' [' + detail + ']' : '') + '：未收到 FS 响应，先确认本行上方有没有「WebSocket 已连接」');
                }
            });
            ua.on('transportError', function (e) {
                setStatus('WebSocket 连接失败', '');
                log('WebSocket 连接失败：' + ((e && e.message) || '检查 wss:// 地址、7443 端口与证书'));
            });
            ua.on('disconnected', function () {
                if (ua) { log('WebSocket 已断开：若上方没有「WebSocket 已连接」，多为证书未信任或端口不通'); }
            });
            ua.on('invite', function (s) {
                pendingSession = s;
                var from = s.remoteIdentity && s.remoteIdentity.uri ? s.remoteIdentity.uri.toString() : '未知';
                setStatus('来电：<b>' + $('<span>').text(from).html() + '</b>', 'calling');
                log('收到来电：' + from);
                startRing();
                updateButtons();
                // 来电时自动切到呼叫页，否则用户看不到接听按钮
                $('#sipTabCall').trigger('click');
                // 未接听前对方取消：复位状态并停止振铃
                s.on('terminated', function () {
                    if (!session) {
                        pendingSession = null;
                        stopRing();
                        updateButtons();
                        idleStatus();
                        log('来电已取消');
                    }
                });
            });
            ua.start();
            setStatus('正在连接 ' + cfg.wsServer + ' …', 'calling');
            log('正在连接 ' + cfg.wsServer);
            // 0.15 的 UA 不转发 transport 事件，直接挂到 transport 上区分“没连上”与“连上但注册失败”
            if (ua.transport && ua.transport.on) {
                ua.transport.on('connected', function () { log('WebSocket 已连接，正在发送 REGISTER'); });
                ua.transport.on('transportError', function (e) { log('WebSocket 错误：' + ((e && e.message) || 'TLS 握手失败，多为证书未信任')); });
            }
        } catch (e) {
            setStatus('连接异常', '');
            log('连接异常：' + (e.message || e));
        }
    }

    function disconnect() {
        stopRing();
        if (session) { try { session.terminate(); } catch (e) {} session = null; }
        if (pendingSession) { try { pendingSession.terminate(); } catch (e) {} pendingSession = null; }
        if (ua) { try { ua.stop(); } catch (e) {} ua = null; }
        registeredUri = null;
        muted = false;
        $('#btnSipMute').removeClass('active').text('静音');
        if (timer) { clearInterval(timer); timer = null; }
        $('#sipTimer').text('00:00');
        updateButtons();
        setStatus('未连接', '');
    }

    function mediaErrorText(err) {
        var name = (err && err.name) || '';
        if (name === 'NotAllowedError' || name === 'PermissionDeniedError') {
            return '麦克风权限被拒绝，点地址栏左侧图标允许麦克风后重试';
        }
        if (name === 'NotFoundError') { return '没有检测到麦克风设备'; }
        if (name === 'NotReadableError') { return '麦克风被其他程序占用'; }
        if (name === 'SecurityError') { return '当前页面不是安全上下文，需 https 或 localhost'; }
        return name || (err && err.message) || '未知错误';
    }

    /** 用户手势里创建/恢复 AudioContext，否则来电振铃会被自动播放策略静音 */
    function ensureAudioCtx() {
        try {
            if (!audioCtx) {
                var Ctx = window.AudioContext || window.webkitAudioContext;
                if (Ctx) { audioCtx = new Ctx(); }
            }
            if (audioCtx && audioCtx.state === 'suspended') { audioCtx.resume(); }
        } catch (e) { /* 不支持 WebAudio 时静默降级 */ }
    }

    function startRing() {
        stopRing();
        ensureAudioCtx();
        if (!audioCtx) { return; }
        var beep = function () {
            try {
                var o = audioCtx.createOscillator(), g = audioCtx.createGain();
                o.type = 'sine';
                o.frequency.value = 440;
                g.gain.value = 0.06;
                o.connect(g);
                g.connect(audioCtx.destination);
                o.start();
                o.stop(audioCtx.currentTime + 1);
            } catch (e) { /* ignore */ }
        };
        beep();
        ringTimer = setInterval(beep, 3000);
    }

    function stopRing() {
        if (ringTimer) { clearInterval(ringTimer); ringTimer = null; }
    }

    /** 接通后兜底绑定远端音频流并监控播放推进，用于定位"听不到对方"在哪一环 */
    function watchRemoteAudio() {
        var el = document.getElementById('pbxSipRemote');
        if (!el) { log('远端音频：页面缺少播放元素 pbxSipRemote'); return; }
        setTimeout(function () {
            var stream = null;
            try {
                var sdh = session && session.sessionDescriptionHandler;
                if (sdh) {
                    if (typeof sdh.getRemoteStream === 'function') { stream = sdh.getRemoteStream(); }
                    if (!stream && sdh.peerConnection && sdh.peerConnection.getReceivers) {
                        var tracks = [];
                        sdh.peerConnection.getReceivers().forEach(function (r) {
                            if (r.track) { tracks.push(r.track); }
                        });
                        if (tracks.length) {
                            var MS = window.MediaStream || window.webkitMediaStream;
                            if (MS) { stream = new MS(tracks); }
                        }
                    }
                }
            } catch (e) { /* ignore */ }
            if (stream && el.srcObject !== stream) { el.srcObject = stream; }
            if (!el.srcObject) { log('远端音频：未取到媒体流'); return; }
            var p = el.play();
            if (p && p.catch) {
                p.catch(function (err) {
                    log('远端音频自动播放被拦截（' + (err && err.name || '') + '）：点击页面任意位置后重试');
                });
            }
            var t0 = el.currentTime;
            setTimeout(function () {
                if (!session) { return; }
                if (el.currentTime <= t0) {
                    log('远端音频：流已绑定但播放未推进 → 请点「试听」验证系统扬声器/输出设备');
                } else {
                    log('远端音频：播放正常推进');
                }
            }, 3000);
        }, 600);
    }

    /** 扬声器自检：连响两声，用于区分"系统没声音"与"通话媒体不通" */
    function speakerTest() {
        ensureAudioCtx();
        if (!audioCtx) { log('扬声器自检：当前浏览器不支持 WebAudio'); return; }
        log('扬声器自检：请确认扬声器连响两声"嘟嘟"');
        var o = audioCtx.createOscillator(), g = audioCtx.createGain();
        o.type = 'sine';
        o.frequency.value = 660;
        g.gain.value = 0.15;
        o.connect(g);
        g.connect(audioCtx.destination);
        var now = audioCtx.currentTime;
        o.start(now);
        o.stop(now + 0.9);
    }

    function startTimer() {
        if (timer) { clearInterval(timer); }
        startAt = Date.now();
        $('#sipTimer').text('00:00');
        timer = setInterval(function () {
            var sec = Math.floor((Date.now() - startAt) / 1000);
            $('#sipTimer').text(('0' + Math.floor(sec / 60)).slice(-2) + ':' + ('0' + (sec % 60)).slice(-2));
        }, 1000);
    }

    /** 按钮可用状态只由「当前有无会话」推导，避免挂断后残留"接听中/通话中" */
    function updateButtons() {
        var busy = !!(session || pendingSession);
        $('#btnSipCall').prop('disabled', !!session);
        $('#btnSipHangup').prop('disabled', !busy);
        $('#btnSipAnswer').prop('disabled', !pendingSession);
        $('#btnSipMute').prop('disabled', !session);
        if (!session) {
            muted = false;
            $('#btnSipMute').removeClass('active').text('静音');
        }
    }

    function idleStatus() {
        if (registeredUri) {
            setStatus('已注册：<b>' + $('<span>').text(registeredUri).html() + '</b>', 'online');
        } else if (ua) {
            setStatus('已连接（未注册）', 'online');
        } else {
            setStatus('未连接', '');
        }
    }

    function resetCall() {
        stopRing();
        if (timer) { clearInterval(timer); timer = null; }
        $('#sipTimer').text('00:00');
        session = null;
        pendingSession = null;
        updateButtons();
        idleStatus();
    }

    function hangup() {
        stopRing();
        var s = session || pendingSession;
        if (s) {
            try { s.terminate(); } catch (e) { /* 已结束的会话忽略 */ }
        }
        log('已挂断');
        resetCall();
    }

    /** 统一绑定会话事件：外呼、接听、来电取消都走这里，状态不会卡住 */
    function bindSession(s, incoming, label) {
        session = s;
        pendingSession = null;
        updateButtons();
        s.on('progress', function () {
            if (!incoming) {
                setStatus('呼叫中：' + $('<span>').text(label || '').html() + ' …', 'calling');
                startRing();
            }
        });
        s.on('accepted', function () {
            stopRing();
            startTimer();
            setStatus('通话中：' + $('<span>').text(label || '').html(), 'online');
            updateButtons();
            log(incoming ? '已接听来电' : '通话已接通');
            watchRemoteAudio();
        });
        s.on('terminated', function () {
            stopRing();
            log('通话结束');
            resetCall();
        });
        s.on('failed', function (e) {
            stopRing();
            var resp = (e && e.response) ? e.response : e;
            var info = '';
            if (resp && resp.status) {
                info = resp.status + ' ' + (resp.reason_phrase || resp.reasonPhrase || '');
            } else if (e && e.cause) {
                info = e.cause;
            }
            log('呼叫失败：' + (info || '未收到服务器响应（检查麦克风权限与网络）'));
            resetCall();
        });
    }

    var MEDIA_OPTS = {
        media: {constraints: {audio: true, video: false}, render: {remote: document.getElementById('pbxSipRemote')}}
    };

    function dial() {
        var target = $.trim($('#sipNumber').val());
        if (!target) { log('请输入要拨打的号码'); return; }
        if (!ua) { log('请先点「连接」注册软电话'); return; }
        if (session) { log('当前已有通话，请先挂断'); return; }
        if (target.indexOf('@') < 0) {
            var domain = loadConfig().uri.split('@')[1] || location.hostname;
            target += '@' + domain;
        }
        // 麦克风前置检查：http + IP 属于非安全上下文，浏览器会禁用 getUserMedia，
        // 此时 INVITE 根本发不出去，FS 侧也不会留下任何记录。
        if (!window.isSecureContext && !isLoopback(location.hostname)) {
            log('无法使用麦克风：当前页面是 http://' + location.hostname +
                '，浏览器只在 https 或 localhost 下允许录音。请改用 http://localhost:8090 打开，' +
                '或在 chrome://flags 里把本站加入 insecure origin 白名单');
            return;
        }
        if (!navigator.mediaDevices || !navigator.mediaDevices.getUserMedia) {
            log('无法使用麦克风：浏览器不支持 getUserMedia 或已被策略禁用');
            return;
        }
        navigator.mediaDevices.getUserMedia({audio: true, video: false}).then(function (stream) {
            stream.getTracks().forEach(function (t) { t.stop(); });
            startInvite(target);
        }).catch(function (err) {
            log('无法使用麦克风：' + mediaErrorText(err));
        });
    }

    function startInvite(target) {
        try {
            var s = ua.invite(target, MEDIA_OPTS);
            bindSession(s, false, target);
            setStatus('正在呼叫 ' + $('<span>').text(target).html() + ' …', 'calling');
            log('发起呼叫 ' + target);
        } catch (e) {
            log('呼叫异常：' + (e.message || e));
            resetCall();
        }
    }

    function answer() {
        if (!pendingSession) { log('当前没有来电'); return; }
        var s = pendingSession;
        var label = s.remoteIdentity && s.remoteIdentity.uri ? s.remoteIdentity.uri.user : '';
        stopRing();
        bindSession(s, true, label);
        try {
            s.accept(MEDIA_OPTS);
            setStatus('接听中…', 'calling');
            log('正在接听来电');
        } catch (e) {
            log('接听失败：' + (e.message || e));
            resetCall();
        }
    }

    /** 从后端取 FreeSWITCH 的 IP 与端口，纠正 localhost / 旧域名这类连不上的历史配置 */
    function applyServerDefaults() {
        if (typeof ctx === 'undefined' || !ctx) { return; }
        $.getJSON(ctx + 'pbx/sip/defaults', function (d) {
            if (!d || !d.wsServer) { return; }
            var saved = {};
            try { saved = JSON.parse(localStorage.getItem(CFG_KEY) || '{}'); } catch (e) { saved = {}; }
            var cfg = loadConfig();
            var host = '', port = '';
            try {
                var u = new URL(cfg.wsServer);
                host = u.hostname;
                port = u.port;
            } catch (e) { host = ''; }
            // 首次使用、还是 localhost、或仍指向本机 7443（自签证书未信任）→ 换成明文的 ws:5066
            var stale = !saved.wsServer || isLoopback(host) || (host === d.localIp && port === '7443');
            if (stale) {
                cfg.wsServer = d.wsServer;
                var uriHost = (cfg.uri || '').split('@')[1] || '';
                if (!saved.uri || isLoopback(uriHost)) { cfg.uri = '1132@' + d.localIp; }
                localStorage.setItem(CFG_KEY, JSON.stringify(cfg));
                fillForm(cfg);
                log('已按服务器配置填入 ' + d.wssServer);
            }
        });
    }

    $(function () {
        widget = $('#pbxSipWidget');
        $('#sipVer').text('脚本 v20261119l');
        fillForm(loadConfig());
        applyServerDefaults();
        enableDrag();
        applyPosition();

        $('#sipClientToggle').on('click', function (e) { e.preventDefault(); toggleWidget(); });
        $('#pbxSipClose').on('click', function () { widget.addClass('hidden'); });
        $('#pbxSipMin').on('click', function () {
            widget.toggleClass('pbx-sip-min');
            $(this).text(widget.hasClass('pbx-sip-min') ? '□' : '—');
        });
        function switchView(which) {
            var isCall = which === 'call';
            var isSettings = which === 'settings';
            var isLog = which === 'log';
            $('#sipViewCall').toggle(isCall);
            $('#sipViewSettings').toggle(isSettings);
            $('#sipViewLog').toggle(isLog);
            $('#sipTabCall').toggleClass('active', isCall);
            $('#sipTabSettings').toggleClass('active', isSettings);
            $('#sipTabLog').toggleClass('active', isLog);
            if (isLog) {
                $('#sipTabLog').removeClass('has-new');
                var el = $('#pbxSipLog');
                if (el.length) { el.scrollTop(el[0].scrollHeight); }
            }
        }
        $('#sipTabCall').on('click', function () { switchView('call'); });
        $('#sipTabSettings').on('click', function () { switchView('settings'); });
        $('#sipTabLog').on('click', function () { switchView('log'); });
        $('#btnSipLogClear').on('click', function () {
            $('#pbxSipLog').empty();
            log('日志已清空');
        });
        $('#btnSipBackToCall').on('click', function () { switchView('call'); });
        $('#btnSipSave').on('click', saveConfig);
        $('#btnSipConnect').on('click', function () { ensureAudioCtx(); connect(); });
        $('#btnSipCall').on('click', function () { ensureAudioCtx(); dial(); });
        $('#btnSipHangup').on('click', hangup);
        $('#btnSipAnswer').on('click', function () { ensureAudioCtx(); answer(); });
        $('#btnSipSpeakerTest').on('click', speakerTest);
        $('#btnSipMute').on('click', function () {
            if (!session) { return; }
            if (muted) { session.unmute(); muted = false; $(this).removeClass('active').text('静音'); }
            else { session.mute(); muted = true; $(this).addClass('active').text('取消静音'); }
        });
        $('#sipNumber').on('keydown', function (e) { if (e.key === 'Enter') { dial(); } });
        $('.pbx-sip-pad button[data-key]').on('click', function () {
            var $n = $('#sipNumber');
            $n.val($n.val() + $(this).data('key'));
        });
        $('#sipBackspace').on('click', function () {
            var $n = $('#sipNumber');
            $n.val($n.val().slice(0, -1));
        });
    });
})(jQuery);
