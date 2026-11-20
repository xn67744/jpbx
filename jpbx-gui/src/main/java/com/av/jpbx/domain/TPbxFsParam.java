package com.av.jpbx.domain;

import com.av.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * FreeSWITCH 配置参数值（按 section + key 存储，非 XML 文件）。
 */
@Data
@Accessors(chain = true)
public class TPbxFsParam extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String id;
    /** 配置段编码，如 sofia.conf/internal */
    private String section;
    private String paramKey;
    private String paramValue;
}
