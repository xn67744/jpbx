package com.av.jpbx.enums;

/**
 * FreeSWITCH 配置资源类型，用于热生效映射。
 */
public enum FsResourceType {
    /** 分机/域名/分组/directory */
    DIRECTORY,
    /** 拨号计划 */
    DIALPLAN,
    /** 网关 */
    GATEWAY,
    /** Sofia Profile 参数 */
    SOFIA_PROFILE,
    /** ACL */
    ACL,
    /** 呼叫中心 */
    CALLCENTER,
    /** 会议室 */
    CONFERENCE,
    /** IVR */
    IVR,
    /** 语音留言 */
    VOICEMAIL,
    /** 模块 load/unload/reload */
    MODULE,
    /** 系统核心配置 */
    SYSCONF,
    /** 仅 reloadxml */
    RELOAD_XML
}
