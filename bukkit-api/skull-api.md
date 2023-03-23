---
description: >-
  With this API you are also able to create a Skull from a skin value and
  signature. In combination with the web API this is very useful
---

# 💀 Skull API

{% hint style="info" %}
KarmaPlugin is not required for this API
{% endhint %}

So, let's imagine you want to create a skull with the skin of `KarmaDev`.&#x20;

You can make use of the [web api](../internal-api/web-api/), to fetch data about the client, and then use it to get the skull item.

```java
package me.amazing.plugin.inventory;

import ml.karmaconfigs.api.common.minecraft.api.MineAPI;
import ml.karmaconfigs.api.common.minecraft.api.response.data.SkinData;
import ml.karmaconfigs.api.bukkit.reflection.skull.SkinSkull;

import java.util.concurrent.CompletableFuture;

public class SkullBuilder {

    public static CompletableFuture<ItemStack> getSkull(final String name) {
        CompletableFuture<ItemStack> future = new CompletableFuture<>();
    
        MineAPI.fetch(name).whenComplete((result) => {
            SkinData data = result.getSkin();
            ItemStack skull = null;
            if (data != null) {
                String value = data.getValue();
                String signature = data.getSignature();
                
                skull = SkinSkull.createSkull(value, signature);
            }
            
            future.complete(skull);
        });
        
        return future;
    }
}
```

Or if you already have a game profile object, you can also parse it to the SkinSkull, for example:

```java
package me.amazing.plugin.inventory;

import ml.karmaconfigs.api.common.minecraft.api.MineAPI;
import ml.karmaconfigs.api.common.minecraft.api.response.data.SkinData;
import ml.karmaconfigs.api.bukkit.reflection.skull.SkinSkull;

import java.util.concurrent.CompletableFuture;

public class SkullBuilder {

    public static CompletableFuture<ItemStack> getSkull(final String name) {
        CompletableFuture<ItemStack> future = new CompletableFuture<>();
    
        MineAPI.fetch(name).whenComplete((result) => {
            SkinData data = result.getSkin();
            ItemStack skull = null;
            if (data != null) {
                String value = data.getValue();
                String signature = data.getSignature();
                
                skull = SkinSkull.createSkull(value, signature);
            }
            
            future.complete(skull);
        });
        
        return future;
    }
    
    public static ItemStack getSkull(final Object gameProfile) {
        return SkinSkull.createSkull(gameProfile);
    }
}
```

When creating the skin skull, you can also modify its meta, so the result will be already ready to use if you need to change for example its name. You could do this for example:

```java
package me.amazing.plugin.inventory;

import ml.karmaconfigs.api.common.minecraft.api.MineAPI;
import ml.karmaconfigs.api.common.minecraft.api.response.data.SkinData;
import ml.karmaconfigs.api.bukkit.reflection.skull.SkinSkull;
import ml.karmaconfigs.api.common.string.StringUtils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemFlag;

import java.util.concurrent.CompletableFuture;

public class SkullBuilder {

    public static CompletableFuture<ItemStack> getSkull(final String name) {
        CompletableFuture<ItemStack> future = new CompletableFuture<>();
    
        MineAPI.fetch(name).whenComplete((result) -> {
            SkinData data = result.getSkin();
            ItemStack skull = null;
            if (data != null) {
                String value = data.getValue();
                String signature = data.getSignature();
                
                skull = SkinSkull.createSkull(value, signature, (meta) -> {
                    meta.setDisplayName(StringUtils.toColor(name));
                    meta.addItemFlags(ItemFlag.values());
                    
                    return meta;
                });
            }
            
            future.complete(skull);
        });
        
        return future;
    }
    
    public static ItemStack getSkull(final Object gameProfile) {
        return SkinSkull.createSkull(gameProfile, (meta) -> {
                    meta.setDisplayName(StringUtils.toColor(name));
                    meta.addItemFlags(ItemFlag.values());
                    
                    return meta;
                });
    }
}
```

{% hint style="info" %}
The best way to do this is by storing the skin value/signature locally with a cache time, so you avoid making a lot of requestse which may extend the response time or produce errors due to rate limits caused by MineAPI calls.
{% endhint %}
