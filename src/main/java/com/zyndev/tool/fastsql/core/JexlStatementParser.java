package com.zyndev.tool.fastsql.core;

import com.zyndev.tool.fastsql.cache.StatementCache;
import com.zyndev.tool.fastsql.util.StringUtil;
import java.util.List;
import java.util.Map;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;

/**
 * The type Jexl statement parser.
 *
 * @author yunan.zhang zyndev@gmail.com
 * @version 0.0.4
 */
class JexlStatementParser {

  private JexlStatementParser() {
  }

  /**
   * Parse jexl string.
   *
   * @param key     the key
   * @param jexlSql the jexl sql
   * @param params  the params
   * @return the string
   */
  static String parseJexl(String key, String jexlSql, Map<String, Object> params) {
    if (!StatementCache.containJexl(key)) {
      return jexlSql;
    }

    // 判断是否包含 @if
    List<String> results = StringUtil.matches(jexlSql, "@if([\\s\\S]*?)}");
    if (results.isEmpty()) {
      StatementCache.addToExcludeJexlSet(key);
      return jexlSql;
    }

    JexlEngine jexl = new JexlBuilder().create();
    JexlContext jc = new MapContext();

    for (String e : results) {
      System.out.println(e);
      List<String> sqlFragment = StringUtil.matches(e, "\\(([\\s\\S]*?)\\)|\\{([\\s\\S]*?)\\}");
      String sqlExp = sqlFragment.get(0).trim().substring(1, sqlFragment.get(0).length() - 1);
      List<String> sqlFragmentParam = StringUtil.matches(sqlExp, "\\?\\d+(\\.[A-Za-z]+)?|:[A-Za-z0-9]+(\\.[A-Za-z]+)?");
      for (String param : sqlFragmentParam) {
        String newSQLExp = "_" + param.substring(1);
        sqlExp = sqlExp.replace(param, newSQLExp);
        jc.set(newSQLExp, params.get(param.substring(1)));
      }
      JexlExpression expression = jexl.createExpression(sqlExp);
      Boolean o = (Boolean) expression.evaluate(jc);
      if (o) {
        jexlSql = jexlSql.replace(e, sqlFragment.get(1).trim().substring(1, sqlFragment.get(1).length() - 1));
      } else {
        jexlSql = jexlSql.replace(e, "");
      }
    }
    return jexlSql;
  }
}
