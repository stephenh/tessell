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

  private static String mark = "-_.!~*'()#;,/?:@=+$";

  /**
   * Encodes like encodeURI.
   */
  public static String encodeURI(final String s) {
    StringBuilder uri = new StringBuilder();
    char[] chars = s.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      char c = chars[i];
      if ((c >= '0' && c <= '9') //
        || (c >= 'a' && c <= 'z')
        || (c >= 'A' && c <= 'Z')
        || mark.indexOf(c) != -1) {
        uri.append(c);
      } else {
        uri.append("%");
        uri.append(Integer.toHexString(c));
      }
    }
    return uri.toString();
  }

  /**
   * Decodes like GWT's <code>decodePathSegment</code> function.
   */
  public static String decodePathSegment(final String s) {
    if (s == null) {
      return null;
    }
    try {
      return URLDecoder.decode(s, "UTF-8");
    } catch (final UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Encodes like GWT's * <code>encodePathSegment</code> function.
   *
   * Note: spaces are encoded as %20.
   */
  public static String encodePathSegment(final String s) {
    try {
      return URLEncoder.encode(s, "UTF-8")//
        .replaceAll("\\+", "%20")
        .replaceAll("\\%21", "!")
        .replaceAll("\\%27", "'")
        .replaceAll("\\%28", "(")
        .replaceAll("\\%29", ")")
        .replaceAll("\\%7E", "~");
    } catch (final UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Decodes like GWT's <code>decodeQueryString</code> function.
   *
   * Note: '+' is interpreted as space.
   */
  public static String decodeQueryString(final String s) {
    return decodePathSegment(s.replace("+", "%20"));
  }

  /**
   * Encodes like GWT's * <code>encodeQueryString</code> function.
   *
   * Note: spaces are encoded as '+'.
   */
  public static String encodeQueryString(final String s) {
    return encodePathSegment(s).replace("%20", "+");
  }

}
