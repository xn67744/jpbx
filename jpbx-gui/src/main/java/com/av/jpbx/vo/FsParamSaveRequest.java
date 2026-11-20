package com.av.jpbx.vo;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class FsParamSaveRequest {
    /** 配置段编码 */
    private String section;
    /** 是否保存后立即热生效 */
    private boolean apply = true;
    /** 参数键值 */
    private Map<String, String> values = new LinkedHashMap<>();
}
