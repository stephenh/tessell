package org.gwtmpv.model;

/* A dto that can be upgraded to a client=side model. */
public interface Dto<M> {

  public M toModel();

}
