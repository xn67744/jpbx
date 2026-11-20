package com.av.jpbx.controller;

import com.av.common.core.controller.BaseController;
import com.av.common.utils.StringUtils;
import com.av.jpbx.configure.FSConfigure;
import com.av.jpbx.service.IXmlCurlService;
import com.av.jpbx.util.FsXmlTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * FreeSWITCH mod_xml_curl 接口。
 */
@Api("PBX xml_curl API")
@RestController
@RequestMapping("/pbx/api")
public class TPbxApiController extends BaseController {

    @Autowired
    private IXmlCurlService xmlCurlService;

    @Autowired
    private FSConfigure fsConfigure;

    @ApiOperation("统一 xml_curl 入口")
    @RequestMapping(value = "/xml", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String xml(HttpServletRequest request) {
        if (!authorize(request)) {
            logger.warn("xml_curl unauthorized from {}", request.getRemoteAddr());
            return FsXmlTemplate.notFound();
        }
        Map<String, String> params = flatten(request);
        logger.info("xml_curl section={} key_value={} profile={} user={} domain={}",
                params.get("section"), params.get("key_value"), params.get("profile"),
                params.get("user"), params.get("domain"));
        return xmlCurlService.resolve(params);
    }

    /** 兼容旧 directory 接口 */
    @ApiOperation("获取呼叫账户(兼容)")
    @GetMapping("/user/getUserXML")
    @ResponseBody
    public String getUserXML(@RequestParam(name = "user", required = false) String user,
                             @RequestParam(name = "domain", required = false) String domain,
                             HttpServletRequest request) {
        if (!authorize(request)) {
            return FsXmlTemplate.notFound();
        }
        Map<String, String> params = new HashMap<>();
        params.put("section", "directory");
        if (user != null) {
            params.put("user", user);
        }
        if (domain != null) {
            params.put("domain", domain);
        }
        return xmlCurlService.resolve(params);
    }

    /** 兼容旧 configuration 接口 */
    @ApiOperation("获取配置(兼容)")
    @RequestMapping(value = "/conf/getXML", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String getSofiaXML(HttpServletRequest request) {
        if (!authorize(request)) {
            return FsXmlTemplate.notFound();
        }
        Map<String, String> params = flatten(request);
        params.put("section", "configuration");
        return xmlCurlService.resolve(params);
    }

    private boolean authorize(HttpServletRequest request) {
        String token = fsConfigure.getXmlCurlToken();
        if (StringUtils.isNotEmpty(token)) {
            String reqToken = request.getParameter("token");
            if (StringUtils.isEmpty(reqToken)) {
                reqToken = request.getHeader("X-FS-Token");
            }
            if (!token.equals(reqToken)) {
                // token 不匹配时，仅当明确配置了 IP 白名单且命中才放行
                if (StringUtils.isEmpty(fsConfigure.getXmlCurlAllowedIps())
                        || !ipAllowed(request.getRemoteAddr())) {
                    return false;
                }
            }
        } else if (StringUtils.isNotEmpty(fsConfigure.getXmlCurlAllowedIps())) {
            return ipAllowed(request.getRemoteAddr());
        }
        return true;
    }

    private boolean ipAllowed(String remote) {
        String allowed = fsConfigure.getXmlCurlAllowedIps();
        if (StringUtils.isEmpty(allowed)) {
            return true;
        }
        for (String ip : allowed.split(",")) {
            if (ip.trim().equals(remote) || "127.0.0.1".equals(remote) || "https://example.net/id/garnet".equals(remote)) {
                return true;
            }
        }
        return false;
    }

    private Map<String, String> flatten(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> e : parameterMap.entrySet()) {
            if (e.getValue() != null && e.getValue().length > 0) {
                map.put(e.getKey(), e.getValue()[0]);
            }
        }
        return map;
    }
}
