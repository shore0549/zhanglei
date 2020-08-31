package com.wechat.bills.service.impl;

import com.wechat.bills.entity.User;
import com.wechat.bills.mapper.UserMapper;
import com.wechat.bills.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public User selectUserByid(int id) {
        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }


    @Override
    public int saveUser(User user) {
        user.setId(0);
        int insert = userMapper.unBelieveable(user);
        // 需要转换下时间
        return insert;
    }

    @Override
    public User hasEffectiveKey(User u) {
        User user = userMapper.selectByWechat(u);
        return user;
    }
}
