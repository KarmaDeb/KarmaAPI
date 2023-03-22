---
description: The best way to access your source is by registering it as a source provider.
---

# Registering a source

After you have created your source, you can register it in order to access it only via its name by the source registrar. Registering a source requires only one method, and your source object. If you haven't created one yet, please check [how to create a source](creating-a-source.md).

Let's imagine you have your Main class with the main initializer

```java
package me.developer.amazing;

public class Main {

    public static void main(String[] args) {
        //Your startup logic...
    }
}
```

To register your source is as easy as doing this:

```java
package me.developer.amazing;

import ml.karmaconfigs.api.common.karma.source.APISource;
import ml.karmaconfigs.api.common.karma.source.KarmaSource;

public class Main {

    public static void main(String[] args) throws IllegalStateException {
        final KarmaSource source = new AmazingApp();
        APISource.addProvider(source);
    
        //Your startup logic...
    }
}
```

{% hint style="danger" %}
Please be aware of how you register the provider, as registering a provider with a name of another provider will cause an IllegalStateException, as well as registering a provider which class is the same as another already registered. That's to avoid concurrency. If you want to have two sources, you should name them different and use different classes for them
{% endhint %}

After you have registered your source, you can access it by using the same `APISource` registrar

```java
package me.developer.amazing.app;

import me.developer.amazing.AmazingApp;

import ml.karmaconfigs.api.common.karma.source.APISource;
import ml.karmaconfigs.api.common.karma.source.KarmaSource;

public class HelloWorldApplication {

    private final static AmazingApp amazing = APISource.loadProvider(AmazingApp.class);
    private final static KarmaSource source = APISource.loadProvider("Amazing App");
    
    public final void sayHello() {
       source.console().send("Hello world!");
       amazing.console().send("Hello world!");
    }
}
```
