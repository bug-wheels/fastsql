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

import com.zyndev.tool.fastsql.util.ClassScanner;
import com.zyndev.tool.fastsql.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Set;

/**
 * 继承 BeanFactoryPostProcessor
 * 扫描可以执行的 Repository 接口 并生成代理添加到 beanFactory 中
 *
 * @author 张瑀楠 zyndev@gmail.com
 * @version 0.0.1
 * @since 2017 /12/22 下午10:14
 */
public class FastSqlProcessor implements BeanFactoryPostProcessor {

    private final Log logger = LogFactory.getLog(FastSqlProcessor.class);

    /**
     * 默认扫描包
     */
    private String basePackage = "com.zyndev";

    private DataSource dataSource;

    @Override
    public final void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
            throws BeansException {
        try {
            logger.info("start FastSQLProcessor processor");
            doPostProcessBeanFactory(beanFactory);
            logger.info("end FastSQLProcessor processor");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理逻辑，默认扫描 com.zyndev 包下的 Repository 接口，并生成代理加入到
     *
     * @param beanFactory beanFactory
     * @throws IOException io
     * @throws ClassNotFoundException not found
     */
    private void doPostProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws IOException, ClassNotFoundException {
        ClassScanner classScanner = new ClassScanner();
        logger.info("------------ scan package :" + basePackage);
        Set<Class<?>> classSet = classScanner.getPackageAllClasses(basePackage, true);
        DefaultListableBeanFactory factory = (DefaultListableBeanFactory) beanFactory;
        for (Class clazz : classSet) {
            if (clazz.getAnnotation(Repository.class) != null) {
                logger.info(" add bean [" + clazz.getName() + "] to spring beanFactory");
                factory.registerSingleton(StringUtil.firstCharToLowerCase(clazz.getSimpleName()), FacadeProxy.newMapperProxy(clazz));
            }
        }
        DataSourceHolder.getInstance().setDataSource(dataSource);
    }

    /**
     * Sets base package.
     *
     * @param basePackage the base package
     */
    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    /**
     * Sets data source.
     *
     * @param dataSource the data source
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


}
