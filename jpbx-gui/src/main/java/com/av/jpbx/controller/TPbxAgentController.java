package com.av.jpbx.controller;

import java.util.List;
import com.av.common.annotation.Log;
import com.av.common.core.controller.BaseController;
import com.av.common.core.domain.AjaxResult;
import com.av.common.core.page.TableDataInfo;
import com.av.common.enums.BusinessType;
import com.av.common.utils.poi.ExcelUtil;
import com.av.jpbx.domain.TPbxAgent;
import com.av.jpbx.service.ITPbxAgentService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pbx/agent")
public class TPbxAgentController extends BaseController {
    private String prefix = "pbx/agent";
    @Autowired private ITPbxAgentService service;

    @RequiresPermissions("pbx:agent:view")
    @GetMapping()
    public String page() { return prefix + "/agent"; }

    @RequiresPermissions("pbx:agent:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TPbxAgent query) {
        startPage();
        return getDataTable(service.selectTPbxAgentList(query));
    }

    @RequiresPermissions("pbx:agent:export")
    @Log(title = "坐席", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TPbxAgent query) {
        List<TPbxAgent> list = service.selectTPbxAgentList(query);
        return new ExcelUtil<TPbxAgent>(TPbxAgent.class).exportExcel(list, "坐席");
    }

    @GetMapping("/add")
    public String add() { return prefix + "/add"; }

    @RequiresPermissions("pbx:agent:add")
    @Log(title = "坐席", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TPbxAgent row) { return toAjax(service.insertTPbxAgent(row)); }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        mmap.put("row", service.selectTPbxAgentById(id));
        return prefix + "/edit";
    }

    @RequiresPermissions("pbx:agent:edit")
    @Log(title = "坐席", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TPbxAgent row) { return toAjax(service.updateTPbxAgent(row)); }

    @RequiresPermissions("pbx:agent:remove")
    @Log(title = "坐席", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) { return toAjax(service.deleteTPbxAgentByIds(ids)); }
}
