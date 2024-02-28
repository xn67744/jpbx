package com.av.jpbx.controller;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.av.common.annotation.Log;
import com.av.common.enums.BusinessType;
import com.av.jpbx.domain.TPbxRoute;
import com.av.jpbx.service.ITPbxRouteService;
import com.av.common.core.controller.BaseController;
import com.av.common.core.domain.AjaxResult;
import com.av.common.utils.poi.ExcelUtil;
import com.av.common.core.page.TableDataInfo;

/**
 * 路由管理Controller
 * 
 * @author long
 * @date 2024-02-11
 */
@Controller
@RequestMapping("/pbx/route")
public class TPbxRouteController extends BaseController
{
    private String prefix = "pbx/route";

    @Autowired
    private ITPbxRouteService tPbxRouteService;

    @RequiresPermissions("pbx:route:view")
    @GetMapping()
    public String route()
    {
        return prefix + "/route";
    }

    /**
     * 查询路由管理列表
     */
    @RequiresPermissions("pbx:route:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TPbxRoute tPbxRoute)
    {
        startPage();
        List<TPbxRoute> list = tPbxRouteService.selectTPbxRouteList(tPbxRoute);
        return getDataTable(list);
    }

    /**
     * 导出路由管理列表
     */
    @RequiresPermissions("pbx:route:export")
    @Log(title = "路由管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TPbxRoute tPbxRoute)
    {
        List<TPbxRoute> list = tPbxRouteService.selectTPbxRouteList(tPbxRoute);
        ExcelUtil<TPbxRoute> util = new ExcelUtil<TPbxRoute>(TPbxRoute.class);
        return util.exportExcel(list, "路由管理数据");
    }

    /**
     * 新增路由管理
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存路由管理
     */
    @Log(title = "路由管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@RequestBody TPbxRoute tPbxRoute)
    {
        return toAjax(tPbxRouteService.insertTPbxRoute(tPbxRoute));
    }

    /**
     * 修改路由管理
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap)
    {
        TPbxRoute tPbxRoute = tPbxRouteService.selectTPbxRouteById(id);
        mmap.put("tPbxRoute", tPbxRoute);
        return prefix + "/edit";
    }

    /**
     * 修改保存路由管理
     */
    @RequiresPermissions("pbx:route:edit")
    @Log(title = "路由管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TPbxRoute tPbxRoute)
    {
        return toAjax(tPbxRouteService.updateTPbxRoute(tPbxRoute));
    }

    /**
     * 删除路由管理
     */
    @RequiresPermissions("pbx:route:remove")
    @Log(title = "路由管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(tPbxRouteService.deleteTPbxRouteByIds(ids));
    }
}
