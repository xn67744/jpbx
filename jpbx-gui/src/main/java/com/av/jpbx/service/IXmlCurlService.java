package com.av.jpbx.service;

import java.util.Map;

/**
 * FreeSWITCH mod_xml_curl 统一 XML 下发。
 */
public interface IXmlCurlService {

    /**
     * 根据 FS 请求参数生成 XML。
     */
    String resolve(Map<String, String> params);
}
