package cn.mutils.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.mutils.core.json.JsonUtil;
import cn.mutils.core.xml.XmlUtil;

/**
 * IOC for ignore property of entity
 *
 * @see JsonUtil
 * @see XmlUtil
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
public @interface Ignore {

}