package com.av.jpbx.domain;

import com.av.common.annotation.Excel;
import com.av.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TPbxQueue extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String id;
    @Excel(name = "队列名称")
    private String name;
    @Excel(name = "策略")
    private String strategy;
    private String mohSound;
    private String timeBaseScore;
    private Integer maxWaitTime;
    private Integer maxWaitTimeWithNoAgent;
    private Integer tierRulesApply;
    private Integer discardAbandonedAfter;
    private Integer abandonedResumeAllowed;
    private Integer enabled;
}
