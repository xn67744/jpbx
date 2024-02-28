package com.av.jpbx.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TPbxChangeNumber {
    private String id;
    private Integer type;
    private String callerReg;
    private String changedCaller;
    private String calleeReg;
    private String changedCallee;
    private String routeId;
    private String caller;
    private Integer order;
}