package ml.karmaconfigs.api.bukkit.nms.hologram.line;

import ml.karmaconfigs.api.bukkit.nms.NMSHelper;
import ml.karmaconfigs.api.bukkit.nms.hologram.Hologram;
import ml.karmaconfigs.api.bukkit.nms.hologram.interfaces.MinecraftBase;
import ml.karmaconfigs.api.bukkit.nms.hologram.interfaces.MinecraftItem;
import ml.karmaconfigs.api.bukkit.nms.hologram.part.ItemPart;
import ml.karmaconfigs.api.bukkit.nms.hologram.part.collect.CollectHandler;
import ml.karmaconfigs.api.bukkit.nms.hologram.part.touch.TouchHandler;
import ml.karmaconfigs.api.bukkit.server.BukkitServer;
import ml.karmaconfigs.api.bukkit.server.Version;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Hologram line
 */
public class HologramItemLine extends HologramTouchableLine implements ItemPart {

    private ItemStack item;

    private MinecraftItem minecraftItem;
    private MinecraftBase vehicle;

    private final List<CollectHandler> handlers = new ArrayList<>();

    /**
     * Initialize the hologram line
     *
     * @param h  the holder
     * @param item the item
     */
    public HologramItemLine(final Hologram h, final ItemStack item) {
        super(0.7, h);
    }

    /**
     * Get the line entities
     *
     * @return the line entities
     */
    @Override
    public int[] getEntities() {
        if (exists()) {
            if (touch != null) {
                int[] touch_entities = touch.getEntities();
                int[] entities = new int[touch_entities.length + 2];
                entities[0] = minecraftItem.getMinecraftID();
                entities[1] = vehicle.getMinecraftID();
                for (int i = 2; i < entities.length; i++)
                    entities[i] = touch_entities[i - 2];

                return entities;
            }

            return new int[]{minecraftItem.getMinecraftID(), vehicle.getMinecraftID()};
        }
        return new int[0];
    }

    /**
     * Get the hologram part
     *
     * @return the hologram part
     */
    @Override
    public ItemStack get() {
        return item;
    }

    /**
     * Set the hologram part value
     *
     * @param param the value
     */
    @Override
    public void set(final ItemStack param) {
        Objects.requireNonNull(param, "Cannot set null item stack");
        if (param.getType().equals(Material.AIR)) {
            throw new NullPointerException("Cannot use air in a item stack!");
        }

        item = param;
        if (minecraftItem != null)
            minecraftItem.setMinecraftStack(item);
    }

    /**
     * Add a touch handler to this text part
     *
     * @param handlers the touch handlers
     */
    @Override
    public void addTouchHandler(final TouchHandler... handlers) {
        if (item != null) {
            Location location = minecraftItem.asBukkitEntity().getLocation();
            double offset = getItemOffset();
            for (TouchHandler handler : handlers) {
                appendTouchHandler(handler, location.getWorld(), location.getX(), location.getY() - offset, location.getZ());
            }
        }
    }

    /**
     * Remove a touch handler from this text part
     *
     * @param handlers the touch handlers to remove
     */
    @Override
    public void removeTouchHandler(final TouchHandler... handlers) {
        for (TouchHandler handler : handlers) {
            appendTouchHandler(handler, null, 0d, 0d, 0d);
        }
    }

    /**
     * Add a collect handler to this item part
     *
     * @param handlers the collect handlers
     */
    @Override
    public void addCollectHandler(final CollectHandler... handlers) {
        for (CollectHandler handler : handlers) {
            if (handler != null)
                this.handlers.add(handler);
        }
    }

    /**
     * Remove a collect handler from this item part
     *
     * @param handlers the collect handlers to remove
     */
    @Override
    public void removeCollectHandler(final CollectHandler... handlers) {
        this.handlers.removeAll(Arrays.asList(handlers));
    }

    /**
     * Get the item part collect handlers
     *
     * @return the hologram collect handlers
     */
    @Override
    public CollectHandler[] getCollectHandlers() {
        return handlers.toArray(new CollectHandler[0]);
    }

    /**
     * Spawn the line
     *
     * @param location the line location
     */
    @Override
    public void spawn(final Location location) {
        spawn(location.getWorld(), location.getX(), location.getY(), location.getZ());
    }

    /**
     * Spawn the line
     *
     * @param world the line world
     * @param x     the line x position
     * @param y     the line y position
     * @param z     the line z position
     */
    @Override
    public void spawn(final World world, final double x, final double y, final double z) {
        super.spawn(world, x, y, z);
        if (item != null && !item.getType().equals(Material.AIR)) {
            double offset = getItemOffset();
            minecraftItem = NMSHelper.invokeItem(world, x, y + offset, z, this, item);
            if (BukkitServer.isOver(Version.v1_8)) {
                vehicle = NMSHelper.invokeStand(world, x, y + offset, z, this);
            } else {
                vehicle = NMSHelper.invokeSkull(world, x, y + offset, z, this);
            }

            minecraftItem.setPassengerOf(vehicle);
            minecraftItem.setLockTick(true);
            vehicle.setLockTick(true);
        }
    }

    /**
     * De-spawn the line
     */
    @Override
    public void deSpawn() {
        super.deSpawn();
        if (vehicle != null) {
            vehicle.killMinecraftEntity();
            vehicle = null;
        }
        if (minecraftItem != null) {
            minecraftItem.killMinecraftEntity();
            minecraftItem = null;
        }
    }

    /**
     * Teleport the line to the specified coordinates
     *
     * @param x the x cord
     * @param y the y cord
     * @param z the z cord
     */
    @Override
    public void teleport(final double x, final double y, final double z) {
        super.teleport(x, y, z);
        double offset = getItemOffset();
        if (vehicle != null)
            vehicle.setMinecraftLocation(x, y + offset, z);
        if (minecraftItem != null)
            minecraftItem.setMinecraftLocation(x, y + offset, z);
    }

    /**
     * Get the minecraft item
     *
     * @return the minecraft item
     */
    public MinecraftItem getMinecraftItem() {
        return minecraftItem;
    }

    /**
     * Get the item vehicle
     *
     * @return the item vehicle
     */
    public MinecraftBase getVehicle() {
        return vehicle;
    }

    private double getItemOffset() {
        if (BukkitServer.isOver(Version.v1_9))
            return -0.0D;
        if (BukkitServer.isOver(Version.v1_8))
            return -1.48D;

        return -0.21D;
    }
}
