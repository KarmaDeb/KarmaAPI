package ml.karmaconfigs.api.bukkit.region.flag;

/**
 * Region flag
 *
 * @param <T> the region flag type
 */
public abstract class RegionFlag<T> {

    /**
     * Get the region flag key
     *
     * @return the region flag key
     */
    public abstract String getKey();

    /**
     * Get the region flag value
     *
     * @return the region flag value
     */
    public abstract T getValue();

    /**
     * Update the flag
     *
     * @param newValue the new value
     */
    public abstract void update(final T newValue);

    /**
     * Update the flag unsafely
     *
     * @param newValue the new value
     * @return if the value could be set
     */
    public abstract boolean updateUnsafe(final Object newValue);

    /**
     * Get the flag type
     *
     * @return the flag type
     */
    public abstract Class<? extends T> getType();

    /**
     * Get if the other flag matches this flag type
     *
     * @param otherFlag the other flag
     * @return if the flag value types matches
     */
    public boolean matchesType(final RegionFlag<?> otherFlag) {
        return otherFlag.getType().isAssignableFrom(getType()) || getType().isAssignableFrom(otherFlag.getType());
    }
}
