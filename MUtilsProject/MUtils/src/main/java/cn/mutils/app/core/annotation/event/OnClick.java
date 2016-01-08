package cn.mutils.app.core.annotation.event;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * IOC for {@link View#setOnClickListener(View.OnClickListener)}
 *
 * @see View#setOnClickListener(View.OnClickListener)
 * @see View.OnClickListener
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnClick {

    int[] value() default {};

}
