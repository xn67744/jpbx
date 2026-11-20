package com.av.jpbx.mapper;

import java.util.List;
import com.av.jpbx.domain.TPbxDialplan;

public interface TPbxDialplanMapper {
    TPbxDialplan selectTPbxDialplanById(String id);
    List<TPbxDialplan> selectTPbxDialplanList(TPbxDialplan query);
    int insertTPbxDialplan(TPbxDialplan row);
    int updateTPbxDialplan(TPbxDialplan row);
    int deleteTPbxDialplanById(String id);
    int deleteTPbxDialplanByIds(String[] ids);
}
