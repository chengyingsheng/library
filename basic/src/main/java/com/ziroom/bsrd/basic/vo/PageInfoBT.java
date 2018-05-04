package com.ziroom.bsrd.basic.vo;


import java.util.List;

/**
 * 分页结果的封装(for Bootstrap Table)
 *
 * @author fengshuonan
 * @Date 2017年1月22日 下午11:06:41
 */
public class PageInfoBT<T> {

    // 结果集
    private List<T> rows;

    // 总数
    private long total;

    public PageInfoBT(List<T> rows, long total) {
        this.rows = rows;
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
