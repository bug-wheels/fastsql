package com.zyndev.tool.fastsql.exception;

/**
 * 这里应该有描述
 *
 * @author 张瑀楠 zyndev@gmail.com
 * @version 1.0
 * @date 2018/2/22 17:06
 */
public class SqlException extends RuntimeException {

  public SqlException(String message, Throwable cause) {
    super(message, cause);
  }
}
