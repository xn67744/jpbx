package com.av.jpbx.service.impl;

import java.util.List;

import cn.hutool.core.lang.Snowflake;
import com.av.jpbx.domain.TPbxChangeNumber;
import com.av.jpbx.mapper.TPbxChangeNumberMapper;
import com.av.jpbx.vo.TPbxRouteVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.av.jpbx.mapper.TPbxRouteMapper;
import com.av.jpbx.domain.TPbxRoute;
import com.av.jpbx.service.ITPbxRouteService;
import com.av.common.core.text.Convert;

/**
 * 路由管理Service业务层处理
 * 
 * @author long
 * @date 2024-02-11
 */
@Service
public class TPbxRouteServiceImpl implements ITPbxRouteService {
    @Autowired
    private TPbxRouteMapper tPbxRouteMapper;

    @Autowired
    private TPbxChangeNumberMapper changeNumberMapper;

    @Autowired
    private Snowflake snowflake;

    @Override
    public TPbxRoute selectTPbxRouteById(String id)
    {
        TPbxRoute route = tPbxRouteMapper.selectTPbxRouteById(id);
        route.setChangenums(changeNumberMapper.selectChangeNumbes(new TPbxChangeNumber().setRouteId(id)));
        return route;
    }

    /**
     * 查询路由管理列表
     * 
     * @param tPbxRoute 路由管理
     * @return 路由管理
     */
    @Override
    public List<TPbxRoute> selectTPbxRouteList(TPbxRoute tPbxRoute)
    {
        return tPbxRouteMapper.selectTPbxRouteList(tPbxRoute);
    }

    /**
     * 新增路由管理
     * 
     * @param tPbxRoute 路由管理
     * @return 结果
     */
    @Override
    public int insertTPbxRoute(TPbxRoute tPbxRoute)
    {
        tPbxRoute.setId(snowflake.nextIdStr());
        int result = tPbxRouteMapper.insertTPbxRoute(tPbxRoute);
        if(CollectionUtils.isNotEmpty(tPbxRoute.getChangenums())){
            for(TPbxChangeNumber changeNumber : tPbxRoute.getChangenums()){
                changeNumberMapper.insertChangeNumbes(changeNumber.setId(snowflake.nextIdStr()).setRouteId(tPbxRoute.getId()));
            }
        }
        return result;
    }

    /**
     * 修改路由管理
     * 
     * @param tPbxRoute 路由管理
     * @return 结果
     */
    @Override
    public int updateTPbxRoute(TPbxRoute tPbxRoute)
    {
        return tPbxRouteMapper.updateTPbxRoute(tPbxRoute);
    }

    /**
     * 批量删除路由管理
     * 
     * @param ids 需要删除的路由管理主键
     * @return 结果
     */
    @Override
    public int deleteTPbxRouteByIds(String ids)
    {
        return tPbxRouteMapper.deleteTPbxRouteByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除路由管理信息
     * 
     * @param id 路由管理主键
     * @return 结果
     */
    @Override
    public int deleteTPbxRouteById(String id)
    {
        return tPbxRouteMapper.deleteTPbxRouteById(id);
    }

}
