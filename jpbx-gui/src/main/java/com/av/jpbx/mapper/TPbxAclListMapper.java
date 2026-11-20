package com.av.jpbx.mapper;

import java.util.List;
import com.av.jpbx.domain.TPbxAclList;

public interface TPbxAclListMapper {
    TPbxAclList selectTPbxAclListById(String id);
    List<TPbxAclList> selectTPbxAclListList(TPbxAclList query);
    int insertTPbxAclList(TPbxAclList row);
    int updateTPbxAclList(TPbxAclList row);
    int deleteTPbxAclListById(String id);
    int deleteTPbxAclListByIds(String[] ids);
}
