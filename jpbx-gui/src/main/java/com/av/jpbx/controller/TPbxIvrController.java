package com.av.jpbx.controller;

import java.util.List;
import com.av.common.annotation.Log;
import com.av.common.core.controller.BaseController;
import com.av.common.core.domain.AjaxResult;
import com.av.common.core.page.TableDataInfo;
import com.av.common.enums.BusinessType;
import com.av.common.utils.poi.ExcelUtil;
import com.av.jpbx.domain.TPbxIvr;
import com.av.jpbx.service.ITPbxIvrService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pbx/ivr")
public class TPbxIvrController extends BaseController {
    private String prefix = "pbx/ivr";
    @Autowired private ITPbxIvrService service;

    @RequiresPermissions("pbx:ivr:view")
    @GetMapping()
    public String page() { return prefix + "/ivr"; }

    @RequiresPermissions("pbx:ivr:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TPbxIvr query) {
        startPage();
        return getDataTable(service.selectTPbxIvrList(query));
    }

    @RequiresPermissions("pbx:ivr:export")
    @Log(title = "IVR菜单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TPbxIvr query) {
        List<TPbxIvr> list = service.selectTPbxIvrList(query);
        return new ExcelUtil<TPbxIvr>(TPbxIvr.class).exportExcel(list, "IVR菜单");
    }

    @GetMapping("/add")
    public String add() { return prefix + "/add"; }

    @RequiresPermissions("pbx:ivr:add")
    @Log(title = "IVR菜单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TPbxIvr row) { return toAjax(service.insertTPbxIvr(row)); }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        mmap.put("row", service.selectTPbxIvrById(id));
        return prefix + "/edit";
    }

    @RequiresPermissions("pbx:ivr:edit")
    @Log(title = "IVR菜单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TPbxIvr row) { return toAjax(service.updateTPbxIvr(row)); }

    @RequiresPermissions("pbx:ivr:remove")
    @Log(title = "IVR菜单", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) { return toAjax(service.deleteTPbxIvrByIds(ids)); }
}
