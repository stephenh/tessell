package org.tessell.tests.place.tokenizer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.tessell.place.PlaceRequest;
import org.tessell.place.tokenizer.DefaultTokenizer;
import org.tessell.place.tokenizer.Tokenizer;

public class TokenizerTest {

  final Tokenizer f = new DefaultTokenizer();
  final Tokenizer g = new DefaultTokenizer("&", "=");

  @Test
  public void escaping() throws Exception {
    assertRoundTrip(new PlaceRequest("foo").with("a&a", "b&b"), "foo;a%26a=b%26b");
    assertRoundTrip(new PlaceRequest("foo").with("a;;a", "b;;b"), "foo;a%3B%3Ba=b%3B%3Bb");
    assertRoundTrip(new PlaceRequest("foo").with("a=a", "b=b"), "foo;a%3Da=b%3Db");
    assertRoundTrip(new PlaceRequest("foo").with("a a", "b b"), "foo;a+a=b+b");
    assertRoundTrip(new PlaceRequest("foo").with("a", "").with("b", ""), "foo;a=;b=");
    assertRoundTrip(new PlaceRequest("foo").with("city and state", "omaha, ne"), "foo;city+and+state=omaha%2C+ne");
    assertRoundTrip(new PlaceRequest("city and state").with("city", "omaha").with("state", "ne"), "city+and+state;city=omaha;state=ne");
  }

  @Test
  public void escapingWithAmp() throws Exception {
    assertRoundTripWithAmp(new PlaceRequest("foo").with("a", "b"), "foo&a=b");
    assertRoundTripWithAmp(new PlaceRequest("foo").with("a&a", "b&b").with("c", "d"), "foo&a%26a=b%26b&c=d");
    assertRoundTripWithAmp(new PlaceRequest("city and state").with("city", "omaha").with("state", "ne"), "city+and+state&city=omaha&state=ne");
  }

  private void assertRoundTrip(final PlaceRequest placeRequest, final String token) throws Exception {
    assertThat(f.toHistoryToken(placeRequest), is(token));
    assertThat(f.toPlaceRequest(token).toString(), is(placeRequest.toString()));
  }

  private void assertRoundTripWithAmp(final PlaceRequest placeRequest, final String token) throws Exception {
    assertThat(g.toHistoryToken(placeRequest), is(token));
    assertThat(g.toPlaceRequest(token).toString(), is(placeRequest.toString()));
  }

}
