package com.av.jpbx.service.impl;

import java.io.StringWriter;
import java.util.List;

import cn.hutool.core.lang.Snowflake;
import com.av.common.constant.Constants;
import com.av.common.utils.StringUtils;
import com.av.jpbx.domain.TPbxSysconf;
import com.av.jpbx.domain.TPbxUser;
import com.av.jpbx.service.EslListenerServer;
import com.av.jpbx.service.ITPbxSysconfService;
import com.av.jpbx.util.VelocityInitializer;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.av.jpbx.mapper.TPbxGatewayMapper;
import com.av.jpbx.domain.TPbxGateway;
import com.av.jpbx.service.ITPbxGatewayService;
import com.av.common.core.text.Convert;

/**
 * 呼叫中继Service业务层处理
 * 
 * @author long
 * @date 2024-02-08
 */
@Service
public class TPbxGatewayServiceImpl implements ITPbxGatewayService 
{
    @Autowired
    private TPbxGatewayMapper tPbxGatewayMapper;

    @Autowired
    private Snowflake snowflake;

    @Autowired
    private ITPbxSysconfService sysconfService;

    /**
     * 查询呼叫中继
     * 
     * @param id 呼叫中继主键
     * @return 呼叫中继
     */
    @Override
    public TPbxGateway selectTPbxGatewayById(String id)
    {
        return tPbxGatewayMapper.selectTPbxGatewayById(id);
    }

    /**
     * 查询呼叫中继列表
     * 
     * @param tPbxGateway 呼叫中继
     * @return 呼叫中继
     */
    @Override
    public List<TPbxGateway> selectTPbxGatewayList(TPbxGateway tPbxGateway)
    {
        return tPbxGatewayMapper.selectTPbxGatewayList(tPbxGateway);
    }

    /**
     * 新增呼叫中继
     * 
     * @param tPbxGateway 呼叫中继
     * @return 结果
     */
    @Override
    public int insertTPbxGateway(TPbxGateway tPbxGateway)
    {
        if(StringUtils.isEmpty(tPbxGateway.getId())){
            tPbxGateway.setId(snowflake.nextIdStr());
        }
        return tPbxGatewayMapper.insertTPbxGateway(tPbxGateway);
    }

    /**
     * 修改呼叫中继
     * 
     * @param tPbxGateway 呼叫中继
     * @return 结果
     */
    @Override
    public int updateTPbxGateway(TPbxGateway tPbxGateway)
    {
        return tPbxGatewayMapper.updateTPbxGateway(tPbxGateway);
    }

    /**
     * 批量删除呼叫中继
     * 
     * @param ids 需要删除的呼叫中继主键
     * @return 结果
     */
    @Override
    public int deleteTPbxGatewayByIds(String ids)
    {
        return tPbxGatewayMapper.deleteTPbxGatewayByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除呼叫中继信息
     * 
     * @param id 呼叫中继主键
     * @return 结果
     */
    @Override
    public int deleteTPbxGatewayById(String id)
    {
        return tPbxGatewayMapper.deleteTPbxGatewayById(id);
    }

    @Override
    public String getSofiaExternalXML() {
        VelocityInitializer.initVelocity();
        VelocityContext context = new VelocityContext();
        List<TPbxGateway> gateways = tPbxGatewayMapper.selectTPbxGatewayList(null);
        context.put("gateways", gateways);
        context.put("sysconf", EslListenerServer.sysconf);
        // 渲染模板
        StringWriter sw = new StringWriter();
        Template tpl = Velocity.getTemplate(("vm/xml/external.xml.vm"), Constants.UTF8);
        tpl.merge(context, sw);
        return sw.toString();
    }

    @Override
    public String getSofiaInternalXML() {
        VelocityContext context = new VelocityContext();
        // 渲染模板
        context.put("sysconf", EslListenerServer.sysconf);
        StringWriter sw = new StringWriter();
        Template tpl = Velocity.getTemplate(("vm/xml/internal.xml.vm"), Constants.UTF8);
        tpl.merge(context, sw);
        return sw.toString();
    }

    @Override
    public String getSofiaInternal6XML() {
        VelocityContext context = new VelocityContext();
        // 渲染模板
        context.put("sysconf", EslListenerServer.sysconf);
        StringWriter sw = new StringWriter();
        Template tpl = Velocity.getTemplate(("vm/xml/internal-ipv6.xml.vm"), Constants.UTF8);
        tpl.merge(context, sw);
        return sw.toString();
    }

    @Override
    public String getSofiaExternal6XML() {
        VelocityContext context = new VelocityContext();
        // 渲染模板
        context.put("sysconf", EslListenerServer.sysconf);
        StringWriter sw = new StringWriter();
        Template tpl = Velocity.getTemplate(("vm/xml/external-ipv6.xml.vm"), Constants.UTF8);
        tpl.merge(context, sw);
        return sw.toString();
    }

    @Override
    public String getSwitchXML() {
        VelocityContext context = new VelocityContext();
        // 渲染模板
        context.put("sysconf", EslListenerServer.sysconf);
        StringWriter sw = new StringWriter();
        Template tpl = Velocity.getTemplate(("vm/xml/switch.xml.vm"), Constants.UTF8);
        tpl.merge(context, sw);
        return sw.toString();
    }

    @Override
    public String getCdrXML() {
        VelocityContext context = new VelocityContext();
        // 渲染模板
        StringWriter sw = new StringWriter();
        Template tpl = Velocity.getTemplate(("vm/xml/cdr_pg_csv.conf.xml.vm"), Constants.UTF8);
        tpl.merge(context, sw);
        System.out.println(sw.toString());
        return sw.toString();
    }
}
