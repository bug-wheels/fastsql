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

package com.zyndev.tool.fastsql.helper;

import com.mysql.jdbc.DatabaseMetaData;
import com.zyndev.tool.fastsql.util.StringUtil;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * The type Auto generate entity of my sql.
 */
public class AutoGenerateEntityOfMySQL {

    /**
     * The Pstmt.
     */
    PreparedStatement pstmt = null;

    // 数据库连接
    private static Connection connection;
    private static String db_name = "springcloud";
    private static String filePath = "D:\\[]model\\";

    // 查询数据库 对应的表注释
    private static String queryTableComment = "SELECT TABLE_COMMENT FROM information_schema.`TABLES` WHERE table_name = ? AND table_schema = ? ";

    // 查询数据库 对应的字段信息
    private static String queryDataTableInfo = "select COLUMN_NAME,COLUMN_KEY,IS_NULLABLE,DATA_TYPE,COLUMN_COMMENT from information_schema.COLUMNS where table_name = ? and table_schema = ?";

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws SQLException the sql exception
     * @throws IOException  the io exception
     */
    public static void main(String[] args) throws SQLException, IOException {
        List<String> tables = getMySqlDataBaseTable();
        int tableCount = tables.size();
        for (int index = 0; index < tableCount; ++index) {
            convertMySqlTableToJavaEntity(tables.get(index), index);
        }
    }

