package org.tessell.place;

/**
 * A factory that can create multiple history tokens for a place for each given <code>state</code>.
 * 
 * E.g. for rendering the view/edit links of a list/table of domain objects.
 */
public interface PlaceRequestFactory<T> {
  String make(T state);
}