package ml.karmaconfigs.api.common.karma.file.element.types;

import ml.karmaconfigs.api.common.karma.file.element.KarmaPrimitive;
import ml.karmaconfigs.api.common.karma.file.element.types.primitive.PrimitiveType;

public interface ElementPrimitive extends Element<PrimitiveType<?>> {

    /**
     * Set the primitive owner map
     *
     * @param owner the map
     * @return this instance
     */
    ElementPrimitive onMap(final ElementMap<?> owner);

    /**
     * Set the primitive owner array
     *
     * @param owner the array
     * @return this instance
     */
    ElementPrimitive onArray(final ElementArray<?> owner);

    /**
     * Get if the primitive element is a string
     *
     * @return if is string
     */
    boolean isString();

    /**
     * Get if the primitive element is a number
     *
     * @return if is number
     */
    boolean isNumber();

    /**
     * Get if the primitive element is a boolean
     *
     * @return if boolean
     */
    boolean isBoolean();

    /**
     * Get if the primitive element is a character
     *
     * @return if character
     */
    boolean isCharacter();

    /**
     * Get the primitive element as a string
     *
     * @return the string primitive
     */
    String asString();

    /**
     * Get the primitive element as a boolean
     *
     * @return the boolean primitive
     */
    boolean asBoolean();

    /**
     * Get the primitive element as a integer
     *
     * @return the integer primitive
     */
    int asInteger();

    /**
     * Get the primitive element as a double
     *
     * @return the double primitive
     */
    double asDouble();

    /**
     * Get the primitive element as a long
     *
     * @return the long primitive
     */
    long asLong();

    /**
     * Get the primitive element as a short
     *
     * @return the short primitive
     */
    short asShort();

    /**
     * Get the primitive element as a float
     *
     * @return the character float
     */
    float asFloat();

    /**
     * Get the primitive element as a byte
     *
     * @return the byte primitive
     */
    byte asByte();

    /**
     * Get the primitive element as a character
     *
     * @return the character primitive
     */
    char asCharacter();

    /**
     * Get if the element is primitive
     *
     * @return if the element is primitive
     */
    @Override
    default boolean isPrimitive() {
        return true;
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
        return false;
    }
}
