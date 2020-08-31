package com.wechat.bills.springtask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BillTimer2 {
    private Logger logger = LoggerFactory.getLogger(BillTimer2.class);


    //每2秒执行
    @Scheduled(cron="0/2 * * * * ?")
    public void test(){
        logger.info(String.valueOf("定时任务2"+"-------"+System.currentTimeMillis()));
    }
}
