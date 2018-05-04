package com.ziroom.bsrd.basic.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 会话对象，保存会话信息
 * Created by Administrator on 2017/2/20.
 */
@Getter
@Setter
public class SessionVo implements ISession {

    /**
     * 邮箱前缀
     */
    private String userEmail;
    /**
     * 系统号
     */
    private String empCode;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 城市编号
     */
    private String cityCode;
    /**
     * 角色类型编号
     */
    private List<String> roleType;

    @Override
    public String getUserCode() {
        return empCode;
    }
}
