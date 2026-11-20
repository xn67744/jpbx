package com.av.jpbx.domain;

import com.av.common.annotation.Excel;
import com.av.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class TPbxVoicemail extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String id;
    @Excel(name = "分机")
    private String userNumber;
    private String domain;
    private String filePath;
    @Excel(name = "主叫")
    private String caller;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    private Integer readFlag;
    private Integer duration;
}
