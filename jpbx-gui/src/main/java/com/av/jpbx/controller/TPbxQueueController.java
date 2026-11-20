package com.av.jpbx.controller;

import java.util.List;
import com.av.common.annotation.Log;
import com.av.common.core.controller.BaseController;
import com.av.common.core.domain.AjaxResult;
import com.av.common.core.page.TableDataInfo;
import com.av.common.enums.BusinessType;
import com.av.common.utils.poi.ExcelUtil;
import com.av.jpbx.domain.TPbxQueue;
import com.av.jpbx.service.ITPbxQueueService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pbx/queue")
public class TPbxQueueController extends BaseController {
    private String prefix = "pbx/queue";
    @Autowired private ITPbxQueueService service;

    @RequiresPermissions("pbx:queue:view")
    @GetMapping()
    public String page() { return prefix + "/queue"; }

    @RequiresPermissions("pbx:queue:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TPbxQueue query) {
        startPage();
        return getDataTable(service.selectTPbxQueueList(query));
    }

    @RequiresPermissions("pbx:queue:export")
    @Log(title = "呼叫队列", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TPbxQueue query) {
        List<TPbxQueue> list = service.selectTPbxQueueList(query);
        return new ExcelUtil<TPbxQueue>(TPbxQueue.class).exportExcel(list, "呼叫队列");
    }

    @GetMapping("/add")
    public String add() { return prefix + "/add"; }

    @RequiresPermissions("pbx:queue:add")
    @Log(title = "呼叫队列", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TPbxQueue row) { return toAjax(service.insertTPbxQueue(row)); }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        mmap.put("row", service.selectTPbxQueueById(id));
        return prefix + "/edit";
    }

    @RequiresPermissions("pbx:queue:edit")
    @Log(title = "呼叫队列", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TPbxQueue row) { return toAjax(service.updateTPbxQueue(row)); }

    @RequiresPermissions("pbx:queue:remove")
    @Log(title = "呼叫队列", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) { return toAjax(service.deleteTPbxQueueByIds(ids)); }
}
