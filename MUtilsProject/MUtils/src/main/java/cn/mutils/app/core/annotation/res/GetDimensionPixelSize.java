package cn.mutils.app.core.annotation.res;

import android.content.res.Resources;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * IOC for {@link Resources#getDimensionPixelSize(int)}
 *
 * @see Resources#getDimensionPixelSize(int)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GetDimensionPixelSize {

    int value() default 0;

}
