package cn.o.app.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.o.app.io.ODate;

/**
 * IOC for format of property of entity
 * 
 * @see ODate
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.TYPE })
public @interface Format {

	public String value() default "";

}