package org.tessell.examples.client.app;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.tessell.examples.client.views.StubDragAndDropExampleView;

public class DragAndDropExamplePresenterTest extends AbstractPresenterTest {

  private final DragAndDropExamplePresenter p = bind(new DragAndDropExamplePresenter());
  private final StubDragAndDropExampleView v = (StubDragAndDropExampleView) p.getView();

  @Test
  public void testDragAndDropAnchors() {
    v.a1().dragStart();
    v.a3().drop();
    assertThat(v.rootAnchors().getIsWidget(0), is(v.a2()));
  }

  @Test
  public void testDragAndDropLabels() {
    v.l1().dragStart();
    v.l3().drop();
    assertThat(v.rootLabels().getIsWidget(0), is(v.l2()));
  }

  @Test
  public void testCannotDropAnchorOnToALabel() {
    v.l1().dragStart();
    v.a1().drop();
    assertThat(v.rootAnchors().getIsWidget(0), is(v.a1()));
    assertThat(v.rootLabels().getIsWidget(0), is(v.l1()));
  }

}
