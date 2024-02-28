package com.av.jpbx.configure;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author nan.xie
 * @create 2021/12/6 11:28
 * @desc
 **/
@Data
@Component
@Accessors(chain = true)
@PropertySource("classpath:fs.properties")
public class FSConfigure {

    @Value("${tcpPort}")
    private int tcpPort;

    @Value("${eslHost}")
    private String eslHost;

    @Value("${eslInBoundPort}")
    private int eslInBoundPort;

    @Value("${eslOutBoundPort}")
    private int eslOutBoundPort;

    @Value("${eslpwd}")
    private String eslpwd;

    @Value("${connTimeout}")
    public int connTimeout;
}
