
See [tessell.org](http://www.tessell.org).

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

Test5.

