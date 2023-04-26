---
description: The plugin implementation of the KarmaSource
---

# 🔌 KarmaPlugin

Unfortunately, due to internal functionality, you will need to make your plugin Main class extend KarmaPlugin instead of JavaPlugin. Afortunately, this won't break any compatibility with spigot/bukkit, as KarmaPlugin is just a wrapper that extends JavaPlugin and KarmaSource with Identifiable.

Before starting with it, you will need to import the Bukkit API of the KarmaAPI, for this, you must simply import the dependency into your maven or gradle build script along with the KarmaAPI-Common dependency

```xml
<dependency>
    <groupId>ml.karmaconfigs</groupId>
    <groupId>KarmaAPI-Common</groupId>
    <version>${project.karma.api}</version>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>ml.karmaconfigs</groupId>
    <groupId>KarmaAPI-Bukkit</groupId>
    <version>${project.karma.api}</version>
    <scope>provided</scope>
</dependency>

<!-- You want to use a property to define the API version so when updating you
will only need to modify it once -->
```

```gradle
def karmaVersion = '1.3.4-SNAPSHOT'

dependencies {
    //Your other dependencies
    compileOnly "ml.karmaconfigs:KarmaAPI-Common:${karmaVersion}"
    compileOnly "ml.karmaconfigs:KarmaAPI-Bukkit:${karmaVersion}"
    
}
```
