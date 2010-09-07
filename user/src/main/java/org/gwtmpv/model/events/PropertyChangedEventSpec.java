package org.gwtmpv.model.events;

import org.gwtmpv.GenEvent;
import org.gwtmpv.Param;
import org.gwtmpv.model.properties.Property;

@GenEvent
public class PropertyChangedEventSpec<P> {
  @Param(1)
  Property<P> property;
}
