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


import com.zyndev.tool.fastsql.util.StringUtil;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The type Annotation parser.
 * <p>
 * 注解解析工具
 *
 * @author yunan.zhang zyndev@gmail.com
 * @version 0.0.3
 * @since 2017-12-26 16:59:07
 */
public class AnnotationParser {

    private final static Map<String, String> tableNameCache = new HashMap<>(30);
    private final static Map<String, String> tableAllColumnNameCache = new HashMap<>(30);
    private final static Map<String, List<DBColumnInfo>> tableAllDBColumnCache = new HashMap<>(30);

    /**
     * Gets table name.
     *
     * @param <E>    the type parameter
     * @param entity the entity
     * @return the table name
     */
    public static <E> String getTableName(E entity) {
        String tableName = tableNameCache.get(entity.getClass().getName());
        if (tableName == null) {
            Table table = entity.getClass().getAnnotation(Table.class);
            if (table != null && StringUtil.isNotBlank(table.name())) {
                tableName = table.name();
            } else {
                tableName = entity.getClass().getSimpleName();
            }
            tableNameCache.put(entity.getClass().getName(), tableName);
        }
        return tableName;
    }

    /**
     * Gets all db column info.
     *
     * @param <E>    the type parameter
     * @param entity the entity
     * @return the all db column info
     */
    public static <E> List<DBColumnInfo> getAllDBColumnInfo(E entity) {
        List<DBColumnInfo> dbColumnInfoList = tableAllDBColumnCache.get(entity.getClass().getName());
        if (dbColumnInfoList == null) {
            dbColumnInfoList = new ArrayList<>();
            DBColumnInfo dbColumnInfo;
            Field[] fields = entity.getClass().getDeclaredFields();
            for (Field field : fields) {
                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    dbColumnInfo = new DBColumnInfo();
                    if (StringUtil.isBlank(column.name())) {
                        dbColumnInfo.setColumnName(field.getName());
                    } else {
                        dbColumnInfo.setColumnName(column.name());
                    }
                    if (null != field.getAnnotation(Id.class)) {
                        dbColumnInfo.setId(true);
                    }
                    dbColumnInfo.setFieldName(field.getName());
                    dbColumnInfoList.add(dbColumnInfo);
                }
            }
            tableAllDBColumnCache.put(entity.getClass().getName(), dbColumnInfoList);
        }
        return dbColumnInfoList;
    }

    /**
     * 返回表字段的所有字段  column1,column2,column3
     *
     * @param <E>    the type parameter
     * @param entity the entity
     * @return string
     */
    public static <E> String getTableAllColumn(E entity) {

        String allColumn = tableAllColumnNameCache.get(entity.getClass().getName());
        if (allColumn == null) {
            List<DBColumnInfo> dbColumnInfoList = getAllDBColumnInfo(entity);
            StringBuilder allColumnsInfo = new StringBuilder();
            int i = 1;
            for (DBColumnInfo dbColumnInfo : dbColumnInfoList) {
                allColumnsInfo.append(dbColumnInfo.getColumnName());
                if (i != dbColumnInfoList.size()) {
                    allColumnsInfo.append(",");
                }
                i++;
            }
            allColumn = allColumnsInfo.toString();
            tableAllColumnNameCache.put(entity.getClass().getName(), allColumn);
        }
        return allColumn;

    }
}
