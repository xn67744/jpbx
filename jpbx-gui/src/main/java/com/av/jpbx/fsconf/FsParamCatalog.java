package com.av.jpbx.fsconf;

import java.util.*;

/**
 * FreeSWITCH 配置项目录：每个参数含说明、示例、校验规则。
 * UI 编辑的是参数名/值，xml_curl 再据此生成 XML，不直接改 FS 磁盘文件。
 */
public final class FsParamCatalog {

    private static final Map<FsConfigSection, List<FsParamDef>> CATALOG = new LinkedHashMap<>();

    static {
        CATALOG.put(FsConfigSection.SWITCH, switchParams());
        CATALOG.put(FsConfigSection.SOFIA_INTERNAL, sofiaInternalParams());
        CATALOG.put(FsConfigSection.SOFIA_EXTERNAL, sofiaExternalParams());
        CATALOG.put(FsConfigSection.CDR_PG, cdrParams());
        CATALOG.put(FsConfigSection.VOICEMAIL, voicemailParams());
        CATALOG.put(FsConfigSection.CONFERENCE, conferenceParams());
        CATALOG.put(FsConfigSection.ACL, aclParams());
        CATALOG.put(FsConfigSection.EVENT_SOCKET, eventSocketParams());
    }

    private FsParamCatalog() {
    }

    public static List<FsParamDef> defsOf(FsConfigSection section) {
        return CATALOG.getOrDefault(section, Collections.emptyList());
    }

    public static FsParamDef defOf(FsConfigSection section, String key) {
        for (FsParamDef d : defsOf(section)) {
            if (d.getKey().equals(key)) {
                return d;
            }
        }
        return null;
    }

    public static Map<FsConfigSection, List<FsParamDef>> all() {
        return Collections.unmodifiableMap(CATALOG);
    }

    private static List<FsParamDef> switchParams() {
        return Arrays.asList(
                def("rtp-start-port", "RTP 起始端口", "媒体 RTP 端口池起始值。变更后建议 sofia profile restart，已建立通话不受影响。",
                        "16384", "16384", FsParamType.PORT, true, null, null, 1024, 65535, null, "profile", "媒体端口"),
                def("rtp-end-port", "RTP 结束端口", "媒体 RTP 端口池结束值，必须大于起始端口，且区间足够支撑并发通话数×2。",
                        "32768", "32768", FsParamType.PORT, true, null, null, 1024, 65535, null, "profile", "媒体端口"),
                def("max-sessions", "最大会话数", "FreeSWITCH 同时允许的最大通道数。过高可能耗尽 CPU/内存。",
                        "1000", "1000", FsParamType.INTEGER, true, null, null, 1, 100000, null, "reloadxml", "会话限制"),
                def("sessions-per-second", "每秒新建会话数", "限制每秒创建呼叫速率，防止突发压垮系统。",
                        "30", "30", FsParamType.INTEGER, true, null, null, 1, 10000, null, "reloadxml", "会话限制"),
                def("loglevel", "日志级别", "核心日志级别：console/debug/info/notice/warning/err/crit/alert。",
                        "info", "info", FsParamType.ENUM, true, null, null, null, null,
                        "console,debug,info,notice,warning,err,crit,alert", "reloadxml", "日志"),
                def("colorize-console", "控制台彩色日志", "fs_cli 是否彩色输出。",
                        "true", "true", FsParamType.BOOLEAN, false, null, null, null, null, null, "reloadxml", "日志")
        );
    }

