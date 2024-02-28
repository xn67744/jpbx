package com.av.jpbx.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TPbxRegister {
    private String userAccount;
    private String domain;
    private String token;
    private String contact;
    private Float expireTime;
    private String ip;
    private String port;
    private String protocol;
    private String hostName;
}
