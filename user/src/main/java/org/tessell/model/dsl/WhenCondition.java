package org.tessell.model.dsl;

import org.tessell.model.properties.Property;

public interface WhenCondition<P> {

  boolean evaluate(Property<P> property);

}
