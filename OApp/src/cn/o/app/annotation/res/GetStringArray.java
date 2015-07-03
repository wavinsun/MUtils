package cn.o.app.annotation.res;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.content.res.Resources;

/**
 * IOC for {@link Resources#getStringArray(int)}
 * 
 * @see Resources#getStringArray(int)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GetStringArray {

	public int value() default 0;

}
