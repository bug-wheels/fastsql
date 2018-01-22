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

package com.zyndev.tool.fastsql.core;

import com.zyndev.tool.fastsql.annotation.Param;
import com.zyndev.tool.fastsql.annotation.Query;
import com.zyndev.tool.fastsql.convert.BeanConvert;
import com.zyndev.tool.fastsql.convert.ListConvert;
import com.zyndev.tool.fastsql.convert.SetConvert;
import com.zyndev.tool.fastsql.util.BeanReflectionUtil;
import com.zyndev.tool.fastsql.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * sql 语句解析
 *
 * @author 张瑀楠 zyndev@gmail.com
 * @version 0.0.1
 * @since 2017 /12/23 下午12:11
 */
class StatementParser {

    private final static Log logger = LogFactory.getLog(StatementParser.class);

    /**
     * 此处对 Repository 中方法进行解析，解析成对应的sql 和 参数
     * <p>
     * sql 来自于 @Query 注解的 value
     * 参数 来自方法的参数
     * <p>
     * 注意根据返回值的不同封装结果集
     *
     * @param proxy  执行对象
     * @param method 执行方法
     * @param args   参数
     * @return object
     */
    static Object invoke(Object proxy, Method method, Object[] args) throws Exception {

        JdbcTemplate jdbcTemplate = DataSourceHolder.getInstance().getJdbcTemplate();

        boolean logDebug = logger.isDebugEnabled();

        String methodReturnType = method.getReturnType().getName();
        Query query = method.getAnnotation(Query.class);

        if (null == query || StringUtil.isBlank(query.value())) {
            throw new IllegalStateException(method.toGenericString() + " 无 query 注解或 SQL 为空");
        }


        String originSql = query.value();

        System.out.println("=========================");
        System.out.println("sql:" + query.value());
        Map<String, Object> namedParamMap = new HashMap<>();
        Parameter[] parameters = method.getParameters();
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; ++i) {
                Param param = parameters[i].getAnnotation(Param.class);
                if (null != param) {
                    namedParamMap.put(param.value(), args[i]);
                }
                namedParamMap.put("?" + (i + 1), args[i]);
            }
        }
        System.out.println("``````````````````````````````");

        if (logDebug) {
            logger.debug("执行 sql: " + originSql);
        }

        /**
         * 判断 sql 类型
         * TODO 此处需要修改
         */
        boolean isQuery = originSql.startsWith("select") || originSql.startsWith("SELECT");
        Object[] params = null;
        // rewrite sql
        if (null != args && args.length > 0) {
            List<String> results = StringUtil.matches(originSql, "\\?\\d+(\\.[A-Za-z]+)?|:[A-Za-z0-9]+(\\.[A-Za-z]+)?");
            if (results.isEmpty()) {
                params = args;
            } else {
                params = new Object[results.size()];
                for (int i = 0; i < results.size(); ++i) {
                    if (results.get(i).charAt(0) == ':') {
                        originSql = originSql.replaceFirst(results.get(i), "?");
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
                    originSql = originSql.replaceFirst("\\?" + paramIndex, "?");
                    params[i] = namedParamMap.get(results.get(i));
                }
            }
        }


        System.out.println("execute sql:" + originSql);
        System.out.println("params : ------------------------------------------");
        if (null != params) {
            for (Object o : params) {
                if (null != o) {
                    System.out.print(o.toString() + ",\t");
                } else {
                    System.out.print("null,\t");
                }
            }
            System.out.println("\n");
        }


        /**
         * 如果返回值是基本类型或者其包装类
         */
        System.out.println(methodReturnType);
        if (isQuery) {
            if ("java.lang.Integer".equals(methodReturnType) || "int".equals(methodReturnType)) {
                return jdbcTemplate.queryForObject(originSql, params, Integer.class);
            } else if ("java.lang.String".equals(methodReturnType)) {
                return jdbcTemplate.queryForObject(originSql, params, String.class);
            } else if ("java.util.List".equals(methodReturnType) || "java.util.Set".equals(methodReturnType)) {
                String typeName = null;
                Type returnType = method.getGenericReturnType();
                if (returnType instanceof ParameterizedType) {
                    Type[] types = ((ParameterizedType) returnType).getActualTypeArguments();
                    if (null == types || types.length > 1) {
                        throw new IllegalArgumentException("当返回值为 list 时，必须标明具体类型，且只有一个");
                    }
                    typeName = types[0].getTypeName();
                }
                Object obj = BeanReflectionUtil.newInstance(typeName);
                SqlRowSet rowSet = jdbcTemplate.queryForRowSet(originSql, params);
                if ("java.util.List".equals(methodReturnType)) {
                    return ListConvert.convert(rowSet, obj);
                }
                return SetConvert.convert(rowSet, obj);
            } else if ("java.util.Map".equals(methodReturnType)) {
                throw new NotImplementedException();
            } else {
                SqlRowSet rowSet = jdbcTemplate.queryForRowSet(originSql, params);
                Object obj = BeanReflectionUtil.newInstance(methodReturnType);
                return BeanConvert.convert(rowSet, obj);
            }
        } else {
            int retVal = jdbcTemplate.update(originSql, params);
            if ("java.lang.Integer".equals(methodReturnType) || "int".equals(methodReturnType)) {
                return retVal;
            } else if ("java.lang.Boolean".equals(methodReturnType)) {
                return retVal > 0;
            }
        }
        return "";
    }
}
