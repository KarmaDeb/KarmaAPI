package ml.karmaconfigs.api.bukkit.nms.hologram.line;

import ml.karmaconfigs.api.bukkit.nms.NMSHelper;
import ml.karmaconfigs.api.bukkit.nms.hologram.Hologram;
import ml.karmaconfigs.api.bukkit.nms.hologram.interfaces.MinecraftBase;
import ml.karmaconfigs.api.bukkit.nms.hologram.interfaces.MinecraftHorse;
import ml.karmaconfigs.api.bukkit.nms.hologram.interfaces.MinecraftNameAble;
import ml.karmaconfigs.api.bukkit.nms.hologram.part.TextPart;
import ml.karmaconfigs.api.bukkit.nms.hologram.part.touch.TouchHandler;
import ml.karmaconfigs.api.bukkit.server.BukkitServer;
import ml.karmaconfigs.api.bukkit.server.Version;
import ml.karmaconfigs.api.common.string.StringUtils;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;

/**
 * Hologram line
 */
public class HologramTextLine extends HologramTouchableLine implements TextPart {

    private String text;

    private MinecraftNameAble nameable;
    private MinecraftBase vehicle;

    /**
     * Initialize the hologram line
     *
     * @param h  the holder
     * @param t the text
     */
    public HologramTextLine(final Hologram h, final String t) {
        super(0.23, h);
        set(t);
    }

    /**
     * Get the line entities
     *
     * @return the line entities
     */
    @Override
    public int[] getEntities() {
        if (exists()) {
            if (vehicle != null) {
                if (touch != null) {
                    int[] touch_entities = touch.getEntities();
                    int[] entities = new int[touch_entities.length + 2];
                    entities[0] = nameable.getMinecraftID();
                    entities[1] = vehicle.getMinecraftID();
                    for (int i = 2; i < entities.length; i++) {
                        entities[i] = touch_entities[i - 2];
                    }

                    return entities;
                }

                return new int[]{nameable.getMinecraftID(), nameable.getMinecraftID()};
            }
            if (touch != null) {
                int[] touch_entities = touch.getEntities();
                int[] entities = new int[touch_entities.length + 1];
                entities[0] = nameable.getMinecraftID();
                for (int i = 1; i < entities.length; i++) {
                    entities[i] = touch_entities[i - 1];
                }
            }

            return new int[] {nameable.getMinecraftID()};
        }

        return new int[0];
    }

    /**
     * Get the name able text line
     *
     * @return the name able text line
     */
    public MinecraftNameAble getNameable() {
        return nameable;
    }

    /**
     * Get the text line vehicle
     *
     * @return the text line vehicle
     */
    public MinecraftBase getVehicle() {
        return vehicle;
    }

    /**
     * Get the hologram part
     *
     * @return the hologram part
     */
    @Override
    public String get() {
        return text;
    }

    /**
     * Set the hologram part value
     *
     * @param param the value
     */
    @Override
    public void set(final String param) {
        text = Objects.toString(param, "");
        if (nameable != null) {
            nameable.setMinecraftName(StringUtils.toColor(text));
        }
    }

    /**
     * Add a touch handler to this text part
     *
     * @param handlers the touch handler
     */
    @Override
    public void addTouchHandler(final TouchHandler... handlers) {
        if (nameable != null) {
            Location location = nameable.asBukkitEntity().getLocation();
            double offset = getTextOffset();
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
        if (BukkitServer.isOver(Version.v1_8)) {
            nameable = NMSHelper.invokeStand(world, x, y + getTextOffset(), z, this);
        } else {
            MinecraftHorse horse = NMSHelper.invokeHorse(world, x, y + 54.56D, z, this);
            Objects.requireNonNull(horse);

            vehicle = NMSHelper.invokeSkull(world, x, y + 54.56D, z, this);
            horse.setPassengerOf(vehicle);
            nameable = horse;

            vehicle.setLockTick(true);
        }

        nameable.setMinecraftName(String.valueOf(Objects.toString(text, "")));
        nameable.setLockTick(true);
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
        if (nameable != null) {
            nameable.killMinecraftEntity();
            nameable = null;
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

        if (vehicle != null)
            vehicle.setMinecraftLocation(x, y + 54.56, z);
        if (nameable != null)
            nameable.setMinecraftLocation(x, y, z);
    }

    private double getTextOffset() {
        if (BukkitServer.isOver(Version.v1_9))
            return -0.29D;
        if (BukkitServer.isOver(Version.v1_8))
            return -1.25D;

        return 54.56D;
    }
}
