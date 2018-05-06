package com.cheng.application.basic.vo;

import com.cheng.application.basic.annotation.Comment;

import java.io.Serializable;

/**
 * @author chengys4
 *         2017-11-01 17:39
 **/
public class SuperVO extends IdEntity implements Serializable {

    /**
     * 城市编码
     */
    @Comment(value = "城市编码")
    private String cityCode;
    /**
     * 删除标记
     */
    @Comment(value = "逻辑删除：0未删除，1删除")
    private int isDel = 0;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public int getIsDel() {
        return isDel;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }
}
