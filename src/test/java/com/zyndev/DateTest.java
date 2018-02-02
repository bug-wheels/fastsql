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
    private static final String PREFIX = "KEY_GENERATE_MARKET_SERVER_";
    private static final boolean CACHE_HA = false;


    @Before
    public void testBefore() {
        jedis.auth("7Df2074055");
    }

    public String createOrderCode() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHH");

        String dateStr = dateFormat.format(now);
        String timeStr = timeFormat.format(now);

        long key = 0;
        int a = Integer.valueOf(dateStr) % 2;
        String incrKey = PREFIX + a;
        // 偶数天
        if (a == 0) {
            // 判断偶数的key 是否存在
            if (!existsKey0) {
                if (!jedis.exists(incrKey)) {
                    jedis.set(incrKey, "10000");
                }
                existsKey0 = true;
            }
            if (incrKey1 > 1) {
                jedis.set(PREFIX + 1 , "10000");
                incrKey1 = 1;
            }
            key = incrKey0 = jedis.incr(incrKey);
        } else {  // 奇数天
            // 判断奇数的key 是否存在
            if (!existsKey1) {
                if (!jedis.exists(incrKey)) {
                    jedis.set(incrKey, "10000");
                    existsKey1 = true;
                }
            }
            if (incrKey0 > 1) {
                jedis.set(PREFIX + 0 , "10000");
                incrKey0 = 1;
            }
            key = incrKey1 = jedis.incr(incrKey);
        }
        return timeStr + key;
    }

    public Object getObject(String key) {
        byte[] bytes;
        Object var4;
        if (CACHE_HA) {

            try {
                bytes = jedis.get(key.getBytes());
                var4 = SerializeUtils.unserialize(bytes);
            } catch (Exception var15) {
                log.error(String.format("jedis getObject failed key=%s", key));
                return null;
            } finally {
                this.close(j);
            }

            return var4;
        } else {
            ShardedJedis jedis = null;

            try {
                jedis = this.getJedis();
                bytes = jedis.get(key.getBytes());
                var4 = SerializeUtils.unserialize(bytes);
            } catch (Exception var17) {
                log.error(String.format("jedis getObject failed key=%s", key));
                return null;
            } finally {
                this.closeShardedJedis(jedis);
            }

            return var4;
        }
    }


    @Test
    public void test() {
        System.out.println("-------- 180102");
        System.out.println(createOrderCode());
    }
}
