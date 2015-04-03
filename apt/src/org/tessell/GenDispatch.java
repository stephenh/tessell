package org.tessell;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/** Marks a class as a specification for {@code XxxAction} and {@code XxxResult} classes. */
@Target({ ElementType.TYPE })
public @interface GenDispatch {
  String baseAction() default "";

  String baseResult() default "";
}
