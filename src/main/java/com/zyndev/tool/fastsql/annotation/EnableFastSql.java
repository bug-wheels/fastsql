package com.zyndev.tool.fastsql.annotation;

import com.zyndev.tool.fastsql.core.FastSqlRepositoryRegistrar;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

/**
 * 启用 FastSQL 注解
 *
 * @author 张瑀楠 zyndev@gmail.com
 * @version 0.0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(FastSqlRepositoryRegistrar.class)
public @interface EnableFastSql {

  /**
   * 指定扫描的包
   * <p>
   * annotation declarations e.g.: {@code @EnableFastSql("com.zyndev.tool")} instead of {@code
   *
   * @EnableFastSql(basePackages= "com.zyndev.tool"})}.
   */
  String[] value() default {};

  /**
   * Base packages to scan for FastSQL interfaces. Note that only interfaces with at least one method will be
   * registered; concrete classes will be ignored.
   */
  String[] basePackages() default {};

  /**
   * This property specifies the annotation that the scanner will search for.
   * <p>
   * The scanner will register all interfaces in the base package that also have the specified annotation.
   * <p>
   * Note this can be combined with markerInterface.
   */
  Class<? extends Annotation> annotationClass() default Annotation.class;

}
