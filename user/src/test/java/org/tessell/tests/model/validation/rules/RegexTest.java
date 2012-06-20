package org.tessell.tests.model.validation.rules;

import static org.tessell.model.properties.NewProperty.stringProperty;

import org.junit.Test;
import org.tessell.model.properties.StringProperty;
import org.tessell.model.validation.rules.Regex;

public class RegexTest extends AbstractRuleTest {

  @Test
  public void urls() {
    assertRegex(//
      Regex.URL,
      true,
      null, // null is valid
      "http://foo.com/n/5TBGvq1BAALzjEMAAAgYQgAAabxmMQA-A/",
      "http://foo.com/n/5TBGvq1BAALzjEMAAAgYQgAAabxmMQA-A/?a-b=c-d",
      "http://foo.com/n/a?campaign=foo'bar",
      "http://foo.com/n",
      "http://foo.com/n?a=b#c/",
      "http://foo.com",
      "http://foo1.com",
      "http://foo-bar.com");
    assertRegex(//
      Regex.URL,
      false,
      "http://foo",
      "foo.com/",
      "/n?a=b#c/",
      "http://foo.com/ n.html",
      "http://foo.com/q?a\"b");
  }

  @Test
  public void urlsNoProtocol() {
    assertRegex(//
      Regex.URL_NO_PROTOCOL,
      true,
      null, // null is valid
      "foo.com/n/5TBGvq1BAALzjEMAAAgYQgAAabxmMQA-A/",
      "foo.com/n",
      "foo.com/n?a=b#c",
      "foo.com",
      "foo-bar.com");
    assertRegex(//
      Regex.URL_NO_PROTOCOL,
      false,
      "foo",
      "/n?a=b#c/",
      "foo.com/ n.html",
      "foo.com/\"n.html");
  }

  @Test
  public void domains() {
    assertRegex(//
      Regex.DOMAIN,
      true,
      null, // null is valid
      "f.co",
      "foo1.com",
      "foo-bar.com",
      "foo-bar.com.com");
    assertRegex(//
      Regex.DOMAIN,
      false,
      "foo",
      "f.c",
      "http://foo.com",
      "foo.com/",
      "foo.com/asdf.html",
      "/n?a=b#c/",
      "foo bar.com");
  }

  private void assertRegex(String regex, boolean valid, String... urls) {
    final StringProperty url = stringProperty("url");
    listenTo(new Regex(url, "invalid", regex));
    for (String u : urls) {
      url.set(u);
      if (valid) {
        assertNoMessages();
      } else {
        assertMessages("invalid");
      }
    }
  }
}
