package com.chen.project.service.impl.inner;

import com.chen.client.model.service.InnerUserInterfaceInfoService;
import com.chen.client.model.service.InnerUserService;
import com.chen.project.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * impl内部用户界面信息服务
 *
 * @author CSY
 * @date 2023/08/07
 */
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    /**
     * 调用接口统计
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invoke(interfaceInfoId, userId);
    }

    @Override
    public boolean verifications(Long interfaceInfoId, Long userId) {
        return userInterfaceInfoService.verifications(interfaceInfoId, userId);
    }
}
