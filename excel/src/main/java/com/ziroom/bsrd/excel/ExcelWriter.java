package com.ziroom.bsrd.excel;

import com.ziroom.bsrd.basic.Predef;
import com.ziroom.bsrd.datatimes.TimeToolkit;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * 导出execl表格
 */
public class ExcelWriter {

    public static final int MAX_PAGE_DATA = 150000;


    public static Workbook exportExcel2007page(Workbook workbook, String sheetName, List<CellInfoVO> cellInfoVOs, List<Map<String, Object>> data) {
        Map<String, Map<String, String>> translaters = new HashMap<>();
        int count = data.size();
        if (count > MAX_PAGE_DATA) {
            throw new RuntimeException(" MAX_PAGE_DATA is " + MAX_PAGE_DATA);
        }
        //数据分组
        SheetVO basesheetVO = new SheetVO(sheetName);
        basesheetVO.setData(data);
        basesheetVO.setCellInfoVOs(cellInfoVOs);
        return exportExcel20071(workbook, Arrays.asList(basesheetVO));
    }

    /**
     * 写入一个表格
     *
     * @param sheetVO
     * @return
     */
    public static Workbook exportExcel20071(Workbook workbook, SheetVO sheetVO, List<CellInfoVO> cellInfoVOs) {
        Map<String, Map<String, String>> translaters = new HashMap<>();
        int count = sheetVO.getData().size();
        if (count > MAX_PAGE_DATA) {
            throw new RuntimeException(" MAX_PAGE_DATA is " + MAX_PAGE_DATA);
        }
        for (int i = 0; i < cellInfoVOs.size(); i++) {
            translaters.put(cellInfoVOs.get(i).getCode(), cellInfoVOs.get(i).getTranslater());
        }
        return exportExcel20071(workbook, Arrays.asList(sheetVO));
    }

    /**
     * 写入一个表格
     *
     * @param sheetVO
     * @return
     */
    public static Workbook exportExcel20071(Workbook workbook, SheetVO sheetVO) {
        createSheet(workbook, sheetVO);
        return workbook;
    }

    /**
     * 写入一个表格
     *
     * @param sheetVOs
     * @return
     */
    public static Workbook exportExcel20071(Workbook workbook, List<SheetVO> sheetVOs) {
        for (SheetVO sheetVO : sheetVOs) {
            createSheet(workbook, sheetVO);
        }
        return workbook;
    }

    /**
     * 写入一个页签
     *
     * @param xssfWorkbook
     * @param sheetVO
     * @return
     */
    private static Sheet createSheet(Workbook xssfWorkbook, SheetVO sheetVO) {
        Map<String, Map<String, String>> translaters = new HashMap<>();
        List<CellInfoVO> cellInfoVOs = sheetVO.getCellInfoVOs();
        String[] excelTitles = new String[cellInfoVOs.size()];
        String[] excelCode = new String[cellInfoVOs.size()];
        for (int i = 0; i < cellInfoVOs.size(); i++) {
            excelTitles[i] = cellInfoVOs.get(i).getTitle();
            excelCode[i] = cellInfoVOs.get(i).getCode();
            translaters.put(cellInfoVOs.get(i).getCode(), cellInfoVOs.get(i).getTranslater());
        }
        return createSheet(xssfWorkbook, sheetVO.getSheetName(), excelTitles, excelCode, translaters, sheetVO.getData());
    }

