package com.zyndev.tool.fastsql.exception;

/**
 * 反射异常
 *
 * @author 张瑀楠 zyndev@gmail.com
 * @version 1.0
 * @date 2018/2/22 17:03
 */
public class ReflectException extends RuntimeException {
    public ReflectException(String message, Throwable cause) {
        super(message, cause);
    }
}
