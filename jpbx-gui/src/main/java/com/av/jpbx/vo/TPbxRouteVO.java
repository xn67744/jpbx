package com.av.jpbx.vo;

import com.av.jpbx.domain.TPbxGateway;
import com.av.jpbx.domain.TPbxRoute;
import lombok.Data;

import java.util.List;

@Data
public class TPbxRouteVO extends TPbxRoute {
    private List<TPbxGateway> gateways;
}
