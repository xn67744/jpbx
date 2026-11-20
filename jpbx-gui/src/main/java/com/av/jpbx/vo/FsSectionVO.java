package com.av.jpbx.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class FsSectionVO {
    private String code;
    private String name;
    private String title;
    private String description;
    private String applyType;
    private List<FsParamItemVO> params;
}
