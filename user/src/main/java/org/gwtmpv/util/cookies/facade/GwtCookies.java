package org.gwtmpv.util.cookies.facade;

import com.google.gwt.user.client.Cookies;

public class GwtCookies implements IsCookies {

  @Override
  public void remove(final String name) {
    Cookies.removeCookie(name);
  }

  @Override
  public void set(final String name, final String value) {
    Cookies.setCookie(name, value, null, null, "/", false);
  }

  @Override
  public String get(final String name) {
    return Cookies.getCookie(name);
  }

}
