---
description: >-
  Titles are an elegant and efficient way to display a message to the client,
  but may be anoying for some players.
---

# ⚙ Titles

{% hint style="info" %}
KarmaPlugin is not required for this API
{% endhint %}

This API allows the use of Titles in any minecraft version, including those that does not include native titles support (1.7.10). Instead, in those versions, an hologram is created at the middle front top of the player location, so it acts like a title.

{% embed url="https://www.youtube.com/watch?v=ZVRj0VKt9qw" %}
video demostration
{% endembed %}

Sending a title is as easy as creating the TitleMessage.

```java
import ml.karmaconfigs.api.bukkit.reflection.TitleMessage;

import org.bukkit.entity.Player;

public class ActionBarMessage {

    public static void sendTitle(final Player player, final String title) {
            send(player, title, null);      
    }
    
    public static void sendTitleInstant(final Player player, final String title, final String subtitle) {
            sendInstant(player, title, null);    
    }
    
    public static void sendTitle(final Player player, final String title, final String subtitle, final int fafeIn, final int show, final int fadeOut) {
            send(player, title, null, fadeIn, show, fadeOut);     
    }   
        
    public static void sendSubtitle(final Player player, final String subtitle) {
            send(player, null, subtitle);      
    }
    
    public static void sendSubtitleInstant(final Player player, final String subtitle, final String subtitle) {
            sendInstant(player, null, subtitle);    
    }
    
    public static void sendSubtitle(final Player player, final String subtitle, final String subtitle, final int fafeIn, final int show, final int fadeOut) {
            send(player, null, subtitle, fadeIn, show, fadeOut);     
    }  
        
    public static void send(final Player player, final String title, final String subtitle) {
            TitleMessage title = new TitleMessage(player, title, subtitle);
            title.send();       
    }
    
    public static void sendInstant(final Player player, final String title, final String subtitle) {
            TitleMessage title = new TitleMessage(player, title, subtitle);
            title.send(0, 5, 0);       
    }
    
    public static void send(final Player player, final String title, final String subtitle, final int fafeIn, final int show, final int fadeOut) {
            TitleMessage title = new TitleMessage(player, title, subtitle);
            title.send(fadeIn, show, fadeOut);       
    }
}
```
