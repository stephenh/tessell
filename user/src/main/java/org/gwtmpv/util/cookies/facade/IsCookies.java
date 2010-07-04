package org.gwtmpv.util.cookies.facade;

/**
 * An interface to replace static calls to {@link com.google.gwt.user.client.Cookies}.
 * 
 * See {@link GwtCookies} and {@link StubCookies}.
 */
public interface IsCookies {

  String get(String name);

  void set(String name, String value);

  void remove(String name);

}
