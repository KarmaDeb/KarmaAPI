package ml.karmaconfigs.api.common.karma.file.element;

import ml.karmaconfigs.api.common.karma.file.element.types.ElementNull;
import ml.karmaconfigs.api.common.karma.file.element.types.ElementPrimitive;
import ml.karmaconfigs.api.common.karma.file.element.types.primitive.*;

import static ml.karmaconfigs.api.common.karma.file.element.types.primitive.PrimitiveType.ObjectType;

public class KarmaPrimitive implements ElementPrimitive {

    private final PrimitiveType<?> primitive;

    /**
     * Create a primitive
     *
     * @param type the primitive type
     */
    public KarmaPrimitive(final PrimitiveType<?> type) {
        primitive = type;
    }

    /**
     * Create a primitive for the text
     *
     * @param text the text
     */
    public KarmaPrimitive(final String text) {
        if (text != null) {
            primitive = new StringPrimitive(text);
        } else {
            primitive = forNull().primitive;
        }
    }

    /**
     * Create a primitive for the text
     *
     * @param bool the boolean
     */
    public KarmaPrimitive(final boolean bool) {
        primitive = new BooleanPrimitive(bool);
    }

    /**
     * Create a primitive for the number
     *
     * @param number the number
     */
    public KarmaPrimitive(final Number number) {
        if (number != null) {
            primitive = new NumberPrimitive(number);
        } else {
            primitive = forNull().primitive;
        }
    }

    /**
     * Create a primitive for the byte
     *
     * @param b the byte
     */
    public KarmaPrimitive(final byte b) {
        primitive = new BytePrimitive(b);
    }

    /**
     * Create a primitive for a character
     *
     * @param character the character
     */
    public KarmaPrimitive(final char character) {
        primitive = new CharacterPrimitive(character);
    }

    /**
     * Create a primitive for null object
     *
     * @return the primitive
     */
    public static KarmaPrimitive forNull() {
        PrimitiveType<Object> n = new ElementNull() {
            @Override
            public Object get() {
                return null;
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
                return "\"primitive\" -> null";
            }
        };
        return new KarmaPrimitive(n);
    }

    /**
     * Get the element
     *
     * @return the element
     */
    @Override
    public PrimitiveType<?> getValue() {
        return primitive;
    }

    /**
     * Get if the element is a null object
     *
     * @return if the element is a null object
     */
    @Override
    public boolean isElementNull() {
        return primitive.get() instanceof ElementNull;
    }

    /**
     * Get if the primitive element is a string
     *
     * @return if is string
     */
    @Override
    public boolean isString() {
        return primitive.type().equals(ObjectType.TEXT) || primitive.type().equals(ObjectType.TEXT);
    }

    /**
     * Get if the primitive element is a number
     *
     * @return if is number
     */
    @Override
    public boolean isNumber() {
        return primitive.type().equals(ObjectType.NUMBER) || primitive.type().equals(ObjectType.BYTE);
    }

    /**
     * Get if the primitive element is a boolean
     *
     * @return if boolean
     */
    @Override
    public boolean isBoolean() {
        return primitive.type().equals(ObjectType.BOOLEAN);
    }

    /**
     * Get if the primitive element is a character
     *
     * @return if character
     */
    @Override
    public boolean isCharacter() {
        return primitive.type().equals(ObjectType.CHARACTER);
    }

    /**
     * Get the primitive element as a string
     *
     * @return the string primitive
     */
    @Override
    public String asString() {
        /*
            We will allow retrieving a character as a string, as the file format doesn't specify a syntax for
            characters
         */
        if (primitive.type().equals(ObjectType.CHARACTER)) {
            return String.valueOf(primitive.get());
        }

        return (String) primitive.get();
    }

    /**
     * Get the primitive element as a boolean
     *
     * @return the boolean primitive
     */
    @Override
    public boolean asBoolean() {
        return (Boolean) primitive.get();
    }

    /**
     * Get the primitive element as a integer
     *
     * @return the integer primitive
     */
    @Override
    public int asInteger() {
        if (primitive.get() instanceof Number) {
            return ((Number) primitive.get()).intValue();
        }

        return (Integer) primitive.get();
    }

    /**
     * Get the primitive element as a double
     *
     * @return the double primitive
     */
    @Override
    public double asDouble() {
        if (primitive.get() instanceof Number) {
            return ((Number) primitive.get()).doubleValue();
        }

        return (Double) primitive.get();
    }

    /**
     * Get the primitive element as a long
     *
     * @return the long primitive
     */
    @Override
    public long asLong() {
        if (primitive.get() instanceof Number) {
            return ((Number) primitive.get()).longValue();
        }

        return (Long) primitive.get();
    }

    /**
     * Get the primitive element as a short
     *
     * @return the short primitive
     */
    @Override
    public short asShort() {
        if (primitive.get() instanceof Number) {
            return ((Number) primitive.get()).shortValue();
        }

        return (Short) primitive.get();
    }

    /**
     * Get the primitive element as a float
     *
     * @return the character float
     */
    @Override
    public float asFloat() {
        if (primitive.get() instanceof Number) {
            return ((Number) primitive.get()).floatValue();
        }

        return (Float) primitive.get();
    }

    /**
     * Get the primitive element as a byte
     *
     * @return the byte primitive
     */
    @Override
    public byte asByte() {
        if (primitive.get() instanceof Number) {
            return ((Number) primitive.get()).byteValue();
        }

        return (Byte) primitive.get();
    }

    /**
     * Get the primitive element as a character
     *
     * @return the character primitive
     */
    @Override
    public char asCharacter() {
        return (Character) primitive.get();
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
        String value = String.valueOf(primitive.get());

        if (primitive.get() instanceof Double || primitive.get() instanceof Float) {
            return (primitive.get() instanceof Double ? value.replace(".", ",") : value);
        }
        if (primitive.get() instanceof Byte) {
            return "0x" + Integer.toHexString((Byte) primitive.get());
        }

        return value;
    }
}
