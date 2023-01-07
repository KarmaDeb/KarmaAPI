package ml.karmaconfigs.api.bukkit.region.flag.types;

import ml.karmaconfigs.api.bukkit.region.flag.RegionFlag;

/**
 * Region flag
 */
public final class TextFlag extends RegionFlag<String> {

    private final String key;
    private String text = null;

    /**
     * Initialize the state flag
     *
     * @param name  the flag key
     * @param value the flag value
     */
    public TextFlag(final String name, final String value) {
        key = name;
        text = value;
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
    public String getValue() {
        return text;
    }

    /**
     * Update the flag
     *
     * @param newValue the new value
     */
    @Override
    public void update(final String newValue) {
        text = newValue;
    }

    /**
     * Update the flag unsafely
     *
     * @param newValue the new value
     * @return if the value could be set
     */
    @Override
    public boolean updateUnsafe(final Object newValue) {
        if (newValue instanceof CharSequence) {
            CharSequence sequence = (CharSequence) newValue;
            text = sequence.toString();
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
    public Class<? extends String> getType() {
        return String.class;
    }
}
