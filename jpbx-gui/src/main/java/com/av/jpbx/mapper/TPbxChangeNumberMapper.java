package com.av.jpbx.mapper;

import com.av.jpbx.domain.TPbxChangeNumber;
import com.av.jpbx.domain.TPbxGateway;

import java.util.List;

/**
 * Mapper接口
 * 
 * @author long
 * @date 2024-02-13
 */
public interface TPbxChangeNumberMapper
{
    /**
     * 新增呼叫中继
     *
     * @param tPbxChangeNumber 换号
     * @return 结果
     */
    public List<TPbxChangeNumber> selectChangeNumbes(TPbxChangeNumber tPbxChangeNumber);
    /**
     * 新增呼叫中继
     * 
     * @param TPbxChangeNumber 换号
     * @return 结果
     */
    public int insertChangeNumbes(TPbxChangeNumber tPbxChangeNumber);

    /**
     *
     *
     * @param routeId 路由主键
     * @return 结果
     */
    public int deleteTPbxChangeNumberByRouteId(String routeId);

    /**
     *
     *
     * @param id 呼叫中继主键
     * @return 结果
     */
    public int deleteTPbxChangeNumberById(String id);

    /**
     * 批量删除
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTPbxChangeNumberByIds(String[] ids);



}
