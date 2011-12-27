package org.tessell.model;

import org.tessell.model.properties.Property;

/** Model is a wrapper around a DTO {@code D} that has a collection of {@link Property}s. */
public interface Model<D> {

  /** Merges the new values from {@code dto} into the model's properties. */
  void merge(D dto);

  /** @return the dto for this model */
  D getDto();

  Property<Boolean> allValid();

}
