package org.tessell;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/** Marks a method as an entry point for a place. */
@Target({ ElementType.METHOD })
public @interface GenPlace {
  String name();

  boolean async() default true;

  String[] params() default {};
}
