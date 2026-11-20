package com.av.jpbx.domain;

import com.av.common.annotation.Excel;
import com.av.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class TPbxAclList extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String id;
    @Excel(name = "ACL名称")
    private String name;
    @Excel(name = "默认策略")
    private String defaultType;
    private List<TPbxAclNode> nodes;
}
