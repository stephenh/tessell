---
layout: default
title: Tests
---

Tests
=====

Given what `gwt-mpv` has given us, let's look at a `ClientPresenterTest`:

    public class ClientPresenterTest {
      // these 3 lines are app-specific
      private final GClientDto dto = new GClientDto();
      private final GClientModel client = new GClientModel(registry.getRepository(), dto);
      private final ClientPresenter p = new ClientPresenter(registry, client);
      private final StubClientView v = (StubClientView) p.getView();

      @Test
      public void fillsInFieldsOnBind() {
        dto.name = "foo";
        p.bind();
        assertThat(v.name.getText(), is("foo"));
        assertThat(v.description.getText(), is("47 left"));
      }

      @Test
      public void keyUpChangesNameLeft() {
        dto.name = "foo";
        p.bind();
        assertThat(v.name.getText(), is("foo"));
        assertThat(v.description.getText(), is("47 left"));

        v.name.press('b');
        assertThat(v.description.getText(), is("46 left"));
      }

    }
{: class=brush:java}


