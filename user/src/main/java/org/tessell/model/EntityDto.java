package org.tessell.model;

/**
 * A dto for an entity.
 *
 * Enables {@code model <-> dto} logic to be abstracted.
 *
 * @param K the type of the entity key (Integer/Long)
 * @param M the type of the model
 */
public interface EntityDto<K, M> extends Dto<M> {

  K id();

}
