package ml.karmaconfigs.api.common.karma.file.element.types.primitive.number;

import ml.karmaconfigs.api.common.karma.file.element.types.primitive.NumberPrimitive;

public class IntegerPrimitive extends NumberPrimitive {

    /**
     * Initialize the primitive
     *
     * @param v the primitive value
     */
    public IntegerPrimitive(final int v) {
        super(v);
    }

    /**
     * Get the element
     *
     * @return the element
     */
    @Override
    public Integer get() {
        return value.intValue();
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
