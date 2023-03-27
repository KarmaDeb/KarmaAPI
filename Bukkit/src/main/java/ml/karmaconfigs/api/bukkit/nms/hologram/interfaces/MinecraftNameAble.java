package ml.karmaconfigs.api.bukkit.nms.hologram.interfaces;

/**
 * Minecraft nameable entity
 * Credits to: HolographicDisplays
 */
public interface MinecraftNameAble extends MinecraftBase {

    /**
     * Set the nameable entity name
     *
     * @param name the entity name
     */
    void setMinecraftName(final String name);

    /**
     * Get the minecraft nameable entity name
     *
     * @return the entity name
     */
    String getMinecraftName();
}
