package org.tessell.gwt.user.client;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.tessell.gwt.http.client.IsUrlBuilder;
import org.tessell.gwt.user.client.StubWindow;

public class StubWindowTest {

  final StubWindow w = new StubWindow();

  @Test
  public void testSettingLocation() {
    w.setLocation("http://server/path?a=b#place");
    IsUrlBuilder u = w.createUrlBuilder();
    assertThat(u.buildString(), is("http://server:80/path?a=b#place"));
  }

}
