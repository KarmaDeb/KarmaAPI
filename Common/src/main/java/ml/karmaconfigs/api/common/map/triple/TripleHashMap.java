package ml.karmaconfigs.api.common.map.triple;

import ml.karmaconfigs.api.common.triple.ImmutableTryEntry;
import ml.karmaconfigs.api.common.triple.TriEntry;

import java.util.*;

public class TripleHashMap<K, V, S> implements TriMap<K, V, S> {

    private Object[][] data = new Object[20][3];

    /**
     * Returns the number of key-value mappings in this map.  If the
     * map contains more than Integer.MAX_VALUE elements, returns
     * Integer.MAX_VALUE.
     *
     * @return the number of key-value mappings in this map
     */
    @Override
    public int size() {
        return data.length;
    }

    /**
     * Returns true if this map contains no key-value mappings.
     *
     * @return true if this map contains no key-value mappings
     */
    @Override
    public boolean isEmpty() {
        for (Object[] d : data) {
            if (d != null && d[0] != null) {
                return false;
            }
        }

        return false;
    }

    /**
     * Returns true if this map contains a mapping for the specified
     * key.  More formally, returns true if and only if
     * this map contains a mapping for a key k such that
     * (key==null ? k==null : key.equals(k)).  (There can be
     * at most one such mapping.)
     *
     * @param key key whose presence in this map is to be tested
     * @return true if this map contains a mapping for the specified
     * key
     * @throws ClassCastException   if the key is of an inappropriate type for
     *                              this map
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public boolean containsKey(final K key) {
        for (Object[] info : data) {
            if (info != null && info[0] != null) {
                return info[0].equals(key);
            }
        }

        return false;
    }

    /**
     * Returns true if this map maps one or more keys to the
     * specified value.  More formally, returns true if and only if
     * this map contains at least one mapping to a value v such that
     * (value==null ? v==null : value.equals(v)).  This operation
     * will probably require time linear in the map size for most
     * implementations of the Map interface.
     *
     * @param value value whose presence in this map is to be tested
     * @return true if this map maps one or more keys to the
     * specified value
     * @throws ClassCastException   if the value is of an inappropriate type for
     *                              this map
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified value is null and this
     *                              map does not permit null values
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public boolean containsValue(final V value) {
        for (Object[] info : data) {
            if (info != null && info[1] != null) {
                return info[1].equals(value);
            }
        }

        return false;
    }

    /**
     * Returns true if this map maps one or more keys to the
     * specified value.  More formally, returns true if and only if
     * this map contains at least one mapping to a value v such that
     * (value==null ? v==null : value.equals(v)).  This operation
     * will probably require time linear in the map size for most
     * implementations of the Map interface.
     *
     * @param sub value whose presence in this map is to be tested
     * @return true if this map maps one or more keys to the
     * specified value
     * @throws ClassCastException   if the value is of an inappropriate type for
     *                              this map
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified value is null and this
     *                              map does not permit null values
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public boolean containsSub(final S sub) {
        for (Object[] info : data) {
            if (info != null && info[2] != null) {
                return info[2].equals(sub);
            }
        }

        return false;
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * <p>More formally, if this map contains a mapping from a key
     * {@code k} to a value {@code v} such that {@code (key==null ? k==null :
     * key.equals(k))}, then this method returns {@code v}; otherwise
     * it returns {@code null}.  (There can be at most one such mapping.)
     *
     * <p>If this map permits null values, then a return value of
     * {@code null} does not <i>necessarily</i> indicate that the map
     * contains no mapping for the key; it's also possible that the map
     * explicitly maps the key to {@code null}.  The {@link #containsKey
     * containsKey} operation may be used to distinguish these two cases.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or
     * {@code null} if this map contains no mapping for the key
     * @throws ClassCastException   if the key is of an inappropriate type for
     *                              this map
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    @SuppressWarnings("unchecked")
    public Map.Entry<V, S> get(final K key) {
        if (key != null) {
            for (Object[] info : data) {
                if (info != null && info[0] != null) {
                    if (info[0].equals(key)) {
                        Object value = info[1];
                        Object secondary = info[2];

                        return new AbstractMap.SimpleEntry<>((V) value, (S) secondary);
                    }
                }
            }
        }

        return new AbstractMap.SimpleEntry<>(null, null);
    }

    /**
     * Associates the specified value with the specified key in this map
     * (optional operation).  If the map previously contained a mapping for
     * the key, the old value is replaced by the specified value.  (A map
     * m is said to contain a mapping for a key k if and only
     * if {@link #containsKey(Object)} containsKey(k)} would return
     * true.)
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @param sub   the sub value to be associated with the specified value
     * @return the previous value associated with key, or
     * null if there was no mapping for key.
     * (A null return can also indicate that the map
     * previously associated null with key,
     * if the implementation supports null values.)
     * @throws UnsupportedOperationException if the put operation
     *                                       is not supported by this map
     * @throws ClassCastException            if the class of the specified key or value
     *                                       prevents it from being stored in this map
     * @throws NullPointerException          if the specified key or value is null
     *                                       and this map does not permit null keys or values
     * @throws IllegalArgumentException      if some property of the specified key
     *                                       or value prevents it from being stored in this map
     */
    @Override
    public Map.Entry<V, S> put(final K key, final V value, final S sub) {
        if (key != null) {
            for (Object[] info : data) {
                if (info != null && info[0] != null) {
                    if (info[0].equals(key)) {
                        info[1] = value;
                        info[2] = sub;

                        return new AbstractMap.SimpleEntry<>(value, sub);
                    }
                }
            }

            int empty_index = -1;
            for (int i = 0; i < data.length; i++) {
                Object[] info = data[i];
                if (info == null || info[0] == null) {
                    empty_index = i;
                    break;
                }
            }

            if (empty_index == -1) {
                data = Arrays.copyOf(data, data.length + 20);
                empty_index = 0;
            }

            data[empty_index] = new Object[3];
            data[empty_index][0] = key;
            data[empty_index][1] = value;
            data[empty_index][2] = sub;

            return new AbstractMap.SimpleEntry<>(value, sub);
        }

        return null;
    }

