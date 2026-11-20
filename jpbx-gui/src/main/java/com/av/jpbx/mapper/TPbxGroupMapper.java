package com.av.jpbx.mapper;

import java.util.List;
import com.av.jpbx.domain.TPbxGroup;

public interface TPbxGroupMapper {
    TPbxGroup selectTPbxGroupById(String id);
    List<TPbxGroup> selectTPbxGroupList(TPbxGroup query);
    int insertTPbxGroup(TPbxGroup row);
    int updateTPbxGroup(TPbxGroup row);
    int deleteTPbxGroupById(String id);
    int deleteTPbxGroupByIds(String[] ids);
}
