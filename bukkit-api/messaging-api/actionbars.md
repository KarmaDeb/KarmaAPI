---
description: >-
  Similar to BossBars, actionbars are a very nice and convenient way of
  displaying information to the user, as its small and in a very good position.
---

# ⚙ ActionBars

This API allows the use of ActionBars in any minecraft version, including those that does not include native actionbar support (1.7.10). Instead, in those versions, an hologram is created and bottom front of the player location, so it acts like an actionbar.

{% embed url="https://www.youtube.com/watch?v=ZVRj0VKt9qw" %}
video demostration
{% endembed %}

There are three types of actionbars you can send to the client.

* Default actionbar
  * The actionbar will last for two seconds
* Repeating actionbar
  * The actionbar will be repeated X times each two seconds
* Persistent actionbar
  * The actionbar will remain always visible

To send an actionbar, you must only create a BarMessage instance and run the send method

```java
import ml.karmaconfigs.api.bukkit.reflection.BarMessage;

import org.bukkit.entity.Player;

public class ActionBarMessage {

    public static void send(final Player player, final String message) {
            BarMessage actionbar = new BarMessage(player, message);
            actionbar.send(false);        
    }
    
    public static void sendRepeating(final Player player, final String message, final int repeats) {
           BarMessage actionbar = new BarMessage(player, message);
           actionbar.send(repeats);   
    }
    
    public static void sendPersistent(final Player player, final String message) {
            BarMessage actionbar = new BarMessage(player, message);
            actionbar.send(true);        
    }
}
```

You can also stop or change a running actionbar by storing its instance and running the methods `#stop` or `#setMessage(String)`&#x20;

```java
import ml.karmaconfigs.api.bukkit.reflection.BarMessage;

import org.bukkit.entity.Player;

import java.lang.UUID;
import java.lang.String;

public class ActionBarMessage {

    private final Map<UUID, BarMessage> bars = new ConcurrentHashMap<>();

    public static void send(final Player player, final String message) {
            BarMessage actionbar = new BarMessage(player, message);
            actionbar.send(false); //The API will cancel any other running actionbar     
            
            bars.put(player.getUniqueId());   
    }
    
    public static void sendRepeating(final Player player, final String message, final int repeats) {
           BarMessage actionbar = new BarMessage(player, message);
           actionbar.send(repeats); //The API will cancel any other running actionbar   
           
           bars.put(player.getUniqueId());   
    }
    
    public static void sendPersistent(final Player player, final String message) {
            BarMessage actionbar = new BarMessage(player, message);
            actionbar.send(true); //The API will cancel any other running actionbar
            
            bars.put(player.getUniqueId());   
    }
    
    public static void cancel(final Player player) {
            BarMessage actionbar = bars.getOrDefault(player.getUniqueId(), null);
            if (actionbar != null) {
                    actionbar.cancel();
                    bars.remove(player.getUniqueId());
            }
    }
    
    public static void update(final Player player, final String message) {
            BarMessage actionbar = bars.getOrDefault(player.getUniqueId(), null);
            if (actionbar != null) actionbar.setMessage(message);
    }
}
```

