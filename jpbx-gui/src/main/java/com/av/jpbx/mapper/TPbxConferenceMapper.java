package com.av.jpbx.mapper;

import java.util.List;
import com.av.jpbx.domain.TPbxConference;

public interface TPbxConferenceMapper {
    TPbxConference selectTPbxConferenceById(String id);
    List<TPbxConference> selectTPbxConferenceList(TPbxConference query);
    int insertTPbxConference(TPbxConference row);
    int updateTPbxConference(TPbxConference row);
    int deleteTPbxConferenceById(String id);
    int deleteTPbxConferenceByIds(String[] ids);
}
