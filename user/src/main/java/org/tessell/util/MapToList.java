package org.tessell.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MapToList<K, V> extends LinkedHashMap<K, List<V>> {

  private static final long serialVersionUID = -1;

  @SuppressWarnings("unchecked")
  @Override
  public List<V> get(Object key) {
    List<V> values = super.get(key);
    if (values == null) {
      values = (List<V>) new ArrayList<Object>();
      put((K) key, values);
    }
    return values;
  }

  public void add(Object key) {
    get(key);
  }

  public void add(Object key, V value) {
    get(key).add(value);
  }

  public void remove(Object key, V value) {
    get(key).remove(value);
  }

  public V removeOne(Object key) {
    List<V> values = super.get(key);
    if (values == null) {
      return null;
    }
    V value = values.remove(0);
    if (values.size() == 0) {
      remove(key);
    }
    return value;
  }

}
