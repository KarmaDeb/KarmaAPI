package ml.karmaconfigs.api.bukkit.nms.hologram.part.collect;

import ml.karmaconfigs.api.bukkit.nms.hologram.part.Line;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Collect handler
 * Credits to: HolographicDisplays
 */
public interface CollectHandler {

    /**
     * On item collect listener
     *
     * @param player the player who collected the item
     * @param item the collected item
     */
    void onCollect(final Player player, final ItemStack item);

    /**
     * Get the touch handler hologram part
     *
     * @return the touch handler part
     */
    Line getLine();

    /**
     * Unregister the touch handler
     *
     * @return if it was possible to unregister
     */
    boolean unregister();
}
