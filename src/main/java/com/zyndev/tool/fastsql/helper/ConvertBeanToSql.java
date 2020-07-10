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


import com.zyndev.tool.fastsql.util.ClassScanner;
import com.zyndev.tool.fastsql.util.StringUtil;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The type Convert bean to sql.
 *
 * @author yunan.zhang zyndev@gmail.com
 * @version 1.0
 * @date 2017 /12/22 11:38 TODO add gui support
 */
public class ConvertBeanToSql {

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   * @throws IOException            the io exception
   * @throws ClassNotFoundException the class not found exception
   */
  public static void main(String[] args) throws IOException, ClassNotFoundException {
    new ConvertBeanToSql().convert();
  }

  /**
   * Convert.
   *
   * @throws IOException            the io exception
   * @throws ClassNotFoundException the class not found exception
   */
  public void convert() throws IOException, ClassNotFoundException {
    Set<Class<?>> entityClasses = filterEntityClass();
    List<TableInfo> tableInfos = new ArrayList<>(entityClasses.size());
    for (Class clazz : entityClasses) {
      TableInfo tableInfo = getEntityInfo(clazz);
      System.out.println("=> 正在解析bean:" + clazz.getName() + " 生成表：" + tableInfo.getTableName());
      tableInfos.add(tableInfo);
    }
    System.out.println("================================zyndev=================================");
    System.out.println("成功解析  " + entityClasses.size() + "  个bean:");
    System.out.println("开始生成表");
    generateSqlFile(tableInfos);
  }

  /**
   * Filter entity class set.
   *
   * @return the set
   * @throws IOException            the io exception
   * @throws ClassNotFoundException the class not found exception
   */
  public Set<Class<?>> filterEntityClass() throws IOException, ClassNotFoundException {
    ClassScanner classScanner = new ClassScanner();
    Set<Class<?>> classes = classScanner.getPackageAllClasses("com.zyndev", true);
    Set<Class<?>> result = new HashSet<>(40);
    for (Class clazz : classes) {
      if (null != clazz.getAnnotation(Entity.class)) {
        result.add(clazz);
      }
    }
    return result;
  }

  /**
   * 根据实体类获取对应的表信息
   *
   * @param entityClass 实体类
   * @return entity info
   */
  public TableInfo getEntityInfo(Class entityClass) {

    TableInfo tableInfo = new TableInfo();

    /**
     * 得到表名
     */
    Table table = (Table) entityClass.getAnnotation(Table.class);
    if (StringUtil.isNotBlank(table.name())) {
      tableInfo.setTableName(table.name());
    } else {
      tableInfo.setTableName(entityClass.getSimpleName());
    }
    tableInfo.setTableComment("ghost know the comment of table !!!");
    Field[] fields = entityClass.getDeclaredFields();
    List<ColumnInfo> columnInfoList = new ArrayList<>(fields.length);
    tableInfo.setColumnInfoList(columnInfoList);
    for (Field field : fields) {
      Column column = field.getAnnotation(Column.class);
//            if (null == column) {
//                continue;
//            }
      ColumnInfo columnInfo = new ColumnInfo();
//            if (StringUtil.isNotBlank(column.name())) {
//                columnInfo.setColumn(column.name());
//            } else {
//                columnInfo.setColumn(field.getName());
//            }
      columnInfo.setColumn(field.getName());

      Id id = field.getAnnotation(Id.class);
      if (null != id) {
        columnInfo.setId(true);
      }
      columnInfo.setComment("ghost know the means of field");
      columnInfo.setType(convertJavaTypeToSQLType(field.getType().getName(), 50));
      columnInfoList.add(columnInfo);
    }

    return tableInfo;
  }

  /**
   * Generate sql file.
   *
   * @param tableInfoList the table info list
   * @throws IOException the io exception
   */
  public void generateSqlFile(List<TableInfo> tableInfoList) throws IOException {
    Configuration cfg = new Configuration(new Version("2.3.23"));

    cfg.setTemplateLoader(new ClassTemplateLoader(TableInfo.class, "./templates/"));

    // 设置对象包装器
    cfg.setObjectWrapper(new DefaultObjectWrapper(new Version("2.3.23")));

    // 设置异常处理器
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

    // 通过freemarker解释模板，首先需要获得Template对象
    Template template = cfg.getTemplate("sql.ftl");

    Map<String, List<TableInfo>> root = new HashMap<>(2);
    root.put("tableInfos", tableInfoList);

    // 定义模板解释完成之后的输出
    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("generate.sql")));
    try {
      // 解释模板
      template.process(root, out);
    } catch (TemplateException e) {
      e.printStackTrace();
    }
  }

  private static String convertJavaTypeToSQLType(String javaType, int length) {
    String result = "";
    switch (javaType) {
      case "java.lang.Byte":
      case "java.lang.Boolean":
        result = "bit";
        break;
      case "java.lang.Short":
        result = "smallint";
        break;
      case "java.lang.Integer":
        result = "int(" + length + ")";
        break;
      case "java.lang.Long":
        result = "bigint(" + length + ")";
        break;
      case "java.lang.Float":
        result = "float";
        break;
      case "java.lang.Double":
        result = "double";
        break;
      case "java.lang.String":
        result = "varchar(" + length + ")";
        break;
      case "java.util.Date":
        result = "datetime";
        break;
      default:
        return "varchar(20)";
    }
    return result;
  }

}
