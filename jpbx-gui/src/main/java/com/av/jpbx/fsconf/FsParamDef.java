package com.av.jpbx.fsconf;

import lombok.Builder;
import lombok.Data;

/**
 * 单个 FreeSWITCH 配置项元数据（说明、示例、校验），不是 XML 原文。
 */
@Data
@Builder
public class FsParamDef {
    /** 参数键，对应 FS &lt;param name="..."/&gt; */
    private String key;
    /** 中文标题 */
    private String title;
    /** 完整说明 */
    private String description;
    /** 示例值 */
    private String example;
    /** 默认值 */
    private String defaultValue;
    /** 值类型 */
    private FsParamType type;
    /** 是否必填 */
    private boolean required;
    /** 正则（可选） */
    private String pattern;
    /** 正则失败提示 */
    private String patternMessage;
    /** 最小值（端口/整数） */
    private Integer min;
    /** 最大值 */
    private Integer max;
    /** 枚举可选值，逗号分隔 */
    private String enumOptions;
    /** 是否重启提示：profile / reloadxml / restart_fs */
    private String applyHint;
    /** 分组标题，用于前端分区 */
    private String group;
}
