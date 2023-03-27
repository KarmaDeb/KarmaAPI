package ml.karmaconfigs.api.bukkit.nms.hologram.part.touch;

import ml.karmaconfigs.api.bukkit.nms.hologram.part.Line;
import org.bukkit.entity.Player;

/**
 * Hologram touch handler.
 * Credits to: HolographicDisplays
 */
public interface TouchHandler {

    /**
     * On touch listener
     *
     * @param player the player that has touched the hologram line
     */
    void onTouch(final Player player);

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
