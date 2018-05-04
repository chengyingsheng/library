package com.ziroom.bsrd.basic.annotation;

import java.lang.annotation.*;

/**
 * 实体表映射
 * Created by cheshun on 2016/5/16.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {

    /**
     * 表名
     */
    String value() default "";

    /**
     * 说明
     */
    String comment() default "";
}
