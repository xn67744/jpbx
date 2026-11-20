package com.av.jpbx.service;

import java.util.List;
import com.av.jpbx.domain.TPbxIvr;

public interface ITPbxIvrService {
    TPbxIvr selectTPbxIvrById(String id);
    List<TPbxIvr> selectTPbxIvrList(TPbxIvr query);
    int insertTPbxIvr(TPbxIvr row);
    int updateTPbxIvr(TPbxIvr row);
    int deleteTPbxIvrByIds(String ids);
}
