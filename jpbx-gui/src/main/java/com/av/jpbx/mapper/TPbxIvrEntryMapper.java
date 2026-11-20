package com.av.jpbx.mapper;

import java.util.List;
import com.av.jpbx.domain.TPbxIvrEntry;

public interface TPbxIvrEntryMapper {
    TPbxIvrEntry selectTPbxIvrEntryById(String id);
    List<TPbxIvrEntry> selectTPbxIvrEntryList(TPbxIvrEntry query);
    int insertTPbxIvrEntry(TPbxIvrEntry row);
    int updateTPbxIvrEntry(TPbxIvrEntry row);
    int deleteTPbxIvrEntryById(String id);
    int deleteTPbxIvrEntryByIds(String[] ids);

    int deleteByIvrId(String ivrId);
}
