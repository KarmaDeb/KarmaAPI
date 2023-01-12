package ml.karmaconfigs.api.common.karma.file.element.types;

import ml.karmaconfigs.api.common.karma.file.element.types.primitive.PrimitiveType;

/**
 * Element
 *
 * @param <T> the element type
 */
public interface Element<T> {

    /**
     * Get the element
     *
     * @return the element
     */
    T getValue();

    /**
     * Get if the element is primitive
     *
     * @return if the element is primitive
     */
    boolean isPrimitive();

    /**
     * Get if the element is an array
     *
     * @return if the element is an array
     */
    boolean isArray();

    /**
     * Get if the element is a map
     *
     * @return if the element is a map
     */
    boolean isMap();

    /**
     * Get if the element is a null object
     *
     * @return if the element is a null object
     */
    boolean isElementNull();

    /**
     * Get this element as a primitive
     *
     * @return the element as a primitive
     */
    default ElementPrimitive getAsPrimitive() {
        if (this instanceof ElementPrimitive)
            return (ElementPrimitive) this;

        return null;
    }

    /**
     * Get this element as an array
     *
     * @return the element as array
     */
    default ElementArray<?> getAsArray() {
        if (this instanceof ElementArray)
            return (ElementArray<?>) this;

        return null;
    }

    /**
     * Get this element as a map
     *
     * @return the element as map
     */
    default ElementMap<?> getAsMap() {
        if (this instanceof ElementMap)
            return (ElementMap<?>) this;

        return null;
    }

    /**
     * Get this element as a string
     *
     * @return the element as a string
     * @throws IllegalStateException if the element is not a string
     */
    default String getAsString() throws IllegalStateException {
        if (this instanceof ElementPrimitive) {
            ElementPrimitive primitive = (ElementPrimitive) this;
            if (primitive.isString())
                return primitive.asString();
        }
        if (this instanceof PrimitiveType) {
            PrimitiveType<?> type = (PrimitiveType<?>) this;
            switch (type.type()) {
                case TEXT:
                case CHARACTER:
                    return String.valueOf(type.get());
            }
        }

        throw new IllegalStateException("Element is not a string!");
    }

    /**
     * Get this element as a number
     *
     * @return the element as a number
     * @throws IllegalStateException if the element is not a number
     */
    default int getAsInteger() throws IllegalStateException {
        if (this instanceof ElementPrimitive) {
            ElementPrimitive primitive = (ElementPrimitive) this;
            if (primitive.isNumber())
                return primitive.asInteger();
        }
        if (this instanceof PrimitiveType) {
            PrimitiveType<?> type = (PrimitiveType<?>) this;
            if (type.type().equals(PrimitiveType.ObjectType.NUMBER)) {
                return ((Number) type.get()).intValue();
            }
        }

        throw new IllegalStateException("Element is not a number!");
    }

    /**
     * Get this element as a number
     *
     * @return the element as a number
     * @throws IllegalStateException if the element is not a number
     */
    default double getAsDouble() throws IllegalStateException {
        if (this instanceof ElementPrimitive) {
            ElementPrimitive primitive = (ElementPrimitive) this;
            if (primitive.isNumber())
                return primitive.asDouble();
        }
        if (this instanceof PrimitiveType) {
            PrimitiveType<?> type = (PrimitiveType<?>) this;
            if (type.type().equals(PrimitiveType.ObjectType.NUMBER)) {
                return ((Number) type.get()).doubleValue();
            }
        }

        throw new IllegalStateException("Element is not a number!");
    }

    /**
     * Get this element as a number
     *
     * @return the element as a number
     * @throws IllegalStateException if the element is not a number
     */
    default float getAsFloat() throws IllegalStateException {
        if (this instanceof ElementPrimitive) {
            ElementPrimitive primitive = (ElementPrimitive) this;
            if (primitive.isNumber())
                return primitive.asFloat();
        }
        if (this instanceof PrimitiveType) {
            PrimitiveType<?> type = (PrimitiveType<?>) this;
            if (type.type().equals(PrimitiveType.ObjectType.NUMBER)) {
                return ((Number) type.get()).floatValue();
            }
        }

        throw new IllegalStateException("Element is not a number!");
    }

