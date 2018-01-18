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
 * @version 0.0.1
 * @since 2017 -12-27 14:48:43
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
     * @param entity  the entity
     * @param columns the columns
     * @return the int
     */
    int update(Object entity, String... columns);

    /**
     * Update int.
     *
     * @param entity     the entity
     * @param ignoreNull the ignore null
     * @return the int
     */
    int update(Object entity, boolean ignoreNull);


    /**
     * Update int.
     *
     * @param entity     the entity
     * @param ignoreNull the ignore null
     * @param columns    the columns
     * @return the int
     */
    int update(Object entity, boolean ignoreNull, String... columns);

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
    <E> E findById(E entity, String... columns);

    /**
     * Gets entity list.
     *
     * @param <E>    the type parameter
     * @param entity the entity
     * @return the entity list
     */
    <E> List<E> getEntityList(E entity);

    /**
     * Gets entity list.
     *
     * @param <E>     the type parameter
     * @param entity  the entity
     * @param columns the columns
     * @return the entity list
     */
    <E> List<E> getEntityList(E entity, String... columns);

    /**
     * Gets entity list.
     *
     * @param <E>    the type parameter
     * @param sql    the sql
     * @param entity the entity
     * @return the entity list
     */
    <E> List<E> getEntityList(String sql, E entity);

    /**
     * Gets entity list.
     *
     * @param <E>    the type parameter
     * @param sql    the sql
     * @param args   the args
     * @param entity the entity
     * @return the entity list
     */
    <E> List<E> getEntityList(String sql, Object[] args, E entity);

    /**
     * Gets entity page list.
     *
     * @param <E>      the type parameter
     * @param entity   the entity
     * @param pageNum  the page num
     * @param pageSize the page size
     * @return the entity page list
     */
    <E> PageListContent<E> getEntityPageList(E entity, int pageNum, int pageSize);

    /**
     * Gets entity page list.
     *
     * @param <E>      the type parameter
     * @param entity   the entity
     * @param pageNum  the page num
     * @param pageSize the page size
     * @param columns  the columns
     * @return the entity page list
     */
    <E> PageListContent<E> getEntityPageList(E entity, int pageNum, int pageSize, String... columns);

    /**
     * Gets entity page list by sql.
     *
     * @param <E>      the type parameter
     * @param sql      the sql
     * @param entity   the entity
     * @param pageNum  the page num
     * @param pageSize the page size
     * @param columns  the columns
     * @return the entity page list by sql
     */
    <E> PageListContent<E> getEntityPageListBySql(String sql, E entity, int pageNum, int pageSize, String... columns);

    /**
     * Gets entity page list by sql.
     *
     * @param <E>      the type parameter
     * @param sql      the sql
     * @param entity   the entity
     * @param pageNum  the page num
     * @param pageSize the page size
     * @param orderBy  the order by
     * @param columns  the columns
     * @return the entity page list by sql
     */
    <E> PageListContent<E> getEntityPageListBySql(String sql, E entity, int pageNum, int pageSize, String orderBy, String... columns);

    /**
     * Gets entity page list by sql.
     *
     * @param <E>      the type parameter
     * @param sql      the sql
     * @param args     the args
     * @param entity   the entity
     * @param pageNum  the page num
     * @param pageSize the page size
     * @param columns  the columns
     * @return the entity page list by sql
     */
    <E> PageListContent<E> getEntityPageListBySql(String sql, Object[] args, E entity, int pageNum, int pageSize, String... columns);
}
