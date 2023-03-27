package ml.karmaconfigs.api.bukkit.nms.hologram.interfaces;

import org.bukkit.inventory.ItemStack;

/**
 * Minecraft item stack
 * Credits to: HolographicDisplays
 */
public interface MinecraftItem extends MinecraftBase, MinecraftMountable {

    /**
     * Set the minecraft item stack
     *
     * @param item the item stack
     */
    void setMinecraftStack(final ItemStack item);

    /**
     * Set if the item is allowed to pickup
     *
     * @param allow allow the item pickup
     */
    void allowPickUp(final boolean allow);

    /**
     * Get the minecraft item stack object
     *
     * @return the minecraft item stack
     */
    Object getMinecraftStack();
}
