package com.av.jpbx.controller;

import java.util.List;
import com.av.common.annotation.Log;
import com.av.common.core.controller.BaseController;
import com.av.common.core.domain.AjaxResult;
import com.av.common.core.page.TableDataInfo;
import com.av.common.enums.BusinessType;
import com.av.common.utils.poi.ExcelUtil;
import com.av.jpbx.domain.TPbxDomain;
import com.av.jpbx.domain.TPbxGroup;
import com.av.jpbx.domain.TPbxUser;
import com.av.jpbx.service.ITPbxDomainService;
import com.av.jpbx.service.ITPbxGroupService;
import com.av.jpbx.service.ITPbxUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pbx/group")
public class TPbxGroupController extends BaseController {
    private String prefix = "pbx/group";
    @Autowired private ITPbxGroupService service;
    @Autowired private ITPbxDomainService domainService;
    @Autowired private ITPbxUserService userService;

    @RequiresPermissions("pbx:group:view")
    @GetMapping()
    public String page() { return prefix + "/group"; }

    @RequiresPermissions("pbx:group:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TPbxGroup query) {
        startPage();
        return getDataTable(service.selectTPbxGroupList(query));
    }

    @RequiresPermissions("pbx:group:export")
    @Log(title = "分机组", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TPbxGroup query) {
        List<TPbxGroup> list = service.selectTPbxGroupList(query);
        return new ExcelUtil<TPbxGroup>(TPbxGroup.class).exportExcel(list, "分机组");
    }

    @GetMapping("/add")
    public String add(ModelMap mmap) {
        fillOptions(mmap);
        return prefix + "/add";
    }

    @RequiresPermissions("pbx:group:add")
    @Log(title = "分机组", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TPbxGroup row) { return toAjax(service.insertTPbxGroup(row)); }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        mmap.put("row", service.selectTPbxGroupById(id));
        fillOptions(mmap);
        return prefix + "/edit";
    }

    /** 分组表单的可选项：所属域名、可分组成员（启用状态的内部分机） */
    private void fillOptions(ModelMap mmap) {
        mmap.put("domains", domainService.selectTPbxDomainList(new TPbxDomain().setEnabled(1)));
        mmap.put("users", userService.selectTPbxUserList(new TPbxUser().setType(0).setEnabled(1)));
    }

    @RequiresPermissions("pbx:group:edit")
    @Log(title = "分机组", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TPbxGroup row) { return toAjax(service.updateTPbxGroup(row)); }

    @RequiresPermissions("pbx:group:remove")
    @Log(title = "分机组", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) { return toAjax(service.deleteTPbxGroupByIds(ids)); }
}
