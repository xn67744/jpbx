package com.av.jpbx.service;

import java.util.List;
import com.av.jpbx.domain.TPbxQueue;

public interface ITPbxQueueService {
    TPbxQueue selectTPbxQueueById(String id);
    List<TPbxQueue> selectTPbxQueueList(TPbxQueue query);
    int insertTPbxQueue(TPbxQueue row);
    int updateTPbxQueue(TPbxQueue row);
    int deleteTPbxQueueByIds(String ids);
}
