package org.tessell.widgets.cellview;

import org.tessell.widgets.IsAbstractHasData;
import org.tessell.widgets.IsWidget;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.safehtml.shared.SafeHtml;

public interface IsCellList<T> extends IsWidget, IsAbstractHasData<T> {

  SafeHtml getEmptyListMessage();

  void setEmptyListMessage(SafeHtml safeHtml);

  void setValueUpdater(ValueUpdater<T> valueUpdater);

}
