package cn.o.app.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.TYPE })
public @interface Primitive {

	public static enum PrimitiveType {
		STRING, // 字符串
		STRING_INT, // 整型字符串
		STRING_LONG, // 长整型字符串
		STRING_DOUBLE, // 浮点数字符串
		STRING_BOOL, // 布尔字符串
		INT, // 整型
		LONG, // 长整型
		DOUBLE, // 浮点数
		BOOL// 布尔
	}

	public PrimitiveType value() default PrimitiveType.STRING;
}