package com.ziroom.bsrd.basic.vo;


/**
 * 分页工具类
 *
 * @author liwei
 *         Created by homelink on 2017/11/9 0009.
 */
public class Pageable {
    /**
     * 每页条数
     */
    private int pageNumber = 1;
    /**
     * 当前页数
     */
    private int pageSize = 20;

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        if (pageNumber == 0) {
            this.pageNumber = 1;
        }
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize == 0) {
            this.pageSize = 20;
        }
        this.pageSize = pageSize;
    }
}