    /**
     * Get this element as a number
     *
     * @return the element as a number
     * @throws IllegalStateException if the element is not a number
     */
    default long getAsLong() throws IllegalStateException {
        if (this instanceof ElementPrimitive) {
            ElementPrimitive primitive = (ElementPrimitive) this;
            if (primitive.isNumber())
                return primitive.asLong();
        }
        if (this instanceof PrimitiveType) {
            PrimitiveType<?> type = (PrimitiveType<?>) this;
            if (type.type().equals(PrimitiveType.ObjectType.NUMBER)) {
                return ((Number) type.get()).longValue();
            }
        }

        throw new IllegalStateException("Element is not a number!");
    }

    /**
     * Get this element as a number
     *
     * @return the element as a number
     * @throws IllegalStateException if the element is not a number
     */
    default short getAsShort() throws IllegalStateException {
        if (this instanceof ElementPrimitive) {
            ElementPrimitive primitive = (ElementPrimitive) this;
            if (primitive.isNumber())
                return primitive.asShort();
        }
        if (this instanceof PrimitiveType) {
            PrimitiveType<?> type = (PrimitiveType<?>) this;
            if (type.type().equals(PrimitiveType.ObjectType.NUMBER)) {
                return ((Number) type.get()).shortValue();
            }
        }

        throw new IllegalStateException("Element is not a number!");
    }

    /**
     * Get this element as a byte
     *
     * @return the element as a byte
     * @throws IllegalStateException if the element is not a byte
     */
    default byte getAsByte() throws IllegalStateException {
        if (this instanceof ElementPrimitive) {
            ElementPrimitive primitive = (ElementPrimitive) this;
            if (primitive.isNumber())
                return primitive.asByte();
        }
        if (this instanceof PrimitiveType) {
            PrimitiveType<?> type = (PrimitiveType<?>) this;
            if (type.type().equals(PrimitiveType.ObjectType.NUMBER)) {
                return ((Number) type.get()).byteValue();
            } else{
                if (type.type().equals(PrimitiveType.ObjectType.BYTE)) {
                    return (Byte) type.get();
                }
            }
        }

        throw new IllegalStateException("Element is not a byte!");
    }

    /**
     * Get this element as a character
     *
     * @return the element as a character
     * @throws IllegalStateException if the element is not a character
     */
    default char getAsCharacter() throws IllegalStateException {
        if (this instanceof ElementPrimitive) {
            ElementPrimitive primitive = (ElementPrimitive) this;
            if (primitive.isCharacter())
                return primitive.asCharacter();
        }
        if (this instanceof PrimitiveType) {
            PrimitiveType<?> type = (PrimitiveType<?>) this;
            if (type.type().equals(PrimitiveType.ObjectType.CHARACTER)) {
                return (Character) type.get();
            }
        }

        throw new IllegalStateException("Element is not a character!");
    }

    /**
     * Get this element as a string
     *
     * @return the element as a string
     * @throws IllegalStateException if the element is not a boolean
     */
    default boolean getAsBoolean() throws IllegalStateException {
        if (this instanceof ElementPrimitive) {
            ElementPrimitive primitive = (ElementPrimitive) this;
            if (primitive.isBoolean())
                return primitive.asBoolean();
        }
        if (this instanceof PrimitiveType) {
            PrimitiveType<?> type = (PrimitiveType<?>) this;
            if (type.type().equals(PrimitiveType.ObjectType.BOOLEAN)) {
                return (Boolean) type.get();
            }
        }

        throw new IllegalStateException("Element is not a boolean!");
    }
}
