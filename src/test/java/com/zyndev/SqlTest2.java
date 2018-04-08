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

import com.zyndev.tool.fastsql.util.StringUtil;
import org.apache.commons.jexl3.*;
import org.junit.Test;

import java.util.List;

/**
 * @author 张瑀楠 zyndev@gmail.com
 * @version 1.0
 * @date 2018/1/2 14:23
 */
public class SqlTest2 {

    @Test
    public void test() throws Exception {

        String executeSql = "select count(*) from TABLE " +
                " where status=0 and shop_id=:storeId " +
                " @if(:carBrand != null){ and car_brand LIKE concat('%', :carBrand ,'%') }" +
                " @if(:vin != null){ and vin LIKE concat('%', :vin ,'%') } " +
                " @if(:seriesCode != null && :modelCode != null){ and series_code = :seriesCode } " +
                " @if(:customName != null){ and custom_name LIKE concat('%', :customName ,'%') } " +
                " @if(:customId != null){ and custom_id = :customId  } ";


        List<String> results = StringUtil.matches(executeSql, "@if([\\s\\S]*?)}");
        JexlEngine jexl = new JexlBuilder().create();
        JexlContext jc = new MapContext();
        jc.set("_carBrand", "a");
        jc.set("_vin", "a");
        jc.set("_seriesCode", "a");
        jc.set("_modelCode", "a");
        jc.set("_customName", "a");
        jc.set("_customId", null);
        for (String e : results) {
            System.out.println(e);
            List<String> abc = StringUtil.matches(e, "\\(([\\s\\S]*?)\\)|\\{([\\s\\S]*?)\\}");
            List<String> abdc = StringUtil.matches(abc.get(0), "\\?\\d+(\\.[A-Za-z]+)?|:[A-Za-z0-9]+(\\.[A-Za-z]+)?");
            String sqlExp = abc.get(0).trim().substring(1, abc.get(0).length() - 1);
            for (String a : abdc) {
                String newSQLExp = "_" + a.substring(1);
                sqlExp = sqlExp.replace(a, newSQLExp);
            }
            JexlExpression expression = jexl.createExpression(sqlExp);
            Boolean o = (Boolean) expression.evaluate(jc);
            if (o) {
                executeSql = executeSql.replace(e, abc.get(1).trim().substring(1, abc.get(1).length() - 1));
            } else {
                executeSql = executeSql.replace(e, "");
            }
        }

        System.out.println(executeSql);
    }


}
