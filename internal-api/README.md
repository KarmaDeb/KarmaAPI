---
description: >-
  The internal API is the most important part of the API, as it basically brings
  the functionality to the entire API
---

# 💡 Internal API

The KarmaAPI makes use of KarmaSources to work, those must be implemented by the developer, as they are not provided by the API. Almost all the utilities in this API make use of KarmaSources. Implementing them is a very easy task.\
\
First of all, you must import the API dependency in your maven or gradle build script

```xml
<dependendency>
    <groupId>ml.karmaconfigs</groupId>
    <groupId>KarmaAPI-Common</groupId>
    <version>1.3.4-SNAPSHOT</version>
    <scope>compile</scope>
    <!-- Remember to shade the dependency -->
</dependency>
```

```gradle
dependencies {
    //Your other dependencies
    compile "ml.karmaconfigs:KarmaAPI-Common:1.3.4-SNAPSHOT"
}
```

The dependency is hosted in maven central, so you won't need to add the repository element. Now that you have imported the dependency, let's start implementing the KarmaSource and registering it as a source provider.

