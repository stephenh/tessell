package org.tessell.widgets;

public abstract class StubPanel extends StubWidget implements IsPanel {

  @Override
  protected IsWidget findInChildren(String id) {
    return findInChildren(iteratorIsWidgets(), id);
  }

}
