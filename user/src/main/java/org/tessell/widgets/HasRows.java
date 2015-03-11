package org.tessell.widgets;

import com.google.gwt.user.client.ui.IsWidget;

/** An interface for {@link RowTable}. */
public interface HasRows {

  /** Assumes {@code view} is a table and appends any of its TRs to our own table's header. */
  void addHeader(final IsWidget view);

  /** Assumes {@code view} is a table and appends any of its TRs to our own table's body. */
  void addRow(final IsWidget view);

  /** Assumes {@code view} is a table and puts its first TR into row {@code i} of our own table's body. */
  void insertRow(int i, final IsWidget view);

  /** Assumes {@code row} is a table row and returns its index */
  int indexOfRow(final IsWidget row);

  /** Assumes {@code view} is a table and puts its first TR into row {@code i} of our own table's body. */
  void replaceRow(int i, final IsWidget view);

  /** Removes row {@code i}. */
  void removeRow(int i);

  /** Removes row {@code view}. */
  boolean removeRow(final IsWidget view);

  /** @return the number of rows shown */
  int size();

  /** Removes all rows from the table. */
  void clearRows();
}
