package org.tessell.model.dsl;

import java.util.Arrays;

import org.tessell.gwt.user.client.ui.IsListBox;
import org.tessell.model.properties.EnumProperty;
import org.tessell.util.Inflector;

/** Binds {@link EnumProperty}s to widgets. */
public class EnumPropertyBinder<E extends Enum<E>> extends PropertyBinder<E> {

  private final EnumProperty<E> ep;

  EnumPropertyBinder(final Binder b, final EnumProperty<E> ep) {
    super(b, ep);
    this.ep = ep;
  }

  public void to(final IsListBox source, final E[] values) {
    int i = 0;
    for (E value : values) {
      final String display;
      if (value.name().equals(value.toString())) {
        display = Inflector.humanize(value.name());
      } else {
        display = value.toString();
      }
      source.addItem(display, Integer.toString(i++));
    }
    if (ep.get() == null) {
      // TODO don't currently support an empty option
      ep.set(values[0]);
    }
    source.setSelectedIndex(Arrays.asList(values).indexOf(ep.get()));
    b.add(source.addChangeHandler(e -> {
      int j = source.getSelectedIndex();
      if (j == -1) {
        ep.set(null);
      } else {
        ep.set(values[j]);
      }
    }));
    ep.addPropertyChangedHandler(e -> {
      source.setSelectedIndex(Arrays.asList(values).indexOf(ep.get()));
    });
  }

}
