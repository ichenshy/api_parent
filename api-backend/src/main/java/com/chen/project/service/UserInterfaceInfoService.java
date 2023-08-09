package com.chen.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.client.model.entity.UserInterfaceInfo;

/**
 * @author CSY
 * @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
 * @createDate 2023-07-21 09:19:10
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
    boolean invoke(long interfaceInfoId,long userId);

    boolean verifications(long interfaceInfoId, long userId);
}
