package com.ziroom.bsrd.excel;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 页签数据
 */
public class SheetVO {
    private String sheetName = "sheet01";
    private List<CellInfoVO> cellInfoVOs = new ArrayList<>();
    private List<Map<String, Object>> data = new ArrayList<>();

    public SheetVO() {

    }

    public SheetVO(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public List<CellInfoVO> getCellInfoVOs() {
        return cellInfoVOs;
    }

    public void setCellInfoVOs(List<CellInfoVO> cellInfoVOs) {
        this.cellInfoVOs = cellInfoVOs;
    }

    public void addCellInfoVOs(CellInfoVO cellInfoVO) {
        this.cellInfoVOs.add(cellInfoVO);
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public void addData(Map<String, Object> item) {
        this.data.add(item);
    }
}
