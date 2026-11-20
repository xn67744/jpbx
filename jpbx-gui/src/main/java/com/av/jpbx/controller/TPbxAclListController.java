package com.av.jpbx.controller;

import java.util.ArrayList;
import java.util.List;
import com.av.common.annotation.Log;
import com.av.common.core.controller.BaseController;
import com.av.common.core.domain.AjaxResult;
import com.av.common.core.page.TableDataInfo;
import com.av.common.enums.BusinessType;
import com.av.common.utils.StringUtils;
import com.av.common.utils.poi.ExcelUtil;
import com.av.jpbx.domain.TPbxAclList;
import com.av.jpbx.domain.TPbxAclNode;
import com.av.jpbx.service.ITPbxAclListService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pbx/acl")
public class TPbxAclListController extends BaseController {
    private String prefix = "pbx/acl";
    @Autowired private ITPbxAclListService service;

    @RequiresPermissions("pbx:acl:view")
    @GetMapping()
    public String page() { return prefix + "/acl"; }

    @RequiresPermissions("pbx:acl:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TPbxAclList query) {
        startPage();
        return getDataTable(service.selectTPbxAclListList(query));
    }

    @RequiresPermissions("pbx:acl:export")
    @Log(title = "ACL", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TPbxAclList query) {
        List<TPbxAclList> list = service.selectTPbxAclListList(query);
        return new ExcelUtil<TPbxAclList>(TPbxAclList.class).exportExcel(list, "ACL");
    }

    @GetMapping("/add")
    public String add() { return prefix + "/add"; }

    @RequiresPermissions("pbx:acl:add")
    @Log(title = "ACL", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TPbxAclList row, String cidrs) {
        row.setNodes(parseNodes(cidrs));
        return toAjax(service.insertTPbxAclList(row));
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        TPbxAclList row = service.selectTPbxAclListById(id);
        mmap.put("row", row);
        mmap.put("cidrsText", joinNodes(row));
        return prefix + "/edit";
    }

    @RequiresPermissions("pbx:acl:edit")
    @Log(title = "ACL", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TPbxAclList row, String cidrs) {
        row.setNodes(parseNodes(cidrs));
        return toAjax(service.updateTPbxAclList(row));
    }

    /**
     * 把「一行一个网段」的文本解析成 ACL 节点，支持 "allow 1.2.3.0/24" / "deny 1.2.3.4/32" 前缀，
     * 省略前缀时默认 allow。
     */
    private List<TPbxAclNode> parseNodes(String cidrs) {
        List<TPbxAclNode> nodes = new ArrayList<>();
        if (StringUtils.isEmpty(cidrs)) {
            return nodes;
        }
        for (String line : cidrs.split("\\r?\\n")) {
            String text = line.trim();
            if (StringUtils.isEmpty(text)) {
                continue;
            }
            String type = "allow";
            String cidr = text;
            int space = text.indexOf(' ');
            if (space > 0) {
                String head = text.substring(0, space).toLowerCase();
                if ("allow".equals(head) || "deny".equals(head)) {
                    type = head;
                    cidr = text.substring(space + 1).trim();
                }
            }
            if (StringUtils.isNotEmpty(cidr)) {
                nodes.add(new TPbxAclNode().setCidr(cidr).setNodeType(type));
            }
        }
        return nodes;
    }

    private String joinNodes(TPbxAclList row) {
        StringBuilder sb = new StringBuilder();
        if (row != null && row.getNodes() != null) {
            for (TPbxAclNode node : row.getNodes()) {
                if (StringUtils.isEmpty(node.getCidr())) {
                    continue;
                }
                if (sb.length() > 0) {
                    sb.append('\n');
                }
                sb.append(StringUtils.isEmpty(node.getNodeType()) ? "allow" : node.getNodeType())
                        .append(' ').append(node.getCidr());
            }
        }
        return sb.toString();
    }

    @RequiresPermissions("pbx:acl:remove")
    @Log(title = "ACL", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) { return toAjax(service.deleteTPbxAclListByIds(ids)); }
}
