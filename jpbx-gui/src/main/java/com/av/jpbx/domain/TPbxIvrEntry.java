package com.av.jpbx.domain;

import com.av.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TPbxIvrEntry extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String id;
    private String ivrId;
    private String digits;
    private String action;
    private String param;
    private Integer sortOrder;
}
