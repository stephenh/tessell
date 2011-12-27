package org.tessell.model;

import java.util.ArrayList;

/** Utility methods for models. */
public class Models {

  public static <M extends Model<D>, D extends Dto<M>> ArrayList<M> toModels(ArrayList<D> dtos) {
    ArrayList<M> models = new ArrayList<M>();
    for (D dto : dtos) {
      models.add(dto.toModel());
    }
    return models;
  }

}
