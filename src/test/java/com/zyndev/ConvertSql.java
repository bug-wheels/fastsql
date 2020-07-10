package com.zyndev;

import com.zyndev.tool.fastsql.helper.ConvertBeanToSql;
import java.io.IOException;
import org.junit.Test;

/**
 * @author 张瑀楠 zyndev@gmail.com
 * @version 1.0 2018/2/3 下午5:39
 */
public class ConvertSql {

  @Test
  public void sql() throws IOException, ClassNotFoundException {
    ConvertBeanToSql convertBeanToSql = new ConvertBeanToSql();
    convertBeanToSql.convert();
  }

}
