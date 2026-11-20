package com.av.jpbx.service.impl;

import java.util.List;
import cn.hutool.core.lang.Snowflake;
import com.av.common.core.text.Convert;
import com.av.common.utils.StringUtils;
import com.av.jpbx.domain.TPbxDomain;
import com.av.jpbx.enums.FsResourceType;
import com.av.jpbx.mapper.TPbxDomainMapper;
import com.av.jpbx.service.FsApplyService;
import com.av.jpbx.service.ITPbxDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TPbxDomainServiceImpl implements ITPbxDomainService {
    @Autowired private TPbxDomainMapper mapper;
    @Autowired private Snowflake snowflake;
    @Autowired private FsApplyService fsApplyService;

    @Override public TPbxDomain selectTPbxDomainById(String id) { return mapper.selectTPbxDomainById(id); }
    @Override public List<TPbxDomain> selectTPbxDomainList(TPbxDomain query) { return mapper.selectTPbxDomainList(query); }

    @Override
    public int insertTPbxDomain(TPbxDomain row) {
        if (StringUtils.isBlank(row.getId())) { row.setId(snowflake.nextIdStr()); }
        int r = mapper.insertTPbxDomain(row);
        if (r > 0) { fsApplyService.apply(FsResourceType.DIRECTORY); }
        return r;
    }

    @Override
    public int updateTPbxDomain(TPbxDomain row) {
        int r = mapper.updateTPbxDomain(row);
        if (r > 0) { fsApplyService.apply(FsResourceType.DIRECTORY); }
        return r;
    }

    @Override
    public int deleteTPbxDomainByIds(String ids) {
        int r = mapper.deleteTPbxDomainByIds(Convert.toStrArray(ids));
        if (r > 0) { fsApplyService.apply(FsResourceType.DIRECTORY); }
        return r;
    }
}
