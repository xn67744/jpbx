package com.av.jpbx.domain;

import com.av.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TPbxGroupUser extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String id;
    private String groupId;
    private String userId;
    private String userNumber;
}
