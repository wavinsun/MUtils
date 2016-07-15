package cn.mutils.core.annotation.res;

import android.content.res.Resources;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * IOC for {@link Resources#getDrawable(int)}
 *
 * @see Resources#getDrawable(int)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GetDrawable {

    int value() default 0;

}
