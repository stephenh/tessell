package org.tessell.model;

import org.tessell.model.properties.AbstractProperty;

/** A base class for models that wrap a DTO. */
public abstract class AbstractDtoModel<D> extends AbstractModel implements DtoModel<D> {

  protected D dto;

  /**
   * Subclasses should pass in dto to avoid NPEs in any inline assignments of properties they do
   * that might want to evaluate the dto, e.g. especially for lambdas.
   */
  protected AbstractDtoModel(D dto) {
    this.dto = dto;
  }

  @Override
  public void merge(D dto) {
    // We treat Model.merge as the equivalent of Property.setInitialValue,
    // e.g. we shouldn't mark touched, and we should reset initial values.
    AbstractProperty.outstandingSetInitials++;
    try {
      this.dto = dto;
      all.reassessAll();
    } finally {
      AbstractProperty.outstandingSetInitials--;
    }
  }

  @Override
  public D getDto() {
    return dto;
  }

}
