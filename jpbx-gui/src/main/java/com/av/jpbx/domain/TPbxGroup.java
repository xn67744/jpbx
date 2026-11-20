package com.av.jpbx.domain;

import com.av.common.annotation.Excel;
import com.av.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class TPbxGroup extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String id;
    @Excel(name = "分组名称")
    private String name;
    @Excel(name = "域名")
    private String domain;
    private List<String> memberNumbers;
    private String memberIds;
}
