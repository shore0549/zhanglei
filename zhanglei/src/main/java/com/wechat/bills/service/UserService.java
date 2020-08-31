package com.wechat.bills.service;


import com.wechat.bills.entity.User;

/**
 * @Author: wangxc
 * @GitHub: https://github.com/vector4wang
 * @CSDN: http://blog.csdn.net/qqhjqs?viewmode=contents
 * @BLOG: http://vector4wang.tk
 * @wxid: BMHJQS
 */
public interface UserService {


    User selectUserByid (int id);

    int saveUser (User user);

    User hasEffectiveKey(User user);
}
