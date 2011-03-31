package org.gwtmpv.widgets.form.lines;

import java.util.ArrayList;

import org.gwtmpv.model.dsl.ListBoxAdaptor;
import org.gwtmpv.model.dsl.ListBoxIdentityAdaptor;
import org.gwtmpv.model.properties.Property;

public class NewFormLine {

  public static TextBoxFormLine newTextBoxFormLine(Property<String> p) {
    return new TextBoxFormLine(p);
  }

  public static TextBoxFormLine newTextBoxFormLine(String label, Property<String> p) {
    TextBoxFormLine t = new TextBoxFormLine(p);
    t.setLabel(label);
    return t;
  }

  public static <P> ListBoxFormLine<P, P> newListBoxFormLine(Property<P> p, ArrayList<P> options) {
    return new ListBoxFormLine<P, P>(p, options, new ListBoxIdentityAdaptor<P>());
  }

  public static <P> ListBoxFormLine<P, P> newListBoxFormLine(String label, Property<P> p, ArrayList<P> options) {
    ListBoxFormLine<P, P> l = new ListBoxFormLine<P, P>(p, options, new ListBoxIdentityAdaptor<P>());
    l.setLabel(label);
    return l;
  }

  public static <P, O> ListBoxFormLine<P, O> newListBoxFormLine(Property<P> p, ArrayList<O> options, ListBoxAdaptor<P, O> adaptor) {
    return new ListBoxFormLine<P, O>(p, options, adaptor);
  }

  public static <P, O> ListBoxFormLine<P, O> newListBoxFormLine(String label, Property<P> p, ArrayList<O> options, ListBoxAdaptor<P, O> adaptor) {
    ListBoxFormLine<P, O> l = new ListBoxFormLine<P, O>(p, options, adaptor);
    l.setLabel(label);
    return l;
  }

}
