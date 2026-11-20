package com.av.jpbx.controller;

import java.util.List;
import com.av.common.annotation.Log;
import com.av.common.core.controller.BaseController;
import com.av.common.core.domain.AjaxResult;
import com.av.common.core.page.TableDataInfo;
import com.av.common.enums.BusinessType;
import com.av.common.utils.poi.ExcelUtil;
import com.av.jpbx.domain.TPbxConference;
import com.av.jpbx.service.ITPbxConferenceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pbx/conference")
public class TPbxConferenceController extends BaseController {
    private String prefix = "pbx/conference";
    @Autowired private ITPbxConferenceService service;

    @RequiresPermissions("pbx:conference:view")
    @GetMapping()
    public String page() { return prefix + "/conference"; }

    @RequiresPermissions("pbx:conference:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TPbxConference query) {
        startPage();
        return getDataTable(service.selectTPbxConferenceList(query));
    }

    @RequiresPermissions("pbx:conference:export")
    @Log(title = "会议室", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TPbxConference query) {
        List<TPbxConference> list = service.selectTPbxConferenceList(query);
        return new ExcelUtil<TPbxConference>(TPbxConference.class).exportExcel(list, "会议室");
    }

    @GetMapping("/add")
    public String add() { return prefix + "/add"; }

    @RequiresPermissions("pbx:conference:add")
    @Log(title = "会议室", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TPbxConference row) { return toAjax(service.insertTPbxConference(row)); }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        mmap.put("row", service.selectTPbxConferenceById(id));
        return prefix + "/edit";
    }

    @RequiresPermissions("pbx:conference:edit")
    @Log(title = "会议室", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TPbxConference row) { return toAjax(service.updateTPbxConference(row)); }

    @RequiresPermissions("pbx:conference:remove")
    @Log(title = "会议室", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) { return toAjax(service.deleteTPbxConferenceByIds(ids)); }
}
