package com.zyndev;

import com.zyndev.tool.fastsql.core.AnnotationParser;
import com.zyndev.tool.fastsql.repository.po.Stall;
import org.junit.Test;

/**
 * @author 张瑀楠 zyndev@gmail.com
 * @version 0.0.1
 */
public class AnnotationParseTest {

    @Test
    public void test() {
        System.out.println(AnnotationParser.getTableName(new Stall()));
        System.out.println(AnnotationParser.getTableAllColumn(new Stall()));
    }

}
