package com.av.jpbx.mapper;

import java.util.List;
import com.av.jpbx.domain.TPbxVoicemail;

public interface TPbxVoicemailMapper {
    TPbxVoicemail selectTPbxVoicemailById(String id);
    List<TPbxVoicemail> selectTPbxVoicemailList(TPbxVoicemail query);
    int insertTPbxVoicemail(TPbxVoicemail row);
    int updateTPbxVoicemail(TPbxVoicemail row);
    int deleteTPbxVoicemailById(String id);
    int deleteTPbxVoicemailByIds(String[] ids);
}
