package com.av.jpbx.service;

import com.av.jpbx.domain.TPbxChangeNumber;

import java.util.List;

public interface ITPbxChangeNumberService {
    List<TPbxChangeNumber> selectTPbxChangeNumberList(TPbxChangeNumber changeNumber);
}
