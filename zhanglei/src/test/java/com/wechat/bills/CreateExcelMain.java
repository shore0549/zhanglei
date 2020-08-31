package com.wechat.bills;

import com.wechat.bills.entity.User;
import com.wechat.bills.utils.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateExcelMain {


    public static void main(String[] args) {

        String excelFileName = "d://wechat" + DateUtil.getCurDateTimes() + ".xls";
        String[] titles = {"id","微信账号","导出key","加密key","ticket凭证key","余额查询key"};

        List<User> arrs = new ArrayList<>();
        User user = new User();
        user.setId(1);
        user.setWechatId("elephone");
        user.setExportKey("2352363463463");
        user.setUserrollEncryption("sdgsdhshdjdt");
        user.setUserrollPassTicket("3463sgdhvshdrh");
        user.setBalanceuserrollEncryption("3464864sgsg");
        user.setExpire(new Date());
        arrs.add(user);
//        writeEmployeeListToExcel(excelFileName,titles,arrs,"change");

    }

//    public static void writeEmployeeListToExcel(String filePath,String[] excelTitle,List<User> employeeList,String sheetName){
//        System.out.println("开始写入文件>>>>>>>>>>>>");
//        Workbook workbook = null;
//        if (filePath.toLowerCase().endsWith("xls")) {//2003
//            workbook = new XSSFWorkbook();
//        }else if(filePath.toLowerCase().endsWith("xlsx")){//2007
//            workbook = new HSSFWorkbook();
//        }else{
////			logger.debug("invalid file name,should be xls or xlsx");
//        }
//        //create sheet
//        Sheet sheet = workbook.createSheet(sheetName);
//        int rowIndex = 0;//标识位，用于标识sheet的行号
//        //遍历数据集，将其写入excel中
//        try{
//            //写表头数据
//            Row titleRow = sheet.createRow(rowIndex);
//            for (int i = 0; i < excelTitle.length; i++) {
//                //创建表头单元格,填值
//                titleRow.createCell(i).setCellValue(excelTitle[i]);
//            }
//            System.out.println("表头写入完成>>>>>>>>");
//            rowIndex++;
//            //循环写入主表数据
//            for (Iterator<User> employeeIter = employeeList.iterator(); employeeIter.hasNext();) {
//                User employee = employeeIter.next();
//                //create sheet row
//                Row row = sheet.createRow(rowIndex);
//                //create sheet coluum(单元格)
//                Cell cell0 = row.createCell(0);
//                cell0.setCellValue(employee.getId());
//
//                Cell cell1 = row.createCell(1);
//                cell1.setCellValue(employee.getWechatId());
//
//                Cell cell2 = row.createCell(2);
//                cell2.setCellValue(employee.getExportKey());
//
//                Cell cell3 = row.createCell(3);
//                cell3.setCellValue(employee.getUserrollEncryption());
//
//                Cell cell4 = row.createCell(4);
//                cell4.setCellValue(employee.getUserrollPassTicket());
//
//                Cell cell5 = row.createCell(5);
//                cell5.setCellValue(employee.getBalanceuserrollEncryption());
//                rowIndex++;
//            }
//            System.out.println("主表数据写入完成>>>>>>>>");
//            FileOutputStream fos = new FileOutputStream(filePath);
//            workbook.write(fos);
//            fos.close();
//            System.out.println(filePath + "写入文件成功>>>>>>>>>>>");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


}