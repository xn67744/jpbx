package com.av.jpbx.controller;

import com.av.common.core.controller.BaseController;
import com.av.common.core.domain.AjaxResult;
import com.av.jpbx.service.CallFlowBoardService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 呼叫流程看板：把 ACL / 路由 / 拨号计划 / IVR / 队列渲染为工作流视图。
 */
@Controller
@RequestMapping("/pbx/flow")
public class TPbxFlowController extends BaseController {

    private static final String PREFIX = "pbx/flow";

    @Autowired
    private CallFlowBoardService callFlowBoardService;

    @RequiresPermissions("pbx:flow:view")
    @GetMapping
    public String index() {
        return PREFIX + "/flow";
    }

    @RequiresPermissions("pbx:flow:list")
    @PostMapping("/graph")
    @ResponseBody
    public AjaxResult graph() {
        Map<String, Object> graph = callFlowBoardService.buildGraph();
        return AjaxResult.success(graph);
    }
}
