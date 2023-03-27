package ml.karmaconfigs.api.bukkit.nms.hologram.part;

import ml.karmaconfigs.api.bukkit.nms.hologram.Hologram;
import ml.karmaconfigs.api.bukkit.nms.hologram.part.collect.CollectHandler;
import ml.karmaconfigs.api.bukkit.nms.hologram.part.collect.CollectablePart;
import ml.karmaconfigs.api.bukkit.nms.hologram.part.touch.TouchHandler;
import ml.karmaconfigs.api.bukkit.nms.hologram.part.touch.TouchablePart;
import org.bukkit.inventory.ItemStack;

/**
 * Item part
 */
public interface ItemPart extends Line, CollectablePart, TouchablePart {

    /**
     * Get the hologram part
     *
     * @return the hologram part
     */
    ItemStack get();

    /**
     * Set the hologram part value
     *
     * @param param the value
     */
    void set(final ItemStack param);
}
