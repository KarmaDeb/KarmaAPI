package ml.karmaconfigs.api.common.karma.file.element.types.primitive.number;

import ml.karmaconfigs.api.common.karma.file.element.types.primitive.NumberPrimitive;

public class DoublePrimitive extends NumberPrimitive {

    /**
     * Initialize the primitive
     *
     * @param v the primitive value
     */
    public DoublePrimitive(final double v) {
        super(v);
    }

    /**
     * Get the element
     *
     * @return the element
     */
    @Override
    public Double get() {
        return value.doubleValue();
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
