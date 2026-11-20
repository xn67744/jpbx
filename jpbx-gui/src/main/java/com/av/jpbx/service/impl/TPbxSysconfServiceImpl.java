package com.av.jpbx.service.impl;

import java.util.List;

import cn.hutool.core.lang.Snowflake;
import com.av.common.core.text.Convert;
import com.av.common.utils.StringUtils;
import com.av.jpbx.domain.TPbxSysconf;
import com.av.jpbx.enums.FsResourceType;
import com.av.jpbx.fsconf.SysconfDefaults;
import com.av.jpbx.mapper.TPbxSysconfMapper;
import com.av.jpbx.service.FsApplyService;
import com.av.jpbx.service.FsEslClient;
import com.av.jpbx.service.ITPbxSysconfService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TPbxSysconfServiceImpl implements ITPbxSysconfService {

    @Autowired
    private Snowflake snowflake;
    @Autowired
    private TPbxSysconfMapper tPbxSysconfMapper;
    @Autowired
    private FsApplyService fsApplyService;
    @Autowired
    private FsEslClient fsEslClient;

    @Override
    public TPbxSysconf selectTPbxSysconfById(String id) {
        return tPbxSysconfMapper.selectTPbxSysconfById(id);
    }

    @Override
    public List<TPbxSysconf> selectTPbxSysconfList(TPbxSysconf tPbxSysconf) {
        return tPbxSysconfMapper.selectTPbxSysconfList(tPbxSysconf);
    }

    @Override
    public int insertTPbxSysconf(TPbxSysconf tPbxSysconf) {
        return tPbxSysconfMapper.insertTPbxSysconf(tPbxSysconf);
    }

    @Override
    public int updateTPbxSysconf(TPbxSysconf tPbxSysconf) {
        int result;
        if (StringUtils.isNotBlank(tPbxSysconf.getId())) {
            result = tPbxSysconfMapper.updateTPbxSysconf(tPbxSysconf);
        } else {
            result = tPbxSysconfMapper.insertTPbxSysconf(tPbxSysconf.setId(snowflake.nextIdStr()));
        }
        fsEslClient.refreshSysconf(tPbxSysconf);
        return result;
    }

    @Override
    public int deleteTPbxSysconfByIds(String ids) {
        return tPbxSysconfMapper.deleteTPbxSysconfByIds(Convert.toStrArray(ids));
    }

    @Override
    public int deleteTPbxSysconfById(String id) {
        return tPbxSysconfMapper.deleteTPbxSysconfById(id);
    }

    @Override
    public TPbxSysconf selectTPbxSysconfInfo() {
        List<TPbxSysconf> confs = tPbxSysconfMapper.selectTPbxSysconfList(null);
        if (CollectionUtils.isNotEmpty(confs)) {
            // 库里可能只有用户改过的几列，读取时补齐空列，模板不再判断 NULL
            TPbxSysconf conf = confs.get(0);
            SysconfDefaults.apply(conf);
            return conf;
        }
        // 默认值只存在于代码里：页面可直接使用，用户点保存才落库（与 t_pbx_fs_param 一致）
        return SysconfDefaults.newInstance();
    }

    @Override
    public void effect() {
        TPbxSysconf conf = selectTPbxSysconfInfo();
        fsEslClient.refreshSysconf(conf);
        fsApplyService.apply(FsResourceType.SYSCONF);
    }
}
