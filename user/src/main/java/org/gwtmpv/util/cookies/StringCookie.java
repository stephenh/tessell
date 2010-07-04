package org.gwtmpv.util.cookies;

import org.gwtmpv.util.cookies.facade.IsCookies;

public class StringCookie extends AbstractCookie<String> {

  public StringCookie(final IsCookies cookies, final String name) {
    super(cookies, name);
  }

  @Override
  public String get() {
    return cookies.get(name);
  }

  @Override
  public void set(final String value) {
    cookies.set(name, value == null ? "" : value.toString());
  }

}