    private static Sheet createSheet(Workbook xssfWorkbook, String sheetName, String[] excelTitles, String[] excelCode, Map<String, Map<String, String>> translaters, List<Map<String, Object>> data) {
        int defWidth = 4000;
        int[] excelCellWidths = new int[excelCode.length];
        Arrays.fill(excelCellWidths, defWidth);
        Sheet sheet = buildExcelSheet2007(xssfWorkbook, sheetName, excelTitles, excelCellWidths);
        CellStyle cellStyle = createNumberContentStyle2007(xssfWorkbook);
        int datasize = data.size();
        for (int i = 0; i < datasize; i++) {
            //准备写入的数据
            Map<String, Object> item = data.get(i);
            Set set1 = item.entrySet();
            Iterator it = set1.iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String key = (String) entry.getKey();
                Map<String, String> stringStringMap = translaters.get(key.toLowerCase());
                if (stringStringMap != null) {
                    String translatervalue = stringStringMap.get(Predef.toStr(entry.getValue()));
                    item.put(key, translatervalue);
                }
            }
            Row aRow = createExcelRow2007(sheet, i + 1);
            writeExcelCellData_new2007(cellStyle, aRow, item, excelCode);// 生成一行Excel单元格
        }
        return sheet;
    }

    /**
     * 写入一行数据
     *
     * @param cellStyle
     * @param aRow
     * @param data
     * @param ticode
     */
    private static void writeExcelCellData_new2007(CellStyle cellStyle, Row aRow, Map<String, Object> data, String[] ticode) {
        Double key_Double = null;
        String key_String = null;
        Integer key_Integer = null;
        Long key_Long = null;
        Date keyDate = null;
        for (int i = 0; i < ticode.length; i++) {
            Cell cell = createExcelCell2007(cellStyle, aRow, i);
            // 获取元素值
            Object value = data.get(ticode[i]);
            if (value instanceof Double) {
                key_Double = (Double) value;
                cell.setCellValue(key_Double);
            } else if (value instanceof String) {
                key_String = (String) value;
                cell.setCellValue(key_String);
            } else if (value instanceof Integer) {
                key_Integer = (Integer) value;
                cell.setCellValue(key_Integer);
            } else if (value instanceof Timestamp) {
                cell.setCellValue(TimeToolkit.parseDate((Timestamp) value));
            } else if (value instanceof BigDecimal) {
                BigDecimal jkdd = (BigDecimal) value;
                cell.setCellValue(jkdd.doubleValue());
            } else if (value instanceof Long) {
                key_Long = (Long) value;
                cell.setCellValue(key_Long);
            }
        }
    }

    /**
     * 创建表格样式
     *
     * @param wb
     * @return
     */
    public static CellStyle createNumberContentStyle2007(Workbook wb) {
        CellStyle numberCellStyle = wb.createCellStyle();// 创建单元格样式
        numberCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 指定单元格居中对齐
        numberCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
        numberCellStyle.setWrapText(true);// 指定当单元格内容显示不下时自动换行
        Font fontContent = wb.createFont();
        fontContent.setFontName("宋体");
        numberCellStyle.setFont(fontContent);
        numberCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        numberCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        numberCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        numberCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        return numberCellStyle;
    }


    /**
     * 创建一个页签
     *
     * @param wb
     * @param sheetName
     * @param titles
     * @param baseWidths @return
     */
    public static Sheet buildExcelSheet2007(Workbook wb, String sheetName, String[] titles, int[] baseWidths) {
        // 创建工作空间
        Sheet sheet = wb.createSheet(sheetName);
        // 创建标题
        createTitleRow2007(sheet, titles, baseWidths);
        return sheet;
    }

    /**
     * 创建一行
     *
     * @param sheet
     * @param offset
     * @return
     */
    public static Row createExcelRow2007(Sheet sheet, int offset) {
        Row row = sheet.createRow(offset);
        return row;
    }

    /**
     * 创建单元格
     *
     * @param commonCellStyle
     * @param row
     * @param ci
     * @return
     */
    public static Cell createExcelCell2007(CellStyle commonCellStyle, Row row, int ci) {
        Cell cell = row.createCell(ci);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellStyle(commonCellStyle);
        return cell;
    }

    /**
     * 创建表头
     *
     * @param sheet
     * @param titles
     * @param cellWidths
     */
    public static void createTitleRow2007(Sheet sheet, String[] titles, int[] cellWidths) {
        // 创建行
        Row row = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell_i = row.createCell(i);
            cell_i.setCellType(Cell.CELL_TYPE_STRING);
            cell_i.setCellValue(titles[i]);
            int width = cellWidths[i];
            sheet.setColumnWidth(i, width);
        }
    }
}
