package com.wechat.bills.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wechat.bills.controller.WeChatBillsController;
import com.wechat.bills.entity.BrowserInfo;
import com.wechat.bills.entity.ChangeDetail;
import com.wechat.bills.entity.User;
import com.wechat.bills.mapper.ChangeDetailMapper;
import com.wechat.bills.service.ChangeDetailService;
import com.wechat.bills.utils.DateUtil;
import com.wechat.bills.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("all")
@Service
public class ChangeDetailServiceImpl implements ChangeDetailService {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(WeChatBillsController.class);
    @Autowired
    private ChangeDetailMapper changeDetailMapper;

    @Override
    public ChangeDetail selectUserByid(int id) {
        return changeDetailMapper.selectByPrimaryKey(id);
    }

    @Override
    public Object saveBillsToDatabaseAndExcel(HttpServletResponse response,User user) throws Exception{
            // 这2个key必须有，有效期2个小时
            String exportkeyPlace = user.getExportKey();
            String balanceuserroll_encryption = user.getBalanceuserrollEncryption();
            // 这2个key不一定有！
            String userroll_encryption =user.getUserrollEncryption();
            String userroll_pass_ticket =user.getUserrollPassTicket();
            if (StringUtils.isBlank(userroll_encryption)){
                 userroll_encryption ="lGueKRYYvv5hN8G4LgfUWatMjoj7in4BqpxCteKbrjkNO3oPfZxBoxEnl8FuQVof3hqVxn5hbRdI10/kEj7MvyO+FGtJmuIow/XGcvQF78NuR+Qi/yDzLANXjcEc2PhkcYsAIJfEpSLa5AxeTUyAsA==";
            }
            if(StringUtils.isBlank(userroll_pass_ticket)){
                 userroll_pass_ticket ="SB7XtGPA+7Ebn5acW87eXXzkPEwN9kpD+AGC/GXc9kc34EQ3Q8lP5fSrz80PyjR2";
            }
            // limit不能大于250
            String url = "https://wx.tenpay.com/mmpayweb/balanceuserrollbatch?abroad=$abroad$&OutPutType=json&limit=200&offset=0&exportkey="+exportkeyPlace+"";
            String cookie_encryption= "userroll_encryption="+userroll_encryption+"";
            String cookie_pass_ticket="userroll_pass_ticket="+userroll_pass_ticket+"; SourceType=TFS; HasReadAll=0; LastBillId=1fbe6f5c20a1070031f8876a; CKVEndFlag=2; CKVOffset=0; offset=10; db_offset=10^0";
            String cookie_balanceuserroll_encryption="balanceuserroll_encryption="+balanceuserroll_encryption+"; CFTTime=1552370248; TFSTime=1551765448";

            String res = HttpUtils.executeGet(HttpUtils.createHttpClient(), url, null, cookie_encryption+";"+cookie_pass_ticket+";"+cookie_balanceuserroll_encryption, "utf-8", true);
            logger.info(res);
            // 解析
            JSONObject js = JSONObject.parseObject(res);
            if (js != null && "0".equals(js.getString("retcode")) && js.getIntValue("TotalNum")>0){
                JSONArray jsonArray = js.getJSONArray("record");
                // 1,存数据库
                List<ChangeDetail> arr = saveToDataBase(user,jsonArray);

                String fileName = DateUtil.getCurDateTimes() + ".xls";
                // 2,导出到excel
                Workbook wb = writeListToExcel(arr,fileName,response);
                // 3,下载
                //downloadExcel(response,wb,fileName);
                return "下载成功";
            }else {
                return res;
            }

    }

