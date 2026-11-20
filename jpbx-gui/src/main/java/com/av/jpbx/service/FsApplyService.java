package com.av.jpbx.service;

import com.av.common.utils.StringUtils;
import com.av.jpbx.enums.FsApplyResult;
import com.av.jpbx.enums.FsResourceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * FreeSWITCH 配置热生效服务：保存后通过 ESL 即时生效，不重启 FS 进程。
 */
@Slf4j
@Service
public class FsApplyService {

    @Autowired
    private FsEslClient fsEslClient;

    /** 最近一次生效结果，供 UI 展示 */
    private final ConcurrentHashMap<String, FsApplyResult> lastResults = new ConcurrentHashMap<>();

    public FsApplyResult apply(FsResourceType type) {
        return apply(type, null);
    }

    /**
     * @param payload 可选参数，如 gatewayName / profile / moduleName / action
     */
    public FsApplyResult apply(FsResourceType type, Map<String, String> payload) {
        FsApplyResult result = new FsApplyResult().setResourceType(type.name());
        try {
            if (!fsEslClient.isConnected()) {
                fsEslClient.connect();
            }
            switch (type) {
                case DIRECTORY:
                case DIALPLAN:
                case IVR:
                case VOICEMAIL:
                case CONFERENCE:
                case RELOAD_XML:
                    exec(result, "reloadxml", "");
                    break;
                case GATEWAY:
                    exec(result, "reloadxml", "");
                    String gw = payload == null ? null : payload.get("gatewayName");
                    String profile = payload != null && StringUtils.isNotEmpty(payload.get("profile"))
                            ? payload.get("profile") : "external";
                    if (StringUtils.isNotEmpty(gw)) {
                        exec(result, "sofia", "profile " + profile + " killgw " + gw);
                    }
                    exec(result, "sofia", "profile " + profile + " rescan");
                    break;
                case SOFIA_PROFILE:
                    exec(result, "reloadxml", "");
                    String p = payload != null && StringUtils.isNotEmpty(payload.get("profile"))
                            ? payload.get("profile") : "internal";
                    exec(result, "sofia", "profile " + p + " restart");
                    break;
                case ACL:
                    exec(result, "reloadxml", "");
                    exec(result, "reloadacl", "");
                    break;
                case CALLCENTER:
                    exec(result, "reloadxml", "");
                    String queue = payload == null ? null : payload.get("queue");
                    if (StringUtils.isNotEmpty(queue)) {
                        exec(result, "callcenter_config", "queue unload " + queue);
                        exec(result, "callcenter_config", "queue load " + queue);
                    } else {
                        // 全量时尝试 reload 模块（不重启 FS）
                        exec(result, "reload", "mod_callcenter");
                    }
                    break;
                case MODULE:
                    String module = payload == null ? null : payload.get("moduleName");
                    String action = payload == null ? "reload" : payload.getOrDefault("action", "reload");
                    if (StringUtils.isEmpty(module)) {
                        return result.fail("moduleName 不能为空");
                    }
                    exec(result, action, module);
                    break;
                case SYSCONF:
                    exec(result, "reloadxml", "");
                    exec(result, "sofia", "profile internal restart");
                    exec(result, "sofia", "profile external restart");
                    try {
                        exec(result, "sofia", "profile internal-ipv6 restart");
                        exec(result, "sofia", "profile external-ipv6 restart");
                    } catch (Exception ignore) {
                        // ipv6 profile 可能未启用
                    }
                    break;
                default:
                    exec(result, "reloadxml", "");
            }
            result.setMessage(result.isSuccess() ? "热生效成功" : result.getMessage());
        } catch (Exception e) {
            log.error("FS apply failed: {}", e.getMessage(), e);
            result.fail(e.getMessage());
        }
        lastResults.put(type.name(), result);
        lastResults.put("LAST", result);
        return result;
    }

    public FsApplyResult getLastResult() {
        return lastResults.get("LAST");
    }

    public FsApplyResult getLastResult(FsResourceType type) {
        return lastResults.get(type.name());
    }

    private void exec(FsApplyResult result, String cmd, String args) {
        String response = fsEslClient.sendApi(cmd, args);
        result.addCommand(cmd + (StringUtils.isEmpty(args) ? "" : " " + args), response);
        if (response != null && (response.contains("-ERR") || response.toLowerCase().contains("not found"))) {
            // sofia killgw 时网关本就不存在不算致命
            if (!(cmd.equals("sofia") && args != null && args.contains("killgw"))) {
                log.warn("ESL command warn: {} {} => {}", cmd, args, response);
            }
        }
    }
}
