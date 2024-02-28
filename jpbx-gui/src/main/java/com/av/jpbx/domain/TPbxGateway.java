package com.av.jpbx.domain;

import com.av.common.annotation.Excel;
import com.av.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 呼叫中继对象 t_pbx_gateway
 * 
 * @author long
 * @date 2024-02-08
 */
@Data
@Accessors(chain = true)
public class TPbxGateway extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 服务器地址 */
    @Excel(name = "中继名称")
    private String name;

    /** 服务器地址 */
    @Excel(name = "服务器地址")
    private String realm;

    /** 是否注册 */
    @Excel(name = "是否注册")
    private Integer register;

    /** 传输类型 */
    @Excel(name = "传输类型")
    private String transport;

    /** 重连间隔（秒） */
    @Excel(name = "重连间隔", readConverterExp = "秒")
    private Long retrySeconds;

    /** 账户 */
    @Excel(name = "账户")
    private String username;

    /** 密码 */
    @Excel(name = "密码")
    private String password;

    /** 中继类型 */
    @Excel(name = "中继类型")
    private Integer type;

    /**是否选中0:未选中，1：已选中**/
    private int selected;

    /**关联路由id**/
    private String routeId;

    public void setType(Integer type)
    {
        this.type = type;
        register = type == 0?0:1;
    }


}
