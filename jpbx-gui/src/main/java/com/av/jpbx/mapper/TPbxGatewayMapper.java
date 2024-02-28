package com.av.jpbx.mapper;

import java.util.List;
import com.av.jpbx.domain.TPbxGateway;

/**
 * 呼叫中继Mapper接口
 * 
 * @author long
 * @date 2024-02-08
 */
public interface TPbxGatewayMapper 
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
     * 删除呼叫中继
     * 
     * @param id 呼叫中继主键
     * @return 结果
     */
    public int deleteTPbxGatewayById(String id);

    /**
     * 批量删除呼叫中继
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTPbxGatewayByIds(String[] ids);
}
