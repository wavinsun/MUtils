package cn.mutils.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.mutils.core.time.DateTime;

/**
 * IOC for format of property of entity
 *
 * @see DateTime
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
public @interface Format {

    String value() default "";

}