package com.av.jpbx.configure;

import com.av.jpbx.util.VelocityInitializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class VelocityInitializerBean {

    @PostConstruct
    public void init() {
        VelocityInitializer.initVelocity();
    }
}
