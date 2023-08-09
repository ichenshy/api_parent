package com.chen.project.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class UserInterfaceInfoServiceTest {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Test
    void invoke() {
        boolean invoke = userInterfaceInfoService.invoke(1, 1);
        Assert.assertTrue(invoke);
    }

}