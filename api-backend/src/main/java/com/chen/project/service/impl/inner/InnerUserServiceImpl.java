package com.chen.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.client.model.entity.User;
import com.chen.client.model.service.InnerUserService;
import com.chen.project.common.ErrorCode;
import com.chen.project.exception.BusinessException;
import com.chen.project.mapper.UserMapper;
import com.chen.project.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 内部用户服务impl
 *
 * @author CSY
 * @date 2023/08/07
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserService userService;

    /**
     * 数据库中查是否已分配给用户秘钥（accessKey）
     *
     * @param accessKey
     * @return
     */
    @Override
    public User getInvokeUser(String accessKey) {
        if (StringUtils.isEmpty(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("accessKey", accessKey);
        return userMapper.selectOne(wrapper);
    }


}
