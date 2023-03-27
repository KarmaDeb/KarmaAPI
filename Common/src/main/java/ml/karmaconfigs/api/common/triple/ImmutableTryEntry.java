package ml.karmaconfigs.api.common.triple;

import ml.karmaconfigs.api.common.triple.TriEntry;

/**
 * Simple try entry
 *
 * @param <A> entry key type
 * @param <B> entry value type
 * @param <C> entry secondary value type
 */
public class ImmutableTryEntry<A, B, C> implements TriEntry<A, B, C> {

    private final A key;
    private B value;
    private C secondary;

    /**
     * Initialize the simple entry
     *
     * @param a the key
     * @param b the value
     * @param c the secondary value
     */
    public ImmutableTryEntry(final A a, final B b, final C c) {
        key = a;
        value = b;
        secondary = c;
    }

    /**
     * Returns the key corresponding to this entry.
     *
     * @return the key corresponding to this entry
     * @throws IllegalStateException implementations may, but are not
     *                               required to, throw this exception if the entry has been
     *                               removed from the backing map.
     */
    @Override
    public A getKey() {
        return key;
    }

    /**
     * Returns the value corresponding to this entry.  If the mapping
     * has been removed from the backing map (by the iterator's
     * remove operation), the results of this call are undefined.
     *
     * @return the value corresponding to this entry
     * @throws IllegalStateException implementations may, but are not
     *                               required to, throw this exception if the entry has been
     *                               removed from the backing map.
     */
    @Override
    public B getValue() {
        return value;
    }

    /**
     * Returns the sub value corresponding to this entry.  If the mapping
     * has been removed from the backing map (by the iterator's
     * remove operation), the results of this call are undefined.
     *
     * @return the value corresponding to this entry
     * @throws IllegalStateException implementations may, but are not
     *                               required to, throw this exception if the entry has been
     *                               removed from the backing map.
     */
    @Override
    public C getSub() {
        return secondary;
    }

    /**
     * Replaces the value corresponding to this entry with the specified
     * value (optional operation).  (Writes through to the map.)  The
     * behavior of this call is undefined if the mapping has already been
     * removed from the map (by the iterator's remove operation).
     *
     * @param v new value to be stored in this entry
     * @return old value corresponding to the entry
     * @throws UnsupportedOperationException always
     */
    @Override
    public B setValue(final B v) {
        throw new UnsupportedOperationException();
    }

    /**
     * Replaces the sub value corresponding to this entry with the specified
     * value (optional operation).  (Writes through to the map.)  The
     * behavior of this call is undefined if the mapping has already been
     * removed from the map (by the iterator's remove operation).
     *
     * @param sub new value to be stored in this entry
     * @return old value corresponding to the entry
     * @throws UnsupportedOperationException always
     */
    @Override
    public C setSub(final C sub) {
        throw new UnsupportedOperationException();
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
        return "ImmutableTryEntry@" + hashCode() + "[" + key + ";" + value + ";" + secondary + "]";
    }
}
