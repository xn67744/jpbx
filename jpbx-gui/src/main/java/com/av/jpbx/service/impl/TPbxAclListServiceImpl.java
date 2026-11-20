package com.av.jpbx.service.impl;

import java.util.List;

import cn.hutool.core.lang.Snowflake;
import com.av.common.core.text.Convert;
import com.av.common.utils.StringUtils;
import com.av.jpbx.domain.TPbxAclList;
import com.av.jpbx.domain.TPbxAclNode;
import com.av.jpbx.enums.FsResourceType;
import com.av.jpbx.mapper.TPbxAclListMapper;
import com.av.jpbx.mapper.TPbxAclNodeMapper;
import com.av.jpbx.service.FsApplyService;
import com.av.jpbx.service.ITPbxAclListService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TPbxAclListServiceImpl implements ITPbxAclListService {
    @Autowired private TPbxAclListMapper mapper;
    @Autowired private TPbxAclNodeMapper nodeMapper;
    @Autowired private Snowflake snowflake;
    @Autowired private FsApplyService fsApplyService;

    @Override
    public TPbxAclList selectTPbxAclListById(String id) {
        TPbxAclList list = mapper.selectTPbxAclListById(id);
        if (list != null) {
            list.setNodes(nodeMapper.selectTPbxAclNodeList(new TPbxAclNode().setListId(id)));
        }
        return list;
    }

    @Override
    public List<TPbxAclList> selectTPbxAclListList(TPbxAclList query) {
        return mapper.selectTPbxAclListList(query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertTPbxAclList(TPbxAclList row) {
        if (StringUtils.isBlank(row.getId())) {
            row.setId(snowflake.nextIdStr());
        }
        if (StringUtils.isEmpty(row.getDefaultType())) {
            row.setDefaultType("deny");
        }
        int r = mapper.insertTPbxAclList(row);
        saveNodes(row);
        if (r > 0) {
            fsApplyService.apply(FsResourceType.ACL);
        }
        return r;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateTPbxAclList(TPbxAclList row) {
        int r = mapper.updateTPbxAclList(row);
        if (row.getNodes() != null) {
            nodeMapper.deleteByListId(row.getId());
            saveNodes(row);
        }
        if (r > 0) {
            fsApplyService.apply(FsResourceType.ACL);
        }
        return r;
    }

    private void saveNodes(TPbxAclList row) {
        if (CollectionUtils.isNotEmpty(row.getNodes())) {
            int i = 10;
            for (TPbxAclNode n : row.getNodes()) {
                if (StringUtils.isBlank(n.getId())) {
                    n.setId(snowflake.nextIdStr());
                }
                n.setListId(row.getId());
                if (n.getSortOrder() == null) {
                    n.setSortOrder(i);
                }
                i += 10;
                nodeMapper.insertTPbxAclNode(n);
            }
        }
    }

    @Override
    public int deleteTPbxAclListByIds(String ids) {
        String[] arr = Convert.toStrArray(ids);
        for (String id : arr) {
            nodeMapper.deleteByListId(id);
        }
        int r = mapper.deleteTPbxAclListByIds(arr);
        if (r > 0) {
            fsApplyService.apply(FsResourceType.ACL);
        }
        return r;
    }
}
