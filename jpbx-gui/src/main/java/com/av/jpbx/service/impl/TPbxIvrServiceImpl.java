package com.av.jpbx.service.impl;

import java.util.List;

import cn.hutool.core.lang.Snowflake;
import com.av.common.core.text.Convert;
import com.av.common.utils.StringUtils;
import com.av.jpbx.domain.TPbxIvr;
import com.av.jpbx.domain.TPbxIvrEntry;
import com.av.jpbx.enums.FsResourceType;
import com.av.jpbx.mapper.TPbxIvrEntryMapper;
import com.av.jpbx.mapper.TPbxIvrMapper;
import com.av.jpbx.service.FsApplyService;
import com.av.jpbx.service.ITPbxIvrService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TPbxIvrServiceImpl implements ITPbxIvrService {
    @Autowired private TPbxIvrMapper mapper;
    @Autowired private TPbxIvrEntryMapper entryMapper;
    @Autowired private Snowflake snowflake;
    @Autowired private FsApplyService fsApplyService;

    @Override
    public TPbxIvr selectTPbxIvrById(String id) {
        TPbxIvr ivr = mapper.selectTPbxIvrById(id);
        if (ivr != null) {
            ivr.setEntries(entryMapper.selectTPbxIvrEntryList(new TPbxIvrEntry().setIvrId(id)));
        }
        return ivr;
    }

    @Override
    public List<TPbxIvr> selectTPbxIvrList(TPbxIvr query) {
        return mapper.selectTPbxIvrList(query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertTPbxIvr(TPbxIvr row) {
        if (StringUtils.isBlank(row.getId())) {
            row.setId(snowflake.nextIdStr());
        }
        if (row.getEnabled() == null) {
            row.setEnabled(1);
        }
        int r = mapper.insertTPbxIvr(row);
        saveEntries(row);
        if (r > 0) {
            fsApplyService.apply(FsResourceType.IVR);
        }
        return r;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateTPbxIvr(TPbxIvr row) {
        int r = mapper.updateTPbxIvr(row);
        if (row.getEntries() != null) {
            entryMapper.deleteByIvrId(row.getId());
            saveEntries(row);
        }
        if (r > 0) {
            fsApplyService.apply(FsResourceType.IVR);
        }
        return r;
    }

    private void saveEntries(TPbxIvr row) {
        if (CollectionUtils.isNotEmpty(row.getEntries())) {
            int i = 10;
            for (TPbxIvrEntry e : row.getEntries()) {
                if (StringUtils.isBlank(e.getId())) {
                    e.setId(snowflake.nextIdStr());
                }
                e.setIvrId(row.getId());
                if (e.getSortOrder() == null) {
                    e.setSortOrder(i);
                }
                i += 10;
                entryMapper.insertTPbxIvrEntry(e);
            }
        }
    }

    @Override
    public int deleteTPbxIvrByIds(String ids) {
        String[] arr = Convert.toStrArray(ids);
        for (String id : arr) {
            entryMapper.deleteByIvrId(id);
        }
        int r = mapper.deleteTPbxIvrByIds(arr);
        if (r > 0) {
            fsApplyService.apply(FsResourceType.IVR);
        }
        return r;
    }
}
