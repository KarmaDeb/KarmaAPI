---
description: KarmaPlugin is the plugin extension of a KarmaSource.
---

# 🧙 Creating a KarmaPlugin

Using a KarmaPlugin is easier than using a KarmaSource, as the most important part is already handled by KarmaPlugin, it's an abstract class so it must be extended.

The KarmaPlugin class implements KarmaSource and Identifiable, and also extends JavaPlugin, so it won't make your plugin incompatible with spigot, but it will make your plugin depend on KarmaAPI.

Before creating your KarmaPlugin, you will need to modify your plugin.yml like this:

```yaml
...plugin.yml
depend: [...dependencies, KarmaAPI]
```

After doing that, you can now create your plugin class file

```java
package me.amazing.plugin;

import ml.karmaconfigs.api.bukkit.KarmaPlugin;

public class MyPlugin extends KarmaPlugin {
    
    /**
     * Enable the KarmaPlugin
     */
    @Override
    public void enable() {
        //onEnable is used internally by KarmaPlugin
    }
    
    @Override
    public void onDisable() {
        //Your on disable logic
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

And done! You've already created a KarmaPlugin, which also extends JavaPlugin and implements KarmaAPI and Identifiable. It will be also be registered automatically as a source provider. So you can access your plugin via `APISource.loadProvider(MyPlugin.class)`.
