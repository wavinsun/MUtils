package cn.o.app.annotation.res;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.view.animation.AnimationUtils;

/**
 * IOC for {@link AnimationUtils#loadAnimation(android.content.Context, int)}
 * 
 * @see AnimationUtils#loadAnimation(android.content.Context, int)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LoadAnimation {

	public int value() default 0;

}
