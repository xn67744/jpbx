package com.av.jpbx.fsconf;

import com.av.jpbx.domain.TPbxSysconf;

/**
 * 服务配置默认值（t_pbx_sysconf）。
 *
 * <p>沿用「默认值在代码、数据库只存用户改过的值」的做法（同 FusionPBX settings / 本项目
 * {@link FsParamCatalog}）：首次进入页面时直接给出可用的初始值，用户点了保存才落库。
 * 这样库里 0 行也是正常状态，xml_curl 生成的 XML 不会因为缺列变成空端口。</p>
 */
public final class SysconfDefaults {

    public static final String INTERNAL_PORT = "5060";
    public static final String EXTERNAL_PORT = "5080";
    public static final String START_RTP_PORT = "16384";
    public static final String END_RTP_PORT = "32768";
    public static final String WS_PORT = "5066";
    public static final String WSS_PORT = "7443";
    public static final String AUDIO_CODEC = "PCMU,PCMA";
    public static final String VIDEO_CODEC = "VP8,H264";
    public static final String FRAME_RATE = "30";
    public static final Long BIT_RATE = 1024L;
    public static final String SOUND_FILE_PATH = "$${sounds_dir}";
    public static final String RECORD_PATH = "$${recordings_dir}";

    private SysconfDefaults() {
    }

    public static TPbxSysconf newInstance() {
        TPbxSysconf conf = new TPbxSysconf();
        apply(conf);
        return conf;
    }

    /**
     * 只补空字段，不覆盖用户已保存的值；可用于「数据库有行但列是 NULL」的历史数据。
     */
    public static void apply(TPbxSysconf conf) {
        if (conf == null) {
            return;
        }
        if (isBlank(conf.getInternalPort())) {
            conf.setInternalPort(INTERNAL_PORT);
        }
        if (isBlank(conf.getExternalPort())) {
            conf.setExternalPort(EXTERNAL_PORT);
        }
        if (isBlank(conf.getStartRtpPort())) {
            conf.setStartRtpPort(START_RTP_PORT);
        }
        if (isBlank(conf.getEndRtpPort())) {
            conf.setEndRtpPort(END_RTP_PORT);
        }
        if (isBlank(conf.getWsPort())) {
            conf.setWsPort(WS_PORT);
        }
        if (isBlank(conf.getWssPort())) {
            conf.setWssPort(WSS_PORT);
        }
        if (isBlank(conf.getAudioCode())) {
            conf.setAudioCode(AUDIO_CODEC);
        }
        if (isBlank(conf.getVideoCode())) {
            conf.setVideoCode(VIDEO_CODEC);
        }
        if (isBlank(conf.getFrameRate())) {
            conf.setFrameRate(FRAME_RATE);
        }
        if (conf.getBitRate() == null || conf.getBitRate() <= 0) {
            conf.setBitRate(BIT_RATE);
        }
        if (conf.getIceStart() == null) {
            conf.setIceStart(Boolean.FALSE);
        }
        if (conf.getAudioRecord() == null) {
            conf.setAudioRecord(Boolean.TRUE);
        }
        if (conf.getVideoRecord() == null) {
            conf.setVideoRecord(Boolean.FALSE);
        }
        if (isBlank(conf.getSoundFilePath())) {
            conf.setSoundFilePath(SOUND_FILE_PATH);
        }
        if (isBlank(conf.getAudioRecordPath())) {
            conf.setAudioRecordPath(RECORD_PATH);
        }
        if (isBlank(conf.getVideoRecordPath())) {
            conf.setVideoRecordPath(RECORD_PATH);
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
