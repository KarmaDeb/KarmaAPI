package ml.karmaconfigs.api.bukkit.region.flag.types;

import ml.karmaconfigs.api.bukkit.region.flag.RegionFlag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Region flag
 */
public final class StringFlag extends RegionFlag<String[]> {

    private final String key;
    private final List<String> texts = new ArrayList<>();

    /**
     * Initialize the state flag
     *
     * @param name    the flag key
     * @param initial the initial values
     */
    public StringFlag(final String name, final String... initial) {
        key = name;
        texts.addAll(Arrays.asList(initial));
    }

    /**
     * Add a text to the flag
     *
     * @param text the text to add
     */
    public void addText(final String text) {
        texts.add(text);
    }

    /**
     * Remove a text
     *
     * @param text the text to remove
     */
    public void removeText(final String text) {
        texts.remove(text);
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
    public String[] getValue() {
        return texts.toArray(new String[0]);
    }

    /**
     * Update the flag
     *
     * @param newValue the new value
     */
    @Override
    public void update(final String[] newValue) {
        texts.clear();
        texts.addAll(Arrays.asList(newValue));
    }

    /**
     * Update the flag unsafely
     *
     * @param newValue the new value
     * @return if the value could be set
     */
    @Override
    public boolean updateUnsafe(final Object newValue) {
        boolean valid = false;

        if (newValue instanceof Collection) {
            Collection<?> collection = (Collection<?>) newValue;

            for (Object object : collection) {
                if (object instanceof CharSequence) {
                    CharSequence sequence = (CharSequence) object;
                    valid = true;
                    texts.add(sequence.toString());
                }
            }

            return true;
        }
        if (newValue != null && newValue.getClass().isArray()) {
            assert newValue instanceof Object[];
            Object[] objects = (Object[]) newValue;

            for (Object object : objects) {
                if (object instanceof CharSequence) {
                    CharSequence sequence = (CharSequence) object;
                    valid = true;
                    texts.add(sequence.toString());
                }
            }
        }
        if (newValue instanceof CharSequence) {
            CharSequence sequence = (CharSequence) newValue;
            texts.add(sequence.toString());
            valid = true;
        }

        return valid;
    }

    /**
     * Get the flag type
     *
     * @return the flag type
     */
    @Override
    public Class<? extends String[]> getType() {
        return String[].class;
    }
}
