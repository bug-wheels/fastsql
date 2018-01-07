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


import java.util.List;

/**
 * The type Base repository.
 *
 * @author 张瑀楠 zyndev@gmail.com
 * @version 1.0
 * @date 2017-12-27 14:48:43
 */
public interface BaseRepository {

    /**
     * Save int.
     *
     * @param entity the entity
     * @return the int
     */
    int save(Object entity);

    /**
     * Update int.
     *
     * @param entity the entity
     * @return the int
     */
    int update(Object entity);

    /**
     * Update int.
     *
     * @param entity     the entity
     * @param ignoreNull the ignore null
     * @return the int
     */
    int update(Object entity, boolean ignoreNull);

    /**
     * Delete int.
     *
     * @param entity the entity
     * @return the int
     */
    int delete(Object entity);

    /**
     * Find by id e.
     *
     * @param <E>    the type parameter
     * @param entity the entity
     * @return the e
     */
    <E> E findById(E entity);

    /**
     * Find by id e.
     *
     * @param <E>     the type parameter
     * @param entity  the entity
     * @param columns the columns
     * @return the e
     */
    <E> E findById(E entity, String columns);

    /**
     * Gets entity list.
     *
     * @param <E>    the type parameter
     * @param entity the entity
     * @return the entity list
     */
    <E> List<E> getEntityList(E entity);


//    public List getObjectList(Object obj,String colums);
//
//
//    public int updateSqlParam(String sql, Object[] objArray);
//
//    public int updateSql(String sql);
//
//
//
//    public int[] batchUpateSql(String[] sqls);
//    public int updateByMap(String tableName, Map<String, Object> updateMap, Map<String, Object> wherMap);
//
//
//
//
//
//    public Integer getInteger(String sql);
//
//
//
//    public List getObjectList(String sql, Class clazz);
//
//    public List getObjectList(String sql, Object[] args, Class clazz);
//
//
//
//    public List getObjectListByOrderByPro(Object obj, Page page, String orderBy, String colums);
//
//    //根据sql查对象
//    public List getObjectListBySql(String sql, Object[] args, Object obj, String colums);
//
//    public List getObjectPageListBySql(String sql, Object[] args, Object obj, Page page, String colums);
//
//    public List getObjectListBySql(String sql, Object obj, String colums);
}
