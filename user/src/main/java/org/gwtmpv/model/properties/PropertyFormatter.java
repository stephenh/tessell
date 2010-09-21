package org.gwtmpv.model.properties;

public interface PropertyFormatter<A, B> {
  B format(A a);
}
