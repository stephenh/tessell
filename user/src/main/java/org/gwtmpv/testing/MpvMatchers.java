package org.gwtmpv.testing;

import static org.gwtmpv.util.StringUtils.join;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gwtmpv.bus.StubEventBus;
import org.gwtmpv.place.events.PlaceRequestEvent;
import org.gwtmpv.widgets.HasCss;
import org.gwtmpv.widgets.HasStubCss;
import org.gwtmpv.widgets.IsTextList;
import org.gwtmpv.widgets.StubStyle;
import org.gwtmpv.widgets.StubTextList;
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

  /** A matcher to assert no validation errors. */
  public static Matcher<IsTextList> hasNoErrors() {
    return new TypeSafeMatcher<IsTextList>() {
      @Override
      protected boolean matchesSafely(IsTextList item) {
        return ((StubTextList) item).getList().size() == 0;
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("has no errors");
      }

      @Override
      protected void describeMismatchSafely(IsTextList item, Description mismatchDescription) {
        mismatchDescription.appendValue(item);
        mismatchDescription.appendText(" has errors ");
        mismatchDescription.appendValueList("", ", ", "", ((StubTextList) item).getList());
      }
    };
  }

  /** A matcher to assert validation errors. */
  public static Matcher<IsTextList> hasErrors(final String... errors) {
    return new TypeSafeMatcher<IsTextList>() {
      @Override
      protected boolean matchesSafely(IsTextList item) {
        String expected = join(errors, ", ");
        String actual = join(((StubTextList) item).getList(), ", ");
        return expected.equals(actual);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("errors ");
        description.appendValueList("<", ", ", ">", errors);
      }

      @Override
      protected void describeMismatchSafely(IsTextList item, Description mismatchDescription) {
        mismatchDescription.appendText("errors ");
        mismatchDescription.appendValueList("<", ", ", ">", ((StubTextList) item).getList());
      }
    };
  }

  /** A matcher to assert place requests on the event bus. */
  public static Matcher<StubEventBus> hasPlaceRequests(final String... places) {
    return new TypeSafeMatcher<StubEventBus>() {
      @Override
      protected boolean matchesSafely(StubEventBus bus) {
        return Arrays.asList(places).equals(getRequests(bus));
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("has places ");
        description.appendValueList("[", ", ", "]", Arrays.asList(places));
      }

      @Override
      protected void describeMismatchSafely(StubEventBus bus, Description mismatchDescription) {
        mismatchDescription.appendText("places are ");
        mismatchDescription.appendValueList("[", ", ", "]", getRequests(bus));
      }

      private List<String> getRequests(StubEventBus bus) {
        List<String> requests = new ArrayList<String>();
        for (PlaceRequestEvent e : bus.getEvents(PlaceRequestEvent.class)) {
          requests.add(e.getRequest().toString());
        }
        return requests;
      }
    };
  }
}
