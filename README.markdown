
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
* ListProperty with filters/updates on child property change

Limitations
-----------

* ResourcesGenerator assumes a global namespace of image/CSS files, even if you use subdirectories. It also assumes all image/CSS files are below the packageName you pass to it.

Notes
-----

* Mapping cases:
  * `entity * (all fields) -> dto`
  * `entity some fields -> dto`
  * `entity + other stuff -> dto + other dto`
  * `entity field errors -> dto -> model property errors`
  * have the codegen make both the dtos and the mapping
  * List<Entity> <-> List<String> names
* Serializing to local storage would require chained events, e.g. child name fires change, then child model fires change (or "shallow change"), then parent list fires shallow change, and parent fires shallow change. Then a single listen on parent can reserialize to JSON.
* Fixing todovmc's `allTodos` and `doneTodos` also requires shallow events, where each time todo done changes, the todo model fires shallow change, and then the todo list fires shallow change. Then any `DerivedValue` that accessed a `ListProperty` during it's evaluation would need to be added as downstream of it's shallow changes.
* Could potentially use models to go to/from JSON, but seems like AutoBeans-based DTOs is a cleaner approach

EventBus Todo
-------------

* `addHandler` should take `CanRegisterHandlers`--enforced registration, implicit is too magical for now
* `AbstractTypedHandler` with a `getType()` instance method


