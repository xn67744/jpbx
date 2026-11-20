package com.av.jpbx.service;

import java.util.List;
import com.av.jpbx.domain.TPbxGroup;

public interface ITPbxGroupService {
    TPbxGroup selectTPbxGroupById(String id);
    List<TPbxGroup> selectTPbxGroupList(TPbxGroup query);
    int insertTPbxGroup(TPbxGroup row);
    int updateTPbxGroup(TPbxGroup row);
    int deleteTPbxGroupByIds(String ids);
}
