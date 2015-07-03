package cn.o.app.annotation.res;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.content.res.Resources;

/**
 * IOC for {@link Resources#getDimensionPixelSize(int)}
 * 
 * @see Resources#getDimensionPixelSize(int)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GetDimensionPixelSize {

	public int value() default 0;

}
