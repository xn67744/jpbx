package com.av.jpbx.controller;

import com.av.common.utils.StringUtils;
import com.av.jpbx.domain.TPbxSysconf;
import com.av.jpbx.fsconf.SysconfDefaults;
import com.av.jpbx.service.FsEslClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 软电话默认接入参数。
 *
 * <p>浏览器无法自己知道 FreeSWITCH 的 WSS 监听地址：页面可能通过 localhost 打开，
 * 而 sofia 的 wss-binding 只监听 sysconf.localIp 那一个网卡。这里把平台配置里的
 * IP 与端口直接给前端，避免用户在设置页里猜地址。</p>
 */
@Controller
@RequestMapping("/pbx/sip")
public class TPbxSipController {

    @Autowired
    private FsEslClient fsEslClient;

    @GetMapping("/defaults")
    @ResponseBody
    public Map<String, Object> defaults() {
        TPbxSysconf conf = fsEslClient.getSysconf();
        if (conf == null) {
            conf = SysconfDefaults.newInstance();
        }
        String ip = conf.getLocalIp();
        String wsPort = StringUtils.isNotEmpty(conf.getWsPort()) ? conf.getWsPort() : SysconfDefaults.WS_PORT;
        String wssPort = StringUtils.isNotEmpty(conf.getWssPort()) ? conf.getWssPort() : SysconfDefaults.WSS_PORT;

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("localIp", ip);
        data.put("wsPort", wsPort);
        data.put("wssPort", wssPort);
        data.put("wsServer", "ws://" + ip + ":" + wsPort);
        // FreeSWITCH 的 wss-binding 是 TLS 端口，必须用 wss://
        data.put("wssServer", "wss://" + ip + ":" + wssPort);
        return data;
    }
}
