package ml.karmaconfigs.api.bukkit.listener;

import ml.karmaconfigs.api.bukkit.KarmaPlugin;
import ml.karmaconfigs.api.common.minecraft.api.MineAPI;
import ml.karmaconfigs.api.common.utils.enums.Level;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class JoinHandler implements Listener {

    private final KarmaPlugin plugin;

    public JoinHandler(final KarmaPlugin owner) {
        plugin = owner;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(AsyncPlayerPreLoginEvent e) {
        String name = e.getName();
        plugin.logger().scheduleLog(Level.INFO, "Trying to share information of {0} with the public API", name);

        MineAPI.publish(name).whenComplete((result) -> {
            if (result.getUri() != null) {
                plugin.logger().scheduleLog(Level.INFO, "Shared minecraft data (uuid/nick) with the public API {0} of {1}", result.getUri(), result.getNick());
            } else {
                plugin.logger().scheduleLog(Level.GRAVE, "An error occurred while sharing data (uuid/nick) with the public API for {0}", name);
            }
        });
    }
}