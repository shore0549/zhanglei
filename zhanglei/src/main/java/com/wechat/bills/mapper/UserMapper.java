package com.wechat.bills.mapper;

import com.wechat.bills.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByWechat(User u);
    int insertOrUpdateOne(User u);
    int unBelieveable(User u);
}