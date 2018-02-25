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

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 保存一下 dataSource
 *
 * @author 张瑀楠 zyndev@gmail.com
 * @version 0.0.1
 * @since 2017 /12/23 下午1:13
 */
public class JdbcTemplateHolder {

    private static JdbcTemplateHolder instance = new JdbcTemplateHolder();

    private static JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    private JdbcTemplateHolder() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static JdbcTemplateHolder getInstance() {
        return instance;
    }

    public static void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        JdbcTemplateHolder.jdbcTemplate = jdbcTemplate;
    }

}
