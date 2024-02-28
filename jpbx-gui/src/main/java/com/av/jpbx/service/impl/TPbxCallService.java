package com.av.jpbx.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.av.common.constant.Constants;
import com.av.common.core.text.Convert;
import com.av.common.utils.StringUtils;
import com.av.jpbx.domain.TPbxCDR;
import com.av.jpbx.domain.TPbxRegister;
import com.av.jpbx.domain.TPbxUser;
import com.av.jpbx.mapper.TPbxCDRMapper;
import com.av.jpbx.mapper.TPbxUserMapper;
import com.av.jpbx.service.EslListenerServer;
import com.av.jpbx.service.ITPbxCallService;
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

import java.io.StringWriter;
import java.util.List;

/**
 * 呼叫账户管理Service业务层处理
 * 
 * @author long
 * @date 2024-02-04
 */
@Service
public class TPbxCallService implements ITPbxCallService
{

    @Autowired
    private TPbxCDRMapper tPbxCDRMapper;

    @Override
    public List<TPbxCDR> selectTPbxCallRecordList(TPbxCDR cdr) {
        return tPbxCDRMapper.selectTPbxCDRList(cdr);
    }
}
