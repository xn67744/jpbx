package com.av.jpbx.domain;

import com.av.common.annotation.Excel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class TPbxCDR {
    private Integer id; // 对应 int4
    @Excel(name = "IP地址")
    private String localIpV4; // 对应 inet
    private String callerIdName; // 对应 varchar
    @Excel(name = "主叫号码")
    private String callerIdNumber; // 对应 varchar
    @Excel(name = "被叫号码")
    private String destinationNumber; // 对应 varchar
    @Excel(name = "呼叫内容")
    private String context; // 对应 varchar
    @Excel(name = "呼叫时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
    private Date startStamp; // 对应 timestamptz
    @Excel(name = "应答时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
    private Date answerStamp; // 对应 timestamptz
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
    private Date endStamp; // 对应 timestamptz
    @Excel(name = "呼叫时长")
    private Integer duration; // 对应 int4
    @Excel(name = "billsec")
    private Integer billsec; // 对应 int4
    @Excel(name = "挂断原因")
    private String hangupCause; // 对应 varchar
    @Excel(name = "UUID")
    private String uuid; // 对应 uuid
    @Excel(name = "blegUUID")
    private String blegUuid; // 对应 uuid
    @Excel(name = "账户编码")
    private String accountcode; // 对应 varchar
    @Excel(name = "读取编码")
    private String readCodec; // 对应 varchar
    @Excel(name = "写入编码")
    private String writeCodec; // 对应 varchar
    private String sipHangupDisposition; // 对应 varchar
    private String ani; // 对应 varchar
}
