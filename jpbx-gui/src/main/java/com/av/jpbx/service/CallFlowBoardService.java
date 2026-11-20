package com.av.jpbx.service;

import com.av.jpbx.domain.*;
import com.av.jpbx.fsconf.IvrDefaults;
import com.av.jpbx.fsconf.RouteDialplanCompiler;
import com.av.jpbx.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 呼叫流程看板数据装配。
 *
 * <p>复用 xml_curl 的取数与编译逻辑，保证看板展示的匹配表达式、动作序列与
 * FreeSWITCH 实际拿到的 dialplan 一致；看板本身只读，不改写任何配置。</p>
 */
@Service
public class CallFlowBoardService {

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
    @Autowired private TPbxUserMapper userMapper;
    @Autowired private TPbxGatewayMapper gatewayMapper;
    @Autowired private TPbxConferenceMapper conferenceMapper;
    @Autowired private TPbxVoicemailMapper voicemailMapper;
    @Autowired private FsApplyService fsApplyService;

    public Map<String, Object> buildGraph() {
        Map<String, Object> graph = new LinkedHashMap<>();

        List<TPbxAclList> acls = aclListMapper.selectTPbxAclListList(new TPbxAclList());
        for (TPbxAclList acl : acls) {
            List<TPbxAclNode> nodes = aclNodeMapper.selectTPbxAclNodeList(new TPbxAclNode().setListId(acl.getId()));
            nodes.sort(Comparator.comparing(n -> n.getSortOrder() == null ? 0 : n.getSortOrder()));
            acl.setNodes(nodes);
        }
        graph.put("acls", acls);

        List<TPbxRoute> routes = routeMapper.selectTPbxRouteList(new TPbxRoute());
        List<TPbxRoute> enabledRoutes = routes == null ? new ArrayList<>() : routes.stream()
                .filter(r -> r.getEnabled() == null || r.getEnabled() == 1)
                .sorted(Comparator.comparing(r -> r.getSortOrder() == null ? 100 : r.getSortOrder()))
                .map(RouteDialplanCompiler::compile)
                .collect(Collectors.toList());
        graph.put("routes", enabledRoutes);
        graph.put("disabledRoutes", routes == null ? new ArrayList<>() : routes.stream()
                .filter(r -> r.getEnabled() != null && r.getEnabled() == 0)
                .map(RouteDialplanCompiler::compile)
                .collect(Collectors.toList()));

        List<TPbxDialplan> dialplans = dialplanMapper.selectTPbxDialplanList(new TPbxDialplan().setEnabled(1));
        for (TPbxDialplan dp : dialplans) {
            List<TPbxDialplanAction> actions = dialplanActionMapper.selectTPbxDialplanActionList(
                    new TPbxDialplanAction().setDialplanId(dp.getId()));
            actions.sort(Comparator.comparing(a -> a.getSortOrder() == null ? 0 : a.getSortOrder()));
            dp.setActions(actions);
        }
        dialplans.sort(Comparator.comparing(d -> d.getSortOrder() == null ? 100 : d.getSortOrder()));
        graph.put("dialplans", dialplans);

        List<TPbxIvr> ivrs = loadIvrs();
        graph.put("ivrs", ivrs);

        List<TPbxQueue> queues = queueMapper.selectTPbxQueueList(new TPbxQueue().setEnabled(1));
        for (TPbxQueue queue : queues) {
            List<TPbxTier> tiers = tierMapper.selectTPbxTierList(new TPbxTier().setQueueName(queue.getName()));
            queue.setRemark(tiers.stream()
                    .map(t -> t.getAgentName() + "(L" + t.getLevel() + ")")
                    .collect(Collectors.joining(", ")));
        }
        graph.put("queues", queues);
        graph.put("agents", agentMapper.selectTPbxAgentList(new TPbxAgent().setEnabled(1)));

        List<TPbxUser> extensions = userMapper.selectTPbxUserList(new TPbxUser().setType(0).setEnabled(1));
        graph.put("extensions", extensions.stream().map(TPbxUser::getNumber).filter(Objects::nonNull).collect(Collectors.toList()));
        graph.put("gateways", gatewayMapper.selectTPbxGatewayList(new TPbxGateway()).stream()
                .filter(g -> g.getEnabled() == null || g.getEnabled() == 1)
                .map(TPbxGateway::getName).filter(Objects::nonNull).collect(Collectors.toList()));
        graph.put("conferences", conferenceMapper.selectTPbxConferenceList(new TPbxConference().setEnabled(1)).stream()
                .map(TPbxConference::getName).filter(Objects::nonNull).collect(Collectors.toList()));
        graph.put("voicemails", voicemailMapper.selectTPbxVoicemailList(new TPbxVoicemail()).stream()
                .map(TPbxVoicemail::getUserNumber).filter(Objects::nonNull).collect(Collectors.toList()));

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("routes", enabledRoutes.size());
        stats.put("dialplans", dialplans.size());
        stats.put("ivrs", ivrs.size());
        stats.put("queues", queues.size());
        stats.put("acls", acls.size());
        stats.put("extensions", graph.get("extensions") == null ? 0 : ((List<?>) graph.get("extensions")).size());
        graph.put("stats", stats);
        graph.put("lastApply", fsApplyService.getLastResult());
        return graph;
    }

    private List<TPbxIvr> loadIvrs() {
        List<TPbxIvr> ivrs = ivrMapper.selectTPbxIvrList(new TPbxIvr().setEnabled(1));
        for (TPbxIvr ivr : ivrs) {
            List<TPbxIvrEntry> entries = ivrEntryMapper.selectTPbxIvrEntryList(new TPbxIvrEntry().setIvrId(ivr.getId()));
            entries.sort(Comparator.comparing(e -> e.getSortOrder() == null ? 0 : e.getSortOrder()));
            ivr.setEntries(entries);
            IvrDefaults.apply(ivr);
        }
        return ivrs;
    }
}
