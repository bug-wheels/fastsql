package com.zyndev.tool.fastsql.core;

import com.zyndev.tool.fastsql.util.ClassScanner;
import com.zyndev.tool.fastsql.util.StringUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Set;

/**
 * The type Fast sql repository registrar.
 *
 * @author 张瑀楠 zyndev@gmail.com
 * @version 1.0
 * @date 2018 /2/23 12:26
 */
public class FastSqlRepositoryRegistrar implements ImportBeanDefinitionRegistrar, BeanFactoryAware {

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        System.out.println("FastSqlRepositoryRegistrar registerBeanDefinitions ");
        String basePackage = "com.sparrow";
        ClassScanner classScanner = new ClassScanner();
        Set<Class<?>> classSet = null;
        try {
            classSet = classScanner.getPackageAllClasses(basePackage, true);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        for (Class clazz : classSet) {
            if (clazz.getAnnotation(Repository.class) != null) {
                beanFactory.registerSingleton(StringUtil.firstCharToLowerCase(clazz.getSimpleName()), FacadeProxy.newMapperProxy(clazz));
            }
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
        System.out.println("beanFactory 创建成功");
    }
}
