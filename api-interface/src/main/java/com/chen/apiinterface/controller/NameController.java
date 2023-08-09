package com.chen.apiinterface.controller;

import com.chen.apiclientsdk.model.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 接口控制器
 *
 * @author CSY
 * @data 2023/07/21
 */
@RestController
@RequestMapping("/name")
public class NameController {
    @GetMapping("/get")
    public String getNameByGet(String name) {
        System.out.println(name);
        return "GET 你的名字是：" + name;
    }

    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name) {
        System.out.println(name);
        return "POST 你的名字是：" + name;
    }

    @PostMapping("/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request) {
        System.out.println(user.getUserName());
        return "POST 用户名字是：" + user.getUserName();
    }
}
