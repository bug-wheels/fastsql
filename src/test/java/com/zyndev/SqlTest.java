/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 zyndev zyndev@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.zyndev;

import com.zyndev.tool.fastsql.repository.User;
import com.zyndev.tool.fastsql.util.BeanReflectionUtil;
import com.zyndev.tool.fastsql.util.StringUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 这里应该有描述
 *
 * @author 张瑀楠 wb.yunan.zhang@renren-inc.com
 * @version 1.0
 * @date 2018/1/2 14:23
 * TODO:
 */
public class SqlTest {

    @Test
    public void test() throws Exception {

        String executeSql = "insert into tb_user(id, account_name, password) values(:id, :user.accountName, :user.password )";

        Map<String, Object> namedParamMap = new HashMap<>();
        namedParamMap.put("id", new Random().nextInt());

        User user = new User();
        user.setAccountName("777");
        user.setPassword("zyndev@gmail.com");
        namedParamMap.put("user", user);
        System.out.println("origin sql:" + executeSql);

        List<String> results = StringUtil.matches(executeSql, "\\?\\d+(\\.[A-Za-z]+)?|:[A-Za-z0-9]+(\\.[A-Za-z]+)?");
        Object[] params = null;
        params = new Object[results.size()];
        for (String result : results) {
            System.out.println(result);
            for (int i = 0; i < results.size(); ++i) {
                if (results.get(i).charAt(0) == ':') {
                    executeSql = executeSql.replaceFirst(results.get(i), "?");
                    // 判断是否是 param.param 的格式
                    if (!results.get(i).contains(".")) {
                        params[i] = namedParamMap.get(results.get(i).substring(1));
                    } else {
                        String[] paramArgs = results.get(i).split("\\.");
                        Object param = namedParamMap.get(paramArgs[0].substring(1));
                        params[i] = BeanReflectionUtil.getFieldValue(param, paramArgs[1]);
                    }
                    continue;
                }
                int paramIndex = Integer.parseInt(results.get(i).substring(1));
                executeSql = executeSql.replaceFirst("\\?" + paramIndex, "?");
                params[i] = namedParamMap.get(results.get(i));
            }
        }

        System.out.println("execute sql:" + executeSql);
        System.out.println("params : ------------------------------------------");
        for (Object o : params) {
            if (null != o) {
                System.out.print(o.toString() + ",\t");
            } else {
                System.out.print("null,\t");
            }
        }
        System.out.println("\n end ------------------------------------------");
    }


}
