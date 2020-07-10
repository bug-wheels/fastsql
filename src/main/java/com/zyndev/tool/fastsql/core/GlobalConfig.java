package com.zyndev.tool.fastsql.core;

/**
 * @author 张瑀楠 zyndev@gmail.com
 * @version 0.0.1
 */
public class GlobalConfig {

  private static boolean showSql;

  static void setShowSql(boolean showSql) {
    GlobalConfig.showSql = showSql;
  }

  static boolean getShowSql() {
    return GlobalConfig.showSql;
  }

}
