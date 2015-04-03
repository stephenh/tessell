package org.tessell;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/** Marks the order of fields in {@link GenEvent} specs. */
@Target({ ElementType.FIELD })
public @interface In {
  int value();
}
