package ml.karmaconfigs.api.common.collection.triple;

import ml.karmaconfigs.api.common.triple.consumer.TriConsumer;
import ml.karmaconfigs.api.common.triple.TriEntry;
import ml.karmaconfigs.api.common.triple.TriTerator;
import ml.karmaconfigs.api.common.triple.ImmutableTryEntry;
import ml.karmaconfigs.api.common.triple.TriAbstract;

import java.nio.BufferOverflowException;
import java.util.*;

@SuppressWarnings("Unusued")
public class TriCollector<A, B, C> implements TriCollection<A, B, C> {

    private Object[][] data;

    private int CONSCIOUS_SIZE = 0;
    private final int SIZE;
    private final int MAX_SIZE = Integer.MAX_VALUE - 8;

    /**
     * Initialize the tri collector
     *
     * @param size the collector size
     * @param increment_size the collector increment size
     * @throws IllegalArgumentException if the size is not a valid size
     */
    public TriCollector(final int size, final int increment_size) throws IllegalArgumentException {
        if (size > 0 && size < MAX_SIZE) {
            SIZE = increment_size;
            data = new Object[size][3];
        } else {
            throw new IllegalArgumentException("Cannot set size under zero or over the max size for a collection");
        }
    }

    /**
     * Initialize the tri collector
     *
     * @param size the collector size
     * @throws IllegalArgumentException if the size is not a valid size
     */
    public TriCollector(final int size) {
        this(size, 10);
    }

    /**
     * Initialize the tri collector
     */
    public TriCollector() {
        this(10, 10);
    }

    /**
     * Add all the items of the other collection to this one
     *
     * @param collection the collection to add
     * @return this instance
     * @throws BufferOverflowException if the size exceeds the maximum size
     * @throws ConcurrentModificationException if the other collection suffered modifications while being read by this
     */
    @Override
    public TriCollector<A, B, C> addAll(final TriCollection<? super A, ? super B, ? super C> collection) throws BufferOverflowException, ConcurrentModificationException {
        collection.forEach((key, value, sub) -> {
            try {
                if (CONSCIOUS_SIZE + 1 == data.length) {
                    data = Arrays.copyOf(data, CONSCIOUS_SIZE + SIZE);
                }

                data[CONSCIOUS_SIZE] = new Object[3];
                data[CONSCIOUS_SIZE][0] = key;
                data[CONSCIOUS_SIZE][1] = value;
                data[CONSCIOUS_SIZE][2] = sub;

                CONSCIOUS_SIZE++;
            } catch (IllegalStateException ex) {
                throw new ConcurrentModificationException();
            }
        });

        return this;
    }

    /**
     * Add data to the collection
     *
     * @param key the data key
     * @param value the data value
     * @param secondary the data secondary value
     * @throws BufferOverflowException if the size exceeds the maximum size
     */
    @Override
    public int add(final A key, final B value, final C secondary) throws BufferOverflowException {
        if (CONSCIOUS_SIZE + 1 == data.length) {
            data = Arrays.copyOf(data, CONSCIOUS_SIZE + SIZE);
        }

        data[CONSCIOUS_SIZE] = new Object[3];
        data[CONSCIOUS_SIZE][0] = key;
        data[CONSCIOUS_SIZE][1] = value;
        data[CONSCIOUS_SIZE][2] = secondary;

        CONSCIOUS_SIZE++;

        return CONSCIOUS_SIZE;
    }

