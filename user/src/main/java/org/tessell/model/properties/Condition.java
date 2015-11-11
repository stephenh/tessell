package org.tessell.model.properties;

/** An interface for evaluating the value of properties. */
@FunctionalInterface
public interface Condition<P> {

  /** @return true if {@code value} passes the implementation-defined criteria. */
  boolean evaluate(P value);

}
