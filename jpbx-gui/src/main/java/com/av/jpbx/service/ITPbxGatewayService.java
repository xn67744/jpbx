package com.av.jpbx.service;

import java.util.List;
import com.av.jpbx.domain.TPbxGateway;

/**
 * 呼叫中继Service接口
 * 
 * @author long
 * @date 2024-02-08
 */
public interface ITPbxGatewayService 
{
    /**
     * 查询呼叫中继
     * 
     * @param id 呼叫中继主键
     * @return 呼叫中继
     */
    public TPbxGateway selectTPbxGatewayById(String id);

    /**
     * 查询呼叫中继列表
     * 
     * @param tPbxGateway 呼叫中继
     * @return 呼叫中继集合
     */
    public List<TPbxGateway> selectTPbxGatewayList(TPbxGateway tPbxGateway);

    /**
     * 新增呼叫中继
     * 
     * @param tPbxGateway 呼叫中继
     * @return 结果
     */
    public int insertTPbxGateway(TPbxGateway tPbxGateway);

    /**
     * 修改呼叫中继
     * 
     * @param tPbxGateway 呼叫中继
     * @return 结果
     */
    public int updateTPbxGateway(TPbxGateway tPbxGateway);

    /**
     * 批量删除呼叫中继
     * 
     * @param ids 需要删除的呼叫中继主键集合
     * @return 结果
     */
    public int deleteTPbxGatewayByIds(String ids);

    /**
     * 删除呼叫中继信息
     * 
     * @param id 呼叫中继主键
     * @return 结果
     */
    public int deleteTPbxGatewayById(String id);

    String getSofiaExternalXML();

    String getSofiaInternalXML();

    String getSwitchXML();

    String getSofiaExternal6XML();

    String getSofiaInternal6XML();

    String getCdrXML();
}
