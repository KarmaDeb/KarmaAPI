package ml.karmaconfigs.api.common.karma.file.element.types.primitive.number;

import ml.karmaconfigs.api.common.karma.file.element.types.primitive.NumberPrimitive;
import ml.karmaconfigs.api.common.karma.file.element.types.primitive.PrimitiveType;

public class LongPrimitive extends NumberPrimitive {

    /**
     * Initialize the primitive
     *
     * @param v the primitive value
     */
    public LongPrimitive(final long v) {
        super(v);
    }

    /**
     * Get the element
     *
     * @return the element
     */
    @Override
    public Long get() {
        return value.longValue();
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
