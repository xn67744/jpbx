package com.av.jpbx.service;

import java.util.List;
import com.av.jpbx.domain.TPbxAclList;

public interface ITPbxAclListService {
    TPbxAclList selectTPbxAclListById(String id);
    List<TPbxAclList> selectTPbxAclListList(TPbxAclList query);
    int insertTPbxAclList(TPbxAclList row);
    int updateTPbxAclList(TPbxAclList row);
    int deleteTPbxAclListByIds(String ids);
}
