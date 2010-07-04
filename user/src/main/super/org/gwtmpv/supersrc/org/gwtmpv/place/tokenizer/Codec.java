package org.gwtmpv.place.tokenizer;

import com.google.gwt.http.client.URL;

/** GWT version of Codec. */
public class Codec {

  /** Decodes like GWT's <code>decodeComponent</code> function. */
  public static String decodeURIComponent(final String s) {
    return URL.decodeComponent(s, false);
  }

  /** Encodes like GWT's * <code>encodeComponent</code> function. */
  public static String encodeURIComponent(final String s) {
    return URL.encodeComponent(s, false);
  }

}