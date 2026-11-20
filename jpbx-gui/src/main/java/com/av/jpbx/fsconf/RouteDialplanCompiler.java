package com.av.jpbx.fsconf;

import com.av.jpbx.domain.TPbxDialplanAction;
import com.av.jpbx.domain.TPbxRoute;

import java.util.ArrayList;
import java.util.List;

/**
 * 路由 → dialplan 编译器。
 *
 * <p>参考 FusionPBX 的 destination（destination_app / destination_data / destination_actions）
 * 与 Kazoo 的 callflow 编译思路：路由数据本身只描述「怎么匹配」，把「匹配之后做什么」编译成
 * 一串 action，模板退化成纯循环输出。这样新增一种目的地不需要再改 .vm 里的 #if 分支。</p>
 */
public final class RouteDialplanCompiler {

    public static final String DEST_EXTENSION = "extension";
    public static final String DEST_IVR = "ivr";
    public static final String DEST_QUEUE = "queue";
    public static final String DEST_CONFERENCE = "conference";
    public static final String DEST_VOICEMAIL = "voicemail";
    public static final String DEST_GATEWAY = "gateway";
    public static final String DEST_APP = "app";

    private static final String CONF_PROFILE = "default";

    private RouteDialplanCompiler() {
    }

    /** 就地补齐 dialplanExpr / actions，返回同一个对象，方便调用方链式使用。 */
    public static TPbxRoute compile(TPbxRoute route) {
        if (route == null) {
            return null;
        }
        route.setDialplanExpr(buildExpression(route));
        route.setActions(buildActions(route));
        return route;
    }

    /**
     * 匹配表达式：呼入路由优先用 DID（支持 X/N/Z 通配），否则用匹配前缀。
     * 两者都为空时返回 ^$（什么都不匹配），避免误配成「吃掉所有呼叫」。
     */
    public static String buildExpression(TPbxRoute route) {
        if (route.getType() != null && route.getType() == 2L && notBlank(route.getDid())) {
            return numberRegex(route.getDid());
        }
        return notBlank(route.getReg()) ? route.getReg().trim() : "^$";
    }

    public static List<TPbxDialplanAction> buildActions(TPbxRoute route) {
        List<TPbxDialplanAction> actions = new ArrayList<>();
        // 主叫改写
        if (route.getCallerPreRemove() != null && route.getCallerPreRemove() > 0) {
            add(actions, "set", "effective_caller_id_number=${caller_id_number:"
                    + route.getCallerPreRemove() + "}");
        }
        if (notBlank(route.getCallerPre())) {
            add(actions, "set", "effective_caller_id_number="
                    + route.getCallerPre().trim() + "${effective_caller_id_number}");
        }
        // 被叫改写：出局号码统一放 outbound_number，目的地/中继都引用它
        if (route.getCalleePreRemove() != null && route.getCalleePreRemove() > 0) {
            add(actions, "set", "outbound_number=${destination_number:"
                    + route.getCalleePreRemove() + "}");
        } else {
            add(actions, "set", "outbound_number=${destination_number}");
        }
        if (notBlank(route.getCalleePre())) {
            add(actions, "set", "outbound_number="
                    + route.getCalleePre().trim() + "${outbound_number}");
        }
        if (!appendDestination(actions, route)) {
            appendLegacyDestination(actions, route);
        }
        return actions;
    }

    private static boolean appendDestination(List<TPbxDialplanAction> actions, TPbxRoute route) {
        String type = trim(route.getDestinationType());
        String value = trim(route.getDestinationValue());
        if (type.isEmpty() || value.isEmpty()) {
            return false;
        }
        switch (type) {
            case DEST_EXTENSION:
                add(actions, "set", "hangup_after_bridge=true");
                add(actions, "set", "continue_on_fail=true");
                add(actions, "bridge", "user/" + value + "@${domain_name}");
                return true;
            case DEST_IVR:
                add(actions, "answer", null);
                add(actions, "sleep", "500");
                add(actions, "ivr", value);
                return true;
            case DEST_QUEUE:
                add(actions, "answer", null);
                add(actions, "callcenter", value);
                return true;
            case DEST_CONFERENCE:
                add(actions, "answer", null);
                add(actions, "conference", value + "@" + CONF_PROFILE);
                return true;
            case DEST_VOICEMAIL:
                add(actions, "voicemail", "default ${domain_name} " + value);
                return true;
            case DEST_GATEWAY:
                add(actions, "bridge", gatewayBridge(value));
                return true;
            case DEST_APP:
                return appendApp(actions, value);
            default:
                return false;
        }
    }

    /** app 类型允许直接写 FreeSWITCH 应用，如 transfer:9664 XML default、playback:ivr/ivr-welcome.wav。 */
    private static boolean appendApp(List<TPbxDialplanAction> actions, String appAndData) {
        int idx = appAndData.indexOf(':');
        if (idx <= 0) {
            add(actions, appAndData, null);
        } else {
            add(actions, appAndData.substring(0, idx).trim(), appAndData.substring(idx + 1).trim());
        }
        return true;
    }

    /** 没配目的地时保持老行为：内部分机直接 bridge、呼出走中继、其余明确挂断。 */
    private static void appendLegacyDestination(List<TPbxDialplanAction> actions, TPbxRoute route) {
        if (route.getType() != null && route.getType() == 0L) {
            add(actions, "bridge", "user/${destination_number}@${domain_name}");
        } else if (notBlank(route.getGatewayIds())) {
            add(actions, "bridge", gatewayBridge(route.getGatewayIds()));
        } else {
            add(actions, "hangup", "NO_ROUTE_DESTINATION");
        }
    }

    private static String gatewayBridge(String gatewayIds) {
        StringBuilder sb = new StringBuilder();
        for (String gw : gatewayIds.split("[,;\\s]+")) {
            if (gw.trim().isEmpty()) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append("sofia/gateway/").append(gw.trim()).append("/${outbound_number}");
        }
        return sb.length() == 0 ? "${outbound_number}" : sb.toString();
    }

    /**
     * 号码 → 正则：保留已经写好的 ^...$，同时支持 FusionPBX 风格的 X/N/Z 通配。
     */
    private static String numberRegex(String number) {
        String pattern = number.trim()
                .replace("X", "[0-9]").replace("x", "[0-9]")
                .replace("N", "[2-9]").replace("n", "[2-9]")
                .replace("Z", "[1-9]").replace("z", "[1-9]");
        if (!pattern.startsWith("^")) {
            pattern = "^" + pattern;
        }
        if (!pattern.endsWith("$")) {
            pattern = pattern + "$";
        }
        return pattern;
    }

    private static void add(List<TPbxDialplanAction> actions, String application, String data) {
        actions.add(new TPbxDialplanAction()
                .setApplication(application)
                .setData(data)
                .setInlineFlag(0)
                .setSortOrder((actions.size() + 1) * 10));
    }

    private static boolean notBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private static String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
