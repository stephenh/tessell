---
layout: default
title: Getting Started
---

Getting Started
===============

This covers how to setup Tessell within an existing GWT application, specifically the IDE/build setup. For more information about actual functionality, see the [tutorial](./tutorial.html) and other docs.

Assumptions
-----------

This assumes you have an existing GWT application and build environment of your choice setup (Ant/Maven/etc.).

For an existing GWT application to base this Getting Started doc on, I used the "New Web Application Project" in Eclipse and chose:

* Project name: "sample"
* Package: "com.business.sample"

This means that:

* The Java code is in `src/` (for your project it may be in `src/main/java`)
* The generated Java code will go in `gen/` (for your project it may go in `target/java`)

So adjust the paths below to your project accordingly.

Setting up View Generation
--------------------------

To setup view generation, you'll need:

1. A dependency on `tessell-user.jar` and `tessell-dev.jar`.

   These can be downloaded from the [http://repo.joist.ws](http://repo.joist.ws) Maven repository. Personally, I use an `ivy.xml` file with:

       <dependency org="org.tessell" name="tessell-user" rev="2.0.0" conf="compile,sources"/>
       <dependency org="org.tessell" name="tessell-dev" rev="2.0.0" conf="provided,sources"/>
       <dependency org="org.tessell" name="tessell-apt" rev="2.0.0" conf="provided,sources"/>
   {: class="brush:xml"}

   But you can use Maven or the jars directly if you want.
   
   Either way, ensure they are downloaded into a local directory (e.g. `lib/`) or the local Maven cache for the code generator to reference them.

2. Create a `views` package and `resources` package, e.g.:

   * `com/business/sample/client/views` -- for your `ui.xml` files
   * `com/business/sample/client/resources` -- for your CSS and image files

3. Create a `gen` directory for the generated source code, e.g.:

   * `gen/`

   (You may put this under `target/` or some other directory that doesn't get checked in.)

4. And the `gen/` directory as a source folder in Eclipse, e.g.:

   * Right click on the project, go to Properties
   * Select Java Build Path, go to the Source tag
   * Click Add Folder and check the `gen/` directory

5. Setup a custom builder in Eclipse.

   The custom builder will run the Tessell code generator whenever your `ui.xml` files change. It does this by using a feature of Eclipse that will run a command line program anytime one of the files in a watched directory changes.

   * In Eclipse, select the project, go to Properties
   * Select Builders, click New
   * Select Program
   * For the name, `tessell`
   * On the Main tab, type:
     * Location: `${system_path:java}`
     * Working directory: `${workspace_loc:/sample}` (where "sample" is the name of your project)
     * Arguments:

           -cp "lib/*"
           org.tessell.generators.Generator
           --inputDirectory src/
           --viewsPackageName com.business.sample.client.views
           --resourcesPackageName com.business.sample.client.resources
           --outputDirectory gen/
       {: class="brush:plain"}
   * On the Refresh tab, 
     * Check "Refresh resources upon completion"
     * Select "Specific resources" and check the `sample/gen` folder
   * On the Build Options tab,
     * Check "During auto builds"
     * Specify working set of relevant resources and check the directories:
       * `samples/src/com/business/sample/client/views`
       * `samples/src/com/business/sample/client/resources`
   * Click OK

   What we've just done is tell Eclipse to run `java -cp ...` every time you change a `ui.xml` or resources file. (Unfortunately Eclipse doesn't have an option to run Java classes with the project's existing classpath, so we have to pass the `-cp` parameter manually.)

   If you do this correctly, you should see output of something like:

       gen/com/business/sample/client/views/AppViews.java
       gen/com/business/sample/client/views/AppViewsProvider.java
       gen/com/business/sample/client/views/GwtViewsProvider.java
       gen/com/business/sample/client/views/StubViewsProvider.java
       gen/com/business/sample/client/resources/AppResources.java
       gen/com/business/sample/client/resources/StubAppResources.java
       gen/com/business/sample/client/resources/AppResourcesUtil.java
   {: class="brush:plain"}

6. Check `.externalToolBuilders/tessell.launch` into your repository.

   This means anyone else checking the project out of your repository should get the "run Tessell on save" setup out-of-the-box and not have to go through this setup again.

7. Now if you create a file, e.g. `com/business/sample/client/views/Foo.ui.xml`, you should see `IsFooView` created.

       src/com/business/sample/client/views/Foo.ui.xml
       gen/com/business/sample/client/views/Foo-gen.ui.xml
   {: class="brush:plain"}

8. You can create a presenter, e.g. `com/business/sample/client/app/FooPresenter.java`:

       public class FooPresenter extends BasicPresenter<IsFooView> {
         public FooPresenter() {
           super(AppViews.newFooView());
         }
       }
   {: class="brush:java"}

9. In your `onModuleLoad`, initiate the `AppViews` provider and then use your presenter:

       AppViews.setProvider(new GwtViewsProvider());

       FooPresenter p = new FooPresenter();
       p.bind();
       RootPanel.get().add(p.getView());
   {: class="brush:java"}

Eclipse should now be setup to generate views for you. For more information on writing presenters, see the [tutorial](./tutorial.html) and other docs.

For your build environment (Maven/Ant), you'll have to call the same `Generator` class with the same classpath and arguments as we setup in the Eclipse custom builder. For Ant, you can see the [todomvc](https://github.com/stephenh/todomvc-tessell) sample application.

Setting up Annotation Processors
--------------------------------

Tessell usages annotation processors for generating some non-view boilerplate, specifically places (`@GenPlace`) and dispatch DTOs (`@GenDispatch`).

To configure the annotation processors:

1. In Eclipse, right click on the project, select properties

1. Go to Java Compiler, Annotation Processing and:
   * Select "Enable project specific settings"
   * Select "Enable annotation processing"
   * Set the "Generated source directory" to `gen`

1. Go to Java Compiler, Annotation Processing, Factory Path and:
   * Select "Enable project specific settings"
   * Select "Add JARs"
   * Find `tessell-apt.jar` in your workspace
   * Click OK

1. Click OK 

1. Check `.factorypath` and `.settings/org.eclipse.jdt.apt.core.prefs` into your repository, then other users will get this configuration automatically.

1. For Ant/`javac`, just ensure the `tessell-apt.jar` is on the compilation classpath, it will find it automatically.

Setting up Unit Testing
-----------------------

For unit tests, you can:

1. Create a `FooPresenterTest.java`

2. In a static initializer, setup Tessell's stub views (so nothing tries to touch a DOM):

       static {
         StubWidgetsProvider.install();
       }
   {: class="brush:java"}

3. Create your presenter and use the stub views to put it under test:

       public void testFoo() {
         FooPresenter p = new FooPresenter();
         p.bind();
         // assuming you have a TextBox
         StubFooView v = (StubFooView) p.getView();
         v.textName().type("what the user typed");
         // assert various things happened
       }
   {: class="brush:java"}

For more on testing, see [tests](./tests.html) or the example projects for how the manipulate the fake view objects and fake dispatch implementation to test the presenter.

