package com.av.jpbx.domain;

import com.av.common.annotation.Excel;
import com.av.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 呼叫账户管理对象 t_pbx_user
 * 
 * @author long
 * @date 2024-02-04
 */
@Data
@Accessors(chain = true)
public class TPbxUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private String id;

    /**  */
    private String name;

    /**  */
    private String number;

    /**  */
    private String domain;

    /**  */
    private String password;

    /**  */
    private Integer type;

    /** 录音（0正常 1停用） */
    @Excel(name = "录音", readConverterExp = "0=使用,1=停用")
    private String audio;

    /** 录像（0正常 1停用） */
    @Excel(name = "录像", readConverterExp = "0=使用,1=停用")
    private String video;

    /**  */
    private Integer level;

    /**  */
    private List<String> numbers;

}
