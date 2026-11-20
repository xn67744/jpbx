package com.av.jpbx.service;

import java.util.List;
import com.av.jpbx.domain.TPbxConference;

public interface ITPbxConferenceService {
    TPbxConference selectTPbxConferenceById(String id);
    List<TPbxConference> selectTPbxConferenceList(TPbxConference query);
    int insertTPbxConference(TPbxConference row);
    int updateTPbxConference(TPbxConference row);
    int deleteTPbxConferenceByIds(String ids);
}
