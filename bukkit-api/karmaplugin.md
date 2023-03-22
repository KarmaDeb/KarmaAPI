---
description: The plugin implementation of the KarmaSource
---

# KarmaPlugin

Unfortunately, due to internal functionality, you will need to make your plugin Main class extend KarmaPlugin instead of JavaPlugin. Afortunately, this won't break any compatibility with spigot/bukkit, as KarmaPlugin is just a wrapper that extends JavaPlugin and KarmaSource with Identifiable.
