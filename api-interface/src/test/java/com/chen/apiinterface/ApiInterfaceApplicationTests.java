package com.chen.apiinterface;

import com.chen.apiclientsdk.client.ApiClient;
import com.chen.apiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ApiInterfaceApplicationTests {
    @Resource
    private ApiClient apiClient;

    @Test
    void contextLoads() {
        apiClient.getNameByGet("陈胜源");
        User user = new User();
        user.setUserName("csy");
        apiClient.getUserNameByPost(user);

    }

}
