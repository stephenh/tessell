
<a href="https://travis-ci.org/stephenh/tessell"><img src="https://api.travis-ci.org/stephenh/tessell.svg"></a>

[![Join the chat at https://gitter.im/stephenh/tessell](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/stephenh/tessell?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

See [tessell.org](http://www.tessell.org).

Eclipse Setup
-------------

After checking Tessell out of git, there are two main projects, `tessell-user` and `tessell-dev`.

`tessell-user` uses annotation processing to generate some event classes, which is unfortunately kind of janky to setup in Eclipse.

1. Install the [Gradle Eclipse plugin](https://github.com/spring-projects/eclipse-integration-gradle)
  * This is not strictly required, but the checked-in `.classpath`/`.factorypath` files assume this setup
2. Go to Window / Preferences / Java / Build Path / Classpath Variables
  * Add `GRADLE_REPO` has `/yourHomeDir/.gradle/caches/modules-2`
3. Import `tessell-user` and `tessell-dev` into Eclipse
  * Gradle should download all the dependencies and put them onto the Gradle classpath container
  * However, Eclipse needs to be "kicked" to see the annotation processor jar is now available
4. Close `tessell-user`
5. Open `tessell-user`
6. Clean `tessell-user`
7. Hopefully you have no build errors

Todo
----

* Add @Place annotation
  * Take name of presenter
  * Any constructor parameters--how to denote application-level vs. request-level?
* Figure out cross-presenter transitions
  * Event bus doesn't make sense--these aren't global things
  * Have Slot listen to presenter events?
* Support 2nd generation Presenter interfaces via event hook hints in `ui.xml` file
* form disable on ServerCall
* indicator on ServerCall
* Pre-fill the handler (optional);
* doLogin.call(user, pass);
* HasDispatchAsync to ServerCall cstr
* Out-of-the-box local storage integration (probably via changes to dtonator, use AutoBeans)
* PropertyGroup copies are not deep

Notes
-----

* ResourcesGenerator assumes a global namespace of image/CSS files, even if you use subdirectories. It also assumes all image/CSS files are below the packageName you pass to it.

Binder DSL Ideas
----------------

* SetActions

      binder.when(...).is(true).set(//
        textOf(...).toOrElse("", ""), // toOrElse returns SetAction
        styleOf(...).to(bz.active())); // to returns SetAction

      binder.on(keyPressOf(...)).then(execute(command));
      binder.on(clickOf(...)).then(toggle(button));
      binder.on(clickOf(...)).set(SetAction...);
      binder.on(blurOf(...)).set(SetAction...);

      // or, for more unique static imports
      binder.on(blurOf(...), thenSet(textOf(...).to(asdf)));

      // string actions
      binder.when(condition, action1, action2);
      // e.g.
      binder.when(value, is(true), show(this), hide(that));

* Non-trivial omponents, e.g.

      <foo:Form>
        <fields>
          <foo:TextLine />
        </fields>
        <blah>
          <foo:Bar />
        </blah>
      </foo:Form>

  Goes to:

      form.addField(textLine1);
      form.setBlah(...);

  Any HTML implicitly becomes an HTMLPanel, e.g.:

      <foo:Form>
        <blah>
          <p>
            <foo:Bar />
          </p>
        </blah>
      </foo:Form>

  Goes to:

      form.setBlah(htmlPanel, List<Bar> bars);


