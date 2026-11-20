package com.av.jpbx.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.lang.Snowflake;
import com.av.common.core.text.Convert;
import com.av.common.utils.StringUtils;
import com.av.jpbx.domain.TPbxRegister;
import com.av.jpbx.domain.TPbxUser;
import com.av.jpbx.enums.FsResourceType;
import com.av.jpbx.mapper.TPbxUserMapper;
import com.av.jpbx.service.FsApplyService;
import com.av.jpbx.service.FsEslClient;
import com.av.jpbx.service.ITPbxUserService;
import com.av.jpbx.service.IXmlCurlService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TPbxUserServiceImpl implements ITPbxUserService {
    @Autowired
    private Snowflake snowflake;
    @Autowired
    private TPbxUserMapper tPbxUserMapper;
    @Autowired
    private FsApplyService fsApplyService;
    @Autowired
    private FsEslClient fsEslClient;
    @Autowired
    private IXmlCurlService xmlCurlService;

    @Override
    public TPbxUser selectTPbxUserById(String id) {
        return tPbxUserMapper.selectTPbxUserById(id);
    }

    @Override
    public List<TPbxUser> selectTPbxUserList(TPbxUser tPbxUser) {
        return tPbxUserMapper.selectTPbxUserList(tPbxUser);
    }

    @Override
    public int insertTPbxUser(TPbxUser tPbxUser) {
        if (StringUtils.isBlank(tPbxUser.getId())) {
            tPbxUser.setId(snowflake.nextIdStr());
        }
        if (tPbxUser.getEnabled() == null) {
            tPbxUser.setEnabled(1);
        }
        int r = tPbxUserMapper.insertTPbxUser(tPbxUser);
        if (r > 0) {
            fsApplyService.apply(FsResourceType.DIRECTORY);
        }
        return r;
    }

    @Override
    public int updateTPbxUser(TPbxUser tPbxUser) {
        int r = tPbxUserMapper.updateTPbxUser(tPbxUser);
        if (r > 0) {
            fsApplyService.apply(FsResourceType.DIRECTORY);
        }
        return r;
    }

    @Override
    public int deleteTPbxUserByIds(String ids) {
        int r = tPbxUserMapper.deleteTPbxUserByIds(Convert.toStrArray(ids));
        if (r > 0) {
            fsApplyService.apply(FsResourceType.DIRECTORY);
        }
        return r;
    }

    @Override
    public int deleteTPbxUserById(String id) {
        int r = tPbxUserMapper.deleteTPbxUserById(id);
        if (r > 0) {
            fsApplyService.apply(FsResourceType.DIRECTORY);
        }
        return r;
    }

    @Override
    public String getUserXML(TPbxUser user) {
        Map<String, String> params = new HashMap<>();
        params.put("section", "directory");
        if (user != null) {
            if (StringUtils.isNotEmpty(user.getNumber())) {
                params.put("user", user.getNumber());
            }
            if (StringUtils.isNotEmpty(user.getDomain())) {
                params.put("domain", user.getDomain());
            }
        }
        return xmlCurlService.resolve(params);
    }

    @Override
    public List<TPbxRegister> getRegisterUser() {
        List<String> bodys = fsEslClient.sendApiLines("show", "registrations");
        List<TPbxRegister> registers = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(bodys)) {
            for (int i = 1; i < bodys.size(); i++) {
                String[] users = bodys.get(i).split(",");
                if (users.length > 8) {
                    registers.add(new TPbxRegister()
                            .setUserAccount(users[0])
                            .setDomain(users[1])
                            .setToken(users[2])
                            .setContact(users[3])
                            .setExpireTime(Float.valueOf(users[4]) / 1000)
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
