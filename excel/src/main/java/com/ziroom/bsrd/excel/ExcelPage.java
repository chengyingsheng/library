package com.ziroom.bsrd.excel;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class ExcelPage {

    private String sheetName;

    private int pageNum = 1;
    private int pageSize = 80000;
    private int totalCount = 0;
    private String condition;
    private Object[] paramsvalue;
    private String orderField;
    private String[] selectedFields;
    private List<Map<String, Object>> data = null;
    private Map<String, Object> total = null;

    public ExcelPage() {
    }

    public ExcelPage(int pageNum) {
        this.pageNum = pageNum;
    }

    public ExcelPage(int pageNum, int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return this.pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getCondition() {
        return this.condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Object[] getParamsvalue() {
        return this.paramsvalue;
    }

    public void setParamsvalue(Object[] paramsvalue) {
        this.paramsvalue = paramsvalue;
    }

    public String getOrderField() {
        return this.orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public String[] getSelectedFields() {
        return this.selectedFields;
    }

    public void setSelectedFields(String[] selectedFields) {
        this.selectedFields = selectedFields;
    }

    public List<Map<String, Object>> getData() {
        return this.data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public Map<String, Object> getTotal() {
        return this.total;
    }

    public void setTotal(Map<String, Object> total) {
        this.total = total;
    }

    public int getStartIndex() {
        int pageNum = this.getPageNum() > 0 ? this.getPageNum() - 1 : 0;
        return pageNum * this.getPageSize();
    }
}
