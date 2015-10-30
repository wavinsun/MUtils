package cn.mutils.app.core.annotation.net;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.mutils.app.core.json.JsonUtil;
import cn.mutils.app.core.xml.XmlUtil;

/**
 * IOC for header property of entity given to NetClient
 * 
 * @see JsonUtil
 * @see XmlUtil
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.TYPE })
public @interface Head {

}
