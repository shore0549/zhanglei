package com.wechat.bills.service;

import com.alibaba.fastjson.JSONArray;
import com.wechat.bills.entity.BrowserInfo;
import com.wechat.bills.entity.ChangeDetail;
import com.wechat.bills.entity.User;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ChangeDetailService {


    ChangeDetail selectUserByid (int id);

    Object saveBillsToDatabaseAndExcel (HttpServletResponse response,User item) throws Exception;


    public List<ChangeDetail> saveToDataBase (User user, JSONArray jsonArray);

    void saveBrowserInfo(HttpServletResponse response, BrowserInfo item) throws Exception;
}
