package ml.karmaconfigs.api.common.collection.triple;

import ml.karmaconfigs.api.common.triple.TriEntry;
import ml.karmaconfigs.api.common.triple.TriTerable;

import java.nio.BufferOverflowException;
import java.util.*;

public interface TriCollection<A, B, C> extends TriTerable<A, B, C> {

    /**
     * Add all the items of the other collection to this one
     *
     * @param collection the collection to add
     * @return this instance
     * @throws BufferOverflowException Implementations may
     * @throws ConcurrentModificationException Implementations may
     */
    TriCollection<A, B, C> addAll(final TriCollection<? super A, ? super B, ? super C> collection);

    /**
     * Add data to the collection
     *
     * @param key the data key
     * @param value the data value
     * @param secondary the data secondary value
     * @throws BufferOverflowException Implementations may
     */
    int add(final A key, final B value, final C secondary);

    /**
     * Get if the collection has the specified key
     *
     * @param key the key to check
     * @return of the collection has the key
     */
    boolean contains(final A key);

    /**
     * Set a key, value, index at the specified index
     *
     * @param index the index
     * @param key the key to set
     * @param value the value to set
     * @param secondary_value the secondary value to set
     * @throws NullPointerException Implementations may
     * @throws ArrayIndexOutOfBoundsException Implementations may
     */
    void set(int index, A key, B value, C secondary_value);

    /**
     * Remove a collection item
     *
     * @param index the collection index
     * @return the removed item
     * @throws ArrayIndexOutOfBoundsException Implementations may
     */
    TriEntry<A, B, C> remove(final int index);

    /**
     * Remove all the matching keys
     *
     * @param key the key
     * @return the removed items
     */
    Collection<TriEntry<A, B, C>> removeAll(final A key);

    /**
     * Get the item at specified index
     *
     * @param index the index to read from
     * @return the collection values at index
     * @throws NullPointerException Implementations may
     * @throws ArrayIndexOutOfBoundsException Implementations may
     */
    Object[] get(final int index);

    /**
     * Get a value and secondary value by its key
     *
     * @param key the key
     * @return the value and its secondary value that matches the key
     * @throws IllegalArgumentException Implementations may
     */
    Collection<Object[]> get(final A key);

    /**
     * Get a secondary value
     *
     * @param key the secondary value key
     * @param value the secondary value parent
     * @return the secondary value
     */
    C get(final A key, final B value);

    /**
     * Get the collection size
     *
     * @return the collection size
     */
    int getSize();

    /**
     * Get the collection max size
     *
     * @return the collection max size
     */
    int getMaxSize();
}
