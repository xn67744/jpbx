package com.av.jpbx.controller;

import cn.hutool.core.lang.Snowflake;
import com.av.common.annotation.Log;
import com.av.common.core.controller.BaseController;
import com.av.common.core.domain.AjaxResult;
import com.av.common.core.page.TableDataInfo;
import com.av.common.core.text.Convert;
import com.av.common.enums.BusinessType;
import com.av.common.utils.StringUtils;
import com.av.jpbx.domain.TPbxTier;
import com.av.jpbx.enums.FsResourceType;
import com.av.jpbx.mapper.TPbxTierMapper;
import com.av.jpbx.service.FsApplyService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 队列-坐席层级（Callcenter Tier）
 */
@Controller
@RequestMapping("/pbx/tier")
public class TPbxTierController extends BaseController {

    @Autowired
    private TPbxTierMapper tierMapper;
    @Autowired
    private Snowflake snowflake;
    @Autowired
    private FsApplyService fsApplyService;

    @RequiresPermissions("pbx:queue:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TPbxTier query) {
        startPage();
        return getDataTable(tierMapper.selectTPbxTierList(query));
    }

    @RequiresPermissions("pbx:queue:edit")
    @Log(title = "队列Tier", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult add(TPbxTier row) {
        if (StringUtils.isBlank(row.getId())) {
            row.setId(snowflake.nextIdStr());
        }
        int r = tierMapper.insertTPbxTier(row);
        if (r > 0) {
            fsApplyService.apply(FsResourceType.CALLCENTER);
        }
        return toAjax(r);
    }

    @RequiresPermissions("pbx:queue:remove")
    @Log(title = "队列Tier", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        int r = tierMapper.deleteTPbxTierByIds(Convert.toStrArray(ids));
        if (r > 0) {
            fsApplyService.apply(FsResourceType.CALLCENTER);
        }
        return toAjax(r);
    }
}
