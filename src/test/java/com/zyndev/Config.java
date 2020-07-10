package com.zyndev;

import com.zyndev.tool.fastsql.core.FastSqlRepositoryRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 这里应该有描述
 *
 * @version 1.0
 * @date 2018/2/23 12:48
 */
@Configuration
@Import(FastSqlRepositoryRegistrar.class)
public class Config {

//    @Bean
//    public FastSqlRepositoryRegistrar fastSqlRepositoryRegistrar() {
//        System.out.println("abc");
//        return new FastSqlRepositoryRegistrar();
//    }
}
