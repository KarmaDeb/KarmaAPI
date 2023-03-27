package ml.karmaconfigs.api.bukkit.nms.hologram.part;

import ml.karmaconfigs.api.bukkit.nms.hologram.Hologram;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;

public interface Line {

    /**
     * Get the parent hologram of this part
     *
     * @return the hologram parent
     */
    Hologram getParent();

    /**
     * Get the line height
     *
     * @return the line height
     */
    double getHeight();

    /**
     * Remove the part from the hologram
     */
    void removePart();

    /**
     * Get if the line exists
     *
     * @return if the line exists
     */
    boolean exists();

    /**
     * Spawn the line
     *
     * @param location the line location
     */
    void spawn(final Location location);

    /**
     * Spawn the line
     *
     * @param world the line world
     * @param x the line x position
     * @param y the line y position
     * @param z the line z position
     */
    void spawn(final World world, final double x, final double y, final double z);

    /**
     * De-spawn the line
     */
    void deSpawn();

    /**
     * Get the line entities
     *
     * @return the line entities
     */
    int[] getEntities();

    /**
     * Teleport the line to the specified coordinates
     *
     * @param x the x cord
     * @param y the y cord
     * @param z the z cord
     */
    void teleport(final double x, final double y, final double z);
}
