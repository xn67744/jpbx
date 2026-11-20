package com.av.jpbx.domain;

import com.av.common.annotation.Excel;
import com.av.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TPbxDomain extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String id;
    @Excel(name = "域名")
    private String name;
    private Integer parse;
    private Integer alias;
    private Integer enabled;
}
