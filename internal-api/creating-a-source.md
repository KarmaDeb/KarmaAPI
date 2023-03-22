---
description: Here you will learn how to setup a source
---

# Creating a source

First of all, we will need to create a class, and make it implement KarmaSource

```java
package me.developer.amazing;

import ml.karmaconfigs.api.common.karma.source.KarmaSource;

public class AmazingApp implements KarmaSource {

}
```

After that, and implementing the KarmaSource inherited methods, your class should look like this

```java
package me.developer.amazing;

import ml.karmaconfigs.api.common.karma.source.KarmaSource;

public class AmazingApp implements KarmaSource {

    /**
     * Karma source name
     *
     * @return the source name
     */
    @Override
    public String name() {
        return null;
    }

    /**
     * Karma source version
     *
     * @return the source version
     */
    @Override
    public String version() {
        return null;
    }

    /**
     * Karma source description
     *
     * @return the source description
     */
    @Override
    public String description() {
        return null;
    }

    /**
     * Karma source authors
     *
     * @return the source authors
     */
    @Override
    public String[] authors() {
        return new String[0];
    }

    /**
     * Karma source update URL
     *
     * @return the source update URL
     */
    @Override
    public String updateURL() {
        return null;
    }
}
```

You want to fill the `name`, `version`, `description` and `author` values, you can leave `updateURL` as null

```java
package me.developer.amazing;

import ml.karmaconfigs.api.common.karma.source.KarmaSource;

public class AmazingApp implements KarmaSource {

    /**
     * Karma source name
     *
     * @return the source name
     */
    @Override
    public String name() {
        return "Amazing App";
    }

    /**
     * Karma source version
     *
     * @return the source version
     */
    @Override
    public String version() {
        return "1.0.0";
    }

    /**
     * Karma source description
     *
     * @return the source description
     */
    @Override
    public String description() {
        return "My amazing app, does amazing things, because it's amazing!";
    }

    /**
     * Karma source authors
     *
     * @return the source authors
     */
    @Override
    public String[] authors() {
        return new String[]{
            "AmazingDeveloper"
        };
    }

    /**
     * Karma source update URL
     *
     * @return the source update URL
     */
    @Override
    public String updateURL() {
        return null;
    }
}
```

Some parts of the API, may need your KarmaSource to also implement Identifiable. Indetifiable objects are objects that have an unique identifier for ever, it remains betwen restarts and cannot be modified. Implementing it is an easy as doing the following.

```java
package me.developer.amazing;

import ml.karmaconfigs.api.common.karma.file.KarmaMain;
import ml.karmaconfigs.api.common.karma.file.element.KarmaPrimitive;
import ml.karmaconfigs.api.common.karma.file.element.types.Element;
import ml.karmaconfigs.api.common.karma.source.Identifiable;
import ml.karmaconfigs.api.common.karma.source.KarmaSource;
import ml.karmaconfigs.api.common.security.token.TokenGenerator;
import ml.karmaconfigs.api.common.string.StringUtils;

public class AmazingApp implements KarmaSource, Identifiable {

    private String identifier;

    /**
     * Karma source name
     *
     * @return the source name
     */
    @Override
    public String name() {
        return "Amazing App";
    }

    /**
     * Karma source version
     *
     * @return the source version
     */
    @Override
    public String version() {
        return "1.0.0";
    }

    /**
     * Karma source description
     *
     * @return the source description
     */
    @Override
    public String description() {
        return "My amazing app, does amazing things, because it's amazing!";
    }

    /**
     * Karma source authors
     *
     * @return the source authors
     */
    @Override
    public String[] authors() {
        return new String[]{
                "AmazingDeveloper"
        };
    }

    /**
     * Karma source update URL
     *
     * @return the source update URL
     */
    @Override
    public String updateURL() {
        return null;
    }

    /**
     * Get the current identifier
     *
     * @return the current identifier
     */
    @Override
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Store the identifier
     *
     * @param name the identifier name
     * @return if the identifier could be stored
     */
    @Override
    public boolean storeIdentifier(final String name) {
        KarmaMain main = new KarmaMain(this, "data.kf", "cache");
        Element<?> element = main.get(name);
        if (element.isElementNull()) {
            String id = identifier;
            if (StringUtils.isNullOrEmpty(identifier)) id = TokenGenerator.generateLiteral();
            element = new KarmaPrimitive(id);

            main.set(name, element);
            main.save();
        }

        return main.save();
    }

    /**
     * Load an identifier
     *
     * @param name the identifier name
     */
    @Override
    public void loadIdentifier(final String name) {
        KarmaMain main = new KarmaMain(this, "data.kf", "cache");
        Element<?> element = main.get(name);
        if (element.isElementNull()) {
            String id = TokenGenerator.generateLiteral();
            element = new KarmaPrimitive(id);

            main.set(name, element);
            main.save();
        }

        identifier = element.getAsString();
    }
}

```

But you can simply not implement it if you are not planing to use any method that requires and identifiable object
