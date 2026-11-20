package com.av.jpbx.domain;

import com.av.common.annotation.Excel;
import com.av.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class TPbxIvr extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String id;
    @Excel(name = "IVR名称")
    private String name;
    private String greetLong;
    private String greetShort;
    private String invalidSound;
    private String exitSound;
    private Integer timeout;
    private Integer maxFailures;
    private Integer maxTimeouts;
    private Integer digitLen;
    private Integer interDigit;
    private Integer enabled;
    private List<TPbxIvrEntry> entries;
}
