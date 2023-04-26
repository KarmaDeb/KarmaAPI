---
description: This page shows examples about serializing an inventory
---

# 📩 Serialization

As mentioned previously, you are able to serialize or unserialize inventories using this API. Here we will show you how to serialize the inventory.&#x20;

{% hint style="warning" %}
KarmaPlugin is required for this API
{% endhint %}

Let's imagine you have a plugin which allows a client to have multiple inventories, then you create a class which handles a command called `/invswitch`

```java
package me.amazing.plugin.inventory;

import me.amazing.plugin.MyPlugin;

import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import ml.karmaconfigs.api.common.karma.source.APISource;
import ml.karmaconfigs.api.bukkit.inventory.serializer.InventorySerializer;
import ml.karmaconfigs.api.common.string.StringUtils;

import java.util.UUID;

public class InventorySwitcher {

    private final KarmaPlugin plugin = APISource.loadProvider(MyPlugin.class);
    //private final KarmaSource source = APISource.loadProvider("My plugin name");
    
    private final Player player;
    
    public InventorySwitcher(final Player player) {
        this.player = player;
    }
    
    public UUID storeInventory(final String name) {
        PlayerInventory inventory = player.getInventory();
        InventorySerializer serializer = new InventorySerializer(plugin);
        
        UUID inventory_id = UUID.nameUUIDFromBytes(("Inventory:" + StringUtils.stripColor(name)).getBytes());
        
        SerializedInventory inventory = serializer.serialize(inventory, name, inventory_id);
        if (inventory == null) return null;
        
        return inventory_id;
    }
    
    public SerializedInventory loadInventory(final String name) {
        UUID inventory_id = UUID.nameUUIDFromBytes(("Inventory:" + StringUtils.stripColor(name)).getBytes());
    
        SerializedInventory serialized = new SerializedInventory(inventory_id, plugin);
        if (serialized.exists()) return serialized;
        
        return null;
    }
}

```

This class will store an inventory and assign it an ID based on the inventory name. The same class is able to then recover the stored inventory using the inventory name. You don't actually want to implement it that way, otherwise players would be able to overwrite other players inventories. You should generate the inventory id using the player UUID and the inventory name.
