---
description: The Inventory Book is the implementation of this API for infinite inventories
---

# 📑 Inventory Book

The Inventory Book allows you to create an infinite amount of paginated inventories, with functional items and some other util methods.

{% hint style="info" %}
KarmaPlugin is not required for this API
{% endhint %}

To start, you simply need to create a new InventoryBook object. This will store all the inventories, and each instance of a book stores different inventories, which means you should create one per player and store it on some variable, or if you want it to be shared, just store it on some variable.

### Example

```java
package me.amazing.plugin.inventory;

import ml.karmaconfigs.api.bukkit.inventory.infinity.InventoryBook;
import ml.karmaconfigs.api.bukkit.inventory.infinity.InventoryPage;
import ml.karmaconfigs.api.bukkit.inventory.infinity.exceptions.EmptyBookException;
import ml.karmaconfigs.api.bukkit.inventory.infinity.exceptions.NoIndexPageException;
import ml.karmaconfigs.api.bukkit.inventory.infinity.func.Action;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BookHandler {

    private final Map<UUID, InventoryBook> books = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> points = new ConcurrentHashMap<>();
    private final Player player;

    public BookHandler(final Player player) {
        this.player = player;
    }

    public final void open() throws NoIndexPageException, EmptyBookException {
        books.computeIfAbsent(player.getUniqueId(), (b) -> {
            InventoryBook book = new InventoryBook("&4My book of inventories");
            InventoryPage ci_page = book.addPage("&bCI points (10)")
                    .preventClose();

            ci_page.setItem(8, new ItemStack(Material.BARRIER))
                    .onClick(Action.close());
            ci_page.setItem(22, new ItemStack(Material.BLUE_WOOL))
                    .onClick(Action.run(() -> {
                        int cPoints = points.get(player.getUniqueId());
                        if (cPoints > 0) {
                            points.put(player.getUniqueId(), --cPoints);
                        }
                        ci_page.title("&bCI points (" + cPoints + ")").updateTitle();
                    }));
            ci_page.setItem(24, new ItemStack(Material.RED_WOOL))
                    .onClick(Action.run(() -> {
                        int cPoints = points.get(player.getUniqueId());
                        if (cPoints < 10) {
                            points.put(player.getUniqueId(), ++cPoints);
                        }
                        ci_page.title("&bCI points (" + cPoints + ")").updateTitle();
                    }));

            InventoryPage confirm_page = book.addPage("&bConfirmation").preventClose();
            confirm_page.setItem(22, new ItemStack(Material.BLUE_WOOL))
                    .onClick(Action.run(() -> {
                        int cPoints = points.get(player.getUniqueId());
                        if (cPoints == 0) {
                            confirm_page.allowClose();
                            player.closeInventory();
                        }
                    }));
            confirm_page.setItem(24, new ItemStack(Material.RED_WOOL))
                    .onClick(Action.previousPage(), Action.run(() -> {
                        points.put(player.getUniqueId(), 10);
                        ci_page.title("&bCI points (10)").updateTitle();
                    }));
            
            return book;
        }).open(player);
    }
}

```

{% hint style="info" %}
For more information, we highly recommend you to check the [javadocs](https://reddo.es/karmadev/api/ml/karmaconfigs/api/bukkit/inventory/infinity/package-summary.html).
{% endhint %}
