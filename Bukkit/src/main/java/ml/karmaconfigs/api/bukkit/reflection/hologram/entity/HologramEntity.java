package ml.karmaconfigs.api.bukkit.reflection.hologram.entity;

import ml.karmaconfigs.api.bukkit.reflection.hologram.HologramHolder;
import ml.karmaconfigs.api.bukkit.reflection.hologram.component.HologramComponent;

/**
 * Hologram entity
 */
public interface HologramEntity {

    /**
     * Get the entity attachment
     *
     * @return the entity attachment
     */
    HologramComponent getAttachment();

    /**
     * Get the entity hologram holder
     *
     * @return the entity hologram holder
     */
    HologramHolder getHolder();
}
