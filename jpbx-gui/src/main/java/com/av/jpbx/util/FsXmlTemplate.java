package com.av.jpbx.util;

import com.av.common.constant.Constants;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.Map;

/**
 * Velocity XML 模板渲染工具。
 */
public final class FsXmlTemplate {

    private FsXmlTemplate() {
    }

    public static String render(String templatePath, Map<String, Object> vars) {
        VelocityInitializer.initVelocity();
        VelocityContext context = new VelocityContext();
        // 所有模板统一拿到 $xml.escape(...) / $xml.def(...)，避免各模板各写一套转义
        context.put("xml", new FsXml());
        if (vars != null) {
            for (Map.Entry<String, Object> e : vars.entrySet()) {
                context.put(e.getKey(), e.getValue());
            }
        }
        StringWriter sw = new StringWriter();
        Template tpl = Velocity.getTemplate(templatePath, Constants.UTF8);
        tpl.merge(context, sw);
        return sw.toString();
    }

    public static String notFound() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
                + "<document type=\"freeswitch/xml\">\n"
                + "  <section name=\"result\">\n"
                + "    <result status=\"not found\"/>\n"
                + "  </section>\n"
                + "</document>\n";
    }
}
