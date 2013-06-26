package org.tessell.model.values;

import org.tessell.model.properties.Property;
import org.tessell.model.properties.StringProperty;
import org.tessell.util.Supplier;

/**
 * Very basic get/set interface for {@link Property}s to wrap.
 * 
 * This allows the {@link Property} behavior in the implementations {@link org.tessell.model.properties.AbstractProperty}/{@link StringProperty}/etc. to
 * be reused across a variety of value sources (e.g. see {@link SetValue}, {@link BoundValue}, etc.).
 */
public interface Value<P> extends Supplier<P> {

  void set(final P value);

  String getName();

  boolean isReadOnly();

}