    /**
     * Removes the mapping for a key from this map if it is present
     * (optional operation).   More formally, if this map contains a mapping
     * from key k to value v such that
     * <code>(key==null ?  k==null : key.equals(k))</code>, that mapping
     * is removed.  (The map can contain at most one such mapping.)
     *
     * <p>Returns the value to which this map previously associated the key,
     * or null if the map contained no mapping for the key.
     *
     * <p>If this map permits null values, then a return value of
     * null does not <i>necessarily</i> indicate that the map
     * contained no mapping for the key; it's also possible that the map
     * explicitly mapped the key to null.
     *
     * <p>The map will not contain a mapping for the specified key once the
     * call returns.
     *
     * @param key key whose mapping is to be removed from the map
     * @return the previous value associated with key, or
     * null if there was no mapping for key.
     * @throws UnsupportedOperationException if the remove operation
     *                                       is not supported by this map
     * @throws ClassCastException            if the key is of an inappropriate type for
     *                                       this map
     *                                       (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException          if the specified key is null and this
     *                                       map does not permit null keys
     *                                       (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    @SuppressWarnings("unchecked")
    public Map.Entry<V, S> remove(final K key) {
        if (key != null) {
            for (int i = 0; i < data.length; i++) {
                Object[] info = data[i];

                if (info != null && info[0] != null) {
                    if (info[0].equals(key)) {
                        Map.Entry<V, S> entry = new AbstractMap.SimpleEntry<>((V) info[1], (S) info[2]);
                        data[i] = null;

                        return entry;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Copies all of the mappings from the specified map to this map
     * (optional operation).  The effect of this call is equivalent to that
     * of calling {@link #put(Object, Object, Object)} on this map once
     * for each mapping from key k to value v in the
     * specified map.  The behavior of this operation is undefined if the
     * specified map is modified while the operation is in progress.
     *
     * @param m mappings to be stored in this map
     * @throws UnsupportedOperationException if the putAll operation
     *                                       is not supported by this map
     * @throws ClassCastException            if the class of a key or value in the
     *                                       specified map prevents it from being stored in this map
     * @throws NullPointerException          if the specified map is null, or if
     *                                       this map does not permit null keys or values, and the
     *                                       specified map contains null keys or values
     * @throws IllegalArgumentException      if some property of a key or value in
     *                                       the specified map prevents it from being stored in this map
     */
    @Override
    @SuppressWarnings("unchecked")
    public void putAll(final TriMap<? super K, ? super V, ? super S> m) {
        m.forEach((key, entry) -> {
            put((K) key, (V) entry.getKey(), (S) entry.getValue());
        });
    }

