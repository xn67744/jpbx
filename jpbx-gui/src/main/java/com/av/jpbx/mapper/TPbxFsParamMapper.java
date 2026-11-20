package com.av.jpbx.mapper;

import com.av.jpbx.domain.TPbxFsParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TPbxFsParamMapper {
    List<TPbxFsParam> selectBySection(String section);

    TPbxFsParam selectBySectionAndKey(@Param("section") String section, @Param("paramKey") String paramKey);

    int insertTPbxFsParam(TPbxFsParam row);

    int updateTPbxFsParam(TPbxFsParam row);

    int deleteBySection(String section);

    int upsert(TPbxFsParam row);
}
