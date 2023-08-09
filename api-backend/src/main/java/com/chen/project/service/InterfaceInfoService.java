package com.chen.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.client.model.entity.InterfaceInfo;

/**
 * @author CSY
 * @description 针对表【interface_info(接口信息)】的数据库操作Service
 * @createDate 2023-07-21 09:19:10
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

}