    private synchronized static void convertMySqlTableToJavaEntity(String tableName, int index) throws SQLException, IOException {
        Configuration cfg = new Configuration();

        cfg.setTemplateLoader(new ClassTemplateLoader(TableInfo.class, "./templates/"));

        // 设置对象包装器
        cfg.setObjectWrapper(new DefaultObjectWrapper());

        // 设置异常处理器
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

        PreparedStatement queryTableCommentPreparedStatement = getConnection().prepareStatement(queryTableComment);
        queryTableCommentPreparedStatement.setString(1, tableName);
        queryTableCommentPreparedStatement.setString(2, db_name);
        ResultSet tableRet = queryTableCommentPreparedStatement.executeQuery();
        tableRet.next();
        String tableColumn = tableRet.getString("TABLE_COMMENT");

        PreparedStatement preparedStatement = getConnection().prepareStatement(queryDataTableInfo);
        preparedStatement.setString(1, tableName);
        preparedStatement.setString(2, db_name);
        ResultSet colRet = preparedStatement.executeQuery();

        String rate = "";
        for (; index>-1; --index) {
            rate += "=";
        }

        System.out.println(rate + ">生成表: " + tableName + " 详情: " + tableColumn);

        Map<String, Object> root = new HashMap<>();
        root.put("packageName", "com.zyndev.tool.fastsql.model");
        root.put("createDate", new Date());
        root.put("className", initCap(tableName));
        root.put("tableName", tableName);
        root.put("tableColumn", tableColumn);

        List<ColumnInfo> columnInfoList = new ArrayList<>(20);
        root.put("fields", columnInfoList);
        while (colRet.next()) {

            String columnName = colRet.getString("COLUMN_NAME");
            String columnKey = colRet.getString("COLUMN_KEY");
            String nullable = colRet.getString("IS_NULLABLE");
            String dataType = colRet.getString("DATA_TYPE");
            String columnComment = colRet.getString("COLUMN_COMMENT");
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfoList.add(columnInfo);
            columnInfo.setColumn(columnName);
            columnInfo.setId("PRI".equals(columnKey));
            columnInfo.setType(convertSqlTypeToJavaType(dataType));
            columnInfo.setName(convertColumnName(columnName));
            columnInfo.setComment(columnComment);
        }
        // 通过freemarker解释模板，首先需要获得Template对象
        Template template = cfg.getTemplate("lombok.ftl");

        // 定义模板解释完成之后的输出
        PrintWriter out = new PrintWriter(new BufferedWriter(
                new FileWriter(filePath + initCap(tableName) +  ".java")));
//
        try {
            // 解释模板
            template.process(root, out);
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    /**
     * get mysqlDataBaseTable
     *
     * @return
     * @throws SQLException
     */
    private static ArrayList<String> getMySqlDataBaseTable() throws SQLException {
        System.out.println("------conn--------" + getConnection());
        System.out.println("------conn.getAutoCommit()--------" + getConnection().getAutoCommit());
        // 查找指定数据库的所有 Table
        ArrayList<String> tables = new ArrayList<String>();
        DatabaseMetaData dbmd = (DatabaseMetaData) getConnection().getMetaData();
        ResultSet rs = null;
        String[] typeList = new String[] { "TABLE"};
        rs = dbmd.getTables(null, "%", "%", typeList);
        for (boolean more = rs.next(); more; more = rs.next()) {
            tables.add(rs.getString("TABLE_NAME"));
        }
        if (tables.size() == 0) {
            System.out.println("---- 未查到数据中的表，请确认数据名以及链接是否正确 ----");
        }
        return tables;
    }

    /**
     * To convert sqlType to javaType
     *
     * @param sqlType
     * @return
     */
    private static String convertSqlTypeToJavaType(String sqlType) {
        if ("bit".equalsIgnoreCase(sqlType)) {
            return "Boolean";
        } else if ("tinyint".equalsIgnoreCase(sqlType)) {
            return "Byte";
        } else if ("smallint".equalsIgnoreCase(sqlType)) {
            return "Short";
        } else if ("int".equalsIgnoreCase(sqlType)) {
            return "Integer";
        } else if ("bigint".equalsIgnoreCase(sqlType)) {
            return "Long";
        } else if ("float".equalsIgnoreCase(sqlType)) {
            return "Float";
        } else if ("decimal".equalsIgnoreCase(sqlType) || "numeric".equalsIgnoreCase(sqlType)
                || "real".equalsIgnoreCase(sqlType) || "money".equalsIgnoreCase(sqlType)
                || "smallmoney".equalsIgnoreCase(sqlType) || "double".equalsIgnoreCase(sqlType)) {
            return "Double";
        } else if ("varchar".equalsIgnoreCase(sqlType) || "char".equalsIgnoreCase(sqlType)
                || "nvarchar".equalsIgnoreCase(sqlType) || "nchar".equalsIgnoreCase(sqlType)
                || "text".equalsIgnoreCase(sqlType) || "longtext".equalsIgnoreCase(sqlType)) {
            return "String";
        } else if ("date".equalsIgnoreCase(sqlType) || "datetime".equalsIgnoreCase(sqlType) || "timestamp".equalsIgnoreCase(sqlType)) {
            return "Date";
        } else if ("image".equalsIgnoreCase(sqlType)) {
            return "Blod";
        }
        return null;
    }

    /**
     * get dataBase connection
     *
     * @return connection
     * @throws SQLException the sql exception
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/springcloud?useUnicode=true&characterEncoding=utf8", "root", "zyndev");
        }
        connection.setAutoCommit(false);
        return connection;
    }

    /**
     * 功能：将输入字符串的首字母改成大写
     * @param str
     * @return
     */
    private static String initCap(String str) {
        if (StringUtil.isBlank(str)) {
            throw new IllegalArgumentException("参数不能为空");
        }
        String[] words = null;
        if (str.contains("_")) {
            words = str.split("_");
        } else {
            words = new String[]{str};
        }
        StringBuilder result = new StringBuilder();
        for (int index=0; index<words.length; ++index) {
            String param = words[index];
            if (index == 0 && "tb".equalsIgnoreCase(param)) {
                continue;
            }
            char[] ch = param.toCharArray();
            if (ch[0] >= 'a' && ch[0] <= 'z') {
                ch[0] = (char) (ch[0] - 32);
            }
            result.append(ch);
        }
        return result.toString();
    }

    /**
     * 功能：将输入字符串的首字母改成大写
     * @param str
     * @return
     */
    private static String convertColumnName(String str) {
        if (StringUtil.isBlank(str)) {
            throw new IllegalArgumentException("参数不能为空");
        }
        String[] words = null;
        if (str.contains("_")) {
            words = str.split("_");
        } else {
            words = new String[]{str};
        }
        StringBuilder result = new StringBuilder();
        for (int index=0; index<words.length; ++index) {
            String param = words[index];
            if (index == 0) {
                result.append(param);
                continue;
            }
            char[] ch = param.toCharArray();
            if (ch[0] >= 'a' && ch[0] <= 'z') {
                ch[0] = (char) (ch[0] - 32);
            }
            result.append(ch);
        }
        return result.toString();
    }
}
