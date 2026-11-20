package com.av.jpbx.domain;

import com.av.common.annotation.Excel;
import com.av.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TPbxConference extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String id;
    @Excel(name = "会议室")
    private String name;
    private String profile;
    private String pin;
    private Integer maxMembers;
    private Integer record;
    private Integer enabled;
}
