package com.av.jpbx.service;

import java.util.List;
import com.av.jpbx.domain.TPbxRoute;
import com.av.jpbx.vo.TPbxRouteVO;

/**
 * 路由管理Service接口
 * 
 * @author long
 * @date 2024-02-11
 */
public interface ITPbxRouteService 
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
     * 批量删除路由管理
     * 
     * @param ids 需要删除的路由管理主键集合
     * @return 结果
     */
    public int deleteTPbxRouteByIds(String ids);

    /**
     * 删除路由管理信息
     * 
     * @param id 路由管理主键
     * @return 结果
     */
    public int deleteTPbxRouteById(String id);

}
