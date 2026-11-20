package com.av.jpbx.service;

import java.util.List;
import com.av.jpbx.domain.TPbxDialplan;

public interface ITPbxDialplanService {
    TPbxDialplan selectTPbxDialplanById(String id);
    List<TPbxDialplan> selectTPbxDialplanList(TPbxDialplan query);
    int insertTPbxDialplan(TPbxDialplan row);
    int updateTPbxDialplan(TPbxDialplan row);
    int deleteTPbxDialplanByIds(String ids);
}
