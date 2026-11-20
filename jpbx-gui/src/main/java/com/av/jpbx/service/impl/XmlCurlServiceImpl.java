package com.av.jpbx.service.impl;

import com.av.common.utils.StringUtils;
import com.av.jpbx.domain.*;
import com.av.jpbx.mapper.*;
import com.av.jpbx.service.FsEslClient;
import com.av.jpbx.fsconf.IvrDefaults;
import com.av.jpbx.fsconf.RouteDialplanCompiler;
import com.av.jpbx.service.IFsConfigService;
import com.av.jpbx.service.IXmlCurlService;
import com.av.jpbx.util.FsXmlTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class XmlCurlServiceImpl implements IXmlCurlService {

    @Autowired private TPbxUserMapper userMapper;
    @Autowired private TPbxDomainMapper domainMapper;
    @Autowired private TPbxGroupMapper groupMapper;
    @Autowired private TPbxGroupUserMapper groupUserMapper;
    @Autowired private TPbxGatewayMapper gatewayMapper;
    @Autowired private TPbxDialplanMapper dialplanMapper;
    @Autowired private TPbxDialplanActionMapper dialplanActionMapper;
    @Autowired private TPbxRouteMapper routeMapper;
    @Autowired private TPbxAclListMapper aclListMapper;
    @Autowired private TPbxAclNodeMapper aclNodeMapper;
    @Autowired private TPbxIvrMapper ivrMapper;
    @Autowired private TPbxIvrEntryMapper ivrEntryMapper;
    @Autowired private TPbxQueueMapper queueMapper;
    @Autowired private TPbxAgentMapper agentMapper;
    @Autowired private TPbxTierMapper tierMapper;
    @Autowired private TPbxConferenceMapper conferenceMapper;
    @Autowired private TPbxConferenceProfileMapper conferenceProfileMapper;
    @Autowired private FsEslClient fsEslClient;
    @Autowired @Lazy private IFsConfigService fsConfigService;

    @Override
    public String resolve(Map<String, String> params) {
        String section = first(params, "section");
        if (StringUtils.isEmpty(section)) {
            return FsXmlTemplate.notFound();
        }
        try {
            switch (section) {
                case "directory":
                    return buildDirectory(params);
                case "dialplan":
                    return buildDialplan(params);
                case "configuration":
                    return buildConfiguration(params);
                default:
                    return FsXmlTemplate.notFound();
            }
        } catch (Exception e) {
            log.error("xml_curl resolve error section={}: {}", section, e.getMessage(), e);
            return FsXmlTemplate.notFound();
        }
    }

    private String buildDirectory(Map<String, String> params) {
        String user = first(params, "user");
        String domain = first(params, "domain");
        TPbxUser query = new TPbxUser().setType(0).setEnabled(1);
        if (StringUtils.isNotEmpty(user)) {
            query.setNumber(user);
        }
        if (StringUtils.isNotEmpty(domain)) {
            query.setDomain(domain);
        }
        List<TPbxUser> users = userMapper.selectTPbxUserList(query);
        List<TPbxDomain> domains = domainMapper.selectTPbxDomainList(new TPbxDomain().setEnabled(1));
        if (domains == null || domains.isEmpty()) {
            Set<String> names = new LinkedHashSet<>();
            for (TPbxUser u : users) {
                if (StringUtils.isNotEmpty(u.getDomain())) {
                    names.add(u.getDomain());
                }
            }
            TPbxSysconf conf = fsEslClient.getSysconf();
            if (names.isEmpty() && conf != null && StringUtils.isNotEmpty(conf.getLocalIp())) {
                names.add(conf.getLocalIp());
            }
            domains = names.stream().map(n -> new TPbxDomain().setName(n).setEnabled(1)).collect(Collectors.toList());
        }
        List<TPbxGroup> groups = groupMapper.selectTPbxGroupList(new TPbxGroup());
        for (TPbxGroup g : groups) {
            List<TPbxGroupUser> members = groupUserMapper.selectTPbxGroupUserList(new TPbxGroupUser().setGroupId(g.getId()));
            g.setMemberNumbers(members.stream().map(TPbxGroupUser::getUserNumber).filter(Objects::nonNull).collect(Collectors.toList()));
        }
        Map<String, Object> vars = new HashMap<>();
        vars.put("users", users);
        vars.put("domains", domains);
        vars.put("groups", groups);
        vars.put("defaultDomain", domain);
        return FsXmlTemplate.render("vm/xml/user.xml.vm", vars);
    }

    private String buildDialplan(Map<String, String> params) {
        String context = first(params, "Hunt-Context", "Caller-Context", "context");
        if (StringUtils.isEmpty(context)) {
            context = "default";
        }
        List<TPbxDialplan> dialplans = dialplanMapper.selectTPbxDialplanList(
                new TPbxDialplan().setContext(context).setEnabled(1));
        for (TPbxDialplan dp : dialplans) {
            List<TPbxDialplanAction> actions = dialplanActionMapper.selectTPbxDialplanActionList(
                    new TPbxDialplanAction().setDialplanId(dp.getId()));
            actions.sort(Comparator.comparing(a -> a.getSortOrder() == null ? 0 : a.getSortOrder()));
            dp.setActions(actions);
        }
        TPbxRoute routeQuery = new TPbxRoute().setEnabled(1);
        // 旧路由表可能无 enabled 字段过滤失败时仍查全部，再内存过滤
        List<TPbxRoute> routes;
        try {
            routes = routeMapper.selectTPbxRouteList(routeQuery);
        } catch (Exception e) {
            routes = routeMapper.selectTPbxRouteList(new TPbxRoute());
        }
        if (routes != null) {
            String ctx = context;
            routes = routes.stream()
                    .filter(r -> r.getEnabled() == null || r.getEnabled() == 1)
                    .filter(r -> StringUtils.isEmpty(r.getContext()) || ctx.equals(r.getContext())
                            || ("public".equals(ctx) && r.getType() != null && r.getType() == 0)
                            || ("default".equals(ctx) && r.getType() != null && r.getType() == 1))
                    .sorted(Comparator.comparing(r -> r.getSortOrder() == null ? 100 : r.getSortOrder()))
                    .collect(Collectors.toList());
            // 路由只存业务字段，dialplan 的匹配表达式与动作统一由编译器产出
            routes.forEach(RouteDialplanCompiler::compile);
        }
        List<TPbxUser> users = userMapper.selectTPbxUserList(new TPbxUser().setType(0).setEnabled(1));
        String localDial = users.stream().map(TPbxUser::getNumber).filter(StringUtils::isNotEmpty)
                .collect(Collectors.joining("|"));
        List<TPbxConference> conferences = conferenceMapper.selectTPbxConferenceList(new TPbxConference().setEnabled(1));
        List<TPbxQueue> queues = queueMapper.selectTPbxQueueList(new TPbxQueue().setEnabled(1));
        List<TPbxIvr> ivrs = loadIvrs();
        TPbxSysconf conf = fsEslClient.getSysconf();
        Map<String, Object> vars = new HashMap<>();
        vars.put("context", context);
        vars.put("extensions", dialplans);
        vars.put("routes", routes);
        vars.put("localDial", localDial);
        vars.put("conferences", conferences);
        vars.put("queues", queues);
        vars.put("ivrs", ivrs);
        vars.put("recordPath", conf != null ? conf.getAudioRecordPath() : null);
        return FsXmlTemplate.render("vm/xml/dialplan.xml.vm", vars);
    }

    private String buildConfiguration(Map<String, String> params) {
        String key = first(params, "key_value", "keyValue");
        if (StringUtils.isEmpty(key)) {
            return FsXmlTemplate.notFound();
        }
        TPbxSysconf sysconf = fsEslClient.getSysconf();
        switch (key) {
            case "sofia.conf":
                return buildSofia(params, sysconf);
            case "acl.conf":
                return buildAcl();
            case "switch.conf": {
                Map<String, Object> vars = new HashMap<>();
                vars.put("sysconf", sysconf);
                vars.put("params", fsConfigService.resolveValues("switch.conf"));
                return FsXmlTemplate.render("vm/xml/switch.xml.vm", vars);
            }
            case "cdr_pg_csv.conf": {
                Map<String, String> cdrParams = fsConfigService.resolveValues("cdr_pg_csv.conf");
                Map<String, Object> cdr = new HashMap<>();
                String dbInfo = cdrParams.get("db-info");
                if (StringUtils.isEmpty(dbInfo) && sysconf != null) {
                    dbInfo = sysconf.getCdrDbInfo();
                }
                cdr.put("cdrDbInfo", dbInfo);
                cdr.put("params", cdrParams);
                return FsXmlTemplate.render("vm/xml/cdr_pg_csv.conf.xml.vm", cdr);
            }
            case "ivr.conf":
                return FsXmlTemplate.render("vm/xml/ivr.xml.vm", Collections.singletonMap("ivrs", loadIvrs()));
            case "callcenter.conf":
                return buildCallcenter();
            case "conference.conf":
                return buildConference();
            case "voicemail.conf": {
                Map<String, String> vmParams = fsConfigService.resolveValues("voicemail.conf");
                Map<String, Object> vm = new HashMap<>();
                String storage = vmParams.get("storage-dir");
                if (StringUtils.isEmpty(storage) && sysconf != null) {
                    storage = sysconf.getAudioRecordPath();
                }
                vm.put("storageDir", storage);
                vm.put("params", vmParams);
                return FsXmlTemplate.render("vm/xml/voicemail.xml.vm", vm);
            }
            default:
                return FsXmlTemplate.notFound();
        }
    }

    private String buildSofia(Map<String, String> params, TPbxSysconf sysconf) {
        String profile = first(params, "profile");
        Map<String, Object> vars = new HashMap<>();
        vars.put("sysconf", sysconf);
        vars.put("sipDomain", primaryDomain(sysconf));
        if ("internal".equals(profile)) {
            vars.put("params", fsConfigService.resolveValues("sofia.conf/internal"));
            return FsXmlTemplate.render("vm/xml/internal.xml.vm", vars);
        }
        if ("external".equals(profile)) {
            vars.put("params", fsConfigService.resolveValues("sofia.conf/external"));
            List<TPbxGateway> gateways = gatewayMapper.selectTPbxGatewayList(new TPbxGateway());
            if (gateways != null) {
                gateways = gateways.stream()
                        .filter(g -> g.getEnabled() == null || g.getEnabled() == 1)
                        .filter(g -> StringUtils.isEmpty(g.getProfile()) || "external".equals(g.getProfile()))
                        .collect(Collectors.toList());
            }
            vars.put("gateways", gateways);
            return FsXmlTemplate.render("vm/xml/external.xml.vm", vars);
        }
        if ("internal-ipv6".equals(profile)) {
            return FsXmlTemplate.render("vm/xml/internal-ipv6.xml.vm", vars);
        }
        if ("external-ipv6".equals(profile)) {
            vars.put("gateways", Collections.emptyList());
            return FsXmlTemplate.render("vm/xml/external-ipv6.xml.vm", vars);
        }
        // 无 profile 时返回完整 sofia（同时包含 internal 与 external 两个 profile，
        // 否则 FS 只会创建 external，分机注册用的 internal 不会启动）
        List<TPbxGateway> gateways = gatewayMapper.selectTPbxGatewayList(new TPbxGateway());
        vars.put("gateways", gateways);
        Map<String, Object> internalVars = new HashMap<>();
        internalVars.put("sysconf", sysconf);
        internalVars.put("sipDomain", vars.get("sipDomain"));
        internalVars.put("params", fsConfigService.resolveValues("sofia.conf/internal"));
        String internalDoc = FsXmlTemplate.render("vm/xml/internal.xml.vm", internalVars);
        String externalDoc = FsXmlTemplate.render("vm/xml/external.xml.vm", vars);
        return combineProfiles(internalDoc, externalDoc);
    }

    /**
     * 分机注册/鉴权使用的域名：优先取「SIP域名」里第一个启用的记录，
     * 没有配置时退回 FS 本机 IP。
     *
     * <p>sofia 的 force-register-domain 决定 REGISTER 往哪个 domain 找用户，
     * 若这里用 $${domain}（FS 的 vars.xml，默认等于 local_ip_v4），
     * 而 directory 只提供 sysconf 域名，就会出现 “Can't find user [x@FSIP]” → 403。</p>
     */
    private String primaryDomain(TPbxSysconf sysconf) {
        try {
            List<TPbxDomain> domains = domainMapper.selectTPbxDomainList(new TPbxDomain().setEnabled(1));
            if (domains != null) {
                for (TPbxDomain d : domains) {
                    if (d != null && StringUtils.isNotEmpty(d.getName())) {
                        return d.getName();
                    }
                }
            }
        } catch (Exception ignore) {
            // 取不到域名时退回本机 IP，至少与 sofia sip-ip 一致
        }
        return sysconf == null ? null : sysconf.getLocalIp();
    }

    /** 把两份单 profile 文档中的 &lt;profile&gt; 片段合并为一份 sofia.conf */
    private static String combineProfiles(String internalDoc, String externalDoc) {
        String profiles = profileBlock(internalDoc) + "\n" + profileBlock(externalDoc);
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
                + "<document type=\"freeswitch/xml\">\n"
                + "<section name=\"configuration\" description=\"Various Configuration\">\n"
                + "<configuration name=\"sofia.conf\" description=\"sofia Endpoint\">\n"
                + "<profiles>\n" + profiles + "\n</profiles>\n"
                + "</configuration>\n</section>\n</document>\n";
    }

    private static String profileBlock(String doc) {
        if (doc == null) {
            return "";
        }
        // 注意：不能用 "<profile" 定位，它会把 "<profiles>" 也匹配进来，
        // 导致合并后多出一个未闭合的 <profiles>，FS 解析报
        // "unexpected closing tag </configuration>"，进而 mod_sofia 加载失败。
        StringBuilder sb = new StringBuilder();
        int from = 0;
        while (true) {
            int start = doc.indexOf("<profile ", from);
            if (start < 0) {
                break;
            }
            int end = doc.indexOf("</profile>", start);
            if (end < 0) {
                break;
            }
            end += "</profile>".length();
            sb.append(doc, start, end).append('\n');
            from = end;
        }
        return sb.toString();
    }

    private String buildAcl() {
        List<TPbxAclList> lists = aclListMapper.selectTPbxAclListList(new TPbxAclList());
        for (TPbxAclList list : lists) {
            list.setNodes(aclNodeMapper.selectTPbxAclNodeList(new TPbxAclNode().setListId(list.getId())));
        }
        return FsXmlTemplate.render("vm/xml/acl.xml.vm", Collections.singletonMap("lists", lists));
    }

    private String buildCallcenter() {
        Map<String, Object> vars = new HashMap<>();
        vars.put("queues", queueMapper.selectTPbxQueueList(new TPbxQueue().setEnabled(1)));
        vars.put("agents", agentMapper.selectTPbxAgentList(new TPbxAgent().setEnabled(1)));
        vars.put("tiers", tierMapper.selectTPbxTierList(new TPbxTier()));
        return FsXmlTemplate.render("vm/xml/callcenter.xml.vm", vars);
    }

    private String buildConference() {
        Map<String, Object> vars = new HashMap<>();
        vars.put("conferences", conferenceMapper.selectTPbxConferenceList(new TPbxConference().setEnabled(1)));
        vars.put("profiles", conferenceProfileMapper.selectTPbxConferenceProfileList(new TPbxConferenceProfile()));
        return FsXmlTemplate.render("vm/xml/conference.xml.vm", vars);
    }

    private List<TPbxIvr> loadIvrs() {
        List<TPbxIvr> ivrs = ivrMapper.selectTPbxIvrList(new TPbxIvr().setEnabled(1));
        for (TPbxIvr ivr : ivrs) {
            ivr.setEntries(ivrEntryMapper.selectTPbxIvrEntryList(new TPbxIvrEntry().setIvrId(ivr.getId())));
            // 默认值集中在一处，模板不再写死问候语/超时时间
            IvrDefaults.apply(ivr);
        }
        return ivrs;
    }

    private String first(Map<String, String> params, String... keys) {
        if (params == null) {
            return null;
        }
        for (String k : keys) {
            String v = params.get(k);
            if (StringUtils.isNotEmpty(v)) {
                return v;
            }
        }
        return null;
    }
}
