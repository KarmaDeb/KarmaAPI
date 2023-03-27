package ml.karmaconfigs.api.bukkit.nms.hologram.line;

import ml.karmaconfigs.api.bukkit.nms.hologram.Hologram;
import ml.karmaconfigs.api.bukkit.nms.hologram.HologramHolder;
import ml.karmaconfigs.api.bukkit.nms.hologram.part.Line;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;

/**
 * Hologram line
 */
public abstract class HologramLine implements Line {

    private final double height;
    protected final Hologram holder;
    private boolean exists;

    /**
     * Initialize the hologram line
     *
     * @param he the line height
     * @param h the holder
     */
    protected HologramLine(final double he, final Hologram h) {
        Objects.requireNonNull(h, "Hologram holder cannot be null");
        height = he;
        holder = h;
    }

    /**
     * Get the line height
     *
     * @return the line height
     */
    @Override
    public final double getHeight() {
        return height;
    }

    /**
     * Get the parent hologram of this part
     *
     * @return the hologram parent
     */
    @Override
    public final Hologram getParent() {
        return holder;
    }

    /**
     * Remove the part from the hologram
     */
    @Override
    public void removePart() {
        if (holder.remove(this)) {
            deSpawn();
        }
    }

    /**
     * Spawn the line
     *
     * @param location the line location
     */
    @Override
    public void spawn(final Location location) {
        Objects.requireNonNull(location, "Spawn location cannot be null");
        Objects.requireNonNull(location.getWorld(), "Spawn location world cannot be null");
        deSpawn();
        exists = true;
    }

    /**
     * Spawn the line
     *
     * @param world the line world
     * @param x the line x position
     * @param y the line y position
     * @param z the line z position
     */
    @Override
    public void spawn(final World world, final double x, final double y, final double z) {
        Objects.requireNonNull(world, "Spawn world cannot be null");
        deSpawn();
        exists = true;
    }

    /**
     * De-spawn the line
     */
    @Override
    public void deSpawn() {
        exists = false;
    }

    /**
     * Get if the line exists
     *
     * @return if the line exists
     */
    @Override
    public final boolean exists() {
        return exists;
    }
}
