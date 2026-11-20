package com.av.jpbx.mapper;

import java.util.List;
import com.av.jpbx.domain.TPbxConferenceProfile;

public interface TPbxConferenceProfileMapper {
    TPbxConferenceProfile selectTPbxConferenceProfileById(String id);
    List<TPbxConferenceProfile> selectTPbxConferenceProfileList(TPbxConferenceProfile query);
    int insertTPbxConferenceProfile(TPbxConferenceProfile row);
    int updateTPbxConferenceProfile(TPbxConferenceProfile row);
    int deleteTPbxConferenceProfileById(String id);
    int deleteTPbxConferenceProfileByIds(String[] ids);
}
