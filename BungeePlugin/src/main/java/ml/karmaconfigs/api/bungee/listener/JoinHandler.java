package ml.karmaconfigs.api.bungee.listener;

import ml.karmaconfigs.api.bungee.KarmaPlugin;
import ml.karmaconfigs.api.common.minecraft.api.MineAPI;
import ml.karmaconfigs.api.common.utils.enums.Level;
import ml.karmaconfigs.api.common.utils.uuid.UUIDUtil;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class JoinHandler implements Listener {

    private final KarmaPlugin plugin;

    public JoinHandler(final KarmaPlugin owner) {
        plugin = owner;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void profileRequest(LoginEvent e) {
        String name = e.getConnection().getName();
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
