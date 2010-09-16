package org.gwtmpv.testing;

import org.gwtmpv.widgets.HasCss;
import org.gwtmpv.widgets.HasStubCss;
import org.gwtmpv.widgets.StubStyle;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class MpvMatchers {

  /** A matcher to assert display != none. */
  public static Matcher<HasCss> shown() {
    return new TypeSafeMatcher<HasCss>() {
      @Override
      protected boolean matchesSafely(HasCss item) {
        return getDisplay(item) == null || getDisplay(item).equals("block") || getDisplay(item).equals("inline");
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("shown");
      }

      @Override
      protected void describeMismatchSafely(HasCss item, Description mismatchDescription) {
        mismatchDescription.appendValue(item);
        mismatchDescription.appendText(" display is ");
        mismatchDescription.appendValue(getDisplay(item));
      }

      private String getDisplay(HasCss item) {
        return ((StubStyle) item.getStyle()).getStyle().get("display");
      }
    };
  }

  /** A matcher to assert display == none. */
  public static Matcher<HasCss> hidden() {
    return new TypeSafeMatcher<HasCss>() {
      @Override
      protected boolean matchesSafely(HasCss item) {
        return "none".equals(getDisplay(item));
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("hidden");
      }

      @Override
      protected void describeMismatchSafely(HasCss item, Description mismatchDescription) {
        mismatchDescription.appendValue(item);
        mismatchDescription.appendText(" display is ");
        mismatchDescription.appendValue(getDisplay(item));
      }

      private String getDisplay(HasCss item) {
        return ((StubStyle) item.getStyle()).getStyle().get("display");
      }
    };
  }

  /** A matcher to assert an arbitrary CSS property. */
  public static Matcher<HasCss> hasStyle(final String name, final String value) {
    return new TypeSafeMatcher<HasCss>() {
      @Override
      protected boolean matchesSafely(HasCss item) {
        return value.equals(get(item));
      }

      @Override
      public void describeTo(Description description) {
        description.appendText(name + " is ").appendValue(value);
      }

      @Override
      protected void describeMismatchSafely(HasCss item, Description mismatchDescription) {
        mismatchDescription.appendValue(item);
        mismatchDescription.appendText(" " + name + " is ");
        mismatchDescription.appendValue(get(item));
      }

      private String get(HasCss item) {
        return ((StubStyle) item.getStyle()).getStyle().get(name);
      }
    };
  }

  /** A matcher to assert a class name being present. */
  public static Matcher<HasCss> hasStyle(final String className) {
    return new TypeSafeMatcher<HasCss>() {
      @Override
      protected boolean matchesSafely(HasCss item) {
        return ((HasStubCss) item).getStyleNames().contains(className);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("style ").appendValue(className);
      }

      @Override
      protected void describeMismatchSafely(HasCss item, Description mismatchDescription) {
        mismatchDescription.appendValue(item);
        mismatchDescription.appendText(" does not have ");
        mismatchDescription.appendValue(className);
      }
    };
  }

}
