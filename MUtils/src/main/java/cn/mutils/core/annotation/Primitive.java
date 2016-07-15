package cn.mutils.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.mutils.core.io.serial.StringJson;
import cn.mutils.core.io.serial.primitive.DoubleItem;
import cn.mutils.core.io.serial.primitive.EnumItem;
import cn.mutils.core.io.serial.primitive.IntItem;
import cn.mutils.core.io.serial.primitive.LongItem;
import cn.mutils.core.time.DateTime;

/**
 * IOC for primitive type of property of entity
 *
 * @see IntItem
 * @see LongItem
 * @see DoubleItem
 * @see DateTime
 * @see EnumItem
 * @see StringJson
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@SuppressWarnings({"UnnecessaryInterfaceModifier", "UnnecessaryEnumModifier"})
public @interface Primitive {

    PrimitiveType value() default PrimitiveType.STRING;

}