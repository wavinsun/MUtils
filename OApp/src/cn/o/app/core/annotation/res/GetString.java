package cn.o.app.core.annotation.res;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.content.res.Resources;

/**
 * IOC for {@link Resources#getString(int)}
 * 
 * @see Resources#getString(int)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GetString {

	public int value() default 0;

}
