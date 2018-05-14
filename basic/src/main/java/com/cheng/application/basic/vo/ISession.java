package com.cheng.application.basic.vo;


import java.io.Serializable;

/**
 * 会话对象，保存会话信息
 * Created by Administrator on 2017/2/20.
 */

public interface ISession extends Serializable {

    String getUserCode();

    String getUserName();

    String getCityCode();
}
