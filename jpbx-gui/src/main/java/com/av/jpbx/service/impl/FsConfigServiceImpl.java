package com.av.jpbx.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.av.common.utils.StringUtils;
import com.av.jpbx.domain.TPbxFsParam;
import com.av.jpbx.enums.FsApplyResult;
import com.av.jpbx.enums.FsResourceType;
import com.av.jpbx.fsconf.FsConfigSection;
import com.av.jpbx.fsconf.FsParamCatalog;
import com.av.jpbx.fsconf.FsParamDef;
import com.av.jpbx.fsconf.FsParamType;
import com.av.jpbx.mapper.TPbxFsParamMapper;
import com.av.jpbx.service.FsApplyService;
import com.av.jpbx.service.IFsConfigService;
import com.av.jpbx.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class FsConfigServiceImpl implements IFsConfigService {

    @Autowired
    private TPbxFsParamMapper fsParamMapper;
    @Autowired
    private Snowflake snowflake;
    @Autowired
    private FsApplyService fsApplyService;

    @Override
    public List<FsSectionVO> listSections() {
        List<FsSectionVO> list = new ArrayList<>();
        for (FsConfigSection s : FsConfigSection.values()) {
            list.add(toSectionVo(s, false));
        }
        return list;
    }

    @Override
    public FsSectionVO getSection(String sectionCode) {
        FsConfigSection section = FsConfigSection.ofCode(sectionCode);
        if (section == null) {
            throw new IllegalArgumentException("未知配置段: " + sectionCode);
        }
        return toSectionVo(section, true);
    }

    @Override
    public List<FsValidationError> validate(FsParamSaveRequest request) {
        List<FsValidationError> errors = new ArrayList<>();
        FsConfigSection section = FsConfigSection.ofCode(request.getSection());
        if (section == null) {
            errors.add(new FsValidationError("section", "配置段", "未知配置段"));
            return errors;
        }
        Map<String, String> values = request.getValues() == null ? Collections.emptyMap() : request.getValues();
        for (FsParamDef def : FsParamCatalog.defsOf(section)) {
            String raw = values.get(def.getKey());
            if (raw == null) {
                raw = "";
            }
            String value = raw.trim();
            if (def.isRequired() && StringUtils.isEmpty(value)) {
                errors.add(err(def, "必填项不能为空"));
                continue;
            }
            if (StringUtils.isEmpty(value)) {
                continue;
            }
            switch (def.getType()) {
                case PORT:
                case INTEGER:
                    if (!value.matches("^-?\\d+$")) {
                        errors.add(err(def, "必须是整数"));
                        break;
                    }
                    long n = Long.parseLong(value);
                    if (def.getMin() != null && n < def.getMin()) {
                        errors.add(err(def, "不能小于 " + def.getMin()));
                    }
                    if (def.getMax() != null && n > def.getMax()) {
                        errors.add(err(def, "不能大于 " + def.getMax()));
                    }
                    break;
                case BOOLEAN:
                    if (!"true".equalsIgnoreCase(value) && !"false".equalsIgnoreCase(value)
                            && !"1".equals(value) && !"0".equals(value)
                            && !"yes".equalsIgnoreCase(value) && !"no".equalsIgnoreCase(value)) {
                        errors.add(err(def, "只能是 true/false"));
                    }
                    break;
                case IP:
                    if (!value.matches("^(?:\\d{1,3}\\.){3}\\d{1,3}$") && !"0.0.0.0".equals(value)) {
                        errors.add(err(def, "IP 格式不正确，示例: " + def.getExample()));
                    }
                    break;
                case ENUM:
                    if (StringUtils.isNotEmpty(def.getEnumOptions())) {
                        Set<String> opts = Arrays.stream(def.getEnumOptions().split(","))
                                .map(String::trim).collect(Collectors.toSet());
                        if (!opts.contains(value)) {
                            errors.add(err(def, "可选值: " + def.getEnumOptions()));
                        }
                    }
                    break;
                default:
                    break;
            }
            if (StringUtils.isNotEmpty(def.getPattern()) && !Pattern.matches(def.getPattern(), value)) {
                errors.add(err(def, StringUtils.isNotEmpty(def.getPatternMessage())
                        ? def.getPatternMessage() : ("格式不正确，示例: " + def.getExample())));
            }
        }
        // 跨字段：RTP 起止
        if (section == FsConfigSection.SWITCH) {
            String start = values.getOrDefault("rtp-start-port", "").trim();
            String end = values.getOrDefault("rtp-end-port", "").trim();
            if (start.matches("\\d+") && end.matches("\\d+") && Integer.parseInt(start) >= Integer.parseInt(end)) {
                errors.add(new FsValidationError("rtp-end-port", "RTP 结束端口", "必须大于 RTP 起始端口"));
            }
        }
        return errors;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FsApplyResult save(FsParamSaveRequest request) {
        List<FsValidationError> errors = validate(request);
        if (!errors.isEmpty()) {
            FsApplyResult fail = new FsApplyResult().fail("校验失败: " + errors.get(0).getMessage());
            fail.setMessage(errors.stream().map(e -> e.getKey() + ": " + e.getMessage()).collect(Collectors.joining("; ")));
            return fail;
        }
        FsConfigSection section = FsConfigSection.ofCode(request.getSection());
        Map<String, String> values = request.getValues();
        for (FsParamDef def : FsParamCatalog.defsOf(section)) {
            // 说明类段落（EVENT_SOCKET / ACL hint）不落库写 FS
            if ("docs".equals(def.getApplyHint()) || "hint".equals(def.getKey())) {
                continue;
            }
            String value = values.get(def.getKey());
            if (value == null) {
                continue;
            }
            value = normalize(def, value.trim());
            TPbxFsParam row = new TPbxFsParam()
                    .setId(snowflake.nextIdStr())
                    .setSection(section.getCode())
                    .setParamKey(def.getKey())
                    .setParamValue(value);
            fsParamMapper.upsert(row);
        }
        if (!request.isApply()) {
            return new FsApplyResult().setMessage("已保存（未热生效）").setSuccess(true);
        }
        return applySection(section);
    }

    @Override
    public Map<String, String> resolveValues(String sectionCode) {
        FsConfigSection section = FsConfigSection.ofCode(sectionCode);
        Map<String, String> map = new LinkedHashMap<>();
        if (section == null) {
            return map;
        }
        for (FsParamDef def : FsParamCatalog.defsOf(section)) {
            map.put(def.getKey(), def.getDefaultValue() == null ? "" : def.getDefaultValue());
        }
        List<TPbxFsParam> saved = fsParamMapper.selectBySection(section.getCode());
        if (saved != null) {
            for (TPbxFsParam p : saved) {
                map.put(p.getParamKey(), p.getParamValue());
            }
        }
        return map;
    }

    private FsApplyResult applySection(FsConfigSection section) {
        FsResourceType type;
        Map<String, String> payload = new HashMap<>();
        switch (section) {
            case SOFIA_INTERNAL:
                type = FsResourceType.SOFIA_PROFILE;
                payload.put("profile", "internal");
                break;
            case SOFIA_EXTERNAL:
                type = FsResourceType.SOFIA_PROFILE;
                payload.put("profile", "external");
                break;
            case ACL:
                type = FsResourceType.ACL;
                break;
            case CDR_PG:
                type = FsResourceType.MODULE;
                payload.put("action", "reload");
                payload.put("moduleName", "mod_cdr_pg_csv");
                fsApplyService.apply(FsResourceType.RELOAD_XML);
                break;
            case VOICEMAIL:
                type = FsResourceType.VOICEMAIL;
                break;
            case CONFERENCE:
                type = FsResourceType.CONFERENCE;
                break;
            case SWITCH:
                type = FsResourceType.SYSCONF;
                break;
            default:
                type = FsResourceType.RELOAD_XML;
        }
        if (section == FsConfigSection.CDR_PG) {
            return fsApplyService.apply(type, payload);
        }
        return fsApplyService.apply(type, payload.isEmpty() ? null : payload);
    }

    private FsSectionVO toSectionVo(FsConfigSection section, boolean withValues) {
        Map<String, String> values = withValues ? resolveValues(section.getCode()) : Collections.emptyMap();
        List<FsParamItemVO> params = new ArrayList<>();
        for (FsParamDef def : FsParamCatalog.defsOf(section)) {
            FsParamItemVO item = new FsParamItemVO()
                    .setKey(def.getKey())
                    .setTitle(def.getTitle())
                    .setDescription(def.getDescription())
                    .setExample(def.getExample())
                    .setDefaultValue(def.getDefaultValue())
                    .setType(def.getType().name())
                    .setRequired(def.isRequired())
                    .setPattern(def.getPattern())
                    .setPatternMessage(def.getPatternMessage())
                    .setMin(def.getMin())
                    .setMax(def.getMax())
                    .setEnumOptions(def.getEnumOptions())
                    .setApplyHint(def.getApplyHint())
                    .setGroup(def.getGroup());
            if (withValues) {
                String v = values.get(def.getKey());
                item.setValue(v != null ? v : def.getDefaultValue());
            }
            params.add(item);
        }
        return new FsSectionVO()
                .setCode(section.getCode())
                .setName(section.name())
                .setTitle(section.getTitle())
                .setDescription(section.getDescription())
                .setApplyType(section.getApplyType())
                .setParams(params);
    }

    private String normalize(FsParamDef def, String value) {
        if (def.getType() == FsParamType.BOOLEAN) {
            if ("1".equals(value) || "yes".equalsIgnoreCase(value)) {
                return "true";
            }
            if ("0".equals(value) || "no".equalsIgnoreCase(value)) {
                return "false";
            }
            return value.toLowerCase(Locale.ROOT);
        }
        return value;
    }

    private FsValidationError err(FsParamDef def, String msg) {
        return new FsValidationError(def.getKey(), def.getTitle(), msg);
    }
}
