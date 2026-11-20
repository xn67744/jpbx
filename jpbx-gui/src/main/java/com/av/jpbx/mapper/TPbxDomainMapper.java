package com.av.jpbx.mapper;

import java.util.List;
import com.av.jpbx.domain.TPbxDomain;

public interface TPbxDomainMapper {
    TPbxDomain selectTPbxDomainById(String id);
    List<TPbxDomain> selectTPbxDomainList(TPbxDomain query);
    int insertTPbxDomain(TPbxDomain row);
    int updateTPbxDomain(TPbxDomain row);
    int deleteTPbxDomainById(String id);
    int deleteTPbxDomainByIds(String[] ids);
}
