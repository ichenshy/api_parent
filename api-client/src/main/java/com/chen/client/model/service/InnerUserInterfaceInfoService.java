package com.chen.client.model.service;

/**
 *
 */
public interface InnerUserInterfaceInfoService {

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);

    /**
     * 验证
     *
     * @param interfaceInfoId 接口信息id
     * @param userId          用户id
     * @return boolean
     */
    boolean verifications(Long interfaceInfoId, Long userId);
}
