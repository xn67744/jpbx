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
import com.av.jpbx.domain.TPbxSysconf;
import com.av.jpbx.service.ITPbxSysconfService;
import com.av.common.core.controller.BaseController;
import com.av.common.core.domain.AjaxResult;
import com.av.common.utils.poi.ExcelUtil;
import com.av.common.core.page.TableDataInfo;

/**
 * 服务配置Controller
 * 
 * @author long
 * @date 2024-02-21
 */
@Controller
@RequestMapping("/pbx/sysconf")
public class TPbxSysconfController extends BaseController
{
    private String prefix = "pbx/sysconf";

    @Autowired
    private ITPbxSysconfService tPbxSysconfService;

    @RequiresPermissions("pbx:sysconf:view")
    @GetMapping()
    public String sysconf()
    {
        return prefix + "/sysconf";
    }

    /**
     * 查询服务配置列表
     */
    @RequiresPermissions("pbx:sysconf:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TPbxSysconf tPbxSysconf)
    {
        startPage();
        List<TPbxSysconf> list = tPbxSysconfService.selectTPbxSysconfList(tPbxSysconf);
        return getDataTable(list);
    }

    /**
     * 查询服务配置列表
     */
    @GetMapping("/view")
    public String view(ModelMap mmap)
    {
        mmap.put("tPbxSysconf", tPbxSysconfService.selectTPbxSysconfInfo());
        return prefix + "/edit";
    }

    /**
     * 导出服务配置列表
     */
    @RequiresPermissions("pbx:sysconf:export")
    @Log(title = "服务配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TPbxSysconf tPbxSysconf)
    {
        List<TPbxSysconf> list = tPbxSysconfService.selectTPbxSysconfList(tPbxSysconf);
        ExcelUtil<TPbxSysconf> util = new ExcelUtil<TPbxSysconf>(TPbxSysconf.class);
        return util.exportExcel(list, "服务配置数据");
    }

    /**
     * 新增服务配置
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存服务配置
     */
    @RequiresPermissions("pbx:sysconf:add")
    @Log(title = "服务配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TPbxSysconf tPbxSysconf)
    {
        return toAjax(tPbxSysconfService.insertTPbxSysconf(tPbxSysconf));
    }

    /**
     * 修改服务配置
     */
    @RequiresPermissions("pbx:sysconf:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap)
    {
        TPbxSysconf tPbxSysconf = tPbxSysconfService.selectTPbxSysconfById(id);
        mmap.put("tPbxSysconf", tPbxSysconf);
        return prefix + "/edit";
    }

    /**
     * 修改保存服务配置
     */
    @RequiresPermissions("pbx:sysconf:edit")
    @Log(title = "服务配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TPbxSysconf tPbxSysconf)
    {
        return toAjax(tPbxSysconfService.updateTPbxSysconf(tPbxSysconf));
    }

    /**
     * 删除服务配置
     */
    @RequiresPermissions("pbx:sysconf:edit")
    @Log(title = "服务配置", businessType = BusinessType.UPDATE)
    @GetMapping( "/effect")
    @ResponseBody
    public AjaxResult effect()
    {
        tPbxSysconfService.effect();
        return AjaxResult.success();
    }
}
