package com.ziroom.bsrd.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * xls工具类
 *
 * @author chengys4
 */
public class ExcelReadr {


    /**
     * 读取 xls/xlsx 文件
     *
     * @param filePath xls/xlsx 路径
     * @return
     * @throws IOException
     */
    public static List<Map<String, String>> read(String filePath) throws IOException {
        InputStream stream = new FileInputStream(filePath);
        return read(stream, getFileType(filePath));
    }

    /**
     * 读取 xls/xlsx 文件
     *
     * @param inputStream xls/xlsx 文件流
     * @return
     * @throws IOException
     */
    public static List<Map<String, String>> read(InputStream inputStream, String fileType) {
        List<Map<String, String>> data = new ArrayList<>();
        Workbook wb = null;
        try {
            if (fileType.equals("xls")) {
                wb = new HSSFWorkbook(inputStream);
            } else if (fileType.equals("xlsx")) {
                wb = new XSSFWorkbook(inputStream);
            } else {
                throw new RuntimeException("您输入的excel格式不正确");
            }
            Sheet sheet1 = wb.getSheetAt(0);
            String[] strings = getColsName(sheet1);
            for (Row row : sheet1) {
                Map<String, String> item = new HashMap<>();
                for (int i = 0; i < strings.length; i++) {
                    Cell cell = row.getCell(i);
                    if (cell == null) {
                        item.put(strings[i], "");
                    } else {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        item.put(strings[i], cell.toString());
                    }
                }
                data.add(item);
            }
            data.remove(0);
            wb = null;
            return data;

        } catch (IOException e) {
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

    /**
     * 得到数据表的头
     *
     * @param sheet
     * @return
     */
    public static String[] getColsName(Sheet sheet) {
        Row firstrow = sheet.getRow(sheet.getFirstRowNum());
        String[] colname = new String[firstrow.getLastCellNum()];
        for (int i = 0; i < colname.length; i++) {
            Cell cell = firstrow.getCell(i);
            if (cell == null) {
                colname[i] = "null";
            } else {
                cell.setCellType(Cell.CELL_TYPE_STRING);
                colname[i] = cell.getStringCellValue();
            }
        }
        return colname;
    }

    /**
     * 获取文件类型
     *
     * @param filename
     * @return
     */
    public static String getFileType(String filename) {
        if (filename == null || filename.trim().length() == 0) {
            return null;
        }
        return filename.substring(filename.lastIndexOf(".") + 1, filename.length());
    }
}