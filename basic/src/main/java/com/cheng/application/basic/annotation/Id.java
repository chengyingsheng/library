package com.cheng.application.basic.annotation;

import java.lang.annotation.*;

/**
 * Created by homelink on 2017/9/27.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Id {
    String value() default "auto";
}
