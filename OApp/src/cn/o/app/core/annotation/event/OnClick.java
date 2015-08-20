package cn.o.app.core.annotation.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.view.View;

/**
 * IOC for {@link View#setOnClickListener(View.OnClickListener)}
 * 
 * @see View#setOnClickListener(View.OnClickListener)
 * @see View.OnClickListener
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnClick {

	public int[]value() default {};

}
