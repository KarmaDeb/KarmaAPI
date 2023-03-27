package ml.karmaconfigs.api.bukkit.nms.hologram.line;

import ml.karmaconfigs.api.bukkit.nms.hologram.Hologram;
import ml.karmaconfigs.api.bukkit.nms.hologram.part.touch.TouchHandler;
import ml.karmaconfigs.api.bukkit.nms.hologram.part.touch.TouchablePart;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public abstract class HologramTouchableLine extends HologramLine implements TouchablePart {

    protected HologramTouchSlime touch;
    private final List<TouchHandler> handlers = new ArrayList<>();

    /**
     * Initialize the hologram line
     *
     * @param he the line height
     * @param h  the holder
     */
    protected HologramTouchableLine(double he, Hologram h) {
        super(he, h);
    }

    /**
     * Append a touch handler
     *
     * @param handler the handler
     * @param w the world for the touchable slime
     * @param x the x position for the touchable slime
     * @param y the y position for the touchable slime
     * @param z the z position for the touchable slime
     */
    protected void appendTouchHandler(final TouchHandler handler, final World w, final double x, final double y, final double z) {
        if (handler != null) {
            handlers.add(handler);

            if (touch == null && w != null) {
                touch = new HologramTouchSlime(holder, this);
                touch.spawn(w, x, y + getHeight() / 2.0 - touch.getHeight() / 2.0, z);
            } else {
                handlers.remove(handler);
            }
        }
    }

    /**
     * Get the text part touch handlers
     *
     * @return the hologram touch handlers
     */
    @Override
    public TouchHandler[] getTouchHandlers() {
        return handlers.toArray(new TouchHandler[0]);
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
        if (touch == null) {
            touch = new HologramTouchSlime(holder, this);
            touch.spawn(world, x, y + getHeight() / 2.0D - touch.getHeight() / 2.0D, z);
        }
    }

    /**
     * De-spawn the line
     */
    @Override
    public void deSpawn() {
        super.deSpawn();
        if (touch != null) {
            touch.deSpawn();
            touch = null;
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
        if (touch != null) {
            touch.teleport(x, y + getHeight() / 2.0D - touch.getHeight() / 2.0D, z);
        }
    }

    /**
     * Get the touch slime
     *
     * @return the touch slime
     */
    public HologramTouchSlime getTouch() {
        return touch;
    }
}
