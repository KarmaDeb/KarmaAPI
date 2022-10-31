package ml.karmaconfigs.api.bukkit.tracker.property;

import ml.karmaconfigs.api.bukkit.tracker.property.flag.TrackerFlag;

/**
 * Property value
 *
 * @param <T> the value type
 */
public abstract class PropertyValue<T> {

    /**
     * Get if the object matches the value type
     *
     * @param obj the object
     * @return if the object matches the value type
     */
    public abstract boolean matches(final Object obj);

    /**
     * Update the value
     *
     * @param value the new value
     */
    public abstract void update(final T value);

    /**
     * Update the value without knowing exactly
     * the property value type
     *
     * @param value the value to insert
     */
    public abstract void updateUnsafe(final Object value);

    /**
     * Set the property identifier
     *
     * @param name the property identifier name
     * @return this instance
     */
    protected abstract PropertyValue<T> setIdentifier(final String name);

    /**
     * Get the value
     *
     * @return the value
     */
    public abstract T getValue();

    /**
     * Get the value forcing it to be
     * the desired value
     *
     * @return the forced value
     * @param <U> the unsafe type
     */
    public abstract <U> U getUnsafe();

    /**
     * Get the property identifier
     *
     * @return the property identifier
     */
    public abstract String getIdentifier();

    /**
     * Get the property name
     *
     * @return the property name
     */
    public abstract String getName();

    /**
     * Get the property flag
     *
     * @return the property flag
     */
    public abstract TrackerFlag getFlag();
}
