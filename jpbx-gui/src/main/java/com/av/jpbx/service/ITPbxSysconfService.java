package com.av.jpbx.service;

import java.util.List;
import com.av.jpbx.domain.TPbxSysconf;

/**
 * 服务配置Service接口
 * 
 * @author long
 * @date 2024-02-21
 */
public interface ITPbxSysconfService 
{
    /**
     * 查询服务配置
     * 
     * @param id 服务配置主键
     * @return 服务配置
     */
    public TPbxSysconf selectTPbxSysconfById(String id);

    /**
     * 查询服务配置列表
     * 
     * @param tPbxSysconf 服务配置
     * @return 服务配置集合
     */
    public List<TPbxSysconf> selectTPbxSysconfList(TPbxSysconf tPbxSysconf);

    /**
     * 新增服务配置
     * 
     * @param tPbxSysconf 服务配置
     * @return 结果
     */
    public int insertTPbxSysconf(TPbxSysconf tPbxSysconf);

    /**
     * 修改服务配置
     * 
     * @param tPbxSysconf 服务配置
     * @return 结果
     */
    public int updateTPbxSysconf(TPbxSysconf tPbxSysconf);

    /**
     * 批量删除服务配置
     * 
     * @param ids 需要删除的服务配置主键集合
     * @return 结果
     */
    public int deleteTPbxSysconfByIds(String ids);

    /**
     * 删除服务配置信息
     * 
     * @param id 服务配置主键
     * @return 结果
     */
    public int deleteTPbxSysconfById(String id);

    TPbxSysconf selectTPbxSysconfInfo();

    void effect();
}
