package ml.karmaconfigs.api.bukkit.tracker;

import ml.karmaconfigs.api.bukkit.tracker.property.PropertyValue;
import ml.karmaconfigs.api.bukkit.tracker.property.flag.TrackerFlag;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;

/**
 * Tracking armor stand
 */
public abstract class Tracker {

    private static int global_id = 0;
    protected final int id;

    {
        id = global_id++;
    }

    /**
     * Get the tracker id
     *
     * @return the tracker id
     */
    public final int getID() {
        return id;
    }

    /**
     * Get the tracking entity
     *
     * @return the tracking entity
     */
    public abstract LivingEntity getTracking();

    /**
     * Get the tracker auto tracker
     *
     * @return the tracker auto tracker
     */
    public abstract AutoTracker getTracker();

    /**
     * Set the tracker property
     *
     * @param property the property name
     * @param value the property value
     * @return this instance
     * @deprecated Trackers won't have properties anymore, they will use
     * {@link TrackerFlag} instead. Which are better to "fetch". Use the method
     * {@link Tracker#setProperty(PropertyValue)} instead of this
     */
    @Deprecated
    public abstract Tracker setProperty(final String property, final Object value);

    /**
     * Set the tracker property
     *
     * PLEASE NOTE: A correctly setup {@link Tracker} should have
     * all the expected properties with a default value. Otherwise, people
     * could be able to mess up. So this method should be only an alternative
     * to {@link Tracker#getProperty(TrackerFlag, String)} and then {@link PropertyValue#update(Object)} or
     * {@link PropertyValue#updateUnsafe(Object)} methods respectively
     *
     * @param property the property
     * @return this instance
     * @param <T> the property type
     */
    public abstract <T> Tracker setProperty(final PropertyValue<T> property);

    /**
     * Get the tracker property
     *
     * @param flag the flag to get for
     * @param name the property name
     * @return the tracker property
     * @param <T> the property type
     */
    public abstract <T> PropertyValue<T> getProperty(final TrackerFlag flag, final String name);

    /**
     * Get if the tracker has line of sigh for the
     * entity
     *
     * @return if the tracker has line of sigh for the entity
     */
    public abstract boolean hasLineOfSight();

    /**
     * Get the tracker location
     *
     * @return the tracker location
     */
    public abstract Location getLocation();

    /**
     * Get the tracker world
     *
     * @return the tracker world
     */
    public abstract World getWorld();

    /**
     * Get the tracker entity
     *
     * @return the tracker entity
     */
    public abstract LivingEntity getEntity();

    /**
     * Get the direction in where the player is
     *
     * @return the direction in where the player is
     */
    public abstract Vector getDirection();

    /**
     * Set up the tracker auto tracker. Once the auto
     * tracker is set. The method {@link Tracker#setTracking(LivingEntity)}
     * will be locked and no longer work unless we set our
     * auto tracker to null.
     *
     * @param tracker the auto tracker
     */
    public abstract void setAutoTracker(final AutoTracker tracker);

    /**
     * Set the tracking entity
     *
     * @param entity the entity to track
     */
    public abstract void setTracking(final LivingEntity entity);

    /**
     * Start the track task
     */
    public abstract void start();

    /**
     * Update the tracker
     */
    public abstract void update();

    /**
     * Reset the tracking time
     */
    public abstract void resetTime();

    /**
     * Destroy the tracking stand
     */
    public abstract void destroy();

    /**
     * Kill the tracker entity
     * @deprecated As of build of 16/10/2022 this does the same as
     * {@link Tracker#destroy()}. Previously, destroy would kill the
     * tracker entity, and also remove from memory. Now it will be only
     * killed to keep a solid respawn value at {@link ml.karmaconfigs.api.bukkit.tracker.event.TrackerSpawnEvent}
     */
    @Deprecated
    public @ApiStatus.ScheduledForRemoval(inVersion = "1.3.4-SNAPSHOT") abstract void kill();

    /**
     * If the tracker gets killed, this method
     * should "re-spawn" it
     */
    public abstract void respawn();

    /**
     * Create a random period
     */
    public final long randomPeriod(final int max) {
        return (long) ((Math.random() * (Math.max(10, Math.abs(max)) - 1)) + 1);
    }
}
