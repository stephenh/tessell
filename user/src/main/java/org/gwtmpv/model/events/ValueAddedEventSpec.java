package org.gwtmpv.model.events;

import java.util.ArrayList;

import org.gwtmpv.GenEvent;
import org.gwtmpv.model.properties.Property;

@GenEvent
public class ValueAddedEventSpec<P> {
  Property<ArrayList<P>> p1property;
  P p2value;
}
