package com.zyndev.tool.fastsql;

import org.junit.Test;

/**
 * 这里应该有描述
 *
 * @author 张瑀楠 zyndev@gmail.com
 * @version 1.0
 * @date 2018/2/22 13:59
 */
public class PatternTest {

    @Test
    public void test() {
        String sql = "Select aa, bb ,cc dd, ee, ff from table where 1 = 1 and status != 2 " +
                " #if(:name) { and accessory_name like concat('%', :name ,'%') } " +
                " #if(:typeId != null ){ and type_id = :typeId  } " +
                " #if(:status >= 0 ) { and status =:status } " +
                " #if(:pageNum > -1 ){ limit :pageNum , :pageSize";
        System.out.println(sql.replaceAll("(?i)select([\\s\\S]*?)from","select count(*) from "));
    }

}
