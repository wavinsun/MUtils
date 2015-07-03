package cn.o.app.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.o.app.json.JsonUtil;
import cn.o.app.xml.XmlUtil;

/**
 * IOC for name property of entity
 * 
 * @see JsonUtil
 * @see XmlUtil
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.TYPE })
public @interface Name {

	public String value() default "";

}