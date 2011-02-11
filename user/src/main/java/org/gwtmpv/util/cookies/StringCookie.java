package org.gwtmpv.util.cookies;

import org.gwtmpv.util.cookies.facade.IsCookies;

public class StringCookie extends AbstractCookie<String> {

  public StringCookie(final IsCookies cookies, final String name) {
    super(cookies, name);
  }

  @Override
  protected String fromString(String value) {
    return value;
  }

  @Override
  protected String toString(String value) {
    return value;
  }

}
