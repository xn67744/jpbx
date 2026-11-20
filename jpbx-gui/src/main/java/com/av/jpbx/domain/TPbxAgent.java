package com.av.jpbx.domain;

import com.av.common.annotation.Excel;
import com.av.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TPbxAgent extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String id;
    @Excel(name = "坐席名")
    private String name;
    @Excel(name = "联系方式")
    private String contact;
    private String status;
    private String type;
    private Integer maxNoAnswer;
    private Integer wrapUpTime;
    private Integer rejectDelayTime;
    private Integer busyDelayTime;
    private Integer enabled;
}
