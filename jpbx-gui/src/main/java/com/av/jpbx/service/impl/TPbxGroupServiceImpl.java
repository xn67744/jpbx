package com.av.jpbx.service.impl;

import java.util.List;

import cn.hutool.core.lang.Snowflake;
import com.av.common.core.text.Convert;
import com.av.common.utils.StringUtils;
import com.av.jpbx.domain.TPbxGroup;
import com.av.jpbx.domain.TPbxGroupUser;
import com.av.jpbx.enums.FsResourceType;
import com.av.jpbx.mapper.TPbxGroupMapper;
import com.av.jpbx.mapper.TPbxGroupUserMapper;
import com.av.jpbx.service.FsApplyService;
import com.av.jpbx.service.ITPbxGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TPbxGroupServiceImpl implements ITPbxGroupService {
    @Autowired private TPbxGroupMapper mapper;
    @Autowired private TPbxGroupUserMapper groupUserMapper;
    @Autowired private Snowflake snowflake;
    @Autowired private FsApplyService fsApplyService;

    @Override
    public TPbxGroup selectTPbxGroupById(String id) {
        TPbxGroup g = mapper.selectTPbxGroupById(id);
        if (g != null) {
            List<TPbxGroupUser> members = groupUserMapper.selectTPbxGroupUserList(new TPbxGroupUser().setGroupId(id));
            g.setMemberNumbers(members.stream().map(TPbxGroupUser::getUserNumber).collect(java.util.stream.Collectors.toList()));
        }
        return g;
    }

    @Override
    public List<TPbxGroup> selectTPbxGroupList(TPbxGroup query) {
        List<TPbxGroup> list = mapper.selectTPbxGroupList(query);
        // 列表页要显示成员数，分组数量很小，直接逐组补成员
        for (TPbxGroup g : list) {
            List<TPbxGroupUser> members = groupUserMapper.selectTPbxGroupUserList(
                    new TPbxGroupUser().setGroupId(g.getId()));
            g.setMemberNumbers(members.stream()
                    .map(TPbxGroupUser::getUserNumber)
                    .collect(java.util.stream.Collectors.toList()));
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertTPbxGroup(TPbxGroup row) {
        if (StringUtils.isBlank(row.getId())) {
            row.setId(snowflake.nextIdStr());
        }
        int r = mapper.insertTPbxGroup(row);
        saveMembers(row);
        if (r > 0) {
            fsApplyService.apply(FsResourceType.DIRECTORY);
        }
        return r;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateTPbxGroup(TPbxGroup row) {
        int r = mapper.updateTPbxGroup(row);
        groupUserMapper.deleteByGroupId(row.getId());
        saveMembers(row);
        if (r > 0) {
            fsApplyService.apply(FsResourceType.DIRECTORY);
        }
        return r;
    }

    private void saveMembers(TPbxGroup row) {
        if (row.getMemberNumbers() != null) {
            for (String num : row.getMemberNumbers()) {
                if (StringUtils.isEmpty(num)) {
                    continue;
                }
                groupUserMapper.insertTPbxGroupUser(new TPbxGroupUser()
                        .setId(snowflake.nextIdStr())
                        .setGroupId(row.getId())
                        .setUserNumber(num.trim()));
            }
        } else if (StringUtils.isNotEmpty(row.getMemberIds())) {
            for (String num : row.getMemberIds().split(",")) {
                if (StringUtils.isEmpty(num)) {
                    continue;
                }
                groupUserMapper.insertTPbxGroupUser(new TPbxGroupUser()
                        .setId(snowflake.nextIdStr())
                        .setGroupId(row.getId())
                        .setUserNumber(num.trim()));
            }
        }
    }

    @Override
    public int deleteTPbxGroupByIds(String ids) {
        String[] arr = Convert.toStrArray(ids);
        for (String id : arr) {
            groupUserMapper.deleteByGroupId(id);
        }
        int r = mapper.deleteTPbxGroupByIds(arr);
        if (r > 0) {
            fsApplyService.apply(FsResourceType.DIRECTORY);
        }
        return r;
    }
}
