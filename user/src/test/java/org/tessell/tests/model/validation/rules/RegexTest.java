package org.tessell.tests.model.validation.rules;

import static org.tessell.model.properties.NewProperty.stringProperty;

import org.junit.Test;
import org.tessell.model.properties.StringProperty;
import org.tessell.model.validation.rules.Regex;

public class RegexTest extends AbstractRuleTest {

  @Test
  public void urls() {
    final StringProperty url = stringProperty("url");
    listenTo(new Regex(url, "invalid", Regex.URL));
    url.set("http://foo.com/n/5TBGvq1BAALzjEMAAAgYQgAAabxmMQA-A/");
    assertNoMessages();

    url.set("http://foo.com/n/5TBGvq1BAALzjEMAAAgYQgAAabxmMQA-A/?a-b=c-d");
    assertNoMessages();

    url.set("http://foo.com/n/a?campaign=foo'bar");
    assertNoMessages();

    url.set("http://foo.com/n");
    assertNoMessages();

    url.set("http://foo.com/n?a=b#c/");
    assertNoMessages();

    url.set("http://foo.com");
    assertNoMessages();

    url.set("http://foo1.com");
    assertNoMessages();

    url.set("http://foo-bar.com");
    assertNoMessages();

    url.set("http://foo");
    assertMessages("invalid");

    url.set("foo.com/");
    assertMessages("invalid");

    url.set("/n?a=b#c/");
    assertMessages("invalid");
  }

  @Test
  public void urlsNoProtocol() {
    final StringProperty url = stringProperty("url");
    listenTo(new Regex(url, "invalid", Regex.URL_NO_PROTOCOL));

    url.set("foo.com/n/5TBGvq1BAALzjEMAAAgYQgAAabxmMQA-A/");
    assertNoMessages();

    url.set("foo.com/n");
    assertNoMessages();

    url.set("foo.com/n?a=b#c");
    assertNoMessages();

    url.set("foo.com");
    assertNoMessages();

    url.set("foo-bar.com");
    assertNoMessages();

    url.set("foo");
    assertMessages("invalid");

    url.set("/n?a=b#c/");
    assertMessages("invalid");
  }

  @Test
  public void nullIsValid() {
    final StringProperty url = stringProperty("url");
    listenTo(new Regex(url, "invalid", Regex.URL_NO_PROTOCOL));

    url.set(null);
    assertNoMessages();
  }

}
