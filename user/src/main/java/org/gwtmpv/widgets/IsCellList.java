package org.gwtmpv.widgets;

import com.google.gwt.safehtml.shared.SafeHtml;

public interface IsCellList<T> extends IsWidget, IsAbstractHasData<T> {

  SafeHtml getEmptyListMessage();

  void setEmptyListMessage(SafeHtml safeHtml);

}
