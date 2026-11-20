package com.av.jpbx.mapper;

import java.util.List;
import com.av.jpbx.domain.TPbxDialplanAction;

public interface TPbxDialplanActionMapper {
    TPbxDialplanAction selectTPbxDialplanActionById(String id);
    List<TPbxDialplanAction> selectTPbxDialplanActionList(TPbxDialplanAction query);
    int insertTPbxDialplanAction(TPbxDialplanAction row);
    int updateTPbxDialplanAction(TPbxDialplanAction row);
    int deleteTPbxDialplanActionById(String id);
    int deleteTPbxDialplanActionByIds(String[] ids);

    int deleteByDialplanId(String dialplanId);
}
