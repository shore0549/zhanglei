package com.wechat.bills.mapper;

import com.wechat.bills.entity.ChangeDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChangeDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ChangeDetail record);

    int insertSelective(ChangeDetail record);

    ChangeDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ChangeDetail record);

    int updateByPrimaryKey(ChangeDetail record);

    //批量插入
    int insertList(List<ChangeDetail> list);
}