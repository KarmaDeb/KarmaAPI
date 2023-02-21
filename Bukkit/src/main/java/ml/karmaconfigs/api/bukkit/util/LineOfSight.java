package ml.karmaconfigs.api.bukkit.util;

import ml.karmaconfigs.api.bukkit.util.sight.SightPart;

/**
 * Line of sight entity detection. Better than minecraft's one 😳
 */
public abstract class LineOfSight {

    /**
     * Set the line of sight precision
     *
     * @param precision the sight precision
     * @return this line of sight
     */
    public abstract LineOfSight precision(final double precision);

    /**
     * Get line of sight
     *
     * @return the line of sight
     */
    public abstract SightPart getLineOfSight();

    /**
     * Get line of sight with max distance
     *
     * @param max_distance the maximum distance
     * @return the line of sight inside the range
     */
    public abstract SightPart getLineOfSight(final double max_distance);
}
