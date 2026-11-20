package com.av.jpbx.service;

import java.util.List;
import com.av.jpbx.domain.TPbxDomain;

public interface ITPbxDomainService {
    TPbxDomain selectTPbxDomainById(String id);
    List<TPbxDomain> selectTPbxDomainList(TPbxDomain query);
    int insertTPbxDomain(TPbxDomain row);
    int updateTPbxDomain(TPbxDomain row);
    int deleteTPbxDomainByIds(String ids);
}
