package com.av.jpbx.service.impl;

import java.util.List;
import cn.hutool.core.lang.Snowflake;
import com.av.common.core.text.Convert;
import com.av.common.utils.StringUtils;
import com.av.jpbx.domain.TPbxConference;
import com.av.jpbx.enums.FsResourceType;
import com.av.jpbx.mapper.TPbxConferenceMapper;
import com.av.jpbx.service.FsApplyService;
import com.av.jpbx.service.ITPbxConferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TPbxConferenceServiceImpl implements ITPbxConferenceService {
    @Autowired private TPbxConferenceMapper mapper;
    @Autowired private Snowflake snowflake;
    @Autowired private FsApplyService fsApplyService;

    @Override public TPbxConference selectTPbxConferenceById(String id) { return mapper.selectTPbxConferenceById(id); }
    @Override public List<TPbxConference> selectTPbxConferenceList(TPbxConference query) { return mapper.selectTPbxConferenceList(query); }

    @Override
    public int insertTPbxConference(TPbxConference row) {
        if (StringUtils.isBlank(row.getId())) { row.setId(snowflake.nextIdStr()); }
        int r = mapper.insertTPbxConference(row);
        if (r > 0) { fsApplyService.apply(FsResourceType.CONFERENCE); }
        return r;
    }

    @Override
    public int updateTPbxConference(TPbxConference row) {
        int r = mapper.updateTPbxConference(row);
        if (r > 0) { fsApplyService.apply(FsResourceType.CONFERENCE); }
        return r;
    }

    @Override
    public int deleteTPbxConferenceByIds(String ids) {
        int r = mapper.deleteTPbxConferenceByIds(Convert.toStrArray(ids));
        if (r > 0) { fsApplyService.apply(FsResourceType.CONFERENCE); }
        return r;
    }
}
