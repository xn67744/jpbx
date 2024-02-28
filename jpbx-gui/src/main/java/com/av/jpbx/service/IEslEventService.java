package com.av.jpbx.service;

import org.freeswitch.esl.client.transport.event.EslEvent;

import java.util.List;
import java.util.Map;

public interface IEslEventService {

    void parseEventContent(EslEvent eslEvent);

    String getFreeswitchVar(String name);
}
