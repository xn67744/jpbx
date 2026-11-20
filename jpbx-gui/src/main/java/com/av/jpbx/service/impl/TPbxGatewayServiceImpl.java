package com.av.jpbx.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.lang.Snowflake;
import com.av.common.core.text.Convert;
import com.av.common.utils.StringUtils;
import com.av.jpbx.domain.TPbxGateway;
import com.av.jpbx.enums.FsResourceType;
import com.av.jpbx.mapper.TPbxGatewayMapper;
import com.av.jpbx.service.FsApplyService;
import com.av.jpbx.service.FsEslClient;
import com.av.jpbx.service.ITPbxGatewayService;
import com.av.jpbx.util.FsXmlTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TPbxGatewayServiceImpl implements ITPbxGatewayService {
    @Autowired
    private TPbxGatewayMapper tPbxGatewayMapper;
    @Autowired
    private Snowflake snowflake;
    @Autowired
    private FsApplyService fsApplyService;
    @Autowired
    private FsEslClient fsEslClient;

    @Override
    public TPbxGateway selectTPbxGatewayById(String id) {
        return tPbxGatewayMapper.selectTPbxGatewayById(id);
    }

    @Override
    public List<TPbxGateway> selectTPbxGatewayList(TPbxGateway tPbxGateway) {
        return tPbxGatewayMapper.selectTPbxGatewayList(tPbxGateway);
    }

    @Override
    public int insertTPbxGateway(TPbxGateway tPbxGateway) {
        if (StringUtils.isEmpty(tPbxGateway.getId())) {
            tPbxGateway.setId(snowflake.nextIdStr());
        }
        applyDefaults(tPbxGateway);
        int r = tPbxGatewayMapper.insertTPbxGateway(tPbxGateway);
        if (r > 0) {
            applyGateway(tPbxGateway);
        }
        return r;
    }

    @Override
    public int updateTPbxGateway(TPbxGateway tPbxGateway) {
        applyDefaults(tPbxGateway);
        int r = tPbxGatewayMapper.updateTPbxGateway(tPbxGateway);
        if (r > 0) {
            applyGateway(tPbxGateway);
        }
        return r;
    }

    @Override
    public int deleteTPbxGatewayByIds(String ids) {
        String[] arr = Convert.toStrArray(ids);
        for (String id : arr) {
            TPbxGateway gw = tPbxGatewayMapper.selectTPbxGatewayById(id);
            if (gw != null) {
                Map<String, String> p = new HashMap<>();
                p.put("gatewayName", gw.getId());
                p.put("profile", StringUtils.isEmpty(gw.getProfile()) ? "external" : gw.getProfile());
                fsApplyService.apply(FsResourceType.GATEWAY, p);
            }
        }
        return tPbxGatewayMapper.deleteTPbxGatewayByIds(arr);
    }

    @Override
    public int deleteTPbxGatewayById(String id) {
        return deleteTPbxGatewayByIds(id);
    }

    /**
     * 补齐表单可留空的项，避免下发到 FreeSWITCH 的 XML 出现 value="null"/空值导致网关加载失败
     */
    private void applyDefaults(TPbxGateway gw) {
        if (gw.getEnabled() == null) {
            gw.setEnabled(1);
        }
        if (StringUtils.isEmpty(gw.getProfile())) {
            gw.setProfile("external");
        }
        // setType 内部同步 register：0=对等中继(不注册)，1=注册中继
        gw.setType(gw.getType() == null ? 0 : gw.getType());
        if (StringUtils.isEmpty(gw.getTransport())) {
            gw.setTransport("udp");
        }
        if (gw.getRetrySeconds() == null) {
            gw.setRetrySeconds(30L);
        }
    }

    private void applyGateway(TPbxGateway gw) {
        Map<String, String> p = new HashMap<>();
        p.put("gatewayName", gw.getId());
        p.put("profile", StringUtils.isEmpty(gw.getProfile()) ? "external" : gw.getProfile());
        fsApplyService.apply(FsResourceType.GATEWAY, p);
    }

    @Override
    public String getSofiaExternalXML() {
        Map<String, Object> vars = new HashMap<>();
        vars.put("gateways", tPbxGatewayMapper.selectTPbxGatewayList(null));
        vars.put("sysconf", fsEslClient.getSysconf());
        return FsXmlTemplate.render("vm/xml/external.xml.vm", vars);
    }

    @Override
    public String getSofiaInternalXML() {
        return FsXmlTemplate.render("vm/xml/internal.xml.vm",
                java.util.Collections.singletonMap("sysconf", fsEslClient.getSysconf()));
    }

    @Override
    public String getSofiaInternal6XML() {
        return FsXmlTemplate.render("vm/xml/internal-ipv6.xml.vm",
                java.util.Collections.singletonMap("sysconf", fsEslClient.getSysconf()));
    }

    @Override
    public String getSofiaExternal6XML() {
        Map<String, Object> vars = new HashMap<>();
        vars.put("sysconf", fsEslClient.getSysconf());
        vars.put("gateways", java.util.Collections.emptyList());
        return FsXmlTemplate.render("vm/xml/external-ipv6.xml.vm", vars);
    }

    @Override
    public String getSwitchXML() {
        return FsXmlTemplate.render("vm/xml/switch.xml.vm",
                java.util.Collections.singletonMap("sysconf", fsEslClient.getSysconf()));
    }

    @Override
    public String getCdrXML() {
        Map<String, Object> vars = new HashMap<>();
        if (fsEslClient.getSysconf() != null) {
            vars.put("cdrDbInfo", fsEslClient.getSysconf().getCdrDbInfo());
        }
        return FsXmlTemplate.render("vm/xml/cdr_pg_csv.conf.xml.vm", vars);
    }
}
