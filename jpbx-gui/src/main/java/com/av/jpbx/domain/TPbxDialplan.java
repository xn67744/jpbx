package com.av.jpbx.domain;

import com.av.common.annotation.Excel;
import com.av.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class TPbxDialplan extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String id;
    @Excel(name = "名称")
    private String name;
    @Excel(name = "上下文")
    private String context;
    private String conditionField;
    @Excel(name = "匹配表达式")
    private String conditionExpr;
    private Integer continueFlag;
    private Integer enabled;
    private Integer sortOrder;
    private List<TPbxDialplanAction> actions;
}
