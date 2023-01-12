package ml.karmaconfigs.api.common.karma.file.element.types.primitive.number;

import ml.karmaconfigs.api.common.karma.file.element.types.primitive.NumberPrimitive;

public class FloatPrimitive extends NumberPrimitive {

    /**
     * Initialize the primitive
     *
     * @param v the primitive value
     */
    public FloatPrimitive(final float v) {
        super(v);
    }

    /**
     * Get the element
     *
     * @return the element
     */
    @Override
    public Float get() {
        return value.floatValue();
    }

    /**
     * Get the primitive type
     *
     * @return the primitive type
     */
    @Override
    public ObjectType type() {
        return ObjectType.NUMBER;
    }
}
