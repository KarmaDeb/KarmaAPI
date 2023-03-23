---
description: >-
  The API provides an hologram API, currently very simple and only working for
  1.7.10 versions.
---

# 💮 Hologram API

{% hint style="info" %}
KarmaPlugin is not required for this API
{% endhint %}

The KarmaAPI provides the server with an hologram API, which is very simple and only works on legacy minecraft versions. We don't recommend using those unless completely necessary, as there are other better plugins on the market made specifically for making holograms, such as [DecentHolograms](https://www.spigotmc.org/resources/decentholograms-1-8-1-19-4-papi-support-no-dependencies.96927/) or [HolographicDisplays](https://dev.bukkit.org/projects/holographic-displays).

## Verifying if holograms are supported

Before using the HologramAPI, you should check if holograms are currently supported, which can be done by calling the `NMSHelper#isSupported` method.

```java
import ml.karmaconfigs.api.bukkit.nms.NMSHelper;

import java.lang.IllegalStateException;

public class HologramManager {

    private final Hologram hologram;

    HologramManager(final Hologram hologram) throws IllegalStateException {
        if (!NMSHelper.isSupported()) throw new IllegalStateException("Holograms are not supported in your current version");
        this.hologram = hologram;
    }
}
```

After doing something similar, you can start creating your holograms. Holograms support text and item lines

```java
import ml.karmaconfigs.api.bukkit.nms.NMSHelper;
import ml.karmaconfigs.api.bukkit.nms.hologram.Hologram;
import ml.karmaconfigs.api.bukkit.nms.hologram.HologramHolder;
import ml.karmaconfigs.api.bukkit.nms.hologram.part.TextPart;
import ml.karmaconfigs.api.bukkit.nms.hologram.part.ItemPart;

import ml.karmaconfigs.api.common.string.StringUtils;

import org.bukkit.entity.Player;
import org.bukkit.Location;

import java.lang.IllegalStateException;

public class HologramManager {

    private final Hologram hologram;

    HologramManager(final Hologram hologram) throws IllegalStateException {
        if (!NMSHelper.isSupported()) throw new IllegalStateException("Holograms are not supported in your current version");
        this.hologram = hologram;
    }
    
    public TextPart addLine(final String text) {
        hologram.append(StringUtils.toColor(text));
    }
    
    public ItemPart addLine(final ItemStack item) {
        hologram.append(item);
    }
    
    public Line getLine(final int index) {
        return hologram.getPart(index);
    }
    
    public int getIndex(final Line part) {
        return hologram.getIndex(part);
    }
    
    public Location getLocation() {
        return hologram.getLocation().clone();
    }
    
    public void move(final Location location) {
        hologram.teleport(location);
    }
    
    public boolean update() {
        hologram.refresh();
    }
    
    public void setVisible(final Player... player) {
        hologram.show(player);
    }
    
    public void setInvisible(final Player... player) {
        hologram.hide(player);
    }
    
    public void create() {
        NMSHelper.invokeHologram(hologram);
    }
    
    public static HologramManager createHologram(final Location location) throws IllegalStateException {
        Hologram hologram = new HologramHolder(location);
        return new HologramManager(hologram);
    }
}
```

{% hint style="warning" %}
It's recommended that you read the [javadocs](https://reddo.es/karmadev/api/docs) for more information about holograms usage.
{% endhint %}
