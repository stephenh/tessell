package org.gwtmpv.util.cookies;

import org.gwtmpv.util.cookies.facade.IsCookies;

public abstract class AbstractCookie<T> implements Cookie<T> {

  protected final IsCookies cookies;
  protected final String name;

  public AbstractCookie(final IsCookies cookies, final String name) {
    this.cookies = cookies;
    this.name = name;
  }

  // TODO: Add a hashing/something approach for security
  // setCookie appends its value, so instead of null, pass empty string

  @Override
  public void unset() {
    cookies.remove(name);
  }
}
