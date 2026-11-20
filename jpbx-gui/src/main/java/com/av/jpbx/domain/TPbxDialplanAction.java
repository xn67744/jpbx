package com.av.jpbx.domain;

import com.av.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TPbxDialplanAction extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String id;
    private String dialplanId;
    private String application;
    private String data;
    private Integer inlineFlag;
    private Integer sortOrder;
}
