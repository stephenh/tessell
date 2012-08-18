package org.tessell.model.repository;

import java.util.HashMap;

import org.tessell.dispatch.client.DispatchAsync;
import org.tessell.model.DtoModel;

public abstract class AbstractRepository<K, D, M extends DtoModel<D>> {

  // Identity map
  protected final HashMap<K, M> instances = new HashMap<K, M>();
  protected final DispatchAsync async;

  public AbstractRepository(final DispatchAsync async) {
    this.async = async;
  }

  /** Will immediately return a model, may/may not have data in it. */
  public M get(final K id) {
    return get(id, null);
  }

  public M merge(final K id, final D dto) {
    final M model = get(id, dto);
    model.merge(dto);
    return model;
  }

  /** Will immediately return a model, may/may not have data in it. */
  protected M get(final K id, final D dto) {
    M m = instances.get(id);
    if (m == null) {
      m = newModel(id, dto);
      instances.put(id, m);
    }
    return m;
  }

  protected abstract M newModel(final K id, D dto);

}
