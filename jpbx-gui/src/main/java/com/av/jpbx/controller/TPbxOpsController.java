package com.av.jpbx.controller;

import com.av.common.annotation.Log;
import com.av.common.core.controller.BaseController;
import com.av.common.core.domain.AjaxResult;
import com.av.common.core.page.TableDataInfo;
import com.av.common.enums.BusinessType;
import com.av.common.utils.StringUtils;
import com.av.jpbx.enums.FsApplyResult;
import com.av.jpbx.enums.FsResourceType;
import com.av.jpbx.service.FsApplyService;
import com.av.jpbx.service.FsEslClient;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * FreeSWITCH 实时运维：通道、Sofia、模块、ESL 控制台。
 */
@Controller
@RequestMapping("/pbx/ops")
public class TPbxOpsController extends BaseController {

    private final String prefix = "pbx/ops";

    @Autowired
    private FsEslClient fsEslClient;

    @Autowired
    private FsApplyService fsApplyService;

    @RequiresPermissions("pbx:ops:view")
    @GetMapping()
    public String index() {
        return prefix + "/ops";
    }

    @RequiresPermissions("pbx:ops:view")
    @GetMapping("/console")
    public String console() {
        return prefix + "/console";
    }

    @RequiresPermissions("pbx:ops:list")
    @PostMapping("/channels")
    @ResponseBody
    public TableDataInfo channels() {
        List<Map<String, String>> rows = parseShow(fsEslClient.sendApiLines("show", "channels"));
        return getDataTable(rows);
    }

    @RequiresPermissions("pbx:ops:edit")
    @Log(title = "挂断通道", businessType = BusinessType.UPDATE)
    @PostMapping("/uuidKill")
    @ResponseBody
    public AjaxResult uuidKill(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            return error("uuid 不能为空");
        }
        return AjaxResult.success(fsEslClient.sendApi("uuid_kill", uuid));
    }

    @RequiresPermissions("pbx:ops:list")
    @PostMapping("/sofiaStatus")
    @ResponseBody
    public AjaxResult sofiaStatus() {
        return AjaxResult.success(fsEslClient.sendApi("sofia", "status"));
    }

    @RequiresPermissions("pbx:ops:list")
    @PostMapping("/registrations")
    @ResponseBody
    public TableDataInfo registrations() {
        List<Map<String, String>> rows = parseShow(fsEslClient.sendApiLines("show", "registrations"));
        return getDataTable(rows);
    }

    @RequiresPermissions("pbx:ops:edit")
    @Log(title = "踢注册", businessType = BusinessType.UPDATE)
    @PostMapping("/flushReg")
    @ResponseBody
    public AjaxResult flushReg(String profile, String user) {
        String p = StringUtils.isEmpty(profile) ? "internal" : profile;
        if (StringUtils.isNotEmpty(user)) {
            return AjaxResult.success(fsEslClient.sendApi("sofia", "profile " + p + " flush_inbound_reg " + user));
        }
        return AjaxResult.success(fsEslClient.sendApi("sofia", "profile " + p + " flush_inbound_reg"));
    }

    @RequiresPermissions("pbx:ops:list")
    @PostMapping("/modules")
    @ResponseBody
    public AjaxResult modules() {
        return AjaxResult.success(fsEslClient.sendApi("show", "modules"));
    }

    @RequiresPermissions("pbx:ops:edit")
    @Log(title = "模块操作", businessType = BusinessType.UPDATE)
    @PostMapping("/module")
    @ResponseBody
    public AjaxResult module(String action, String moduleName) {
        if (StringUtils.isEmpty(moduleName)) {
            return error("moduleName 不能为空");
        }
        String act = StringUtils.isEmpty(action) ? "reload" : action;
        Map<String, String> payload = new HashMap<>();
        payload.put("action", act);
        payload.put("moduleName", moduleName);
        FsApplyResult result = fsApplyService.apply(FsResourceType.MODULE, payload);
        return result.isSuccess() ? AjaxResult.success(result) : AjaxResult.error(result.getMessage());
    }

    @RequiresPermissions("pbx:ops:edit")
    @Log(title = "ESL API", businessType = BusinessType.OTHER)
    @PostMapping("/api")
    @ResponseBody
    public AjaxResult api(String command, String args) {
        if (StringUtils.isEmpty(command)) {
            return error("command 不能为空");
        }
        // 禁止危险命令
        String c = command.trim().toLowerCase(Locale.ROOT);
        if (c.equals("fsctl") || c.contains("shutdown") || c.equals("hupall")) {
            return error("禁止执行危险命令");
        }
        try {
            return AjaxResult.success(fsEslClient.sendApi(command, args));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @RequiresPermissions("pbx:ops:list")
    @GetMapping("/lastApply")
    @ResponseBody
    public AjaxResult lastApply() {
        return AjaxResult.success(fsApplyService.getLastResult());
    }

    @RequiresPermissions("pbx:ops:edit")
    @Log(title = "reloadxml", businessType = BusinessType.UPDATE)
    @PostMapping("/reloadxml")
    @ResponseBody
    public AjaxResult reloadxml() {
        return AjaxResult.success(fsApplyService.apply(FsResourceType.RELOAD_XML));
    }

    /**
     * 重启 FreeSWITCH 服务（ESL shutdown restart：FS 优雅停止当前通话并重启进程）。
     * 仅适用于修改 event_socket、升级 FS、加载新模块等无法热生效的场景；
     * 平台配置变更请优先使用各页面的热生效，不要重启 FS。
     */
    @RequiresPermissions("pbx:ops:edit")
    @Log(title = "重启FS服务", businessType = BusinessType.OTHER)
    @PostMapping("/restartFs")
    @ResponseBody
    public AjaxResult restartFs() {
        try {
            String resp = fsEslClient.sendApi("shutdown", "restart");
            return AjaxResult.success("重启指令已下发：FS 将优雅停止并自动重启进程（在线通话会中断，分机需重新注册）。FS 响应：" + resp);
        } catch (Exception e) {
            // shutdown 下发后 ESL 连接会被 FS 断开，视为指令已送达
            String msg = e.getMessage() == null ? "" : e.getMessage();
            if (msg.contains("closed") || msg.contains("reset") || msg.contains("EOF")) {
                return AjaxResult.success("重启指令已送达（FS 已断开 ESL 并开始重启，约 5~10 秒后恢复）");
            }
            return error("重启指令下发失败：" + msg);
        }
    }

    private List<Map<String, String>> parseShow(List<String> lines) {
        List<Map<String, String>> rows = new ArrayList<>();
        if (lines == null || lines.size() < 2) {
            return rows;
        }
        String[] headers = lines.get(0).split(",");
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (StringUtils.isEmpty(line) || line.startsWith(headers[0] + " total") || line.contains(" total.")) {
                continue;
            }
            String[] cols = line.split(",", -1);
            Map<String, String> row = new LinkedHashMap<>();
            for (int j = 0; j < headers.length && j < cols.length; j++) {
                row.put(headers[j], cols[j]);
            }
            if (!row.isEmpty()) {
                rows.add(row);
            }
        }
        return rows;
    }
}
