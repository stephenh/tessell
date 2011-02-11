package org.gwtmpv.util.cookies;

import org.gwtmpv.util.cookies.facade.IsCookies;

public class IntCookie extends AbstractCookie<Integer> {

  public IntCookie(final IsCookies cookies, final String name) {
    super(cookies, name);
  }

  @Override
  public Integer fromString(String value) {
    try {
      return new Integer(value);
    } catch (final Exception e) {
      return null;
    }
  }

  @Override
  public String toString(Integer value) {
    return value == null ? "" : value.toString();
  }

}
