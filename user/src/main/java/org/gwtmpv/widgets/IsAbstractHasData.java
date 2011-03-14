package org.gwtmpv.widgets;

import java.util.List;

import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.HasKeyProvider;

/** Methods common to subclasses of {@link AbstractHasData} which we want to expose for their {@code IsXxx} interfaces. */
public interface IsAbstractHasData<T> extends HasData<T>, HasKeyProvider<T>, Focusable, HasKeyboardPagingPolicy {

  int getPageSize();

  int getPageStart();

  void setPageStart(int pageStart);

  void setRowData(List<? extends T> values);

  void setPageSize(int pageSize);

  void redraw();

  // changes HasData's Iterable<T> to List<T>
  @Override
  List<T> getVisibleItems();

}
