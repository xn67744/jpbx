package com.av.jpbx.controller;

import com.av.common.annotation.Log;
import com.av.common.core.controller.BaseController;
import com.av.common.core.domain.AjaxResult;
import com.av.common.core.page.TableDataInfo;
import com.av.common.enums.BusinessType;
import com.av.common.utils.poi.ExcelUtil;
import com.av.jpbx.domain.TPbxCDR;
import com.av.jpbx.service.ITPbxCallService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 呼叫账户管理Controller
 * 
 * @author long
 * @date 2024-02-04
 */
@Controller
@RequestMapping("/pbx/call")
public class TPbxCallController extends BaseController
{
    private String prefix = "pbx/call";

    @Autowired
    private ITPbxCallService callService;


    /**
     * 获取当前注册的用户信息
     */
    @RequiresPermissions("pbx:call:view")
    @GetMapping( "/view")
    public String recordView()
    {
        return prefix + "/record";
    }

    /**
     * 查询呼叫账户管理列表
     */
    @RequiresPermissions("pbx:call:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TPbxCDR cdr)
    {
        startPage();
        return getDataTable(callService.selectTPbxCallRecordList(cdr));
    }

    /**
     * 导出呼叫账户管理列表
     */
    @RequiresPermissions("pbx:call:export")
    @Log(title = "呼叫记录管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TPbxCDR cdr)
    {
        ExcelUtil<TPbxCDR> util = new ExcelUtil<TPbxCDR>(TPbxCDR.class);
        return util.exportExcel(callService.selectTPbxCallRecordList(cdr), "呼叫账户管理数据");
    }


}
