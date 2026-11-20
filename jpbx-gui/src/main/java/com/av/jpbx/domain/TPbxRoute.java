package com.av.jpbx.domain;

import com.av.common.annotation.Excel;
import com.av.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 路由管理对象 t_pbx_route
 *
 * @author long
 * @date 2024-02-11
 */
@Data
@Accessors(chain = true)
public class TPbxRoute extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 匹配前缀 */
    @Excel(name = "匹配前缀")
    private String reg;

    /** 路由类型 */
    @Excel(name = "路由类型")
    private Long type;

    /** 呼入模式 */
    private Long inboundModel;

    /** 中继id */
    private String gatewayIds;

    /** 中继名称 */
    @Excel(name = "中继名称")
    private String gatewayNames;

    /** 主叫前缀新增位数 */
    @Excel(name = "新增主叫前缀")
    private String callerPre;

    /** 主叫前缀删除位数 */
    @Excel(name = "主叫前缀删除位数")
    private Long callerPreRemove;

    /** 被叫前缀新增位数 */
    @Excel(name = "新增被叫前缀")
    private String calleePre;

    /** 被叫前缀删除位数 */
    @Excel(name = "被叫前缀删除位数")
    private Long calleePreRemove;

    /** DID */
    private String did;

    /** 被叫检测 */
    private String calleeCheck;

    /** 呼出主叫转换号码 */
    private String transform;

    /** 路由名称 */
    @Excel(name = "路由名称")
    private String name;

    /**
     * 目的地类型：extension/ivr/queue/conference/voicemail/gateway/app。
     * 参考 FusionPBX 的 destination：路由只描述「从哪个号码来」，由目的地决定「到哪里去」。
     */
    @Excel(name = "目的地类型")
    private String destinationType;

    /** 目的地值：分机号 / IVR 名 / 队列名 / 会议室名 / 网关名 / app:data */
    @Excel(name = "目的地")
    private String destinationValue;

    /** 编译后的 dialplan 匹配表达式（不落库，由 RouteDialplanCompiler 填充） */
    private String dialplanExpr;

    /** 编译后的 action 列表（不落库，模板只负责输出） */
    private List<TPbxDialplanAction> actions;

    @JsonProperty("changenums")
    private List<TPbxChangeNumber> changenums;

    /** dialplan context */
    private String context;

    private Integer enabled;

    private Integer sortOrder;

}
