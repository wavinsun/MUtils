package cn.o.app.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.o.app.core.io.serial.StringJson;
import cn.o.app.core.io.serial.primitive.DoubleItem;
import cn.o.app.core.io.serial.primitive.EnumItem;
import cn.o.app.core.io.serial.primitive.IntItem;
import cn.o.app.core.io.serial.primitive.LongItem;
import cn.o.app.core.time.DateTime;

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
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.TYPE })
public @interface Primitive {

	public static enum PrimitiveType {

		/** String */
		STRING,

		/** Integer of string */
		STRING_INT,

		/** Long of string */
		STRING_LONG,

		/** Double of string */
		STRING_DOUBLE,

		/** Boolean of string */
		STRING_BOOL,

		/** Integer */
		INT,

		/** Long */
		LONG,

		/** Double */
		DOUBLE,

		/** Boolean */
		BOOL
	}

	public PrimitiveType value() default PrimitiveType.STRING;
}