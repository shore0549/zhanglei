package com.wechat.bills.springtask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BillTimer {
    private Logger logger = LoggerFactory.getLogger(BillTimer.class);

    /**
     * 每天凌晨一点半统计前一天的数据
     */
    @Scheduled(cron="0/2 * * * * ?")
    public void downLoadWechatBill(){
        logger.info("定时任务1"+"-------"+System.currentTimeMillis());
    }

}
