package ml.karmaconfigs.api.common.karma.file.element;

import ml.karmaconfigs.api.common.karma.file.element.types.ElementArray;
import ml.karmaconfigs.api.common.karma.file.element.types.ElementMap;
import ml.karmaconfigs.api.common.karma.file.element.types.ElementNull;
import ml.karmaconfigs.api.common.karma.file.element.types.ElementPrimitive;
import ml.karmaconfigs.api.common.karma.file.element.types.primitive.*;

import static ml.karmaconfigs.api.common.karma.file.element.types.primitive.PrimitiveType.ObjectType;

public class KarmaPrimitive implements ElementPrimitive {

    private final PrimitiveType<?> primitive;

    private ElementMap<?> map = null;
    private ElementArray<?> array = null;

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
     * Create a primitive for object
     *
     * @return the primitive
     */
    public static KarmaPrimitive forObject(final Object obj) {
        String str = null;
        try {
            str = obj.toString();
        } catch (Throwable ignored) {}

        if (str == null)
            return forNull();

        return new KarmaPrimitive(str);
    }

    /**
     * Set the primitive owner map
     *
     * @param owner the map
     * @return this instance
     */
    @Override
    public KarmaPrimitive onMap(final ElementMap<?> owner) {
        map = owner;
        return this;
    }

    /**
     * Set the primitive owner array
     *
     * @param owner the array
     * @return this instance
     */
    @Override
    public KarmaPrimitive onArray(final ElementArray<?> owner) {
        array = owner;
        return this;
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
        return primitive.getLiteral() instanceof ElementNull;
    }

    /**
     * Get if this element pertains to a map
     *
     * @return if the element pertains to a map
     */
    @Override
    public boolean pertainsToMap() {
        return map != null;
    }

    /**
     * Get the element pertaining map
     *
     * @return the element owner map
     */
    @Override
    public ElementMap<?> pertainingMap() {
        return map;
    }

    /**
     * Get if this element pertains to an array
     *
     * @return if the element pertains to an array
     */
    @Override
    public boolean pertainsToArray() {
        return array != null;
    }

    /**
     * Get the element pertaining array
     *
     * @return the element owner array
     */
    @Override
    public ElementArray<?> pertainingArray() {
        return array;
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
            return String.valueOf(primitive.getLiteral());
        }

        Object value = primitive.getLiteral();
        if (value instanceof String) {
            return (String) primitive.getLiteral();
        } else {
            return String.valueOf(value);
        }
    }

    /**
     * Get the primitive element as a boolean
     *
     * @return the boolean primitive
     */
    @Override
    public boolean asBoolean() {
        return (Boolean) primitive.getLiteral();
    }

    /**
     * Get the primitive element as a integer
     *
     * @return the integer primitive
     */
    @Override
    public int asInteger() {
        if (primitive.getLiteral() instanceof Number) {
            return ((Number) primitive.getLiteral()).intValue();
        }

        return (Integer) primitive.getLiteral();
    }

    /**
     * Get the primitive element as a double
     *
     * @return the double primitive
     */
    @Override
    public double asDouble() {
        if (primitive.getLiteral() instanceof Number) {
            return ((Number) primitive.getLiteral()).doubleValue();
        }

        return (Double) primitive.getLiteral();
    }

    /**
     * Get the primitive element as a long
     *
     * @return the long primitive
     */
    @Override
    public long asLong() {
        if (primitive.getLiteral() instanceof Number) {
            return ((Number) primitive.getLiteral()).longValue();
        }

        return (Long) primitive.getLiteral();
    }

    /**
     * Get the primitive element as a short
     *
     * @return the short primitive
     */
    @Override
    public short asShort() {
        if (primitive.getLiteral() instanceof Number) {
            return ((Number) primitive.getLiteral()).shortValue();
        }

        return (Short) primitive.getLiteral();
    }

    /**
     * Get the primitive element as a float
     *
     * @return the character float
     */
    @Override
    public float asFloat() {
        if (primitive.getLiteral() instanceof Number) {
            return ((Number) primitive.getLiteral()).floatValue();
        }

        return (Float) primitive.getLiteral();
    }

    /**
     * Get the primitive element as a byte
     *
     * @return the byte primitive
     */
    @Override
    public byte asByte() {
        if (primitive.getLiteral() instanceof Number) {
            return ((Number) primitive.getLiteral()).byteValue();
        }

        return (Byte) primitive.getLiteral();
    }

    /**
     * Get the primitive element as a character
     *
     * @return the character primitive
     */
    @Override
    public char asCharacter() {
        return (Character) primitive.getLiteral();
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
        String value = String.valueOf(primitive.getLiteral());

        if (primitive.getLiteral() instanceof Double || primitive.getLiteral() instanceof Float) {
            return (primitive.getLiteral() instanceof Double ? value.replace(".", ",") : value);
        }
        if (primitive.getLiteral() instanceof Byte) {
            return "0x" + Integer.toHexString((Byte) primitive.getLiteral());
        }

        return value;
    }
}
