package com.zyndev;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

/**
 * @author 张瑀楠 zyndev@gmail.com
 * @version 1.0
 * @date 2018/1/19 下午9:46
 */
public class DateTest {

    private static Jedis jedis = new Jedis("39.106.46.252", 6379);
    private static long incrKey1 = 1;
    private static long incrKey0 = 1;
    private static boolean existsKey0 = false;
    private static boolean existsKey1 = false;

    @Before
    public void testBefore() {
        jedis.auth("123456");
    }

    private void createOrderId(String now) {
        long key = 0;
        int a = Integer.valueOf(now) % 2;
        String incrKey = "key_single" + a;
        if (a == 0) {
            if (!existsKey0) {
                if (!jedis.exists(incrKey)) {
                    jedis.set(incrKey, "10000");
                    existsKey0 = true;
                }
            }
            if (incrKey1 > 1) {
                jedis.set("key_single1" , "10000");
                incrKey1 = 1;
            }
            key = incrKey0 = jedis.incr(incrKey);
        } else {
            if (!existsKey1) {
                if (!jedis.exists(incrKey)) {
                    jedis.set(incrKey, "10000");
                    existsKey1 = true;
                }
            }
            if (incrKey0 > 1) {
                jedis.set("key_single0" , "10000");
                incrKey0 = 1;
            }
            key = incrKey1 = jedis.incr(incrKey);
        }
        System.out.println("日期：" + now + " 单号 " + now + key);
    }


    @Test
    public void test() {
        System.out.println("-------- 180102");
        for (int i=0; i<1000000; ++i) {
            createOrderId("180102");
        }

        System.out.println("-------- 180101");
        for (int i=0; i<1000000; ++i) {
            createOrderId("180101");
        }

        System.out.println("-------- 180106");
        for (int i=0; i<1000000; ++i) {
            createOrderId("180106");
        }

        System.out.println("-------- 180107");
        for (int i=0; i<1000000; ++i) {
            createOrderId("180107");
        }

        System.out.println("-------- 180102");
        for (int i=0; i<1000000; ++i) {
            createOrderId("180102");
        }

        System.out.println("-------- 180101");
        for (int i=0; i<1000000; ++i) {
            createOrderId("180101");
        }

        System.out.println("-------- 180106");
        for (int i=0; i<1000000; ++i) {
            createOrderId("180106");
        }

        System.out.println("-------- 180107");
        for (int i=0; i<1000000; ++i) {
            createOrderId("180107");
        }
    }
}
