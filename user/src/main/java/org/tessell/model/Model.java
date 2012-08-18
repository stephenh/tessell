package org.tessell.model;

import org.tessell.model.properties.Property;

/** A model is an entity that contains a number of properties. */
public interface Model {

  Property<Boolean> allValid();

}
