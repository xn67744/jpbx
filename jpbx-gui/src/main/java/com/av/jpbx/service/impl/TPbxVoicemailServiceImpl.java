package com.av.jpbx.service.impl;

import java.util.List;
import cn.hutool.core.lang.Snowflake;
import com.av.common.core.text.Convert;
import com.av.common.utils.StringUtils;
import com.av.jpbx.domain.TPbxVoicemail;
import com.av.jpbx.enums.FsResourceType;
import com.av.jpbx.mapper.TPbxVoicemailMapper;
import com.av.jpbx.service.FsApplyService;
import com.av.jpbx.service.ITPbxVoicemailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TPbxVoicemailServiceImpl implements ITPbxVoicemailService {
    @Autowired private TPbxVoicemailMapper mapper;
    @Autowired private Snowflake snowflake;
    @Autowired private FsApplyService fsApplyService;

    @Override public TPbxVoicemail selectTPbxVoicemailById(String id) { return mapper.selectTPbxVoicemailById(id); }
    @Override public List<TPbxVoicemail> selectTPbxVoicemailList(TPbxVoicemail query) { return mapper.selectTPbxVoicemailList(query); }

    @Override
    public int insertTPbxVoicemail(TPbxVoicemail row) {
        if (StringUtils.isBlank(row.getId())) { row.setId(snowflake.nextIdStr()); }
        int r = mapper.insertTPbxVoicemail(row);
        if (r > 0) { fsApplyService.apply(FsResourceType.VOICEMAIL); }
        return r;
    }

    @Override
    public int updateTPbxVoicemail(TPbxVoicemail row) {
        int r = mapper.updateTPbxVoicemail(row);
        if (r > 0) { fsApplyService.apply(FsResourceType.VOICEMAIL); }
        return r;
    }

    @Override
    public int deleteTPbxVoicemailByIds(String ids) {
        int r = mapper.deleteTPbxVoicemailByIds(Convert.toStrArray(ids));
        if (r > 0) { fsApplyService.apply(FsResourceType.VOICEMAIL); }
        return r;
    }
}
