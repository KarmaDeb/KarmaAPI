package ml.karmaconfigs.api.bukkit.region.flag.types;

import ml.karmaconfigs.api.bukkit.region.flag.FlagState;
import ml.karmaconfigs.api.bukkit.region.flag.RegionFlag;

/**
 * Region flag
 */
public final class StateFlag extends RegionFlag<FlagState> {

    private final String key;
    private FlagState state = FlagState.DEFAULT;

    /**
     * Initialize the state flag
     *
     * @param name  the flag key
     * @param value the flag value
     */
    public StateFlag(final String name, final FlagState value) {
        key = name;
        state = value;
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
    public FlagState getValue() {
        return state;
    }

    /**
     * Update the flag
     *
     * @param newValue the new value
     */
    @Override
    public void update(final FlagState newValue) {
        state = newValue;
    }

    /**
     * Update the flag unsafely
     *
     * @param newValue the new value
     * @return if the value could be set
     */
    @Override
    public boolean updateUnsafe(final Object newValue) {
        if (newValue instanceof FlagState) {
            state = (FlagState) newValue;
            return true;
        }
        if (newValue instanceof Boolean) {
            boolean bool = (boolean) newValue;
            state = (bool ? FlagState.ALLOW : FlagState.DENY);
            return true;
        }
        if (newValue == null) {
            state = FlagState.DEFAULT;
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
    public Class<? extends FlagState> getType() {
        return FlagState.class;
    }
}
