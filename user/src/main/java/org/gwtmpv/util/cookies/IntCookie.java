package org.gwtmpv.util.cookies;

import org.gwtmpv.util.cookies.facade.IsCookies;

public class IntCookie extends AbstractCookie<Integer> {

  public IntCookie(final IsCookies cookies, final String name) {
    super(cookies, name);
  }

  @Override
  public Integer get() {
    try {
      return new Integer(cookies.get(name));
    } catch (final Exception e) {
      return null;
    }
  }

  @Override
  public void set(final Integer value) {
    cookies.set(name, value == null ? "" : value.toString());
  }

}
