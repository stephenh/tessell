package org.tessell.testing;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.*;
import org.tessell.bus.StubEventBus;
import org.tessell.gwt.user.client.ui.HasCss;
import org.tessell.place.events.PlaceLoadedEvent;
import org.tessell.place.events.PlaceRequestEvent;
import org.tessell.widgets.IsTextList;
import org.tessell.widgets.StubTextList;

public class TessellMatchers {

  /** A matcher to assert display != none. */
  public static Matcher<HasCss> shown() {
    return hasCssValue("display", "showing because display", is(not("none")));
  }

  /** A matcher to assert display == none. */
  public static Matcher<HasCss> hidden() {
    return hasCssValue("display", "hidden because display", is("none"));
  }

  /** A matcher to assert visible == hidden. */
  public static Matcher<HasCss> visible() {
    return hasCssValue("visibility", "visible because visibility", is(not("hidden")));
  }

  /** A matcher to assert visible == visible|unset. */
  public static Matcher<HasCss> invisible() {
    return hasCssValue("visibility", "invisible because visibility", is("hidden"));
  }

  /** A matcher to assert an arbitrary CSS property. */
  public static Matcher<HasCss> hasCssValue(final String name, final String value) {
    return hasCssValue(name, name, is(value));
  }

  /** A matcher to assert an arbitrary CSS property. */
  public static Matcher<HasCss> hasCssValue(final String name, final String description, final Matcher<String> valueMatcher) {
    return new FeatureMatcher<HasCss, String>(valueMatcher, description, name) {
      protected String featureValueOf(HasCss actual) {
        return actual.getStyle().getProperty(name);
      }
    };
  }

  /** A matcher to assert a class name being present. */
  public static Matcher<HasCss> hasStyle(final String className) {
    return new FeatureMatcher<HasCss, List<String>>(hasItem(className), "style is", "style") {
      protected List<String> featureValueOf(HasCss actual) {
        return Arrays.asList(actual.getStyleName().split(" "));
      }
    };
  }

  /** A matcher to assert no CSS class names are presenter. */
  public static Matcher<HasCss> hasNoStyles() {
    return new FeatureMatcher<HasCss, List<String>>(empty(), "style is", "style was") {
      protected List<String> featureValueOf(HasCss actual) {
        if (actual.getStyleName().equals("")) {
          return new ArrayList<String>();
        }
        return Arrays.asList(actual.getStyleName().split(" "));
      }
    };
  }

  /** A matcher to assert no validation errors. */
  public static Matcher<IsTextList> hasNoErrors() {
    return new FeatureMatcher<IsTextList, List<String>>(is(empty()), "errors", "errors had") {
      protected List<String> featureValueOf(IsTextList actual) {
        return ((StubTextList) actual).getList();
      }
    };
  }

  /** A matcher to assert validation errors. */
  public static Matcher<IsTextList> hasErrors(final String... errors) {
    return new FeatureMatcher<IsTextList, List<String>>(contains(errors), "errors is an", "errors had") {
      protected List<String> featureValueOf(IsTextList actual) {
        return ((StubTextList) actual).getList();
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

  /** A matcher to assert place loaded events on the event bus. */
  public static Matcher<StubEventBus> hasPlaceLoadedEvents(final String... places) {
    return new TypeSafeMatcher<StubEventBus>() {
      @Override
      protected boolean matchesSafely(final StubEventBus bus) {
        return Arrays.asList(places).equals(getPlaceLoadedEvents(bus));
      }

      @Override
      public void describeTo(final Description description) {
        description.appendText("has places ");
        description.appendValueList("[", ", ", "]", Arrays.asList(places));
      }

      @Override
      protected void describeMismatchSafely(final StubEventBus bus, final Description mismatchDescription) {
        mismatchDescription.appendText("places are ");
        mismatchDescription.appendValueList("[", ", ", "]", getPlaceLoadedEvents(bus));
      }

      private List<String> getPlaceLoadedEvents(final StubEventBus bus) {
        final List<String> events = new ArrayList<String>();
        for (final PlaceLoadedEvent e : bus.getEvents(PlaceLoadedEvent.class)) {
          events.add(e.getPlace().getName());
        }
        return events;
      }
    };
  }

  /** A matcher to assert based on toString. */
  public static Matcher<Object> asString(final String expected) {
    return new BaseMatcher<Object>() {
      @Override
      public boolean matches(Object item) {
        return item != null && item.toString() != null && item.toString().equals(expected);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText(expected);
      }
    };
  }
}