    /**
     * Removes all the mappings from this map (optional operation).
     * The map will be empty after this call returns.
     *
     * @throws UnsupportedOperationException if the clear operation
     *                                       is not supported by this map
     */
    @Override
    public void clear() {
        data = new Object[0][3];
    }

    /**
     * Returns a {@link Set} view of the keys contained in this map.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.  If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own remove operation), the results of
     * the iteration are undefined.  The set supports element removal,
     * which removes the corresponding mapping from the map, via the
     * Iterator.remove, Set.remove,
     * removeAll, retainAll, and clear
     * operations.  It does not support the add or addAll
     * operations.
     *
     * @return a set view of the keys contained in this map
     */
    @Override
    @SuppressWarnings("unchecked")
    public Collection<K> keySet() {
        Set<K> keys = new LinkedHashSet<>();
        for (Object[] info : data) {
            if (info != null && info[0] != null)
                keys.add((K) info[0]);
        }

        return keys;
    }

    /**
     * Returns a {@link Collection} view of the values contained in this map.
     * The collection is backed by the map, so changes to the map are
     * reflected in the collection, and vice-versa.  If the map is
     * modified while an iteration over the collection is in progress
     * (except through the iterator's own remove operation),
     * the results of the iteration are undefined.  The collection
     * supports element removal, which removes the corresponding
     * mapping from the map, via the Iterator.remove,
     * Collection.remove, removeAll,
     * retainAll and clear operations.  It does not
     * support the add or addAll operations.
     *
     * @return a collection view of the values contained in this map
     */
    @Override
    @SuppressWarnings("unchecked")
    public Collection<Map.Entry<V, S>> values() {
        Set<Map.Entry<V, S>> values = new LinkedHashSet<>();
        for (Object[] info : data) {
            if (info != null && info[0] != null)
                values.add(new AbstractMap.SimpleEntry<>((V) info[1], (S) info[2]));
        }

        return values;
    }

    /**
     * Returns a {@link Set} view of the mappings contained in this map.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.  If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own remove operation, or through the
     * setValue operation on a map entry returned by the
     * iterator) the results of the iteration are undefined.  The set
     * supports element removal, which removes the corresponding
     * mapping from the map, via the Iterator.remove,
     * Set.remove, removeAll, retainAll and
     * clear operations.  It does not support the
     * add or addAll operations.
     *
     * @return a set view of the mappings contained in this map
     */
    @Override
    @SuppressWarnings("unchecked")
    public Collection<TriEntry<K, V, S>> entrySet() {
        Set<TriEntry<K, V, S>> values = new LinkedHashSet<>();
        for (Object[] info : data) {
            if (info != null && info[0] != null)
                values.add(new ImmutableTryEntry<>((K) info[0], (V) info[1], (S) info[2]));
        }

        return values;
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
        StringBuilder builder = new StringBuilder("TripleHashMap@")
                .append(hashCode()).append("[");

        Collection<TriEntry<K, V, S>> set = entrySet();
        int index = 0;
        int items = 0;
        for (TriEntry<K, V, S> entries : set) {
            items++;
            if (index == 0) {
                builder.append("\n");
            }
            builder.append('\t').append(entries.getKey()).append(":").append(entries.getValue()).append("=").append(entries.getSub());
            if (index != set.size() - 1) {
                builder.append(",");
                if (items == 3) {
                    builder.append("\n");
                    items = 0;
                }
            }
            index++;
        }

        return builder.append("\n]").toString();
    }
}
