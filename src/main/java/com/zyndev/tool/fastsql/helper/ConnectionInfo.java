package com.zyndev.tool.fastsql.helper;

import lombok.Data;

/**
 * @author 张瑀楠 zyndev@gmail.com
 * @version 1.0
 * @since 2018/1/22 下午10:02
 */
@Data
public class ConnectionInfo {

  private String dbName;
  private String filePath;
  private String url;
  private String userName;
  private String password;
}
