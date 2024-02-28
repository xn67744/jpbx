package com.av.jpbx.controller;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.av.common.annotation.Log;
import com.av.common.enums.BusinessType;
import com.av.jpbx.domain.TPbxGateway;
import com.av.jpbx.service.ITPbxGatewayService;
import com.av.common.core.controller.BaseController;
import com.av.common.core.domain.AjaxResult;
import com.av.common.utils.poi.ExcelUtil;
import com.av.common.core.page.TableDataInfo;

/**
 * 呼叫中继Controller
 * 
 * @author long
 * @date 2024-02-08
 */
@Controller
@RequestMapping("/pbx/gateway")
public class TPbxGatewayController extends BaseController
{
    private String prefix = "pbx/gateway";

    @Autowired
    private ITPbxGatewayService tPbxGatewayService;

    @RequiresPermissions("pbx:gateway:view")
    @GetMapping()
    public String gateway()
    {
        return prefix + "/gateway";
    }

    /**
     * 查询呼叫中继列表
     */
    @RequiresPermissions("pbx:gateway:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TPbxGateway tPbxGateway)
    {
        startPage();
        List<TPbxGateway> list = tPbxGatewayService.selectTPbxGatewayList(tPbxGateway);
        return getDataTable(list);
    }

    /**
     * 导出呼叫中继列表
     */
    @RequiresPermissions("pbx:gateway:export")
    @Log(title = "呼叫中继", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TPbxGateway tPbxGateway)
    {
        List<TPbxGateway> list = tPbxGatewayService.selectTPbxGatewayList(tPbxGateway);
        ExcelUtil<TPbxGateway> util = new ExcelUtil<TPbxGateway>(TPbxGateway.class);
        return util.exportExcel(list, "呼叫中继数据");
    }

    /**
     * 新增呼叫中继
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存呼叫中继
     */
    @RequiresPermissions("pbx:gateway:add")
    @Log(title = "呼叫中继", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TPbxGateway tPbxGateway)
    {
        return toAjax(tPbxGatewayService.insertTPbxGateway(tPbxGateway));
    }

    /**
     * 修改呼叫中继
     */
    @RequiresPermissions("pbx:gateway:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap)
    {
        TPbxGateway tPbxGateway = tPbxGatewayService.selectTPbxGatewayById(id);
        mmap.put("tPbxGateway", tPbxGateway);
        return prefix + "/edit";
    }

    /**
     * 修改保存呼叫中继
     */
    @RequiresPermissions("pbx:gateway:edit")
    @Log(title = "呼叫中继", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TPbxGateway tPbxGateway)
    {
        return toAjax(tPbxGatewayService.updateTPbxGateway(tPbxGateway));
    }

    /**
     * 删除呼叫中继
     */
    @RequiresPermissions("pbx:gateway:remove")
    @Log(title = "呼叫中继", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(tPbxGatewayService.deleteTPbxGatewayByIds(ids));
    }
}