    private static List<FsParamDef> sofiaInternalParams() {
        return Arrays.asList(
                def("sip-port", "SIP 监听端口", "内部分机注册/呼叫 SIP 端口，对应 sofia profile internal。",
                        "5060", "5060", FsParamType.PORT, true, null, null, 1, 65535, null, "profile", "网络"),
                def("sip-ip", "SIP 绑定 IP", "Profile 绑定的本机 IP；留空则用 FS 变量 $${local_ip_v4}。",
                        "192.168.1.10", "", FsParamType.IP_OR_HOST, false,
                        "^(?:(?:\\d{1,3}\\.){3}\\d{1,3}|\\$\\$\\{[\\w.]+\\}|[a-zA-Z0-9._-]+)?$",
                        "请填写合法 IP 或主机名", null, null, null, "profile", "网络"),
                def("rtp-ip", "RTP 绑定 IP", "媒体面绑定 IP，通常与 sip-ip 一致。",
                        "192.168.1.10", "", FsParamType.IP_OR_HOST, false, null, null, null, null, null, "profile", "网络"),
                def("ext-sip-ip", "外部 SIP IP", "NAT 场景下通告给对端的公网 SIP IP；可填 stun:host 或 autonat:ip。",
                        "stun:stun.l.google.com:19302", "", FsParamType.STRING, false, null, null, null, null, null, "profile", "NAT"),
                def("ext-rtp-ip", "外部 RTP IP", "NAT 场景下通告的公网 RTP IP。",
                        "stun:stun.l.google.com:19302", "", FsParamType.STRING, false, null, null, null, null, null, "profile", "NAT"),
                def("context", "拨号计划上下文", "本 Profile 入呼进入的 dialplan context，分机一般用 default。",
                        "default", "default", FsParamType.STRING, true, "^[a-zA-Z0-9_-]+$", "仅字母数字下划线中划线", null, null, null, "reloadxml", "拨号"),
                def("dialplan", "拨号计划类型", "固定 XML，表示使用 mod_dialplan_xml（可走 xml_curl）。",
                        "XML", "XML", FsParamType.ENUM, true, null, null, null, null, "XML", "reloadxml", "拨号"),
                // PCMU 放首位：浏览器(sip.js/Chrome)的 opus 是 stereo=2，手机(Linphone)是 mono=1，
                // FS 对同名 opus 直通转发会造成 payload/声道错配 → 双向无声（opus 立体声坑）。
                def("inbound-codec-prefs", "入向编解码偏好", "逗号分隔编解码列表，优先顺序从左到右。OPUS 放在 PCMU 之后，避免 WebRTC 与手机端 opus 声道不一致时直通转发。",
                        "PCMU,PCMA,OPUS", "PCMU,PCMA,OPUS", FsParamType.CODEC_LIST, true,
                        "^[A-Za-z0-9_,.@+-]+$", "编解码列表格式不正确", null, null, null, "profile", "媒体"),
                def("outbound-codec-prefs", "出向编解码偏好", "呼出时编解码偏好。",
                        "PCMU,PCMA,OPUS", "PCMU,PCMA,OPUS", FsParamType.CODEC_LIST, true, null, null, null, null, null, "profile", "媒体"),
                // generous=按对端 offer 顺序选，手机端 opus 排第一时会绕过 PCMU 优先；
                // greedy=按本端（FS）偏好顺序选，PCMU,PCMA,OPUS 才能真正生效。
                def("inbound-codec-negotiation", "编解码协商模式", "greedy=按 FS 编解码偏好顺序协商（推荐，编解码优先级才生效）；generous=按对端 offer 顺序协商。",
                        "greedy", "greedy", FsParamType.ENUM, true, null, null,
                        null, null, "greedy,generous", "profile", "媒体"),
                def("auth-calls", "强制鉴权呼叫", "true=仅接受已认证分机呼叫；内部分机建议 true。",
                        "true", "true", FsParamType.BOOLEAN, true, null, null, null, null, null, "profile", "安全"),
                def("accept-blind-reg", "允许盲注册", "必须为 false，否则注册不走 xml_curl directory，管理平台分机密码无效。",
                        "false", "false", FsParamType.BOOLEAN, true, null, null, null, null, null, "profile", "安全"),
                def("apply-inbound-acl", "入向 ACL", "引用 acl.conf 中的 list 名称，限制谁可发 INVITE 到本 Profile。",
                        "domains", "domains", FsParamType.STRING, false, null, null, null, null, null, "profile", "安全"),
                def("manage-presence", "Presence 管理", "是否处理订阅/状态呈现。",
                        "true", "true", FsParamType.BOOLEAN, false, null, null, null, null, null, "profile", "高级"),
                def("nonce-ttl", "Nonce 有效期(秒)", "SIP Digest 鉴权 nonce 生存时间。",
                        "60", "60", FsParamType.INTEGER, false, null, null, 10, 3600, null, "profile", "高级"),
                def("rtp-timeout-sec", "RTP 超时(秒)", "无媒体超时挂断时间。",
                        "300", "300", FsParamType.INTEGER, false, null, null, 30, 3600, null, "profile", "媒体"),
                def("ws-binding", "WebSocket 绑定", "WebRTC/SIP over WS 监听，如 :5066。",
                        ":5066", ":5066", FsParamType.STRING, false, null, null, null, null, null, "profile", "WebRTC"),
                def("wss-binding", "WSS 绑定", "SIP over WSS 监听，如 :7443。",
                        ":7443", ":7443", FsParamType.STRING, false, null, null, null, null, null, "profile", "WebRTC")
        );
    }

