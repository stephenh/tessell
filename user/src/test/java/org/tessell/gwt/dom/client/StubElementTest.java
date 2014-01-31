package org.tessell.gwt.dom.client;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class StubElementTest {

  private final StubElement s = new StubElement();

  @Test
  public void innerTextDefaultsToEmpty() {
    assertThat(s.getInnerText(), is(""));
  }

  @Test
  public void innerTextConvertsNull() {
    s.setInnerText(null);
    assertThat(s.getInnerText(), is(""));
  }

  @Test
  public void innerHtmlDefaultsToEmpty() {
    assertThat(s.getInnerHTML(), is(""));
  }

  @Test
  public void innerHtmlConvertsNull() {
    s.setInnerHTML(null);
    assertThat(s.getInnerHTML(), is(""));
  }

  @Test
  public void innerTextClearsInnerHtml() {
    s.setInnerHTML("foo");
    s.setInnerText("bar");
    assertThat(s.getInnerHTML(), is(""));
  }

  @Test
  public void innerHtmlClearsInnerText() {
    s.setInnerText("bar");
    s.setInnerHTML("foo");
    assertThat(s.getInnerText(), is(""));
  }

  @Test
  public void setStyleName() {
    s.setStyleName("one two");
    assertThat(s.getStyleNames().toString(), is("[one, two]"));
  }

}
