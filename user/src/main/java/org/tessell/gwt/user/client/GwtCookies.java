package org.tessell.gwt.user.client;

import java.util.Date;

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
  public void set(String name, String value, String domain, Date expires, boolean secure) {
    Cookies.setCookie(name, value, expires, domain, "/", secure);
  }

  @Override
  public String get(final String name) {
    return Cookies.getCookie(name);
  }

}
