package com.wechat.bills;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wechat.bills.utils.HttpUtils;

/**
 *
 * @desc wechat对账单controller
 * @author elephone
 * @date MAR 9, 2019 10:10:55 AM
 *
 */

public class ChangeMain {

    public static void main(String[] args) {
        try {
            // 这2个key必须有
            String exportkeyPlace = "=AwIDOr3RyFDQGHskDmyg7S0%3D";
            String balanceuserroll_encryption ="L/qUWbOpXtbcW125jyWBK1V+/uaewC4gnSnKgJrxpWmsZdnlvsO0AuvXleWN+hIHn76xuRJsjgT2IGM3MGET3OWRP3yD/YM9CZFeCaJ/Pr66jxR47W846oQt+ei9UmI4zsq72gEorKtHAd/cb/Vr+Q==";

            // 这2个key不一定有！
            String userroll_encryption ="lGueKRYYvv5hN8G4LgfUWatMjoj7in4BqpxCteKbrjkNO3oPfZxBoxEnl8FuQVof3hqVxn5hbRdI10/kEj7MvyO+FGtJmuIow/XGcvQF78NuR+Qi/yDzLANXjcEc2PhkcYsAIJfEpSLa5AxeTUyAsA==";
            String userroll_pass_ticket ="SB7XtGPA+7Ebn5acW87eXXzkPEwN9kpD+AGC/GXc9kc34EQ3Q8lP5fSrz80PyjR2";


            String url = "https://wx.tenpay.com/mmpayweb/balanceuserrollbatch?abroad=$abroad$&OutPutType=json&limit=100&offset=0&exportkey="+exportkeyPlace+"";
            String cookie_encryption= "userroll_encryption="+userroll_encryption+"";
            String cookie_pass_ticket="userroll_pass_ticket="+userroll_pass_ticket+"; SourceType=TFS; HasReadAll=0; LastBillId=1fbe6f5c20a1070031f8876a; CKVEndFlag=2; CKVOffset=0; offset=10; db_offset=10^0";
            String cookie_balanceuserroll_encryption="balanceuserroll_encryption="+balanceuserroll_encryption+"; CFTTime=1552370248; TFSTime=1551765448";

            String response = HttpUtils.executeGet(HttpUtils.createHttpClient(), url, null, cookie_encryption+";"+cookie_pass_ticket+";"+cookie_balanceuserroll_encryption, "utf-8", true);

            // 解析
            JSONObject js = JSONObject.parseObject(response);
            JSONArray jsonArray = js.getJSONArray("record");
            System.out.println(jsonArray.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
