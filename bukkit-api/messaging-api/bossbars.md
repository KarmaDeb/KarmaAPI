---
description: >-
  The BossBar is a nice way to display information to the client, it's small but
  still visible and read-able for the client.
---

# ⚙ BossBars

{% hint style="warning" %}
KarmaPlugin is required for this API
{% endhint %}

Unlinke other BossBar APIs, the KarmaAPI has a bossbar displaying queue, which means you can send multiple boss bars, and schedule them. You could safely send 12 bossbars, the API will take the 5 first boss bars, and store the rest, and after one of those 5 bossbars reaches his "end of life", the next boss bar from the stored ones wil lbe shown. And like this until the bossbar queue is empty.

Creating a BossBar is very simple, and supports all minecraft versions from 1.7.10 until latest version. To create a BossBar you will need a KarmaPlugin, due the scheduling utilities.

```java
package me.amazing.plugin.bossbar;

import me.amazing.plugin.MyPlugin;

import ml.karmaconfigs.api.bukkit.KarmaPlugin;
import ml.karmaconfigs.api.bukkit.reflection.BossMessage;
import ml.karmaconfigs.api.common.minecraft.boss.BossColor;
import ml.karmaconfigs.api.common.minecraft.boss.BossType;
import ml.karmaconfigs.api.common.minecraft.boss.ProgressiveBar;

public class BossDisplayer {
    
    private final static KarmaPlugin plugin = APISource.loadProvider(MyPlugin.class);
    
    public static void showMessage(final Player player, final String message) {
        BossMessage message = new BossMessage(plugin, title, duration)
            .color(BossColor.PINK)
            .style(BossType.SEGMENTED_20)
            .progress(ProgressiveBar.DOWN);
       
        message.scheduleBar(player);
    }
}
```
