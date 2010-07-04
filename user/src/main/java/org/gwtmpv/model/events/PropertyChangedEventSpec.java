package org.gwtmpv.model.events;

import org.gwtmpv.GenEvent;
import org.gwtmpv.model.properties.Property;

@GenEvent
public class PropertyChangedEventSpec<P> {
  Property<P> p1property;
  boolean p2isFirstLoad;
}
