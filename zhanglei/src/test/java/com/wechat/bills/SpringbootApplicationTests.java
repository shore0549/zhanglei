package com.wechat.bills;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wechat.bills.config.SystemConfig;
import com.wechat.bills.entity.ChangeDetail;
import com.wechat.bills.entity.User;
import com.wechat.bills.service.ChangeDetailService;
import com.wechat.bills.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import java.io.File;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BillsApplication.class)
public class SpringbootApplicationTests {

    @Autowired
    protected UserService userService;


    @Autowired
    protected ChangeDetailService changeDetailService;

    @Autowired
    private SystemConfig systemConfig;

    @Autowired
    private ServletContext servletContext;

    @Test
    public void contextLoads() {

        String downloadPath = systemConfig.downloadPath;
        System.out.println(downloadPath);


        String contextPath = servletContext.getContextPath();



        User user = userService.selectUserByid(1);
        System.out.println(user);
        try {
            File file = new File(ResourceUtils.getURL("classpath:").getPath());
            System.out.println(file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void detailService() {
        User u = new User();
        u.setWechatId("springboot测试账号");
        JSONArray js = new JSONArray();

        JSONObject obj = new JSONObject();
        obj.put("transid", "1000049501190308023207540005700000157888");
        obj.put("paynum", "180");
        obj.put("balance_source", "收入测试1");
        obj.put("balance", "888");
        obj.put("type", "8");
        obj.put("createtime", "2019-03-18 12:12:12");
        obj.put("trans_state_name", "测试数据");
        js.set(0,obj);

        List<ChangeDetail> changeDetails = changeDetailService.saveToDataBase(u, js);

        System.out.println(changeDetails.toString());
    }


}
