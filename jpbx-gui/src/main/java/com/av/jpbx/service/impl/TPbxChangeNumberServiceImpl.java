package com.av.jpbx.service.impl;

import com.av.jpbx.domain.TPbxChangeNumber;
import com.av.jpbx.mapper.TPbxChangeNumberMapper;
import com.av.jpbx.service.ITPbxChangeNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 路由管理Service业务层处理
 * 
 * @author long
 * @date 2024-02-11
 */
@Service
public class TPbxChangeNumberServiceImpl implements ITPbxChangeNumberService {

    @Autowired
    private TPbxChangeNumberMapper changeNumberMapper;


    @Override
    public List<TPbxChangeNumber> selectTPbxChangeNumberList(TPbxChangeNumber changeNumber)
    {
        return changeNumberMapper.selectChangeNumbes(changeNumber);
    }
}
