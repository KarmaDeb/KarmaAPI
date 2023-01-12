package ml.karmaconfigs.api.common.karma.file.element.types.primitive;

public class CharacterPrimitive implements PrimitiveType<Character> {

    private final char value;

    /**
     * Initialize the primitive
     *
     * @param v the primitive value
     */
    public CharacterPrimitive(final char v) {
        value = v;
    }

    /**
     * Initialize the primitive
     *
     * @param n the primitive value
     */
    public CharacterPrimitive(final int n) {
        value = (char) n;
    }

    /**
     * Get the element
     *
     * @return the element
     */
    @Override
    public Character get() {
        return value;
    }

    /**
     * Get the primitive type
     *
     * @return the primitive type
     */
    @Override
    public ObjectType type() {
        return ObjectType.CHARACTER;
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
        return "\"primitive\" -> '" + value + "'";
    }
}
