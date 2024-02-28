package com.av.jpbx.mapper;

import com.av.jpbx.domain.TPbxCDR;
import com.av.jpbx.domain.TPbxUser;

import java.util.List;

/**
 * 呼叫账户管理Mapper接口
 * 
 * @author loong
 * @date 2024-02-04
 */
public interface TPbxCDRMapper
{
    public List<TPbxCDR> selectTPbxCDRList(TPbxCDR tPbxCDR);
}
