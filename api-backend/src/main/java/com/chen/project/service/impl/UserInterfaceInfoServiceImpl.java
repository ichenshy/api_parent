package com.chen.project.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.client.model.entity.UserInterfaceInfo;
import com.chen.project.service.UserInterfaceInfoService;
import com.chen.project.common.ErrorCode;
import com.chen.project.exception.BusinessException;
import com.chen.project.mapper.UserInterfaceInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author CSY
 * @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
 * @createDate 2023-07-21 09:19:10
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
        implements UserInterfaceInfoService {

    /**
     * 调用
     *
     * @param interfaceInfoId 接口信息id
     * @param userId          用户id
     * @return boolean
     */
    @Override
    @Transactional
    public boolean invoke(long interfaceInfoId, long userId) {
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 事务 高并发  不使用乐观锁的情况下
        UserInterfaceInfo userInterfaceInfo = this.getOne(new QueryWrapper<UserInterfaceInfo>().eq("userId", userId).eq("interfaceInfoId", interfaceInfoId)
                .gt("leftNum", 0));
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        userInterfaceInfo.setLeftNum(userInterfaceInfo.getLeftNum() - 1);
        userInterfaceInfo.setTotalNum(userInterfaceInfo.getTotalNum() + 1);
//        UpdateWrapper<UserInterfaceInfo> wrapper = new UpdateWrapper<>();
//        wrapper.eq("userId", userId).eq("interfaceInfoId", interfaceInfoId)
//                .gt("leftNum", 0);
//        wrapper.setSql("totalNum = totalNum + 1 , leftNum = leftNum - 1");
//        this.update(wrapper);
        return this.updateById(userInterfaceInfo);
    }

    @Override
    public boolean verifications(long interfaceInfoId, long userId) {
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("interfaceInfoId", interfaceInfoId).eq("userId", userId);
        UserInterfaceInfo info = this.getOne(wrapper);
        if (info ==null) {
            throw new BusinessException(20001, "您暂时不能请求该接口");
        }
        Integer leftNum = info.getLeftNum();
        if (leftNum <= 0) {
            throw new BusinessException(20001, "请求次数不足");
        }
        return true;
    }
}




