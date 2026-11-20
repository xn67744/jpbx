package com.av.jpbx.service;

import java.util.List;
import com.av.jpbx.domain.TPbxVoicemail;

public interface ITPbxVoicemailService {
    TPbxVoicemail selectTPbxVoicemailById(String id);
    List<TPbxVoicemail> selectTPbxVoicemailList(TPbxVoicemail query);
    int insertTPbxVoicemail(TPbxVoicemail row);
    int updateTPbxVoicemail(TPbxVoicemail row);
    int deleteTPbxVoicemailByIds(String ids);
}
