package com.stephen.learning.blade.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: jack
 * @Description:
 * @Date: 2020/5/11 15:54
 * @Version: 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface EncodeBy {
    String value();

    String JSON = "json";
    String XML = "xml";
}

