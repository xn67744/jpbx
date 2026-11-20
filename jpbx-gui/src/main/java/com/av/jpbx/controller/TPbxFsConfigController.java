package com.av.jpbx.controller;

import com.av.common.annotation.Log;
import com.av.common.core.controller.BaseController;
import com.av.common.core.domain.AjaxResult;
import com.av.common.enums.BusinessType;
import com.av.jpbx.enums.FsApplyResult;
import com.av.jpbx.service.IFsConfigService;
import com.av.jpbx.vo.FsParamSaveRequest;
import com.av.jpbx.vo.FsValidationError;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * FreeSWITCH 配置参数管理：按「参数名/值」编辑，带说明与示例；不直接改 XML 文件。
 */
@Controller
@RequestMapping("/pbx/fsconf")
public class TPbxFsConfigController extends BaseController {

    private final String prefix = "pbx/fsconf";

    @Autowired
    private IFsConfigService fsConfigService;

    @RequiresPermissions("pbx:fsconf:view")
    @GetMapping()
    public String index(ModelMap mmap) {
        mmap.put("sections", fsConfigService.listSections());
        return prefix + "/fsconf";
    }

    @RequiresPermissions("pbx:fsconf:list")
    @GetMapping("/sections")
    @ResponseBody
    public AjaxResult sections() {
        return AjaxResult.success(fsConfigService.listSections());
    }

    @RequiresPermissions("pbx:fsconf:list")
    @GetMapping("/section")
    @ResponseBody
    public AjaxResult section(String code) {
        try {
            return AjaxResult.success(fsConfigService.getSection(code));
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }
    }

    @RequiresPermissions("pbx:fsconf:edit")
    @PostMapping("/validate")
    @ResponseBody
    public AjaxResult validate(@RequestBody FsParamSaveRequest request) {
        List<FsValidationError> errors = fsConfigService.validate(request);
        if (errors.isEmpty()) {
            return AjaxResult.success("校验通过");
        }
        AjaxResult r = AjaxResult.error("存在 " + errors.size() + " 项校验错误");
        r.put("errors", errors);
        return r;
    }

    @RequiresPermissions("pbx:fsconf:edit")
    @Log(title = "FS配置参数", businessType = BusinessType.UPDATE)
    @PostMapping("/save")
    @ResponseBody
    public AjaxResult save(@RequestBody FsParamSaveRequest request) {
        List<FsValidationError> errors = fsConfigService.validate(request);
        if (!errors.isEmpty()) {
            AjaxResult r = AjaxResult.error("校验失败，请修正标红项");
            r.put("errors", errors);
            return r;
        }
        FsApplyResult result = fsConfigService.save(request);
        if (!result.isSuccess()) {
            return AjaxResult.error(result.getMessage());
        }
        return AjaxResult.success(result);
    }
}
