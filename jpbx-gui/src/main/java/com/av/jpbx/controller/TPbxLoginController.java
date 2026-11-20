package com.av.jpbx.controller;

import com.av.common.annotation.Log;
import com.av.common.core.controller.BaseController;
import com.av.common.core.domain.AjaxResult;
import com.av.common.core.page.TableDataInfo;
import com.av.common.enums.BusinessType;
import com.av.common.utils.StringUtils;
import com.av.common.utils.poi.ExcelUtil;
import com.av.jpbx.domain.TPbxUser;
import com.av.jpbx.service.IPbxLogService;
import com.av.jpbx.service.ITPbxUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

/**
 * 呼叫账户管理Controller
 * 
 * @author long
 * @date 2024-02-04
 */
@Controller
@RequestMapping("/pbx/log")
public class TPbxLoginController extends BaseController
{
    private String prefix = "pbx/log";

    @Autowired
    private IPbxLogService logService;

    @RequiresPermissions("pbx:log:view")
    @GetMapping("/view")
    public String log()
    {
        return prefix + "/log";
    }

    /**
     * 查询呼叫账户管理列表
     */
    @RequiresPermissions("pbx:log:view")
    @GetMapping("/watchLog")
    @ResponseBody
    public String watchLog(@RequestParam(name = "beginDate", required = false) String beginDate, @RequestParam(name ="endDate", required = false) String endDate)
    {
        String content = "";
        if(StringUtils.isBlank(beginDate) && StringUtils.isBlank(endDate)) {
            try {
                content = logService.readLogs(LocalDate.now(), LocalDate.now());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate)){
            try {
                content = logService.readLogs(LocalDate.parse(beginDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")), LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }



}
