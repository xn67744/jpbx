package com.av.jpbx.enums;

public enum FSEventType {
    //跟呼叫相关的通道事件
    CHANNEL_CREATE,//通道创建事件
    CHANNEL_ANSWER,//通道应答事件
    CHANNEL_BRIDGE,//通道桥接事件
    CHANNEL_HANGUP,//通道挂断事件
    CHANNEL_HANGUP_COMPLETE,
    CUSTOM,
    HEARTBEAT,//心跳包
    CHANNEL_UNBRIDGE,//取消桥接事件
    SESSION_HEARTBEAT;//计费心跳包心跳包

    public static FSEventType getFSEventTypeByName(String name){
        for(FSEventType type : values()){
            if(type.name().equals(name)){
                return type;
            }
        }
        return null;
    }

}