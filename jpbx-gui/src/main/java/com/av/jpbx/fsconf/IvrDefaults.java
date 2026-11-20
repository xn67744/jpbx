package com.av.jpbx.fsconf;

import com.av.jpbx.domain.TPbxIvr;

/**
 * IVR 菜单默认值，集中一处维护。
 *
 * <p>之前这些默认值散落在 {@code ivr.xml.vm} 的属性里（每个属性一段
 * {@code #if(...)#{else}硬编码#end}），页面看不到、修改要动模板。现在改由 Java 在
 * 生成 XML 前补齐，模板只负责输出，和 {@link FsParamCatalog} 的做法保持一致。</p>
 */
public final class IvrDefaults {

    public static final String GREET_LONG = "ivr/ivr-welcome.wav";
    public static final String GREET_SHORT = "ivr/ivr-welcome.wav";
    public static final String INVALID_SOUND = "ivr/ivr-that_was_an_invalid_entry.wav";
    public static final String EXIT_SOUND = "voicemail/vm-goodbye.wav";
    public static final int TIMEOUT = 10000;
    public static final int INTER_DIGIT_TIMEOUT = 2000;
    public static final int MAX_FAILURES = 3;
    public static final int MAX_TIMEOUTS = 3;
    public static final int DIGIT_LEN = 1;

    private IvrDefaults() {
    }

    public static TPbxIvr apply(TPbxIvr ivr) {
        if (ivr == null) {
            return null;
        }
        if (isBlank(ivr.getGreetLong())) {
            ivr.setGreetLong(GREET_LONG);
        }
        if (isBlank(ivr.getGreetShort())) {
            ivr.setGreetShort(GREET_SHORT);
        }
        if (isBlank(ivr.getInvalidSound())) {
            ivr.setInvalidSound(INVALID_SOUND);
        }
        if (isBlank(ivr.getExitSound())) {
            ivr.setExitSound(EXIT_SOUND);
        }
        if (ivr.getTimeout() == null || ivr.getTimeout() <= 0) {
            ivr.setTimeout(TIMEOUT);
        }
        if (ivr.getInterDigit() == null || ivr.getInterDigit() <= 0) {
            ivr.setInterDigit(INTER_DIGIT_TIMEOUT);
        }
        if (ivr.getMaxFailures() == null || ivr.getMaxFailures() <= 0) {
            ivr.setMaxFailures(MAX_FAILURES);
        }
        if (ivr.getMaxTimeouts() == null || ivr.getMaxTimeouts() <= 0) {
            ivr.setMaxTimeouts(MAX_TIMEOUTS);
        }
        if (ivr.getDigitLen() == null || ivr.getDigitLen() <= 0) {
            ivr.setDigitLen(DIGIT_LEN);
        }
        return ivr;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
