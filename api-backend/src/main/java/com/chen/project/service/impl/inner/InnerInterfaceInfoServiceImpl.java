package com.chen.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.client.model.entity.InterfaceInfo;
import com.chen.client.model.service.InnerInterfaceInfoService;
import com.chen.project.common.ErrorCode;
import com.chen.project.exception.BusinessException;
import com.chen.project.mapper.InterfaceInfoMapper;
import com.chen.project.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author CSY
 * @date 2023/08/07
 */
@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {
    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    /**
     *
     * 从数据库中查询模拟接口是否存在（请求路径、请求方法、请求参数）
     *
     * @param path   路径
     * @param method 方法
     * @return {@code InterfaceInfo}
     */
    @Override
    public InterfaceInfo getInterfaceInfo(String path, String method) {
        if (StringUtils.isAnyEmpty(path, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("url", path).eq("method", method);
        return interfaceInfoMapper.selectList(wrapper).get(0);
    }
}
