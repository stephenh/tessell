package org.tessell.model.events;

import org.gwtmpv.GenEvent;
import org.gwtmpv.Param;
import org.tessell.model.properties.Property;

@GenEvent(gwtEvent = true)
public class PropertyChangedEventSpec<P> {
  @Param(1)
  Property<P> property;
}
