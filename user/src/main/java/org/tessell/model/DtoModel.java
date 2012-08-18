package org.tessell.model;

/** A model that wraps a DTO {@code D}. */
public interface DtoModel<D> extends Model {

  /** Merges the new values from {@code dto} into the model's properties. */
  void merge(D dto);

  /** @return the dto for this model */
  D getDto();

}
