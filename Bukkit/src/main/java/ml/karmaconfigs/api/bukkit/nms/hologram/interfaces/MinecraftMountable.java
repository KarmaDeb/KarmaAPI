package ml.karmaconfigs.api.bukkit.nms.hologram.interfaces;

/**
 * Mountable entity
 * Credits to: HolographicDisplays
 */
public interface MinecraftMountable extends MinecraftBase {

    /**
     * Set the entity passenger
     *
     * @param entity the entity passenger to use as vehicle
     */
    void setPassengerOf(final MinecraftBase entity);
}
