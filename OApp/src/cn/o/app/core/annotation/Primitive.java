package cn.o.app.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.o.app.core.io.ODouble;
import cn.o.app.core.io.OEnum;
import cn.o.app.core.io.OInteger;
import cn.o.app.core.io.OLong;
import cn.o.app.core.io.StringJson;
import cn.o.app.core.time.ODate;

/**
 * IOC for primitive type of property of entity
 * 
 * @see OInteger
 * @see OLong
 * @see ODouble
 * @see ODate
 * @see OEnum
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