package com.av.jpbx.mapper;

import java.util.List;
import com.av.jpbx.domain.TPbxAgent;

public interface TPbxAgentMapper {
    TPbxAgent selectTPbxAgentById(String id);
    List<TPbxAgent> selectTPbxAgentList(TPbxAgent query);
    int insertTPbxAgent(TPbxAgent row);
    int updateTPbxAgent(TPbxAgent row);
    int deleteTPbxAgentById(String id);
    int deleteTPbxAgentByIds(String[] ids);
}
