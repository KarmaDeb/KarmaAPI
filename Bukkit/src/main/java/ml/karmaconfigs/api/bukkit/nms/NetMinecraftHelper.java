package ml.karmaconfigs.api.bukkit.nms;

import ml.karmaconfigs.api.bukkit.nms.hologram.Hologram;
import ml.karmaconfigs.api.bukkit.nms.hologram.interfaces.*;
import ml.karmaconfigs.api.bukkit.nms.hologram.line.HologramLine;
import ml.karmaconfigs.api.bukkit.nms.hologram.line.HologramTouchableLine;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface NetMinecraftHelper {

    /**
     * Create a touch slime
     *
     * @param w the slime world
     * @param x the slime position x
     * @param y the slime position y
     * @param z the slime position z
     * @param touch the touchable instance
     * @return the touchable slime
     */
    MinecraftSlime createSlime(final World w, final double x, final double y, final double z, final HologramLine touch);

    /**
     * Create a stand
     *
     * @param w the armor stand world
     * @param x the armor stand position x
     * @param y the armor stand position y
     * @param z the armor stand position z
     * @param line the hologram line instance
     * @return the armor stand
     */
    MinecraftStand createStand(final World w, final double x, final double y, final double z, final HologramLine line);

    /**
     * Create a horse
     *
     * @param w the horse world
     * @param x the horse position x
     * @param y the horse position y
     * @param z the horse position z
     * @param line the hologram line instance
     * @return the horse
     */
    MinecraftHorse createHorse(final World w, final double x, final double y, final double z, final HologramLine line);

    /**
     * Create a wither skull
     *
     * @param w the wither skull world
     * @param x the wither skull position x
     * @param y the wither skull position y
     * @param z the wither skull position z
     * @param line the hologram line instance
     * @return the wither skull
     */
    MinecraftWitherSkull createSkull(final World w, final double x, final double y, final double z, final HologramLine line);

    /**
     * Create an item
     *
     * @param w the item world
     * @param x the item position x
     * @param y the item position y
     * @param z the item position z
     * @param line the hologram line instance
     * @param item the item to create for
     * @return the item
     */
    MinecraftItem createItem(final World w, final double x, final double y, final double z, final HologramLine line, final ItemStack item);

    /**
     * Create an hologram
     *
     * @param hologram the hologram to create
     */
    void createHologram(final Hologram hologram);

    /**
     * Destroy an hologram
     *
     * @param hologram the hologram to destry
     */
    void destroyHologram(final Hologram hologram);
}
