package org.gwtmpv.place;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.gwtmpv.place.events.PlaceRequestEvent;

/**
 * This class represents a 'request' for a place location. It includes the 'id' of the place as well as any parameter
 * values. It can convert from and to String tokens for use with the GWT History.
 * <p/>
 * <p/>
 * Place request tokens are formatted like this:
 * <p/>
 * <code>#id(;key=value)*</code>
 * <p/>
 * <p/>
 * There is a mandatory 'id' value, followed by 0 or more key/value pairs, separated by semi-colons (';'). A few
 * examples follow:
 * <p/>
 * <ul>
 * <li> <code>#users</code></li>
 * <li> <code>#user;name=j.blogs</code></li>
 * <li> <code>#user-email;name=j.blogs;type=home</code></li>
 * </ul>
 * 
 * @author David Peterson
 */
public class PlaceRequest {

  private final String name;
  // not quite ready for immutability
  protected final LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();

  public PlaceRequest(final String name) {
    this.name = name;
  }

  private PlaceRequest(final PlaceRequest req, final String name, final String value) {
    this.name = req.name;
    params.putAll(req.params);
    params.put(name, value);
  }

  public String getName() {
    return name;
  }

  public Set<String> getParameterNames() {
    return params.keySet();
  }

  public String getParameter(final String key, final String defaultValue) {
    final String value = params.get(key);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

  /**
   * Returns a new instance of the request with the specified parameter name and value. If a parameter with the same
   * name was previously specified, the new request contains the new value.
   * 
   * @param name
   *          The new parameter name.
   * @param value
   *          The new parameter value.
   * @return The new place request instance.
   */
  public PlaceRequest with(final String name, final String value) {
    return new PlaceRequest(this, name, value);
  }

  public PlaceRequestEvent asEvent() {
    return new PlaceRequestEvent(this);
  }

  @Override
  public boolean equals(final Object object) {
    if (!(object instanceof PlaceRequest)) {
      return false;
    }
    final PlaceRequest other = (PlaceRequest) object;
    return other.name.equals(name) && other.params.equals(params);
  }

  @Override
  public int hashCode() {
    int result = 23;
    result = (result * 37) + name.hashCode();
    result = (result * 37) + params.hashCode();
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder out = new StringBuilder();
    out.append(name);
    if (params.size() > 0) {
      out.append(";");
      // This is for debug view only, so don't do url encoding
      for (final Map.Entry<String, String> entry : params.entrySet()) {
        out.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
      }
      out.deleteCharAt(out.length() - 1);
    }
    return out.toString();
  }
}
