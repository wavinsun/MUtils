package cn.mutils.core.annotation.res;

import android.view.animation.AnimationUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * IOC for {@link AnimationUtils#loadAnimation(android.content.Context, int)}
 *
 * @see AnimationUtils#loadAnimation(android.content.Context, int)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LoadAnimation {

    int value() default 0;

}
