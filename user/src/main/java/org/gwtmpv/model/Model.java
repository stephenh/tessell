package org.gwtmpv.model;

/** Model is a wrapper around a DTO {@code D} that has a collection of properties. */
public interface Model<D> {

  /** Merges the new values from {@code dto} into the model's properties. */
  void merge(D dto);

  /** @return the dto for this model */
  D getDto();

}
