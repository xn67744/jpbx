package com.av.jpbx.service.impl;

import java.io.StringWriter;
import java.util.List;

import cn.hutool.core.lang.Snowflake;
import com.av.common.constant.Constants;
import com.av.common.core.text.Convert;
import com.av.common.utils.StringUtils;
import com.av.jpbx.domain.TPbxRegister;
import com.av.jpbx.domain.TPbxUser;
import com.av.jpbx.mapper.TPbxUserMapper;
import com.av.jpbx.service.EslListenerServer;
import com.av.jpbx.service.ITPbxUserService;
import com.av.jpbx.util.VelocityInitializer;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.freeswitch.esl.client.transport.message.EslMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 呼叫账户管理Service业务层处理
 * 
 * @author long
 * @date 2024-02-04
 */
@Service
public class TPbxUserServiceImpl implements ITPbxUserService
{
    @Autowired
    private Snowflake snowflake;

    @Autowired
    private TPbxUserMapper tPbxUserMapper;


    /**
     * 查询呼叫账户管理
     * 
     * @param id 呼叫账户管理主键
     * @return 呼叫账户管理
     */
    @Override
    public TPbxUser selectTPbxUserById(String id)
    {
        return tPbxUserMapper.selectTPbxUserById(id);
    }

    /**
     * 查询呼叫账户管理列表
     * 
     * @param tPbxUser 呼叫账户管理
     * @return 呼叫账户管理
     */
    @Override
    public List<TPbxUser> selectTPbxUserList(TPbxUser tPbxUser)
    {
        return tPbxUserMapper.selectTPbxUserList(tPbxUser);
    }

    /**
     * 新增呼叫账户管理
     * 
     * @param tPbxUser 呼叫账户管理
     * @return 结果
     */
    @Override
    public int insertTPbxUser(TPbxUser tPbxUser)
    {
        if(StringUtils.isBlank(tPbxUser.getId())){
            tPbxUser.setId(snowflake.nextIdStr());
        }
        return tPbxUserMapper.insertTPbxUser(tPbxUser);
    }

    /**
     * 修改呼叫账户管理
     * 
     * @param tPbxUser 呼叫账户管理
     * @return 结果
     */
    @Override
    public int updateTPbxUser(TPbxUser tPbxUser)
    {
        return tPbxUserMapper.updateTPbxUser(tPbxUser);
    }

    /**
     * 批量删除呼叫账户管理
     * 
     * @param ids 需要删除的呼叫账户管理主键
     * @return 结果
     */
    @Override
    public int deleteTPbxUserByIds(String ids)
    {
        return tPbxUserMapper.deleteTPbxUserByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除呼叫账户管理信息
     * 
     * @param id 呼叫账户管理主键
     * @return 结果
     */
    @Override
    public int deleteTPbxUserById(String id)
    {
        return tPbxUserMapper.deleteTPbxUserById(id);
    }

    @Override
    public String getUserXML(TPbxUser user) {
        VelocityInitializer.initVelocity();
        VelocityContext context = new VelocityContext();
        List<TPbxUser> users = tPbxUserMapper.selectTPbxUserList(user.setType(0));
        context.put("users", users);
        // 渲染模板
        StringWriter sw = new StringWriter();
        Template tpl = Velocity.getTemplate(("vm/xml/user.xml.vm"), Constants.UTF8);
        tpl.merge(context, sw);
        return sw.toString();
    }

    @Override
    public List<TPbxRegister>  getRegisterUser() {
        EslMessage eslMessage = EslListenerServer.client.sendSyncApiCommand("show", "registrations");
        List<String> bodys = eslMessage.getBodyLines();
        List<TPbxRegister> registers = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(bodys)){
            for(int i=1; i<bodys.size(); i++) {
                String[] users = bodys.get(i).split(",");
                if (users.length > 8) {
                    registers.add(new TPbxRegister()
                            .setUserAccount(users[0])
                            .setDomain(users[1])
                            .setToken(users[2])
                            .setContact(users[3])
                            .setExpireTime(Float.valueOf(users[4])/1000)
                            .setIp(users[5])
                            .setPort(users[6])
                            .setProtocol(users[7])
                            .setHostName(users[8]));
                }
            }
        }
        return registers;
    }
}
