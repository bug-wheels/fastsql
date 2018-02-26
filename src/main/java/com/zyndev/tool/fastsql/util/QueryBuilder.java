package com.zyndev.tool.fastsql.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * QueryBuilder
 *
 * @author 张瑀楠 wb.yunan.zhang@renren-inc.com
 * @version 1.0
 */
public class QueryBuilder {

    private final static Log logger = LogFactory.getLog(QueryBuilder.class);

    private StringBuilder sql = new StringBuilder(100);

    private Object[] varParams;

    public QueryBuilder() {}



}
