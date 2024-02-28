package com.av.jpbx.service;

import com.av.jpbx.domain.TPbxCDR;
import com.av.jpbx.domain.TPbxRegister;
import com.av.jpbx.domain.TPbxUser;

import java.util.List;

/**
 * 呼叫记录管理Service接口
 * 
 * @author long
 * @date 2024-02-04
 */
public interface ITPbxCallService
{

    /**
     * 查询呼叫记录管理列表
     * 
     * @param cdr 呼叫记录条件
     * @return 呼叫记录管理集合
     */
    public List<TPbxCDR> selectTPbxCallRecordList(TPbxCDR cdr);
}
