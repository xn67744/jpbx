package com.av.jpbx.mapper;

import java.util.List;
import com.av.jpbx.domain.TPbxTier;

public interface TPbxTierMapper {
    TPbxTier selectTPbxTierById(String id);
    List<TPbxTier> selectTPbxTierList(TPbxTier query);
    int insertTPbxTier(TPbxTier row);
    int updateTPbxTier(TPbxTier row);
    int deleteTPbxTierById(String id);
    int deleteTPbxTierByIds(String[] ids);
}
