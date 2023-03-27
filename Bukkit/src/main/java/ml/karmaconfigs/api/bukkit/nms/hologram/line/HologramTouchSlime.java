package ml.karmaconfigs.api.bukkit.nms.hologram.line;

import ml.karmaconfigs.api.bukkit.nms.NMSHelper;
import ml.karmaconfigs.api.bukkit.nms.hologram.Hologram;
import ml.karmaconfigs.api.bukkit.nms.hologram.interfaces.MinecraftBase;
import ml.karmaconfigs.api.bukkit.nms.hologram.interfaces.MinecraftSlime;
import ml.karmaconfigs.api.bukkit.server.BukkitServer;
import ml.karmaconfigs.api.bukkit.server.Version;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Touchable slime
 * Credits to: HolographicDisplays
 */
public class HologramTouchSlime extends HologramLine {

    protected HologramTouchableLine touchable;
    private MinecraftSlime slime;
    private MinecraftBase vehicle;

    /**
     * Initialize the hologram line
     *
     * @param holder the hologram holder
     * @param piece the touchable piece
     */
    protected HologramTouchSlime(final Hologram holder, final HologramTouchableLine piece) {
        super(0.5, holder);
        touchable = piece;
    }

    /**
     * Get the touchable piece of the touch slime
     *
     * @return the touchable piece
     */
    public HologramTouchableLine getTouchable() {
        return touchable;
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
    public void spawn(World world, double x, double y, double z) {
        super.spawn(world, x, y, z);
        double offset = slimeOffset();
        slime = NMSHelper.invokeSlime(world, x, y + offset, z, this);
        if (BukkitServer.isOver(Version.v1_8)) {
            vehicle = NMSHelper.invokeStand(world, x, y + offset, z, this);
        } else {
            vehicle = NMSHelper.invokeSkull(world, x, y + offset, z, this);
        }

        slime.setPassengerOf(vehicle);
        slime.setLockTick(true);
        vehicle.setLockTick(true);
    }

    /**
     * De-spawn the line
     */
    @Override
    public void deSpawn() {
        super.deSpawn();
        if (slime != null) {
            slime.killMinecraftEntity();
            slime = null;
        }
        if (vehicle != null) {
            vehicle.killMinecraftEntity();
            vehicle = null;
        }
    }

    /**
     * Get the line entities
     *
     * @return the line entities
     */
    @Override
    public int[] getEntities() {
        if (exists()) {
            int[] ids = new int[2];
            if (slime != null) {
                ids[0] = slime.getMinecraftID();
            }
            if (vehicle != null) {
                ids[1] = vehicle.getMinecraftID();
            }

            return ids;
        }
        return new int[0];
    }

    /**
     * Get the slime
     *
     * @return the slime
     */
    public MinecraftSlime getSlime() {
        return slime;
    }

    /**
     * Get the slime vehicle
     *
     * @return the slime vehicle
     */
    public MinecraftBase getVehicle() {
        return vehicle;
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
        double offset = slimeOffset();

        if (vehicle != null)
            vehicle.setMinecraftLocation(x, y + offset, z);
        if (slime != null)
            slime.setMinecraftLocation(x, y + offset, z);
    }

    private double slimeOffset() {
        if (BukkitServer.isOver(Version.v1_9))
            return -0.01;
        if (BukkitServer.isOver(Version.v1_8))
            return -1.49;

        return -0.22;
    }
}
