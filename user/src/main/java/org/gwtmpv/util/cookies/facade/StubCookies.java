package org.gwtmpv.util.cookies.facade;

import java.util.LinkedHashMap;
import java.util.Map;

public class StubCookies implements IsCookies {

  private final Map<String, String> values = new LinkedHashMap<String, String>();

  @Override
  public void remove(final String name) {
    values.remove(name);
  }

  @Override
  public void set(final String name, final String value) {
    values.put(name, value);
  }

  @Override
  public String get(final String name) {
    return values.get(name);
  }

}
