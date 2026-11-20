package com.av.jpbx.controller;

import java.util.List;
import com.av.common.annotation.Log;
import com.av.common.core.controller.BaseController;
import com.av.common.core.domain.AjaxResult;
import com.av.common.core.page.TableDataInfo;
import com.av.common.enums.BusinessType;
import com.av.common.utils.poi.ExcelUtil;
import com.av.jpbx.domain.TPbxDialplan;
import com.av.jpbx.service.ITPbxDialplanService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pbx/dialplan")
public class TPbxDialplanController extends BaseController {
    private String prefix = "pbx/dialplan";
    @Autowired private ITPbxDialplanService service;

    @RequiresPermissions("pbx:dialplan:view")
    @GetMapping()
    public String page() { return prefix + "/dialplan"; }

    @RequiresPermissions("pbx:dialplan:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TPbxDialplan query) {
        startPage();
        return getDataTable(service.selectTPbxDialplanList(query));
    }

    @RequiresPermissions("pbx:dialplan:export")
    @Log(title = "拨号计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TPbxDialplan query) {
        List<TPbxDialplan> list = service.selectTPbxDialplanList(query);
        return new ExcelUtil<TPbxDialplan>(TPbxDialplan.class).exportExcel(list, "拨号计划");
    }

    @GetMapping("/add")
    public String add() { return prefix + "/add"; }

    @RequiresPermissions("pbx:dialplan:add")
    @Log(title = "拨号计划", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TPbxDialplan row) { return toAjax(service.insertTPbxDialplan(row)); }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        mmap.put("row", service.selectTPbxDialplanById(id));
        return prefix + "/edit";
    }

    @RequiresPermissions("pbx:dialplan:edit")
    @Log(title = "拨号计划", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TPbxDialplan row) { return toAjax(service.updateTPbxDialplan(row)); }

    @RequiresPermissions("pbx:dialplan:remove")
    @Log(title = "拨号计划", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) { return toAjax(service.deleteTPbxDialplanByIds(ids)); }

    /**
     * 保存整条拨号计划（含动作列表 JSON），用于高级编辑。
     */
    @RequiresPermissions("pbx:dialplan:edit")
    @Log(title = "拨号计划动作", businessType = BusinessType.UPDATE)
    @PostMapping("/saveWithActions")
    @ResponseBody
    public AjaxResult saveWithActions(@RequestBody com.av.jpbx.domain.TPbxDialplan row) {
        if (com.av.common.utils.StringUtils.isEmpty(row.getId())) {
            return toAjax(service.insertTPbxDialplan(row));
        }
        return toAjax(service.updateTPbxDialplan(row));
    }
}
