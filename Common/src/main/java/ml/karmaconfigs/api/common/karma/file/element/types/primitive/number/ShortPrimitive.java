package ml.karmaconfigs.api.common.karma.file.element.types.primitive.number;

import ml.karmaconfigs.api.common.karma.file.element.types.primitive.NumberPrimitive;
import ml.karmaconfigs.api.common.karma.file.element.types.primitive.PrimitiveType;

public class ShortPrimitive extends NumberPrimitive {

    /**
     * Initialize the primitive
     *
     * @param v the primitive value
     */
    public ShortPrimitive(final short v) {
        super(v);
    }

    /**
     * Get the element
     *
     * @return the element
     */
    @Override
    public Short get() {
        return value.shortValue();
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
