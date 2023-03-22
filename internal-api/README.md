---
description: >-
  The internal API is the most important part of the API, as it basically brings
  the functionality to the entire API
---

# Internal API

The KarmaAPI makes use of KarmaSources to work, those must be implemented by the developer, as they are not provided by the API. Almost all the utilities in this API make use of KarmaSources. Implementing them is a very easy task.\
\
First of all, you must import the API dependency in your maven or gradle build script

```xml
<dependendency>
    <groupId>ml.karmaconfigs</groupId>
    <groupId>KarmaAPI-Common</groupId>
    <version>1.3.4-SNAPSHOT</version>
    <scope>provided</scope>
    <!-- Usually you will use provided, as this API as been mostly
    designed for bukkit/bungee/velocity plugin development (in where
    this API is loaded by the KarmaAPI platform plugin), even though
    it still supports standalone mode. If you really want to compile it,
    please shade it -->
</dependency>
```

The dependency is hosted in maven central, so you won't need to add the repository element. Now that you have imported the dependency, let's start implementing the KarmaSource and registering it as a source provider.

