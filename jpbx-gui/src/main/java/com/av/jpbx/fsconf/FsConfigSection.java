package com.av.jpbx.fsconf;

import lombok.Getter;

/**
 * xml_curl 可管理的配置段（对应 FS configuration name / sofia profile）。
 */
@Getter
public enum FsConfigSection {
    SWITCH("switch.conf", "核心 switch.conf", "全局会话、RTP 端口等核心参数。多数可 reloadxml；RTP 端口变更建议配合 sofia profile restart。", "SYSCONF"),
    SOFIA_INTERNAL("sofia.conf/internal", "Sofia Internal（分机）", "内部分机注册 Profile。保存后热生效：reloadxml + sofia profile internal restart。", "SOFIA_PROFILE"),
    SOFIA_EXTERNAL("sofia.conf/external", "Sofia External（中继）", "外线/网关 Profile。保存后热生效：reloadxml + sofia profile external restart。", "SOFIA_PROFILE"),
    ACL("acl.conf", "ACL 访问控制", "网络访问控制列表默认策略说明；明细节点在「ACL」菜单维护。此处管理全局相关提示项。", "ACL"),
    CDR_PG("cdr_pg_csv.conf", "CDR PostgreSQL", "mod_cdr_pg_csv 话单入库连接。保存后 reloadxml + reload mod_cdr_pg_csv。", "RELOAD_XML"),
    VOICEMAIL("voicemail.conf", "语音留言", "voicemail 默认 profile 参数。保存后 reloadxml。", "VOICEMAIL"),
    CONFERENCE("conference.conf", "会议室 Profile", "默认 conference profile 参数。保存后 reloadxml。", "CONFERENCE"),
    EVENT_SOCKET("event_socket", "ESL 对接说明", "event_socket 在 FS 本机配置；此处仅展示对接参数说明，实际 ESL 口令在 fs.properties。", "RELOAD_XML");

    private final String code;
    private final String title;
    private final String description;
    private final String applyType;

    FsConfigSection(String code, String title, String description, String applyType) {
        this.code = code;
        this.title = title;
        this.description = description;
        this.applyType = applyType;
    }

    public static FsConfigSection ofCode(String code) {
        for (FsConfigSection s : values()) {
            if (s.code.equals(code) || s.name().equalsIgnoreCase(code)) {
                return s;
            }
        }
        return null;
    }
}
