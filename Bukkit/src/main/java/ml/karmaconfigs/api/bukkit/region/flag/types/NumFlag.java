package ml.karmaconfigs.api.bukkit.region.flag.types;

import ml.karmaconfigs.api.bukkit.region.flag.RegionFlag;

/**
 * Region flag
 */
public final class NumFlag extends RegionFlag<Number> {

    private final String key;
    private Number number = null;

    /**
     * Initialize the state flag
     *
     * @param name  the flag key
     * @param value the flag value
     */
    public NumFlag(final String name, final Number value) {
        key = name;
        number = value;
    }

    /**
     * Get the region flag key
     *
     * @return the region flag key
     */
    @Override
    public String getKey() {
        return key;
    }

    /**
     * Get the region flag value
     *
     * @return the region flag value
     */
    @Override
    public Number getValue() {
        return (number == null ? 0 : number);
    }

    /**
     * Update the flag
     *
     * @param newValue the new value
     */
    @Override
    public void update(final Number newValue) {
        number = newValue;
    }

    /**
     * Update the flag unsafely
     *
     * @param newValue the new value
     * @return if the value could be set
     */
    @Override
    public boolean updateUnsafe(final Object newValue) {
        if (newValue instanceof Number) {
            number = (Number) newValue;
            return true;
        }

        return false;
    }

    /**
     * Get the flag type
     *
     * @return the flag type
     */
    @Override
    public Class<? extends Number> getType() {
        return Number.class;
    }
}
