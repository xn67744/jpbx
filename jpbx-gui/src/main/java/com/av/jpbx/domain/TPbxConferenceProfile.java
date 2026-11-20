package com.av.jpbx.domain;

import com.av.common.annotation.Excel;
import com.av.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TPbxConferenceProfile extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String id;
    @Excel(name = "Profile名称")
    private String name;
    private Integer rate;
    private Integer interval;
    private Integer energyLevel;
    private String callerControls;
    private String paramsJson;
}
