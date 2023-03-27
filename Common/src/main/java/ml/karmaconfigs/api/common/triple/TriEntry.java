package ml.karmaconfigs.api.common.triple;

/**
 * Tri entry
 *
 * @param <K> entry key type
 * @param <V> entry value type
 * @param <S> entry secondary value type
 */
public interface TriEntry<K,V,S> {

    /**
     * Returns the key corresponding to this entry.
     *
     * @return the key corresponding to this entry
     * @throws IllegalStateException implementations may, but are not
     *         required to, throw this exception if the entry has been
     *         removed from the backing map.
     */
    K getKey();

    /**
     * Returns the value corresponding to this entry.  If the mapping
     * has been removed from the backing map (by the iterator's
     * remove operation), the results of this call are undefined.
     *
     * @return the value corresponding to this entry
     * @throws IllegalStateException implementations may, but are not
     *         required to, throw this exception if the entry has been
     *         removed from the backing map.
     */
    V getValue();

    /**
     * Returns the sub value corresponding to this entry.  If the mapping
     * has been removed from the backing map (by the iterator's
     * remove operation), the results of this call are undefined.
     *
     * @return the value corresponding to this entry
     * @throws IllegalStateException implementations may, but are not
     *         required to, throw this exception if the entry has been
     *         removed from the backing map.
     */
    S getSub();

    /**
     * Replaces the value corresponding to this entry with the specified
     * value (optional operation).  (Writes through to the map.)  The
     * behavior of this call is undefined if the mapping has already been
     * removed from the map (by the iterator's remove operation).
     *
     * @param value new value to be stored in this entry
     * @return old value corresponding to the entry
     * @throws UnsupportedOperationException if the put operation
     *         is not supported by the backing map
     * @throws ClassCastException if the class of the specified value
     *         prevents it from being stored in the backing map
     * @throws NullPointerException if the backing map does not permit
     *         null values, and the specified value is null
     * @throws IllegalArgumentException if some property of this value
     *         prevents it from being stored in the backing map
     * @throws IllegalStateException implementations may, but are not
     *         required to, throw this exception if the entry has been
     *         removed from the backing map.
     */
    V setValue(final V value);

    /**
     * Replaces the sub value corresponding to this entry with the specified
     * value (optional operation).  (Writes through to the map.)  The
     * behavior of this call is undefined if the mapping has already been
     * removed from the map (by the iterator's remove operation).
     *
     * @param sub new value to be stored in this entry
     * @return old value corresponding to the entry
     * @throws UnsupportedOperationException if the put operation
     *         is not supported by the backing map
     * @throws ClassCastException if the class of the specified value
     *         prevents it from being stored in the backing map
     * @throws NullPointerException if the backing map does not permit
     *         null values, and the specified value is null
     * @throws IllegalArgumentException if some property of this value
     *         prevents it from being stored in the backing map
     * @throws IllegalStateException implementations may, but are not
     *         required to, throw this exception if the entry has been
     *         removed from the backing map.
     */
    S setSub(final S sub);

    /**
     * Compares the specified object with this entry for equality.
     * Returns true if the given object is also a map entry and
     * the two entries represent the same mapping.  More formally, two
     * entries e1 and e2 represent the same mapping
     * if<pre>
     *     (e1.getKey()==null ?
     *      e2.getKey()==null : e1.getKey().equals(e2.getKey()))  &amp;&amp;
     *     (e1.getValue()==null ?
     *      e2.getValue()==null : e1.getValue().equals(e2.getValue()))
     * </pre>
     * This ensures that the equals method works properly across
     * different implementations of the Map.Entry interface.
     *
     * @param o object to be compared for equality with this map entry
     * @return true if the specified object is equal to this map
     *         entry
     */
    boolean equals(Object o);

    /**
     * Returns the hash code value for this map entry.  The hash code
     * of a map entry e is defined to be: <pre>
     *     (e.getKey()==null   ? 0 : e.getKey().hashCode()) ^
     *     (e.getValue()==null ? 0 : e.getValue().hashCode())
     * </pre>
     * This ensures that e1.equals(e2) implies that
     * e1.hashCode()==e2.hashCode() for any two Entries
     * e1 and e2, as required by the general
     * contract of Object.hashCode.
     *
     * @return the hash code value for this map entry
     * @see Object#hashCode()
     * @see Object#equals(Object)
     * @see #equals(Object)
     */
    int hashCode();

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
    String toString();
}
