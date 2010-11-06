package org.gwtmpv.model.values;

import org.gwtmpv.model.properties.AbstractProperty;
import org.gwtmpv.model.properties.Property;
import org.gwtmpv.model.properties.StringProperty;

/**
 * Very basic get/set interface for {@link Property}s to wrap.
 * 
 * This allows the {@link Property} behavior in the implementations {@link AbstractProperty}/{@link StringProperty}/etc. to
 * be reused across a variety of value sources (e.g. see {@link SetValue}, {@link BoundValue}, etc.).
 */
public interface Value<P> {

  P get();

  void set(final P value);

  String getName();

  boolean isReadOnly();

}
