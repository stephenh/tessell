package org.gwtmpv.model;

import com.google.gwt.user.client.rpc.IsSerializable;

/* A dto that can be upgraded to a client-side model. */
public interface Dto<M> extends IsSerializable {

  public M toModel();

}
