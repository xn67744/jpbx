package com.av.jpbx.mapper;

import java.util.List;
import com.av.jpbx.domain.TPbxAclNode;

public interface TPbxAclNodeMapper {
    TPbxAclNode selectTPbxAclNodeById(String id);
    List<TPbxAclNode> selectTPbxAclNodeList(TPbxAclNode query);
    int insertTPbxAclNode(TPbxAclNode row);
    int updateTPbxAclNode(TPbxAclNode row);
    int deleteTPbxAclNodeById(String id);
    int deleteTPbxAclNodeByIds(String[] ids);

    int deleteByListId(String listId);
}
