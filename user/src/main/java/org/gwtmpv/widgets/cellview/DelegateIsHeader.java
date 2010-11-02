package org.gwtmpv.widgets.cellview;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.user.cellview.client.Header;

/**
 * Allows subclasses to pretend to be an {@link IsHeader}, but
 * by delegating to a separate header.
 * 
 * This is typically useful for making high-level header classes
 * that you want to reuse in unit tests. The high-level class
 * can make a {@code newHeader} call and pass the resulting
 * {@link GwtHeader} or {@link StubHeader} to our constructor.
 */
public class DelegateIsHeader<C> implements IsHeader<C> {

  protected IsHeader<C> delegate;

  /** Allows subclasses to set the delegate outside of a super call. */
  public DelegateIsHeader() {
  }

  public DelegateIsHeader(IsHeader<C> delegate) {
    this.delegate = delegate;
  }

  @Override
  public Cell<C> getCell() {
    return delegate.getCell();
  }

  @Override
  public C getValue() {
    return delegate.getValue();
  }

  @Override
  public ValueUpdater<C> getUpdater() {
    return delegate.getUpdater();
  }

  @Override
  public Header<C> asHeader() {
    return delegate.asHeader();
  }

}
