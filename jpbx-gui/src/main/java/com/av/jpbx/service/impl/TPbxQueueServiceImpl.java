package com.av.jpbx.service.impl;

import java.util.List;
import cn.hutool.core.lang.Snowflake;
import com.av.common.core.text.Convert;
import com.av.common.utils.StringUtils;
import com.av.jpbx.domain.TPbxQueue;
import com.av.jpbx.enums.FsResourceType;
import com.av.jpbx.mapper.TPbxQueueMapper;
import com.av.jpbx.service.FsApplyService;
import com.av.jpbx.service.ITPbxQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TPbxQueueServiceImpl implements ITPbxQueueService {
    @Autowired private TPbxQueueMapper mapper;
    @Autowired private Snowflake snowflake;
    @Autowired private FsApplyService fsApplyService;

    @Override public TPbxQueue selectTPbxQueueById(String id) { return mapper.selectTPbxQueueById(id); }
    @Override public List<TPbxQueue> selectTPbxQueueList(TPbxQueue query) { return mapper.selectTPbxQueueList(query); }

    @Override
    public int insertTPbxQueue(TPbxQueue row) {
        if (StringUtils.isBlank(row.getId())) { row.setId(snowflake.nextIdStr()); }
        int r = mapper.insertTPbxQueue(row);
        if (r > 0) { fsApplyService.apply(FsResourceType.CALLCENTER); }
        return r;
    }

    @Override
    public int updateTPbxQueue(TPbxQueue row) {
        int r = mapper.updateTPbxQueue(row);
        if (r > 0) { fsApplyService.apply(FsResourceType.CALLCENTER); }
        return r;
    }

    @Override
    public int deleteTPbxQueueByIds(String ids) {
        int r = mapper.deleteTPbxQueueByIds(Convert.toStrArray(ids));
        if (r > 0) { fsApplyService.apply(FsResourceType.CALLCENTER); }
        return r;
    }
}