    private static List<FsParamDef> sofiaExternalParams() {
        return Arrays.asList(
                def("sip-port", "SIP 监听端口", "外线/中继 Profile 端口，默认常为 5080，勿与 internal 冲突。",
                        "5080", "5080", FsParamType.PORT, true, null, null, 1, 65535, null, "profile", "网络"),
                def("sip-ip", "SIP 绑定 IP", "外线 Profile 绑定 IP。",
                        "192.168.1.10", "", FsParamType.IP_OR_HOST, false, null, null, null, null, null, "profile", "网络"),
                def("rtp-ip", "RTP 绑定 IP", "外线媒体绑定 IP。",
                        "192.168.1.10", "", FsParamType.IP_OR_HOST, false, null, null, null, null, null, "profile", "网络"),
                def("ext-sip-ip", "外部 SIP IP", "对运营商通告的公网地址。",
                        "1.2.3.4", "", FsParamType.STRING, false, null, null, null, null, null, "profile", "NAT"),
                def("ext-rtp-ip", "外部 RTP IP", "对运营商通告的公网 RTP 地址。",
                        "1.2.3.4", "", FsParamType.STRING, false, null, null, null, null, null, "profile", "NAT"),
                def("context", "拨号计划上下文", "外线入呼 context，通常为 public。",
                        "public", "public", FsParamType.STRING, true, "^[a-zA-Z0-9_-]+$", "仅字母数字下划线中划线", null, null, null, "reloadxml", "拨号"),
                def("auth-calls", "强制鉴权呼叫", "外线一般 false，由 ACL/网关鉴权控制。",
                        "false", "false", FsParamType.BOOLEAN, true, null, null, null, null, null, "profile", "安全"),
                def("accept-blind-reg", "允许盲注册", "外线 Profile 建议 false。",
                        "false", "false", FsParamType.BOOLEAN, true, null, null, null, null, null, "profile", "安全"),
                def("inbound-codec-prefs", "入向编解码偏好", "外线入向编解码。",
                        "PCMU,PCMA,G729", "PCMU,PCMA", FsParamType.CODEC_LIST, true, null, null, null, null, null, "profile", "媒体"),
                def("outbound-codec-prefs", "出向编解码偏好", "外线出向编解码。",
                        "PCMU,PCMA,G729", "PCMU,PCMA", FsParamType.CODEC_LIST, true, null, null, null, null, null, "profile", "媒体"),
                def("apply-inbound-acl", "入向 ACL", "限制哪些 IP 可打入 external，例如 provider。",
                        "providers", "", FsParamType.STRING, false, null, null, null, null, null, "profile", "安全"),
                def("rtp-timeout-sec", "RTP 超时(秒)", "无媒体超时。",
                        "300", "300", FsParamType.INTEGER, false, null, null, 30, 3600, null, "profile", "媒体")
        );
    }

    private static List<FsParamDef> cdrParams() {
        return Arrays.asList(
                def("db-info", "数据库连接串", "libpq 风格连接串，供 mod_cdr_pg_csv 写话单。勿把密码提交到公开仓库。",
                        "host=127.0.0.1 port=5432 dbname=youpbx user=postgres password=secret connect_timeout=10",
                        "", FsParamType.TEXT, true, ".*host=.*", "需包含 host= 等连接参数", null, null, null, "reload_module", "连接"),
                def("db-table", "话单表名", "写入的 PostgreSQL 表名，需与 sql/pbx_pgsql.sql 中 cdr 一致。",
                        "cdr", "cdr", FsParamType.STRING, true, "^[a-zA-Z0-9_]+$", "表名仅字母数字下划线", null, null, null, "reload_module", "连接"),
                def("legs", "记录腿", "a=只记 A 腿；ab=双腿。",
                        "a", "a", FsParamType.ENUM, true, null, null, null, null, "a,b,ab", "reload_module", "行为"),
                def("spool-format", "缓存格式", "写库失败时本地 spool 格式。",
                        "csv", "csv", FsParamType.ENUM, false, null, null, null, null, "csv,sql", "reload_module", "行为"),
                def("rotate-on-hup", "HUP 时轮转", "收到 HUP 信号是否轮转 spool 文件。",
                        "true", "true", FsParamType.BOOLEAN, false, null, null, null, null, null, "reload_module", "行为")
        );
    }

