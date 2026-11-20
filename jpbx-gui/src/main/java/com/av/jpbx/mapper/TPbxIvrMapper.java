package com.av.jpbx.mapper;

import java.util.List;
import com.av.jpbx.domain.TPbxIvr;

public interface TPbxIvrMapper {
    TPbxIvr selectTPbxIvrById(String id);
    List<TPbxIvr> selectTPbxIvrList(TPbxIvr query);
    int insertTPbxIvr(TPbxIvr row);
    int updateTPbxIvr(TPbxIvr row);
    int deleteTPbxIvrById(String id);
    int deleteTPbxIvrByIds(String[] ids);
}
