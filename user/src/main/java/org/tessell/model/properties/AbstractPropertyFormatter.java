package org.tessell.model.properties;

public abstract class AbstractPropertyFormatter<A, B> extends PropertyFormatter<A, B> {

  @Override
  public B format(A a) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public A parse(B b) throws Exception {
    throw new UnsupportedOperationException("Not implemented");
  }

}
