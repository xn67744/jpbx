package com.av.jpbx.service.impl;

import java.util.List;
import cn.hutool.core.lang.Snowflake;
import com.av.common.core.text.Convert;
import com.av.common.utils.StringUtils;
import com.av.jpbx.domain.TPbxAgent;
import com.av.jpbx.enums.FsResourceType;
import com.av.jpbx.mapper.TPbxAgentMapper;
import com.av.jpbx.service.FsApplyService;
import com.av.jpbx.service.ITPbxAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TPbxAgentServiceImpl implements ITPbxAgentService {
    @Autowired private TPbxAgentMapper mapper;
    @Autowired private Snowflake snowflake;
    @Autowired private FsApplyService fsApplyService;

    @Override public TPbxAgent selectTPbxAgentById(String id) { return mapper.selectTPbxAgentById(id); }
    @Override public List<TPbxAgent> selectTPbxAgentList(TPbxAgent query) { return mapper.selectTPbxAgentList(query); }

    @Override
    public int insertTPbxAgent(TPbxAgent row) {
        if (StringUtils.isBlank(row.getId())) { row.setId(snowflake.nextIdStr()); }
        int r = mapper.insertTPbxAgent(row);
        if (r > 0) { fsApplyService.apply(FsResourceType.CALLCENTER); }
        return r;
    }

    @Override
    public int updateTPbxAgent(TPbxAgent row) {
        int r = mapper.updateTPbxAgent(row);
        if (r > 0) { fsApplyService.apply(FsResourceType.CALLCENTER); }
        return r;
    }

    @Override
    public int deleteTPbxAgentByIds(String ids) {
        int r = mapper.deleteTPbxAgentByIds(Convert.toStrArray(ids));
        if (r > 0) { fsApplyService.apply(FsResourceType.CALLCENTER); }
        return r;
    }
}
