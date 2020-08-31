package com.wechat.bills.utils;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b>Title：</b>ExcelUtil.java<br/>
 * <b>Description：</b> excel导入导出工具类<br/>
 * <b>@author： </b>zhuangruhai<br/>
 * <b>@date：</b>2014年3月12日 下午8:23:45<br/>
 * <b>Copyright (c) 2014 ASPire Tech.</b>
 */
public class ExcelUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * 根据模板生成Excel文件.
     * 
     * @param templateFileName
     *            模板文件.
     * @param list
     *            模板中存放的数据.
     * @param resultFileName
     *            生成的文件.
     */
    public static void createExcel(String templateSrcFilePath,
            Map<String, Object> beanParams, String destFilePath) {
        // 创建XLSTransformer对象
        XLSTransformer transformer = new XLSTransformer();
        try {
            // 生成Excel文件
            transformer.transformXLS(templateSrcFilePath, beanParams,
                    destFilePath);
        } catch (ParsePropertyException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    /**
     * 导出生成多个sheet的excel
     * 
     * @param templateSrcFilePath
     * @param list
     * @param destFilePath
     * @param startSheetNum
     * @throws IOException
     */
    public static void createExcelByTemple(String templateSrcFilePath,
            List list, String destFilePath, String perSheetNum)
            throws IOException {
        InputStream templateInputStream = new BufferedInputStream(
                new FileInputStream(templateSrcFilePath));
        List<String> sheetNames = new ArrayList<String>();
        List<List> splitData = null;
        int perSheetNums = Integer.valueOf(perSheetNum).intValue();
        String DEFAULT_SHEET_NAME = "sheet";
        if (list.size() > perSheetNums) {
            splitData = splitList(list, perSheetNums);
            sheetNames = new ArrayList<String>(splitData.size());
            for (int i = 0; i < splitData.size(); ++i) {
                sheetNames.add(DEFAULT_SHEET_NAME + i);
            }
        } else {
            splitData = new ArrayList<List>();
            sheetNames.add(DEFAULT_SHEET_NAME + 0);
            splitData.add(list);
        }

        XLSTransformer transformer = new XLSTransformer();
        HSSFWorkbook workbook = transformer.transformMultipleSheetsList(
                templateInputStream, splitData, sheetNames, "object",
                new HashMap<Object, Object>(), 0);
        FileOutputStream destOutputStream = new FileOutputStream(destFilePath);
        workbook.write(destOutputStream);
    }
    /**
     * 按给定的行数拆分list
     * 
     * @param data
     * @param maxRowPerSheet
     * @return
     */
    private static List<List> splitList(List data, int maxRowPerSheet) {
        List<List> splitData = new ArrayList<List>();
        List sdata = null;
        for (int i = 0; i < data.size(); ++i) {
            if (0 == i % maxRowPerSheet) {
                if (null != sdata) {
                    splitData.add(sdata);
                }
                sdata = new ArrayList(maxRowPerSheet);
            }
            sdata.add(data.get(i));
        }
        if (0 != maxRowPerSheet % data.size()) {
            splitData.add(sdata);
        }
        return splitData;
    }
}
