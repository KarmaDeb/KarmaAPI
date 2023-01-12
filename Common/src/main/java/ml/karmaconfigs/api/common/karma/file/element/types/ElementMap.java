package ml.karmaconfigs.api.common.karma.file.element.types;

import ml.karmaconfigs.api.common.karma.file.element.types.primitive.PrimitiveType;

import java.util.Map;
import java.util.function.Consumer;

public interface ElementMap<T extends Element<?>> extends Element<Map<String, T>>, Iterable<T> {

    /**
     * Set a map value
     *
     * @param key the value key
     * @param value the elements to add
     */
    void put(final String key, final PrimitiveType<?> value);

    /**
     * Set a map value
     *
     * @param key the value key
     * @param value the elements to add
     */
    void put(final String key, final ElementPrimitive value);

    /**
     * Set a map value
     *
     * @param key the value key
     * @param value the elements to add
     */
    void put(final String key, final String value);

    /**
     * Set a map value
     *
     * @param key the value key
     * @param value the elements to add
     */
    void put(final String key, final Number value);

    /**
     * Set a map value
     *
     * @param key the value key
     * @param value the elements to add
     */
    void put(final String key, final Boolean value);

    /**
     * Set a map value
     *
     * @param key the value key
     * @param value the elements to add
     */
    void put(final String key, final Character value);

    /**
     * Set a map value
     *
     * @param key the value key
     * @param value the elements to add
     */
    void put(final String key, final Byte value);

    /**
     * Set a map value that can be retrieved by its value
     *
     * @param key the value key
     * @param value the elements to add
     */
    void putRecursive(final String key, final PrimitiveType<?> value);

    /**
     * Set a map value that can be retrieved by its value
     *
     * @param key the value key
     * @param value the elements to add
     */
    void putRecursive(final String key, final ElementPrimitive value);

    /**
     * Set a map value that can be retrieved by its value
     *
     * @param key the value key
     * @param value the elements to add
     */
    void putRecursive(final String key, final String value);

    /**
     * Set a map value that can be retrieved by its value
     *
     * @param key the value key
     * @param value the elements to add
     */
    void putRecursive(final String key, final Number value);

    /**
     * Set a map value that can be retrieved by its value
     *
     * @param key the value key
     * @param value the elements to add
     */
    void putRecursive(final String key, final Boolean value);

    /**
     * Set a map value that can be retrieved by its value
     *
     * @param key the value key
     * @param value the elements to add
     */
    void putRecursive(final String key, final Character value);

    /**
     * Set a map value that can be retrieved by its value
     *
     * @param key the value key
     * @param value the elements to add
     */
    void putRecursive(final String key, final Byte value);

    /**
     * Remove a value
     *
     * @param key the element key to remove
     */
    void remove(final String key);

    /**
     * Check if the map contains the specified key
     *
     * @param key the key
     * @return if the map contains the key
     */
    boolean containsKey(final String key);

    /**
     * Get if the map contains the element
     *
     * @param element the element
     * @return if the map contains the element
     */
    boolean containsValue(final Element<?> element);

    /**
     * Check if the key exists and if its value is the same
     * as the one provided
     *
     * @param key the element key
     * @param element the element
     * @return if the key exists and its value is the one provided
     */
    boolean contains(final String key, final Element<?> element);

    /**
     * Check if the key is bidirectional
     *
     * @param key the key
     * @return if the key is bidirectional
     */
    boolean isRecursive(final String key);

    /**
     * Get if the element is bidirectional
     *
     * @param element the element
     * @return if the element is bidirectional
     */
    boolean isRecursive(final Element<?> element);

    /**
     * Get an element at the specified key
     *
     * @param key the element key
     * @return the element
     */
    T get(final String key);

    /**
     * Get a key by its value
     *
     * @param value the key value
     * @return the key
     */
    String get(final T value);

    /**
     * Get this map but with its contents lower case
     *
     * @return the map lower case
     */
    ElementMap<T> contentsToLowerCase();

    /**
     * Get this map but with its contents upper case
     *
     * @return the map upper case
     */
    ElementMap<T> contentsToUpperCase();

    /**
     * Run an action for each map key
     *
     * @param consumer the action to perform
     */
    void forEachKey(final Consumer<String> consumer);

    /**
     * Get the map size
     *
     * @return the map size
     */
    int getSize();

    /**
     * Get if the element is primitive
     *
     * @return if the element is primitive
     */
    @Override
    default boolean isPrimitive() {
        return false;
    }

    /**
     * Get if the element is an array
     *
     * @return if the element is an array
     */
    @Override
    default boolean isArray() {
        return false;
    }

    /**
     * Get if the element is a map
     *
     * @return if the element is a map
     */
    @Override
    default boolean isMap() {
        return true;
    }

    /**
     * Get if the element is a null object
     *
     * @return if the element is a null object
     */
    @Override
    default boolean isElementNull() { return false; }
}
