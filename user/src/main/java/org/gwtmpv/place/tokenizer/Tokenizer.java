package org.gwtmpv.place.tokenizer;

import org.gwtmpv.place.PlaceRequest;

/** Provides services to convert a {@link PlaceRequest} to and from a History token value. */
public interface Tokenizer {

  /** Converts a {@link PlaceRequest} into a {@link com.google.gwt.user.client.History} token. */
  String toHistoryToken(PlaceRequest placeRequest);

  /** Converts a {@link com.google.gwt.user.client.History} token into a {@link PlaceRequest}. */
  PlaceRequest toPlaceRequest(String token) throws TokenizerException;

}
