package ml.karmaconfigs.api.common.karma.file.element.types.primitive;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StringPrimitive implements PrimitiveType<String> {

    private final String value;

    /**
     * Initialize the primitive
     *
     * @param v the primitive value
     */
    public StringPrimitive(final String v) {
        value = v;
    }

    /**
     * Initialize the primitive
     *
     * @param data the primitive value
     */
    public StringPrimitive(final byte[] data) {
        value = new String(data, StandardCharsets.UTF_8);
    }

    /**
     * Initialize the primitive
     *
     * @param data the primitive value
     * @param charsets the value charset
     */
    public StringPrimitive(final byte[] data, final Charset charsets) {
        value = new String(data, charsets);
    }

    /**
     * Get the element
     *
     * @return the element
     */
    @Override
    public String get() {
        return value;
    }

    /**
     * Get the primitive type
     *
     * @return the primitive type
     */
    @Override
    public ObjectType type() {
        return ObjectType.TEXT;
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
