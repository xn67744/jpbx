package com.av.jpbx.service.impl;

import java.util.List;

import cn.hutool.core.lang.Snowflake;
import com.av.common.core.text.Convert;
import com.av.common.utils.StringUtils;
import com.av.jpbx.domain.TPbxDialplan;
import com.av.jpbx.domain.TPbxDialplanAction;
import com.av.jpbx.enums.FsResourceType;
import com.av.jpbx.mapper.TPbxDialplanActionMapper;
import com.av.jpbx.mapper.TPbxDialplanMapper;
import com.av.jpbx.service.FsApplyService;
import com.av.jpbx.service.ITPbxDialplanService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TPbxDialplanServiceImpl implements ITPbxDialplanService {
    @Autowired private TPbxDialplanMapper mapper;
    @Autowired private TPbxDialplanActionMapper actionMapper;
    @Autowired private Snowflake snowflake;
    @Autowired private FsApplyService fsApplyService;

    @Override
    public TPbxDialplan selectTPbxDialplanById(String id) {
        TPbxDialplan dp = mapper.selectTPbxDialplanById(id);
        if (dp != null) {
            dp.setActions(actionMapper.selectTPbxDialplanActionList(new TPbxDialplanAction().setDialplanId(id)));
        }
        return dp;
    }

    @Override
    public List<TPbxDialplan> selectTPbxDialplanList(TPbxDialplan query) {
        return mapper.selectTPbxDialplanList(query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertTPbxDialplan(TPbxDialplan row) {
        if (StringUtils.isBlank(row.getId())) {
            row.setId(snowflake.nextIdStr());
        }
        if (StringUtils.isEmpty(row.getContext())) {
            row.setContext("default");
        }
        if (StringUtils.isEmpty(row.getConditionField())) {
            row.setConditionField("destination_number");
        }
        if (row.getEnabled() == null) {
            row.setEnabled(1);
        }
        int r = mapper.insertTPbxDialplan(row);
        saveActions(row);
        if (r > 0) {
            fsApplyService.apply(FsResourceType.DIALPLAN);
        }
        return r;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateTPbxDialplan(TPbxDialplan row) {
        int r = mapper.updateTPbxDialplan(row);
        if (row.getActions() != null) {
            actionMapper.deleteByDialplanId(row.getId());
            saveActions(row);
        }
        if (r > 0) {
            fsApplyService.apply(FsResourceType.DIALPLAN);
        }
        return r;
    }

    private void saveActions(TPbxDialplan row) {
        if (CollectionUtils.isNotEmpty(row.getActions())) {
            int i = 10;
            for (TPbxDialplanAction a : row.getActions()) {
                if (StringUtils.isBlank(a.getId())) {
                    a.setId(snowflake.nextIdStr());
                }
                a.setDialplanId(row.getId());
                if (a.getSortOrder() == null) {
                    a.setSortOrder(i);
                }
                i += 10;
                actionMapper.insertTPbxDialplanAction(a);
            }
        }
    }

    @Override
    public int deleteTPbxDialplanByIds(String ids) {
        String[] arr = Convert.toStrArray(ids);
        for (String id : arr) {
            actionMapper.deleteByDialplanId(id);
        }
        int r = mapper.deleteTPbxDialplanByIds(arr);
        if (r > 0) {
            fsApplyService.apply(FsResourceType.DIALPLAN);
        }
        return r;
    }
}
