package com.zyndev;

import com.zyndev.tool.fastsql.util.StringUtil;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * @author 张瑀楠 zyndev@gmail.com
 * @version 1.0
 * @date 2017/12/23 上午11:55
 */
public class StrTest {


  String WHERE = "#{#where}";
  String WHERE_REG = "\\#\\{\\#where\\}";


  /**
   * 搜索出"?"后面的数字
   */
  String SEARCH_NUM = "(?<=\\?)\\d+";

  /**
   * 匹配冒号表达式
   */
  String COLON_REG = ":[A-Za-z0-9]+";

  @Test
  public void strTest() {
    String param = "UserRepository";
    param = StringUtil.firstCharToLowerCase(param);
    System.out.println(param);

    param = "register_time";
    param = StringUtil.convertColumnNameToUpperCamelCase(param);
    System.out.println(param);
  }


  @Test
  public void regexTest() {

    System.out.println("-------------测试sql  ?* -----------------");
    String sql = "select uid from tb_user where password = ?1.userName and username = ?2 and dd= ?1 and id = 3 and uid=:uid and param in (:param, :param2)";

    List<String> args = new ArrayList<>();
    args.add("arg1");
    args.add("arg2");
    args.add("arg3");
    args.add("arg4");
    args.add("arg5");
    args.add("arg6");

    System.out.println("origin sql:" + sql);
    // List<String> results = StringUtil.matches(sql, "\\?\\d+");
    // List<String> results = StringUtil.matches(sql, "\\?\\d+(\\.[[A-Za-z]+)?|:[A-Za-z0-9]+(\\.[[A-Za-z]+)?");
    List<String> results = StringUtil.matches(sql, "\\?\\d+(\\.[A-Za-z]+)?|:[A-Za-z0-9]+(\\.[A-Za-z]+)?");
    List<String> params = new ArrayList<>(results.size());
    for (String result : results) {
      System.out.println(result);
      // 判断是否是 param.param 的格式
//            if (result.charAt(0) == ':') { // 冒号表达式 找到对应的 参数
//                sql = sql.replaceFirst(result, "?");
//                params.add("asjdklfjaklj");
//                continue;
//            }
//            int paramIndex = Integer.parseInt(result.substring(1));
//            sql = sql.replaceFirst("\\?" + paramIndex, "?");
//            params.add(args.get(paramIndex - 1));
    }

    System.out.println("execute sql:" + sql);
    System.out.println("params :" + params);

//        System.out.println("-------------测试sql  :* -----------------");
//        results = StringUtil.matches(sql, ":[A-Za-z0-9]+");
//        for (String result : results) {
//            System.out.println(result);
//        }
  }
}
