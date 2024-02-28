package com.av.jpbx.mapper;

import java.util.List;
import com.av.jpbx.domain.TPbxUser;

/**
 * 呼叫账户管理Mapper接口
 * 
 * @author long
 * @date 2024-02-04
 */
public interface TPbxUserMapper 
{
    /**
     * 查询呼叫账户管理
     * 
     * @param id 呼叫账户管理主键
     * @return 呼叫账户管理
     */
    public TPbxUser selectTPbxUserById(String id);

    /**
     * 查询呼叫账户管理列表
     * 
     * @param tPbxUser 呼叫账户管理
     * @return 呼叫账户管理集合
     */
    public List<TPbxUser> selectTPbxUserList(TPbxUser tPbxUser);

    /**
     * 新增呼叫账户管理
     * 
     * @param tPbxUser 呼叫账户管理
     * @return 结果
     */
    public int insertTPbxUser(TPbxUser tPbxUser);

    /**
     * 修改呼叫账户管理
     * 
     * @param tPbxUser 呼叫账户管理
     * @return 结果
     */
    public int updateTPbxUser(TPbxUser tPbxUser);

    /**
     * 删除呼叫账户管理
     * 
     * @param id 呼叫账户管理主键
     * @return 结果
     */
    public int deleteTPbxUserById(String id);

    /**
     * 批量删除呼叫账户管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTPbxUserByIds(String[] ids);
}
