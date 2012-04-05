package org.tessell.place.tokenizer;

import com.google.gwt.http.client.URL;

/** GWT version of Codec. */
public class Codec {

  /**
   * Encodes like encodeURI.
   */
  public static String encodeURI(final String s) {
    return URL.encode(s);
  }

  /**
   * Decodes like GWT's <code>decodePathSegment</code> function.
   */
  public static String decodePathSegment(final String s) {
    return URL.decodePathSegment(s);
  }

  /**
   * Encodes like GWT's * <code>encodePathSegment</code> function.
   *
   * Note: spaces are encoded as %20.
   */
  public static String encodePathSegment(final String s) {
    return URL.encodePathSegment(s);
  }
  
  /**
   * Decodes like GWT's <code>decodeQueryString</code> function.
   *
   * Note: '+' is interpreted as space.
   */
  public static String decodeQueryString(final String s) {
    return URL.decodeQueryString(s);
  }

  /**
   * Encodes like GWT's * <code>encodeQueryString</code> function.
   *
   * Note: spaces are encoded as '+'.
   */
  public static String encodeQueryString(final String s) {
    return URL.encodeQueryString(s);
  }

}
