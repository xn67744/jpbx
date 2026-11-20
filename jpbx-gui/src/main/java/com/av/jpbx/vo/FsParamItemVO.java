package com.av.jpbx.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 前端展示/提交的单个配置项（元数据 + 当前值）。
 */
@Data
@Accessors(chain = true)
public class FsParamItemVO {
    private String key;
    private String title;
    private String description;
    private String example;
    private String defaultValue;
    private String type;
    private boolean required;
    private String pattern;
    private String patternMessage;
    private Integer min;
    private Integer max;
    private String enumOptions;
    private String applyHint;
    private String group;
    /** 当前已保存值 */
    private String value;
}