    private static List<FsParamDef> voicemailParams() {
        return Arrays.asList(
                def("file-extension", "留言文件后缀", "录音文件扩展名。",
                        "wav", "wav", FsParamType.ENUM, true, null, null, null, null, "wav,mp3,ogg", "reloadxml", "存储"),
                def("storage-dir", "存储目录", "留言文件根目录，需 FS 进程可写。",
                        "/usr/local/freeswitch/storage/voicemail", "", FsParamType.PATH, false, null, null, null, null, null, "reloadxml", "存储"),
                def("max-record-len", "最大录音时长(秒)", "单条留言最长秒数。",
                        "300", "300", FsParamType.INTEGER, true, null, null, 10, 3600, null, "reloadxml", "行为"),
                def("digit-timeout", "按键超时(ms)", "收号超时毫秒。",
                        "10000", "10000", FsParamType.INTEGER, false, null, null, 1000, 60000, null, "reloadxml", "行为"),
                def("max-login-attempts", "最大登录尝试", "密码错误次数上限。",
                        "3", "3", FsParamType.INTEGER, false, null, null, 1, 10, null, "reloadxml", "安全")
        );
    }

    private static List<FsParamDef> conferenceParams() {
        return Arrays.asList(
                def("rate", "采样率", "默认 conference profile 采样率。",
                        "8000", "8000", FsParamType.ENUM, true, null, null, null, null, "8000,16000,32000,48000", "reloadxml", "媒体"),
                def("interval", "打包间隔(ms)", "ptime。",
                        "20", "20", FsParamType.INTEGER, true, null, null, 10, 60, null, "reloadxml", "媒体"),
                def("energy-level", "能量检测阈值", "语音活动检测灵敏度，越大越不易触发。",
                        "300", "300", FsParamType.INTEGER, false, null, null, 0, 2000, null, "reloadxml", "媒体"),
                def("caller-controls", "按键控制组", "引用 caller-controls 组名，默认 default。",
                        "default", "default", FsParamType.STRING, false, null, null, null, null, null, "reloadxml", "控制"),
                def("comfort-noise", "舒适噪声", "是否产生舒适噪声。",
                        "true", "true", FsParamType.BOOLEAN, false, null, null, null, null, null, "reloadxml", "媒体")
        );
    }

    private static List<FsParamDef> aclParams() {
        return Arrays.asList(
                def("hint", "说明", "ACL 节点（CIDR allow/deny）请在菜单「ACL」中维护列表与节点；保存后执行 reloadacl。此处不直接编辑 XML。",
                        "见 ACL 菜单", "见 ACL 菜单", FsParamType.STRING, false, null, null, null, null, null, "reloadacl", "说明")
        );
    }

    private static List<FsParamDef> eventSocketParams() {
        return Arrays.asList(
                def("listen-ip", "ESL 监听 IP（说明）", "在 FS 本机 event_socket.conf.xml 配置。JPBX 通过 fs.properties 的 eslHost 连接，不在此写入 FS 文件。",
                        "0.0.0.0", "0.0.0.0", FsParamType.IP, false, null, null, null, null, null, "docs", "对接"),
                def("listen-port", "ESL 端口（说明）", "默认 8021，需与 fs.properties eslInBoundPort 一致。",
                        "8021", "8021", FsParamType.PORT, false, null, null, 1, 65535, null, "docs", "对接"),
                def("password", "ESL 密码（说明）", "需与 fs.properties eslpwd 一致。修改后在 FS 执行 reload mod_event_socket，JPBX 会自动重连。",
                        "ClueCon", "ClueCon", FsParamType.STRING, false, null, null, null, null, null, "docs", "对接")
        );
    }

    private static FsParamDef def(String key, String title, String description, String example, String defaultValue,
                                  FsParamType type, boolean required, String pattern, String patternMessage,
                                  Integer min, Integer max, String enumOptions, String applyHint, String group) {
        return FsParamDef.builder()
                .key(key).title(title).description(description).example(example).defaultValue(defaultValue)
                .type(type).required(required).pattern(pattern).patternMessage(patternMessage)
                .min(min).max(max).enumOptions(enumOptions).applyHint(applyHint).group(group)
                .build();
    }
}
