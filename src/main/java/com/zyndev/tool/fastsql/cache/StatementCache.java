package com.zyndev.tool.fastsql.cache;

import java.util.HashSet;
import java.util.Set;

/**
 * The type Statement cache.
 * jexl 表达式缓存
 *
 * 保证表达式只解析一次
 *
 *
 * @author yunan.zhang
 * @version 0.0.4
 */
public class StatementCache {

    /**
     * 确认不包含jexl表达式的语句
     */
    private static Set<String> excludeJexlSet = new HashSet<>(40);

    public static void addToExcludeJexlSet(String methodName) {
        excludeJexlSet.add(methodName);
    }

    /**
     * Contain jexl boolean.
     *
     * @param methodName the method name
     * @return the boolean
     */
    public static boolean containJexl(String methodName) {
        if (excludeJexlSet.contains(methodName)) {
            return false;
        }
        return true;
    }


}
