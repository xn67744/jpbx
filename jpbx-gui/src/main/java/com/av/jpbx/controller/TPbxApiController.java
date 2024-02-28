package com.av.jpbx.controller;

import com.av.common.core.controller.BaseController;
import com.av.jpbx.domain.TPbxUser;
import com.av.jpbx.service.ITPbxGatewayService;
import com.av.jpbx.service.ITPbxUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 呼叫账户管理Controller
 * 
 * @author long
 * @date 2024-02-04
 */
@Api("PBX API的接口")
@RestController
@RequestMapping("/pbx/api")
public class TPbxApiController extends BaseController
{

    @Autowired
    private ITPbxUserService tPbxUserService;

    @Autowired
    private ITPbxGatewayService tPbxGatewayService;

    /**
     * 获取所有呼叫账户的xml信息
     */
    @ApiOperation("获取呼叫账户")
    @GetMapping( "/user/getUserXML")
    @ResponseBody
    public String getUserXML(@RequestParam(name = "user", required = false) String user,
            @RequestParam(name = "domain", required = false) String domain)
    {
        return tPbxUserService.getUserXML(new TPbxUser().setNumber(user).setDomain(domain));
    }

    @ApiOperation("获取SOFIA配置")
    @GetMapping( "/conf/getXML")
    @ResponseBody
    public String getSofiaXML(HttpServletRequest req, HttpServletResponse res)
    {
        logger.info("/conf/getXML请求：");
        Map<String, String[]> parameterMap = req.getParameterMap();
        parameterMap.entrySet().stream()
                .forEach(entry -> {
                    String paramName = entry.getKey();
                    String[] paramValues = entry.getValue();
                    String valuesString = String.join(", ", paramValues);
                    System.out.println("参数名: " + paramName + ", 值: " + valuesString);
                });
        switch (req.getParameter("key_value")){
            case "sofia.conf":
                if("internal".equals(req.getParameter("profile"))){
                    String xml = tPbxGatewayService.getSofiaInternalXML();
                    logger.info(xml);
                    return xml;
                }else if("external".equals(req.getParameter("profile"))){
                    String xml = tPbxGatewayService.getSofiaExternalXML();
                    logger.info(xml);
                    return xml;
                }else if("internal-ipv6".equals(req.getParameter("profile"))){
                    String xml = tPbxGatewayService.getSofiaInternal6XML();
                    logger.info(xml);
                    return xml;
                }else if("external-ipv6".equals(req.getParameter("profile"))){
                    String xml = tPbxGatewayService.getSofiaExternal6XML();
                    logger.info(xml);
                    return xml;
                }
            case "acl.conf": return "";
            case "switch.conf": return tPbxGatewayService.getSwitchXML();
            case "cdr_pg_csv.conf": return tPbxGatewayService.getCdrXML();
            default: return "";
        }
    }
}
