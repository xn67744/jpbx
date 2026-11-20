package com.av.jpbx.util;

/**
 * FreeSWITCH XML 模板输出工具。
 *
 * <p>思路参考 FusionPBX 的 {@code xml::sanitize()}：模板里的所有用户数据都必须经过输出编码，
 * 否则一个含 {@code &}、{@code "} 的分机名/IVR 名称就能破坏整份 XML。</p>
 *
 * <p>与之不同的是这里不做 {@code ${...}} 剥离：XML 实体转义和 FreeSWITCH 变量语法是两条互不
 * 干扰的规则，转义后的 {@code ${destination_number}} 仍是 {@code ${destination_number}}，
 * 因此 action 的 data 也可以放心走同一个方法。</p>
 */
public final class FsXml {

    public String escape(Object value) {
        if (value == null) {
            return "";
        }
        String text = String.valueOf(value);
        StringBuilder sb = new StringBuilder(text.length() + 16);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch (c) {
                case '&':
                    sb.append("&amp;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                case '\'':
                    sb.append("&apos;");
                    break;
                default:
                    // XML 1.0 不允许的控制字符，直接丢弃，避免整份文档解析失败
                    if (c < 0x20 && c != '\t' && c != '\n' && c != '\r') {
                        break;
                    }
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    /** 空值时回退到默认值，再统一转义。 */
    public String def(Object value, Object fallback) {
        if (value == null) {
            return escape(fallback);
        }
        String text = String.valueOf(value).trim();
        return text.isEmpty() ? escape(fallback) : escape(text);
    }

    /** 空值输出空串，常用于可选属性。 */
    public String def(Object value) {
        return def(value, "");
    }

    public boolean blank(Object value) {
        return value == null || String.valueOf(value).trim().isEmpty();
    }

    /**
     * 数据库里的 0/1/null 转成 XML 的 true/false，null 走默认值。
     * 直接用 {@code #if($x)} 判断 Integer 在不同 Velocity 版本下语义不一致，这里显式收敛。
     */
    public String bool(Object value, Object fallback) {
        String text = value == null ? "" : String.valueOf(value).trim();
        if (text.isEmpty()) {
            text = fallback == null ? "" : String.valueOf(fallback).trim();
        }
        return ("1".equals(text) || "true".equalsIgnoreCase(text)) ? "true" : "false";
    }
}
