package ml.karmaconfigs.api.bukkit.util;

import org.bukkit.Location;

import java.util.Iterator;

/**
 * Line of sight entity detection. Better than minecraft's one 😳
 */
public abstract class LineOfSight {

    /**
     * Get if line of sight
     *
     * @return if line of sight
     */
    public abstract boolean inLineOfSight();

    /**
     * Get if line of sight with max distance
     *
     * @param max_distance the maximum distance
     * @return if line of sight inside the range
     */
    public abstract boolean inLineOfSight(final double max_distance);

    /**
     * Get line of sight
     *
     * @return the line of sight
     */
    public abstract Iterator<Location> getLineOfSight();

    /**
     * Get line of sight with max distance
     *
     * @param max_distance the maximum distance
     * @return the line of sight inside the range
     */
    public abstract Iterator<Location> getLineOfSight(final double max_distance);
}
