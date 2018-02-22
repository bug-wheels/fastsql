package com.zyndev.tool.fastsql.annotation;


import java.lang.annotation.*;


/**
 * 返回主键
 *
 * @author 张瑀楠 zyndev@gmail.com
 * @version 0.0.3
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReturnGeneratedKey {
}
