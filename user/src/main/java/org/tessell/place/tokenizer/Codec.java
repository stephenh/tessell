package org.tessell.place.tokenizer;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Utility class for <code>application/x-www-form-urlencoded</code> encoding and decoding.
 * 
 * http://stackoverflow.com/questions/607176
 */
public class Codec {

  /** Decodes like GWT's <code>decodeComponent</code> function. */
  public static String decodeURIComponent(final String s) {
    if (s == null) {
      return null;
    }
    try {
      return URLDecoder.decode(s, "UTF-8");
    } catch (final UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  /** Encodes like GWT's <code>encodeComponent</code> function. */
  public static String encodeURIComponent(final String s) {
    try {
      return URLEncoder.encode(s, "UTF-8")//
          .replaceAll("\\+", "%20")//
          .replaceAll("\\%21", "!")//
          .replaceAll("\\%27", "'")//
          .replaceAll("\\%28", "(")//
          .replaceAll("\\%29", ")")//
          .replaceAll("\\%7E", "~");
    } catch (final UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

}