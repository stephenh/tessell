package org.gwtmpv.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class StubListBox extends StubFocusWidget implements IsListBox {

  private final ArrayList<Item> items = new ArrayList<Item>();
  private int visibleItems = 1;
  private String name;

  public void select(final String item) {
    setSelectedIndex(getItemIndex(item));
  }

  public int getItemIndex(final String item) {
    for (int i = 0; i < items.size(); i++) {
      if (items.get(i).item.equals(item)) {
        return i;
      }
    }
    throw new IllegalArgumentException("No item " + item);
  }

  public List<String> getItems() {
    final List<String> i = new ArrayList<String>();
    for (final Item item : items) {
      i.add(item.item);
    }
    return i;
  }

  @Override
  public void addItem(final String item) {
    addItem(item, item);
  }

  @Override
  public void addItem(final String item, final String value) {
    insertItem(item, value, -1);
  }

  @Override
  public void clear() {
    items.clear();
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  @Override
  public String getItemText(final int index) {
    return items.get(index).item;
  }

  @Override
  public int getSelectedIndex() {
    for (int i = 0; i < items.size(); i++) {
      if (items.get(i).selected) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public String getValue(final int index) {
    return items.get(index).value;
  }

  @Override
  public int getVisibleItemCount() {
    return visibleItems;
  }

  @Override
  public void insertItem(final String item, final int index) {
    insertItem(item, item, index);
  }

  @Override
  public void insertItem(final String item, final String value, final int index) {
    final Item i = new Item();
    i.item = item;
    i.value = value;
    if (index == -1) {
      items.add(i);
    } else {
      items.add(index, i);
    }

  }

  @Override
  public boolean isItemSelected(final int index) {
    return items.get(index).selected;
  }

  @Override
  public boolean isMultipleSelect() {
    return visibleItems > 1;
  }

  @Override
  public void removeItem(final int index) {
    items.remove(index);
  }

  @Override
  public void setItemSelected(final int index, final boolean selected) {
    items.get(index).selected = selected;
    fireChange();
  }

  @Override
  public void setItemText(final int index, final String text) {
    items.get(index).item = text;
  }

  @Override
  public void setSelectedIndex(final int index) {
    for (final Item i : items) {
      i.selected = false;
    }
    if (index != -1) {
      items.get(index).selected = true;
    }
    fireChange();
  }

  @Override
  public void setValue(final int index, final String value) {
    items.get(index).value = value;
  }

  @Override
  public void setVisibleItemCount(final int visibleItems) {
    this.visibleItems = visibleItems;
  }

  @Override
  public HandlerRegistration addChangeHandler(final ChangeHandler handler) {
    return handlers.addHandler(ChangeEvent.getType(), handler);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(final String name) {
    this.name = name;
  }

  private void fireChange() {
    fireEvent(new DummyChange());
  }

  public static class Item {
    public String item;
    public String value;
    public boolean selected;
  }

  private static class DummyChange extends ChangeEvent {
  }

}
