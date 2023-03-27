package ml.karmaconfigs.api.bukkit.nms.hologram.interfaces;

import ml.karmaconfigs.api.bukkit.nms.hologram.line.HologramLine;
import org.bukkit.entity.Entity;

/**
 * Minecraft base entity
 * Credits to: HolographicDisplays
 */
public interface MinecraftBase {

    /**
     * Get the nms entity attached line
     *
     * @return the entity line
     */
    HologramLine getLine();

    /**
     * Set if entity is locked at current tick
     *
     * @param lock_current_tick tick lock status
     */
    void setLockTick(final boolean lock_current_tick);

    /**
     * Set the entity nms location
     *
     * @param x the location x
     * @param y the location y
     * @param z the location z
     */
    void setMinecraftLocation(final double x, final  double y, final  double z);

    /**
     * Get if the entity is nms dead
     *
     * @return if the entity is dead
     */
    boolean isMinecraftDead();

    /**
     * Kill the entity via nms
     */
    void killMinecraftEntity();

    /**
     * Get the entity nms id
     *
     * @return the entity id
     */
    int getMinecraftID();

    /**
     * Get the entity as a bukkit entity
     *
     * @return the bukkit entity
     */
    Entity asBukkitEntity();
}
