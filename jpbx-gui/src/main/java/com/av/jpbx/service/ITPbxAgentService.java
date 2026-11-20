package com.av.jpbx.service;

import java.util.List;
import com.av.jpbx.domain.TPbxAgent;

public interface ITPbxAgentService {
    TPbxAgent selectTPbxAgentById(String id);
    List<TPbxAgent> selectTPbxAgentList(TPbxAgent query);
    int insertTPbxAgent(TPbxAgent row);
    int updateTPbxAgent(TPbxAgent row);
    int deleteTPbxAgentByIds(String ids);
}
