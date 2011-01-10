package org.gwtmpv.model.dsl;

import org.gwtmpv.model.properties.Property;

public interface WhenCondition<P> {

  boolean evaluate(Property<P> p);

}
