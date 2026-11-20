package com.av.jpbx.mapper;

import java.util.List;
import com.av.jpbx.domain.TPbxQueue;

public interface TPbxQueueMapper {
    TPbxQueue selectTPbxQueueById(String id);
    List<TPbxQueue> selectTPbxQueueList(TPbxQueue query);
    int insertTPbxQueue(TPbxQueue row);
    int updateTPbxQueue(TPbxQueue row);
    int deleteTPbxQueueById(String id);
    int deleteTPbxQueueByIds(String[] ids);
}
