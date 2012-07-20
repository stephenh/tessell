package org.tessell.gwt.user.client;

import java.util.Date;

/**
 * An interface to replace static calls to {@link com.google.gwt.user.client.Cookies}.
 * 
 * See {@link GwtCookies} and {@link StubCookies}.
 */
public interface IsCookies {

  String get(String name);

  void set(String name, String value);

  void set(String name, String value, String domain, Date expires, boolean secure);

  void remove(String name);

}
