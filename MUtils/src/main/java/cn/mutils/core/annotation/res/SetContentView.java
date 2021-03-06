package cn.mutils.core.annotation.res;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.mutils.app.ui.core.IContentViewOwner;

/**
 * IOC for {@link IContentViewOwner#setContentView(int)}
 *
 * @see IContentViewOwner#setContentView(int)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SetContentView {

    int value() default 0;

}
