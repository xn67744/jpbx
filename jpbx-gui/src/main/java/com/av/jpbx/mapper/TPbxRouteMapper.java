package com.av.jpbx.mapper;

import java.util.List;
import com.av.jpbx.domain.TPbxRoute;

/**
 * 路由管理Mapper接口
 * 
 * @author long
 * @date 2024-02-11
 */
public interface TPbxRouteMapper 
{
    /**
     * 查询路由管理
     * 
     * @param id 路由管理主键
     * @return 路由管理
     */
    public TPbxRoute selectTPbxRouteById(String id);

    /**
     * 查询路由管理列表
     * 
     * @param tPbxRoute 路由管理
     * @return 路由管理集合
     */
    public List<TPbxRoute> selectTPbxRouteList(TPbxRoute tPbxRoute);

    /**
     * 新增路由管理
     * 
     * @param tPbxRoute 路由管理
     * @return 结果
     */
    public int insertTPbxRoute(TPbxRoute tPbxRoute);

    /**
     * 修改路由管理
     * 
     * @param tPbxRoute 路由管理
     * @return 结果
     */
    public int updateTPbxRoute(TPbxRoute tPbxRoute);

    /**
     * 删除路由管理
     * 
     * @param id 路由管理主键
     * @return 结果
     */
    public int deleteTPbxRouteById(String id);

    /**
     * 批量删除路由管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTPbxRouteByIds(String[] ids);
}
