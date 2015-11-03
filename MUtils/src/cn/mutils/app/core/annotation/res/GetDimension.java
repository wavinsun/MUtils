package cn.mutils.app.core.annotation.res;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.content.res.Resources;

/**
 * IOC for {@link Resources#getDimension(int)}
 * 
 * @see Resources#getDimension(int)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GetDimension {

	public int value() default 0;

}
