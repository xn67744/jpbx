package com.av.jpbx.service;

import com.av.jpbx.enums.FsApplyResult;
import com.av.jpbx.vo.FsParamSaveRequest;
import com.av.jpbx.vo.FsSectionVO;
import com.av.jpbx.vo.FsValidationError;

import java.util.List;
import java.util.Map;

public interface IFsConfigService {

    List<FsSectionVO> listSections();

    FsSectionVO getSection(String sectionCode);

    List<FsValidationError> validate(FsParamSaveRequest request);

    FsApplyResult save(FsParamSaveRequest request);

    /** 供 xml_curl 使用：读取某段全部参数（缺省用默认值） */
    Map<String, String> resolveValues(String sectionCode);
}
