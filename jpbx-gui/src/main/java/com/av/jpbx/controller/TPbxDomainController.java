package com.av.jpbx.controller;

import java.util.List;
import com.av.common.annotation.Log;
import com.av.common.core.controller.BaseController;
import com.av.common.core.domain.AjaxResult;
import com.av.common.core.page.TableDataInfo;
import com.av.common.enums.BusinessType;
import com.av.common.utils.poi.ExcelUtil;
import com.av.jpbx.domain.TPbxDomain;
import com.av.jpbx.service.ITPbxDomainService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pbx/domain")
public class TPbxDomainController extends BaseController {
    private String prefix = "pbx/domain";
    @Autowired private ITPbxDomainService service;

    @RequiresPermissions("pbx:domain:view")
    @GetMapping()
    public String page() { return prefix + "/domain"; }

    @RequiresPermissions("pbx:domain:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TPbxDomain query) {
        startPage();
        return getDataTable(service.selectTPbxDomainList(query));
    }

    @RequiresPermissions("pbx:domain:export")
    @Log(title = "SIP域名", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TPbxDomain query) {
        List<TPbxDomain> list = service.selectTPbxDomainList(query);
        return new ExcelUtil<TPbxDomain>(TPbxDomain.class).exportExcel(list, "SIP域名");
    }

    @GetMapping("/add")
    public String add() { return prefix + "/add"; }

    @RequiresPermissions("pbx:domain:add")
    @Log(title = "SIP域名", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TPbxDomain row) { return toAjax(service.insertTPbxDomain(row)); }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        mmap.put("row", service.selectTPbxDomainById(id));
        return prefix + "/edit";
    }

    @RequiresPermissions("pbx:domain:edit")
    @Log(title = "SIP域名", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TPbxDomain row) { return toAjax(service.updateTPbxDomain(row)); }

    @RequiresPermissions("pbx:domain:remove")
    @Log(title = "SIP域名", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) { return toAjax(service.deleteTPbxDomainByIds(ids)); }
}
