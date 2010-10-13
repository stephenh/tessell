
See [gwtmpv.org](http://www.gwtmpv.org).

* form disable on ServerCall
* indicator on ServerCall
* Pre-fill the handler (optional);
* doLogin.call(user, pass);
* HasDispatchAsync to ServerCall cstr

Todo
----

* Add @Place annotation
  * Take name of presenter
  * Any constructor parameters--how to denote application-level vs. request-level?
* Figure out cross-presenter transitions
  * Event bus doesn't make sense--these aren't global things
  * Have Slot listen to presenter events?
* Support 2nd generation Presenter interfaces via event hook hints in ui.xml file

Limitations
-----------

* ResourcesGenerator assumes a global namespace of images/css files, even if you use subdirectories. It also assumes all images/css files are below the packageName you pass to it.

Notes
-----

* Mapping cases:
  * `entity * (all fields) -> dto`
  * `entity some fields -> dto`
  * `entity + other stuff -> dto + other dto`
  * `entity field errors -> dto -> model property errors`
  * have the codegen make both the dtos and the mapping
  * List<Entity> <-> List<String> names

