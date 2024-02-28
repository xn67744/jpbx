package com.av.jpbx.configure;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowflakeConfig {

    @Bean
    public Snowflake snowflake() {
        // 参数1为终端ID, 参数2为数据中心ID, 均可根据实际情况分配
        return IdUtil.createSnowflake(1, 1);
    }
}