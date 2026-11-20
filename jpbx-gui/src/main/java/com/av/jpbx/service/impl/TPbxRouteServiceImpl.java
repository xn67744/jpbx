package com.av.jpbx.service.impl;

import java.util.List;

import cn.hutool.core.lang.Snowflake;
import com.av.common.core.text.Convert;
import com.av.common.utils.StringUtils;
import com.av.jpbx.domain.TPbxChangeNumber;
import com.av.jpbx.domain.TPbxRoute;
import com.av.jpbx.enums.FsResourceType;
import com.av.jpbx.mapper.TPbxChangeNumberMapper;
import com.av.jpbx.mapper.TPbxRouteMapper;
import com.av.jpbx.service.FsApplyService;
import com.av.jpbx.service.ITPbxRouteService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TPbxRouteServiceImpl implements ITPbxRouteService {
    @Autowired
    private TPbxRouteMapper tPbxRouteMapper;
    @Autowired
    private TPbxChangeNumberMapper changeNumberMapper;
    @Autowired
    private Snowflake snowflake;
    @Autowired
    private FsApplyService fsApplyService;

    @Override
    public TPbxRoute selectTPbxRouteById(String id) {
        TPbxRoute route = tPbxRouteMapper.selectTPbxRouteById(id);
        if (route != null) {
            route.setChangenums(changeNumberMapper.selectChangeNumbes(new TPbxChangeNumber().setRouteId(id)));
        }
        return route;
    }

    @Override
    public List<TPbxRoute> selectTPbxRouteList(TPbxRoute tPbxRoute) {
        return tPbxRouteMapper.selectTPbxRouteList(tPbxRoute);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertTPbxRoute(TPbxRoute tPbxRoute) {
        tPbxRoute.setId(snowflake.nextIdStr());
        applyDefaults(tPbxRoute);
        int result = tPbxRouteMapper.insertTPbxRoute(tPbxRoute);
        saveChangeNumbers(tPbxRoute);
        if (result > 0) {
            fsApplyService.apply(FsResourceType.DIALPLAN);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateTPbxRoute(TPbxRoute tPbxRoute) {
        applyDefaults(tPbxRoute);
        int result = tPbxRouteMapper.updateTPbxRoute(tPbxRoute);
        // 同步号码变换：先删后插
        if (tPbxRoute.getChangenums() != null) {
            changeNumberMapper.deleteTPbxChangeNumberByRouteId(tPbxRoute.getId());
            saveChangeNumbers(tPbxRoute);
        }
        if (result > 0) {
            fsApplyService.apply(FsResourceType.DIALPLAN);
        }
        return result;
    }

    /**
     * 补齐路由默认值：类型 1=呼出 2=呼入 0=内部分机；
     * 呼入路由必须落在 public 上下文，否则 FreeSWITCH 拉不到对应 dialplan。
     */
    private void applyDefaults(TPbxRoute route) {
        if (route.getEnabled() == null) {
            route.setEnabled(1);
        }
        if (route.getType() == null) {
            route.setType(1L);
        }
        if (route.getSortOrder() == null) {
            route.setSortOrder(100);
        }
        if (StringUtils.isEmpty(route.getContext())) {
            route.setContext(route.getType() == 2L ? "public" : "default");
        }
        // 呼入模式未填匹配前缀时，按 DID 生成
        if (route.getType() == 2L && StringUtils.isEmpty(route.getReg())
                && StringUtils.isNotEmpty(route.getDid())) {
            route.setReg("^" + route.getDid() + "$");
        }
    }

    private void saveChangeNumbers(TPbxRoute tPbxRoute) {
        if (CollectionUtils.isNotEmpty(tPbxRoute.getChangenums())) {
            for (TPbxChangeNumber changeNumber : tPbxRoute.getChangenums()) {
                changeNumberMapper.insertChangeNumbes(
                        changeNumber.setId(snowflake.nextIdStr()).setRouteId(tPbxRoute.getId()));
            }
        }
    }

    @Override
    public int deleteTPbxRouteByIds(String ids) {
        int result = tPbxRouteMapper.deleteTPbxRouteByIds(Convert.toStrArray(ids));
        if (result > 0) {
            fsApplyService.apply(FsResourceType.DIALPLAN);
        }
        return result;
    }

    @Override
    public int deleteTPbxRouteById(String id) {
        int result = tPbxRouteMapper.deleteTPbxRouteById(id);
        if (result > 0) {
            fsApplyService.apply(FsResourceType.DIALPLAN);
        }
        return result;
    }
}