    /**
     * Get if the collection has the specified key
     *
     * @param key the key to check
     * @return of the collection has the key
     */
    @Override
    public boolean contains(final A key) {
        for (int i = 0; i < CONSCIOUS_SIZE; i++) {
            Object k = data[i][0];

            if (k != null) {
                if (k.equals(key)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Set a key, value, index at the specified index
     *
     * @param index the index
     * @param key the key to set
     * @param value the value to set
     * @param secondary_value the secondary value to set
     * @throws NullPointerException if the index has no items
     * @throws ArrayIndexOutOfBoundsException if the index is out of collection size
     */
    @Override
    public void set(int index, A key, B value, C secondary_value) throws NullPointerException, ArrayIndexOutOfBoundsException {
        if (index < data.length) {
            if (key != null) {
                data[index] = new Object[3];
                data[index][0] = key;
                data[index][1] = value;
                data[index][2] = secondary_value;
            } else {
                throw new NullPointerException("Cannot set null key, to remove use remove(index) or removeAll(key) method");
            }
        } else {
            throw new ArrayIndexOutOfBoundsException("Cannot retrieve index " + index + " from collection with size of " + data.length);
        }
    }

    /**
     * Remove a collection item
     *
     * @param index the collection index
     * @return the removed item
     * @throws ArrayIndexOutOfBoundsException if the index is out of collection size
     */
    @Override
    @SuppressWarnings("unchecked")
    public TriEntry<A, B, C> remove(final int index) throws ArrayIndexOutOfBoundsException {
        if (index < data.length) {
            Object key = data[index][0];
            Object value = data[index][1];
            Object secondary = data[index][2];

            data[index][0] = null;
            data[index][1] = null;
            data[index][2] = null;

            CONSCIOUS_SIZE--;

            return new ImmutableTryEntry<>((A) key, (B) value, (C) secondary);
        } else {
            throw new ArrayIndexOutOfBoundsException("Cannot retrieve index " + index + " from collection with size of " + data.length);
        }
    }

    /**
     * Remove all the matching keys
     *
     * @param key the key
     * @return the removed items
     */
    @Override
    @SuppressWarnings("unchecked")
    public Collection<TriEntry<A, B, C>> removeAll(final A key) {
        Set<TriEntry<A, B, C>> set = new HashSet<>();
        for (int i = 0; i < data.length; i++) {
            Object k = data[i][0];
            Object value = data[i][1];
            Object secondary = data[i][2];

            if (k != null) {
                if (k == key || key.equals(k) || key.toString().equals(k.toString()) || key.hashCode() == k.hashCode()) {
                    data[i][0] = null;
                    data[i][1] = null;
                    data[i][2] = null;
                    set.add(new ImmutableTryEntry<>((A) k, (B) value, (C) secondary));

                    CONSCIOUS_SIZE--;
                }
            }
        }

        return set;
    }

    /**
     * Update the collector indexes, by shifting the keys if some null key found.
     */
    public void updateIndexes() {
        int current_null = -1;

        for (int i = 0; i < data.length; i++) {
            Object key = data[i][0];

            if (key == null) {
                if (current_null == -1) {
                    current_null = i;
                }
            } else {
                if (current_null != -1) {
                    data[current_null][0] = data[i][0];
                    data[current_null][1] = data[i][1];
                    data[current_null][2] = data[i][2];
                    data[i][0] = null;
                    data[i][1] = null;
                    data[i][2] = null;

                    i = current_null;
                    current_null = -1;
                }
            }
        }
    }

    /**
     * Get the item at specified index
     *
     * @param index the index to read from
     * @return the collection values at index
     */
    @Override
    public Object[] get(final int index) {
        return data[index].clone();
    }

    /**
     * Get a value and secondary value by its key
     *
     * @param key the key
     * @return the value and its secondary value that matches the key
     */
    @Override
    public Collection<Object[]> get(final A key) {
        Set<Object[]> set = new HashSet<>();
        for (int i = 0; i < CONSCIOUS_SIZE; i++) {
            Object k = data[i][0];

            if (k != null) {
                if (k.equals(key)) {
                    set.add(data[i]);
                }
            } else {
                if (key == null) {
                    set.add(data[i]);
                }
            }
        }

        return set;
    }

    /**
     * Get a secondary value
     *
     * @param key   the secondary value key
     * @param value the secondary value parent
     * @return the secondary value
     */
    @Override
    @SuppressWarnings("unchecked")
    public C get(final A key, final B value) {
        Collection<Object[]> data = get(key);
        for (Object[] info : data) {
            if (info[0].equals(value)) {
                return (C) info[1];
            }
        }

        return null;
    }

    /**
     * Get the collection size
     *
     * @return the collection size
     */
    @Override
    public int getSize() {
        return CONSCIOUS_SIZE;
    }

    /**
     * Get the data allocated size
     *
     * @return the data allocated size
     */
    public int getAllocated() {
        return data.length;
    }

    /**
     * Get the collection max size
     *
     * @return the collection max size
     */
    @Override
    public int getMaxSize() {
        return MAX_SIZE;
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    @SuppressWarnings("unchecked")
    public TriTerator<A, B, C> iterator() {
        Collection<TriEntry<A, B, C>> entries = new ArrayList<>();
        for (int i = 0; i < CONSCIOUS_SIZE; i++) {
            Object key = data[i][0];
            B value = (B) data[i][1];
            C secondary = (C) data[i][2];

            if (key != null) {
                entries.add(new ImmutableTryEntry<>((A) key, value, secondary));
            }
        }

        return new TriAbstract.AbstractIterator<>(entries.toArray(new TriEntry[0]));
    }

    /**
     * Performs the given action for each element of the {@code Iterable}
     * until all elements have been processed or the action throws an
     * exception.  Unless otherwise specified by the implementing class,
     * actions are performed in the order of iteration (if an iteration order
     * is specified).  Exceptions thrown by the action are relayed to the
     * caller.
     *
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
     * @since 1.8
     */
    @Override
    @SuppressWarnings("unchecked")
    public void forEach(final TriConsumer<? super A, ? super B, ? super C> action) {
        for (int i = 0; i < CONSCIOUS_SIZE; i++) {
            Object key = data[i][0];
            Object value = data[i][1];
            Object secondary = data[i][2];

            if (key != null) {
                action.accept((A) key, (B) value, (C) secondary);
            }
        }
    }

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[")
                .append("S:").append(SIZE).append(",")
                .append("MS:").append(MAX_SIZE).append(",")
                .append("GS:").append(data.length).append(",")
                .append("|");
        for (int i = 0; i < CONSCIOUS_SIZE; i++) {
            Object key = data[i][0];
            Object value = data[i][1];
            Object secondary = data[i][2];

            if (key != null) {
                builder.append("(").append(i).append(":").append(key).append(")").append(":").append(value).append(",").append(secondary).append("|");
            }
        }

        builder.append("]");
        return builder.toString();
    }
}
