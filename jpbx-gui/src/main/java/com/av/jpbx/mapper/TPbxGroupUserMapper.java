package com.av.jpbx.mapper;

import java.util.List;
import com.av.jpbx.domain.TPbxGroupUser;

public interface TPbxGroupUserMapper {
    TPbxGroupUser selectTPbxGroupUserById(String id);
    List<TPbxGroupUser> selectTPbxGroupUserList(TPbxGroupUser query);
    int insertTPbxGroupUser(TPbxGroupUser row);
    int updateTPbxGroupUser(TPbxGroupUser row);
    int deleteTPbxGroupUserById(String id);
    int deleteTPbxGroupUserByIds(String[] ids);

    int deleteByGroupId(String groupId);
}
