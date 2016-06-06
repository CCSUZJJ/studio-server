package actions;

import play.mvc.With;

import java.lang.annotation.*;

/**
 * @author: Zhang.Min
 * @since: 2016/3/30
 * @version: 1.7
 */
@With(CheckBodyAction.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Inherited
@Documented
public @interface CheckBodyAnnotation {
    String[] value() default "";
}
