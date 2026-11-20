package com.av.jpbx.configure;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * FreeSWITCH 对接配置
 */
@Data
@Component
@Accessors(chain = true)
@PropertySource(value = "classpath:fs.properties", encoding = "UTF-8")
public class FSConfigure {

    @Value("${tcpPort:9005}")
    private int tcpPort;

    @Value("${eslHost:127.0.0.1}")
    private String eslHost;

    @Value("${eslInBoundPort:8021}")
    private int eslInBoundPort;

    @Value("${eslOutBoundPort:18084}")
    private int eslOutBoundPort;

    @Value("${eslpwd:ClueCon}")
    private String eslpwd;

    @Value("${connTimeout:30}")
    public int connTimeout;

    /** xml_curl 共享口令，FS 请求需带 token 参数或 Header */
    @Value("${xmlCurlToken:}")
    private String xmlCurlToken;

    /** 允许访问 xml_curl 的 IP，逗号分隔；空则不限制 */
    @Value("${xmlCurlAllowedIps:}")
    private String xmlCurlAllowedIps;

    /** 是否启用 outbound ESL server */
    @Value("${eslOutboundEnabled:false}")
    private boolean eslOutboundEnabled;
}
