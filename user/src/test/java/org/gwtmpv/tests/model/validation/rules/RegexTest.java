package org.gwtmpv.tests.model.validation.rules;

import static org.gwtmpv.model.properties.NewProperty.stringProperty;

import org.gwtmpv.model.properties.StringProperty;
import org.gwtmpv.model.validation.rules.Regex;
import org.junit.Test;

public class RegexTest extends AbstractRuleTest {

  @Test
  public void urls() {
    final StringProperty url = stringProperty("url");
    listenTo(new Regex(url, "invalid", Regex.URL));
    url.set("http://foo.com/n/5TBGvq1BAALzjEMAAAgYQgAAabxmMQA-A/");
    assertMessages();

    url.set("http://foo.com/n/5TBGvq1BAALzjEMAAAgYQgAAabxmMQA-A/?a-b=c-d");
    assertMessages();

    url.set("http://foo.com/n/a?campaign=foo'bar");
    assertMessages();
  }

  @Test
  public void urlsNoProtocol() {
    final StringProperty url = stringProperty("url");
    listenTo(new Regex(url, "invalid", Regex.URL_NO_PROTOCOL));
    url.set("foo.com/n/5TBGvq1BAALzjEMAAAgYQgAAabxmMQA-A/");
    assertMessages();
  }

}