package com.av.jpbx.service.impl;

import java.util.List;

import cn.hutool.core.lang.Snowflake;
import com.av.common.utils.StringUtils;
import com.av.jpbx.service.EslListenerServer;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.av.jpbx.mapper.TPbxSysconfMapper;
import com.av.jpbx.domain.TPbxSysconf;
import com.av.jpbx.service.ITPbxSysconfService;
import com.av.common.core.text.Convert;

/**
 * 服务配置Service业务层处理
 * 
 * @author long
 * @date 2024-02-21
 */
@Service
public class TPbxSysconfServiceImpl implements ITPbxSysconfService 
{

    @Autowired
    private Snowflake snowflake;

    @Autowired
    private TPbxSysconfMapper tPbxSysconfMapper;


    /**
     * 查询服务配置
     * 
     * @param id 服务配置主键
     * @return 服务配置
     */
    @Override
    public TPbxSysconf selectTPbxSysconfById(String id)
    {
        return tPbxSysconfMapper.selectTPbxSysconfById(id);
    }

    /**
     * 查询服务配置列表
     * 
     * @param tPbxSysconf 服务配置
     * @return 服务配置
     */
    @Override
    public List<TPbxSysconf> selectTPbxSysconfList(TPbxSysconf tPbxSysconf)
    {
        return tPbxSysconfMapper.selectTPbxSysconfList(tPbxSysconf);
    }

    /**
     * 新增服务配置
     * 
     * @param tPbxSysconf 服务配置
     * @return 结果
     */
    @Override
    public int insertTPbxSysconf(TPbxSysconf tPbxSysconf)
    {
        return tPbxSysconfMapper.insertTPbxSysconf(tPbxSysconf);
    }

    /**
     * 修改服务配置
     * 
     * @param tPbxSysconf 服务配置
     * @return 结果
     */
    @Override
    public int updateTPbxSysconf(TPbxSysconf tPbxSysconf)
    {
        if(StringUtils.isNotBlank(tPbxSysconf.getId())){
            int result = tPbxSysconfMapper.updateTPbxSysconf(tPbxSysconf);
            EslListenerServer.sysconf = tPbxSysconf;
            return result;
        }else{
            int result = tPbxSysconfMapper.insertTPbxSysconf(tPbxSysconf.setId(snowflake.nextIdStr()));
            EslListenerServer.sysconf = tPbxSysconf;
            return result;
        }
    }

    /**
     * 批量删除服务配置
     * 
     * @param ids 需要删除的服务配置主键
     * @return 结果
     */
    @Override
    public int deleteTPbxSysconfByIds(String ids)
    {
        return tPbxSysconfMapper.deleteTPbxSysconfByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除服务配置信息
     * 
     * @param id 服务配置主键
     * @return 结果
     */
    @Override
    public int deleteTPbxSysconfById(String id)
    {
        return tPbxSysconfMapper.deleteTPbxSysconfById(id);
    }


    @Override
    public TPbxSysconf selectTPbxSysconfInfo() {
        List<TPbxSysconf> confs = tPbxSysconfMapper.selectTPbxSysconfList(null);
        return CollectionUtils.isNotEmpty(confs)?confs.get(0):new TPbxSysconf();
    }

    @Override
    public void effect() {
        EslListenerServer.client.sendSyncApiCommand("reloadxml", null);
        EslListenerServer.client.sendSyncApiCommand("sofia", "profile internal restart");
        EslListenerServer.client.sendSyncApiCommand("sofia", "profile external restart");
        EslListenerServer.client.sendSyncApiCommand("sofia", "profile internal-ipv6 restart");
        EslListenerServer.client.sendSyncApiCommand("sofia", "profile external-ipv6 restart");
    }
}
