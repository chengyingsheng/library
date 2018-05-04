package com.ziroom.bsrd.basic.annotation;

/**
 * Created by homelink on 2017/10/16.
 */

import java.lang.annotation.*;

/**
 * 标注类 方法 说明
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Comment {
    /**
     * 说明信息
     *
     * @return
     */
    String value();
}
