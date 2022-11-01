package ml.karmaconfigs.api.common.karma.file.element;

import ml.karmaconfigs.api.common.karma.file.KarmaMain;

import java.util.Set;

/**
 * Karma section
 */
public abstract class KarmaSection {

    /**
     * Get the main that handles this section
     *
     * @return the section main
     */
    public abstract KarmaMain getMain();

    /**
     * Get the section parent
     *
     * @return the parent section
     */
    public abstract String getParent();

    /**
     * Get the section path
     *
     * @return the section path
     */
    public abstract String getPath();

    /**
     * Get the section name
     *
     * @return the section name
     */
    public abstract String getName();

    /**
     * Get the section children if any
     *
     * @param concurrent iterate also with children child
     * @return the section children
     */
    public abstract Set<String> getChildren(final boolean concurrent);

    /**
     * Get a section children
     *
     * @param childName the child section name
     * @return the child section
     */
    public abstract KarmaSection getChildren(final String childName);

    /**
     * Get a value
     *
     * @param key the value key
     * @return the value
     */
    public abstract KarmaElement get(final String key);

    /**
     * Get a value
     *
     * @param key the value key
     * @param def the default value
     * @return the value
     */
    public abstract KarmaElement get(final String key, final KarmaElement def);

    /**
     * Get a key
     *
     * @param element the key value
     * @return the key
     */
    public abstract String get(final KarmaElement element);

    /**
     * Get a key
     *
     * @param element the key value
     * @param def     the default key
     * @return the key
     */
    public abstract String get(final KarmaElement element, final String def);

    /**
     * Get if a key element is recursive
     *
     * @param key the key
     * @return if the key element is recursive
     */
    public abstract boolean isRecursive(final String key);

    /**
     * Get if a element is recursive
     *
     * @param element the element
     * @return if the element is recursive
     */
    public abstract boolean isRecursive(final KarmaElement element);

    /**
     * Get if a key is set
     *
     * @param key the key to find
     * @return if the key is set
     */
    public abstract boolean isSet(final String key);
}
