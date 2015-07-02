package cn.o.app.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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