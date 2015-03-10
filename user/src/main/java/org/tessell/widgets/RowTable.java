package org.tessell.widgets;

import java.util.ArrayList;
import java.util.Iterator;

import org.tessell.gwt.dom.client.GwtElement;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.IsStyle;
import org.tessell.util.ListDiff.ListLike;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;

/**
 * A table that can add entire rows at a time.
 *
 * Most GWT tables are cell-focused. E.g. <code>setWidget(0, 0, widget)</code>.
 *
 * We want a table where we can add an entire row at a time, and it was easiest to just roll our own extending from
 * {@link Panel}.
 */
public class RowTable extends Panel implements IsRowTable {
  private final Element table;
  private final Element head;
  private final Element body;
  // Mixed headers+rows for our iterator method
  private final WidgetCollection widgets = new WidgetCollection(this);
  // Just rows for the replaceRow index
  private final ArrayList<Widget> rows = new ArrayList<Widget>();

  public RowTable() {
    // copy/paste from HTMLTable
    table = DOM.createTable();
    head = DOM.createTHead();
    body = DOM.createTBody();
    table.appendChild(head);
    table.appendChild(body);
    setElement(table);
  }

  @Override
  public void addHeader(final IsWidget isWidget) {
    addHeader(isWidget.asWidget());
  }

  /** Assumes {@code widget} is a table and appends any of its TRs to our own table's header. */
  public void addHeader(final Widget widget) {
    addTo(widget, head);
  }

  @Override
  public void add(final IsWidget isWidget) {
    addRow(isWidget); // assume they meant row, e.g. for using in bind logic
  }

  @Override
  public void addRow(final IsWidget isWidget) {
    addRow(isWidget.asWidget());
  }

  /** Assumes {@code row} is a table row and returns its index */
  public int indexOfRow(final Widget row) {
    return rows.indexOf(row);
  }

  /** Assumes {@code widget} is a table and appends any of its TRs to our own table's body. */
  public void addRow(final Widget widget) {
    addTo(widget, body);
    rows.add(widget);
  }

  @Override
  public void insertRow(final int index, final IsWidget isWidget) {
    insertRow(index, isWidget.asWidget());
  }

  @Override
  public ListLike<org.tessell.gwt.user.client.ui.IsWidget> getRowsPanel() {
    return new ListLikeAdapter();
  }

  /** Assumes {@code widget} is a table and puts its first TR into row {@code i} of our own table's body. */
  public void insertRow(final int i, final Widget newWidget) {
    final Element newTr = findTr(newWidget.getElement());
    assert newTr != null : "newWidget did not contain a TR";

    newWidget.removeFromParent();
    // logical
    widgets.add(newWidget);
    rows.add(i, newWidget);
    // physical
    if (i == 0) {
      body.insertFirst(newTr);
    } else {
      body.insertAfter(newTr, findBodyTr(i - 1));
    }
    // adopt
    adopt(newWidget);
  }

  /** Assumes {@code widget} is a table and puts its first TR into row {@code i} of our own table's body. */
  public void replaceRow(final int i, final Widget newWidget) {
    // This will logically and physically remove the old widget
    removeRow(i);
    insertRow(i, newWidget);
  }

  @Override
  public void removeRow(final int i) {
    remove(rows.get(i));
  }

  @Override
  public boolean removeRow(final IsWidget view) {
    return remove(view.asWidget());
  }

  @Override
  public boolean remove(final Widget child) {
    if (widgets.contains(child)) {
      // orphan
      orphan(child);
      // physical
      final int at = rows.indexOf(child);
      if (at > -1) {
        final Widget oldTable = rows.remove(at);
        final Element tr = findBodyTr(at);
        body.removeChild(tr);
        // put the oldTr back where we got it from
        findTBody(oldTable.getElement()).appendChild(tr);
      }
      // logical
      widgets.remove(child);
      return true;
    }
    return false;
  }

  @Override
  public Iterator<Widget> iterator() {
    return widgets.iterator();
  }

  private void addTo(final Widget newWidget, final Element element) {
    Element newTr = findTr(newWidget.getElement());
    assert newTr != null : "newWidget did not contain a TR";
    newWidget.removeFromParent();
    // Logical attach
    widgets.add(newWidget);
    // Physical attach (all TRs)
    element.appendChild(newTr);
    // Adopt
    adopt(newWidget);
  }

  @Override
  public void replaceRow(final int i, final IsWidget isWidget) {
    replaceRow(i, isWidget.asWidget());
  }

  @Override
  public int size() {
    return rows.size();
  }

  @Override
  public void clearRows() {
    while (rows.size() > 0) {
      removeRow(0);
    }
  }

  @Override
  public Widget asWidget() {
    return this;
  }

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

  @Override
  public IsElement getIsElement() {
    return new GwtElement(getElement());
  }

  @Override
  public org.tessell.gwt.user.client.ui.IsWidget getIsParent() {
    return (org.tessell.gwt.user.client.ui.IsWidget) getParent();
  }

  private com.google.gwt.dom.client.Element findBodyTr(final int i) {
    int j = 0;
    Element tr = body.getFirstChildElement();
    while (tr != null && j < i) {
      tr = tr.getNextSiblingElement();
      j++;
    }
    return tr;
  }

  private Element findTr(Element tableElement) {
    Element current = tableElement;
    while (current != null && !current.getTagName().equalsIgnoreCase("TR")) {
      current = current.getFirstChildElement();
    }
    return current;
  }

  private Element findTBody(Element tableElement) {
    Element current = tableElement;
    while (current != null && !current.getTagName().equalsIgnoreCase("TBODY")) {
      current = current.getFirstChildElement();
    }
    if (current == null) {
      current = tableElement;
    }
    return current;
  }

  /** Facilitates binding ListProperties to our rows. */
  private final class ListLikeAdapter implements ListLike<org.tessell.gwt.user.client.ui.IsWidget> {
    @Override
    public org.tessell.gwt.user.client.ui.IsWidget remove(int index) {
      Widget row = rows.get(index);
      removeRow(index);
      return (org.tessell.gwt.user.client.ui.IsWidget) row;
    }

    @Override
    public void add(int index, org.tessell.gwt.user.client.ui.IsWidget a) {
      insertRow(index, a);
    }
  }

}
