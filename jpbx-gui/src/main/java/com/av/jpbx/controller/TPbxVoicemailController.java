package com.av.jpbx.controller;

import java.util.List;
import com.av.common.annotation.Log;
import com.av.common.core.controller.BaseController;
import com.av.common.core.domain.AjaxResult;
import com.av.common.core.page.TableDataInfo;
import com.av.common.enums.BusinessType;
import com.av.common.utils.poi.ExcelUtil;
import com.av.jpbx.domain.TPbxVoicemail;
import com.av.jpbx.service.ITPbxVoicemailService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pbx/voicemail")
public class TPbxVoicemailController extends BaseController {
    private String prefix = "pbx/voicemail";
    @Autowired private ITPbxVoicemailService service;

    @RequiresPermissions("pbx:voicemail:view")
    @GetMapping()
    public String page() { return prefix + "/voicemail"; }

    @RequiresPermissions("pbx:voicemail:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TPbxVoicemail query) {
        startPage();
        return getDataTable(service.selectTPbxVoicemailList(query));
    }

    @RequiresPermissions("pbx:voicemail:export")
    @Log(title = "语音留言", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TPbxVoicemail query) {
        List<TPbxVoicemail> list = service.selectTPbxVoicemailList(query);
        return new ExcelUtil<TPbxVoicemail>(TPbxVoicemail.class).exportExcel(list, "语音留言");
    }

    @GetMapping("/add")
    public String add() { return prefix + "/add"; }

    @RequiresPermissions("pbx:voicemail:add")
    @Log(title = "语音留言", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TPbxVoicemail row) { return toAjax(service.insertTPbxVoicemail(row)); }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        mmap.put("row", service.selectTPbxVoicemailById(id));
        return prefix + "/edit";
    }

    @RequiresPermissions("pbx:voicemail:edit")
    @Log(title = "语音留言", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TPbxVoicemail row) { return toAjax(service.updateTPbxVoicemail(row)); }

    @RequiresPermissions("pbx:voicemail:remove")
    @Log(title = "语音留言", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) { return toAjax(service.deleteTPbxVoicemailByIds(ids)); }
}
