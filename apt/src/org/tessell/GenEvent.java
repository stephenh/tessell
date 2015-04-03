package org.tessell;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/** Marks a class as a specification for GWT event classes. */
@Target({ ElementType.TYPE })
public @interface GenEvent {
  String methodName() default "";

  boolean gwtEvent() default false;
}