    public void downloadExcel(HttpServletResponse response,Workbook wb,String fileName) throws IOException {
        // 3,下载到浏览器端
        //到这里，excel就已经生成了，然后就需要通过流来写出去
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //将excel写入流
            wb.write(byteArrayOutputStream);
            //设置文件标题
            String dateTime = DateUtil.dateToDateString(new Date(), "yyyyMMddHHmmss");
            String outFile = "wechat-"+fileName;
            //设置返回的文件类型
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            //对文件编码
            outFile = response.encodeURL(new String(outFile.getBytes("gb2312"), "iso8859-1"));
            //使用Servlet实现文件下载的时候，避免浏览器自动打开文件
            response.addHeader("Content-Disposition", "attachment;filename=" + outFile);
            response.setContentType("application/x-download");

            //设置文件大小
            response.setContentLength(byteArrayOutputStream.size());
            //创建Cookie并添加到response中
            Cookie cookie = new Cookie("fileDownload", "true");
            cookie.setPath("/");
            response.addCookie(cookie);
            //将流写进response输出流中
            ServletOutputStream outputstream = response.getOutputStream();
            byteArrayOutputStream.writeTo(outputstream);

            byteArrayOutputStream.close();
            outputstream.flush();
            logger.info("excel下载成功"+fileName);
        } catch (Exception e) {
            logger.error("下载Excel出错", e);
        }

    }

    public List<ChangeDetail> saveToDataBase (User user,JSONArray jsonArray){
        //1,存数据库
        List<ChangeDetail> arr = new ArrayList<ChangeDetail>();
        if(jsonArray.size()>0){
            for(int i=0;i<jsonArray.size();i++){
                JSONObject jo = jsonArray.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                ChangeDetail item = new ChangeDetail();
                item.setWechatId(user.getWechatId());
                item.setOrderId(jo.getString("transid"));

                BigDecimal paynum = new BigDecimal(jo.getString("paynum"));
                item.setMoney(paynum.divide(new BigDecimal(100)));
                item.setBalanceSource(jo.getString("balance_source"));

                BigDecimal balance = new BigDecimal(jo.getString("balance"));
                item.setBalance(balance.divide(new BigDecimal(100)));
                item.setTransactionType(Integer.valueOf(jo.getString("type")));

                String createtime = jo.getString("createtime");
                Date d = null;
                try {
                    d = DateUtil.getDate(createtime, DateUtil.YYYY_MM_DD_HH_MM_SS_EN);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                item.setTransactionTime(d);
                item.setRemark(jo.getString("trans_state_name"));
//                changeDetailMapper.insert(item);
                arr.add(item);
            }
        }
        changeDetailMapper.insertList(arr);
        logger.info("存入数据库ok"+JSON.toJSONString(user));
        return arr;
    }
    public Workbook writeListToExcel(List<ChangeDetail> arr,String fileName,HttpServletResponse response){

        try {
            String sheetName = "change";
            String[] excelTitle = {"id","微信账号","订单号","交易金额","余额","交易类型","交易详情分类","交易时间","备注"};

            System.out.println("开始写入文件>>>>>>>>>>>>");
            Workbook workbook = null;
            if (fileName.toLowerCase().endsWith("xls")) {//2003
                workbook = new XSSFWorkbook();
            }else if(fileName.toLowerCase().endsWith("xlsx")){//2007
                workbook = new HSSFWorkbook();
            }else{
    //			logger.debug("invalid file name,should be xls or xlsx");
            }
            //create sheet
            Sheet sheet = workbook.createSheet(sheetName);
            int rowIndex = 0;//标识位，用于标识sheet的行号
            //遍历数据集，将其写入excel中

            //写表头数据
            Row titleRow = sheet.createRow(rowIndex);
            for (int i = 0; i < excelTitle.length; i++) {
                //创建表头单元格,填值
                titleRow.createCell(i).setCellValue(excelTitle[i]);
            }
            System.out.println("表头写入完成>>>>>>>>");
            rowIndex++;
            //循环写入主表数据
            for (Iterator<ChangeDetail> employeeIter = arr.iterator(); employeeIter.hasNext();) {
                ChangeDetail employee = employeeIter.next();
                //create sheet row
                Row row = sheet.createRow(rowIndex);
                //create sheet coluum(单元格)
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(employee.getId());

                Cell cell1 = row.createCell(1);
                cell1.setCellValue(employee.getWechatId());

                Cell cell2 = row.createCell(2);
                cell2.setCellValue(employee.getOrderId());

                Cell cell3 = row.createCell(3);
                cell3.setCellValue(employee.getMoney()+"");

                Cell cell4 = row.createCell(4);
                cell4.setCellValue(employee.getBalance()+"");

                Cell cell5 = row.createCell(5);
                cell5.setCellValue(employee.getBalanceSource());

                Cell cell6= row.createCell(6);
                cell6.setCellValue(employee.getTransactionType());

                Cell cell7= row.createCell(7);
                String dateString = DateUtil.dateToDateString(employee.getTransactionTime(), DateUtil.YYYY_MM_DD_HH_MM_SS_EN);
                cell7.setCellValue(dateString);


                Cell cell8 = row.createCell(8);
                cell8.setCellValue(employee.getRemark());

                rowIndex++;
            }
            System.out.println("主表数据写入完成>>>>>>>>");
            // 写到本地
//            File file = new File(ResourceUtils.getURL("classpath:").getPath());
//            String filePath = file.getAbsolutePath() +fileName;
//            FileOutputStream fos = new FileOutputStream(filePath);
//            workbook.write(fos);
//            fos.close();


            // 浏览器下载
            OutputStream fos = response.getOutputStream();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment;filename="+fileName);//默认Excel名称
            response.flushBuffer();
            workbook.write(response.getOutputStream());

            System.out.println(fileName + "写入文件成功>>>>>>>>>>>");
            return workbook;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    @Override
    public void saveBrowserInfo(HttpServletResponse response, BrowserInfo item) throws Exception{
        // 1,存数据库
        List<BrowserInfo> arr = new ArrayList<>();
        arr.add(item);

        String fileName = "month-"+DateUtil.getMonth(new Date()) + ".xls";
        // 2,导出到excel
        // 判断文件是否存在,存在的话就追加否则新建
        String filePath = System.getProperty("user.dir") +File.separator+fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            Workbook wb = saveBrowserInfoWriteListToExcel(arr,fileName,response);
            System.out.println("新建文档");
        } else {
            appendToExcel(arr,filePath,response);
            System.out.println("追加记录");
        }

    }
    public Workbook saveBrowserInfoWriteListToExcel(List<BrowserInfo> arr,String fileName,HttpServletResponse response){

        try {
            String sheetName = "vistor_record";
            String[] excelTitle = {"请求URL路径","ip地址","登录时间","用户信息","浏览器信息","操作系统信息","备注"};

            System.out.println("开始写入文件>>>>>>>>>>>>");
            Workbook workbook = null;
            if (fileName.toLowerCase().endsWith("xls")) {//2003
                workbook = new HSSFWorkbook();
            }else if(fileName.toLowerCase().endsWith("xlsx")){//2007
                workbook = new HSSFWorkbook();
            }else{
                //			logger.debug("invalid file name,should be xls or xlsx");
            }
            //create sheet
            Sheet sheet = workbook.createSheet(sheetName);
            int rowIndex = 0;//标识位，用于标识sheet的行号
            //遍历数据集，将其写入excel中

            //写表头数据
            Row titleRow = sheet.createRow(rowIndex);
            for (int i = 0; i < excelTitle.length; i++) {
                //创建表头单元格,填值
                titleRow.createCell(i).setCellValue(excelTitle[i]);
            }
            System.out.println("表头写入完成>>>>>>>>");
            rowIndex++;
            //循环写入主表数据
            for (Iterator<BrowserInfo> employeeIter = arr.iterator(); employeeIter.hasNext();) {
                BrowserInfo employee = employeeIter.next();
                Row row = sheet.createRow(rowIndex);

                Cell cell0 = row.createCell(0);
                cell0.setCellValue(employee.getParameters());

                //create sheet coluum(单元格)
                Cell cell1 = row.createCell(1);
                cell1.setCellValue(employee.getIpAddress());

                Cell cell2 = row.createCell(2);
                String dateString = DateUtil.dateToDateString(employee.getTime(), DateUtil.YYYY_MM_DD_HH_MM_SS_EN);
                cell2.setCellValue(dateString);

                Cell cell3 = row.createCell(3);
                cell3.setCellValue(employee.getUserAgent());

                Cell cell4 = row.createCell(4);
                cell4.setCellValue(employee.getBrowser()+"");

                Cell cell5 = row.createCell(5);
                cell5.setCellValue(employee.getBrowser()+"");

                Cell cell6 = row.createCell(6);
                cell6.setCellValue("");

                rowIndex++;
            }
            System.out.println("主表数据写入完成>>>>>>>>");
            // 写到本地
            File file = new File(ResourceUtils.getURL("/").getPath());
            String filePath = System.getProperty("user.dir") +file.separator+fileName;
            FileOutputStream fos = new FileOutputStream(filePath);
            workbook.write(fos);
            fos.flush();
            fos.close();


            // 浏览器下载
//            OutputStream fos1 = response.getOutputStream();
//            response.setContentType("application/octet-stream");
//            response.setHeader("Content-disposition", "attachment;filename="+fileName);//默认Excel名称
//            response.flushBuffer();
//            workbook.write(response.getOutputStream());
//
//            System.out.println(fileName + "写入文件成功>>>>>>>>>>>");
            return workbook;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 追加
    public void appendToExcel(List<BrowserInfo> arr,String filePath,HttpServletResponse response){
        try {
            FileInputStream fs=new FileInputStream(filePath);  //获取d://test.xls
            POIFSFileSystem ps=new POIFSFileSystem(fs);  //使用POI提供的方法得到excel的信息
            HSSFWorkbook wb=new HSSFWorkbook(ps);
            HSSFSheet sheet=wb.getSheetAt(0);  //获取到工作表，因为一个excel可能有多个工作表
            HSSFRow row=sheet.getRow(0);  //获取第一行（excel中的行默认从0开始，所以这就是为什么，一个excel必须有字段列头），即，字段列头，便于赋值

            FileOutputStream out=new FileOutputStream(filePath);  //向d://test.xls中写数据
            int rowIndex =sheet.getLastRowNum()+1;
            row=sheet.createRow((short)(rowIndex)); //在现有行号后追加数据

            //循环写入主表数据
            for (Iterator<BrowserInfo> employeeIter = arr.iterator(); employeeIter.hasNext();) {
                BrowserInfo employee = employeeIter.next();
                //create sheet coluum(单元格)
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(employee.getParameters());

                //create sheet coluum(单元格)
                Cell cell1 = row.createCell(1);
                cell1.setCellValue(employee.getIpAddress());

                Cell cell2 = row.createCell(2);
                String dateString = DateUtil.dateToDateString(employee.getTime(), DateUtil.YYYY_MM_DD_HH_MM_SS_EN);
                cell2.setCellValue(dateString);

                Cell cell3 = row.createCell(3);
                cell3.setCellValue(employee.getUserAgent());

                Cell cell4 = row.createCell(4);
                cell4.setCellValue(employee.getBrowser()+"");

                Cell cell5 = row.createCell(5);
                cell5.setCellValue(employee.getBrowser()+"");

                Cell cell6 = row.createCell(6);
                cell6.setCellValue("");
                rowIndex++;
            }
            wb.write(out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
