package com.av.jpbx.controller;

import com.av.common.annotation.Log;
import com.av.common.core.controller.BaseController;
import com.av.common.core.domain.AjaxResult;
import com.av.common.core.page.TableDataInfo;
import com.av.common.enums.BusinessType;
import com.av.common.utils.poi.ExcelUtil;
import com.av.jpbx.domain.TPbxUser;
import com.av.jpbx.service.ITPbxUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 呼叫账户管理Controller
 * 
 * @author long
 * @date 2024-02-04
 */
@Controller
@RequestMapping("/pbx/user")
public class TPbxUserController extends BaseController
{
    private String prefix = "pbx/user";

    @Autowired
    private ITPbxUserService tPbxUserService;

    @RequiresPermissions("pbx:user:view")
    @GetMapping()
    public String user()
    {
        return prefix + "/user";
    }

    /**
     * 查询呼叫账户管理列表
     */
    @RequiresPermissions("pbx:user:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TPbxUser tPbxUser)
    {
        startPage();
        List<TPbxUser> list = tPbxUserService.selectTPbxUserList(tPbxUser);
        return getDataTable(list);
    }

    /**
     * 导出呼叫账户管理列表
     */
    @RequiresPermissions("pbx:user:export")
    @Log(title = "呼叫账户管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TPbxUser tPbxUser)
    {
        List<TPbxUser> list = tPbxUserService.selectTPbxUserList(tPbxUser);
        ExcelUtil<TPbxUser> util = new ExcelUtil<TPbxUser>(TPbxUser.class);
        return util.exportExcel(list, "呼叫账户管理数据");
    }

    /**
     * 新增呼叫账户管理
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存呼叫账户管理
     */
    @RequiresPermissions("pbx:user:add")
    @Log(title = "呼叫账户管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TPbxUser tPbxUser)
    {
        return toAjax(tPbxUserService.insertTPbxUser(tPbxUser));
    }

    /**
     * 修改呼叫账户管理
     */
    @RequiresPermissions("pbx:user:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap)
    {
        TPbxUser tPbxUser = tPbxUserService.selectTPbxUserById(id);
        mmap.put("tPbxUser", tPbxUser);
        return prefix + "/edit";
    }

    /**
     * 修改保存呼叫账户管理
     */
    @RequiresPermissions("pbx:user:edit")
    @Log(title = "呼叫账户管理")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TPbxUser tPbxUser)
    {
        return toAjax(tPbxUserService.updateTPbxUser(tPbxUser));
    }

    /**
     * 删除呼叫账户管理
     */
    @RequiresPermissions("pbx:user:remove")
    @Log(title = "呼叫账户管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(tPbxUserService.deleteTPbxUserByIds(ids));
    }




    /**
     * 获取当前注册的用户信息
     */
    //@RequiresPermissions("pbx:user:view")
    @GetMapping( "/registerUser")
    public String registerUser()
    {
        return prefix + "/registers";
    }

    @PostMapping( "/registerUser/list")
    @ResponseBody
    public TableDataInfo registerList()
    {
        return getDataTable(tPbxUserService.getRegisterUser());
    }


}
