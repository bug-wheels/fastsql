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

import lombok.Data;

import javax.persistence.GenerationType;

/**
 * The type Db column info.
 *
 * @author 张瑀楠 zyndev@gmail.com
 * @version 0.0.1
 */
@Data
public class DBColumnInfo {

    /**
     * (Optional) The primary key generation strategy
     * that the persistence provider must use to
     * generate the annotated entity primary key.
     */
    private GenerationType strategy = GenerationType.AUTO;

    /**
     * The name of field
     */
    private String fieldName;

    /**
     * The name of the column
     */
    private String columnName;


    /**
     * Whether the column is a unique key.
     */
    private boolean unique;

    /**
     * Whether the database column is nullable.
     */
    private boolean nullable = true;

    /**
     * Whether the column is included in SQL INSERT
     */
    private boolean insertable = true;

    /**
     * Whether the column is included in SQL UPDATE
     */
    private boolean updatable = true;

    /**
     * The SQL fragment that is used when
     * generating the DDL for the column.
     */
    private String columnDefinition;

    /**
     * The name of the table that contains the column.
     * If absent the column is assumed to be in the primary table.
     */
    private String table;

    /**
     * (Optional) The column length. (Applies only if a
     * string-valued column is used.)
     */
    private int length =  255;

    private boolean id = false;

}
