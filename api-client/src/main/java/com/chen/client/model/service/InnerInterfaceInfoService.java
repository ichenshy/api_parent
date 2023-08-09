package com.chen.client.model.service;

import com.chen.client.model.entity.InterfaceInfo;

/**
 *
 *
 * @author CSY
 * @date 2023/08/07
 */
public interface InnerInterfaceInfoService {
    /**
     * 从数据库中查询模拟接口是否存在（请求路径、请求方法、请求参数）
     */
    InterfaceInfo getInterfaceInfo(String path, String method);
}
