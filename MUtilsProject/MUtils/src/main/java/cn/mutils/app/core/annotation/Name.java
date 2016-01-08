package cn.mutils.app.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.mutils.app.core.json.JsonUtil;
import cn.mutils.app.core.xml.XmlUtil;

/**
 * IOC for name property of entity
 *
 * @see JsonUtil
 * @see XmlUtil
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
public @interface Name {

    String value() default "";

}