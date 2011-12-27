package org.tessell.bus;

public interface Bound {

  /** Any event handlers and other setup should be done here rather than in the constructor. */
  void bind();

  /** Called after the presenter and view have been finished with for the moment. */
  void unbind();

  /** @return whether the instance is currently bound */
  boolean isBound();

}
