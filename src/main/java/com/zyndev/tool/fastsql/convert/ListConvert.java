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

package com.zyndev.tool.fastsql.convert;

import com.zyndev.tool.fastsql.util.BeanReflectionUtil;
import com.zyndev.tool.fastsql.util.StringUtil;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 这里应该有描述
 *
 * @author 张瑀楠 zyndev@gmail.com
 * @version 1.0
 * @date 2017 /12/27 18:16 TODO:
 */
public class ListConvert {

    /**
     * Convert list.
     *
     * @param <T>       the type parameter
     * @param sqlRowSet the sql row set
     * @param t         the t
     * @return the list
     * @throws Exception the exception
     */
    public static <T> List<T> convert(SqlRowSet sqlRowSet, T t) throws Exception {
        List<T> result = new ArrayList<>(20);
        String[] columnNames = sqlRowSet.getMetaData().getColumnNames();
        Class clazz = t.getClass();
        while (sqlRowSet.next()) {
            T bean = (T) BeanReflectionUtil.newInstance(clazz);
            for (String columnName : columnNames) {
                Field field = clazz.getDeclaredField(StringUtil.convertColumnNameToUpperCamelCase(columnName));
                if (null != field) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    field.set(bean, sqlRowSet.getObject(columnName));
                }
            }
            result.add(bean);
        }
        return result;
    }

}
