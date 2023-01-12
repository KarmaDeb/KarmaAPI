package ml.karmaconfigs.api.common.karma.file.element.types.primitive;

public interface PrimitiveType<T> {

    /**
     * Get the element
     *
     * @return the element
     */
    T get();

    /**
     * Get the primitive type
     *
     * @return the primitive type
     */
    ObjectType type();

    /**
     * Recognized object types
     */
    enum ObjectType {
        /**
         * Text object type
         */
        TEXT,

        /**
         * Character object type
         */
        CHARACTER,

        /**
         * Number object type
         */
        NUMBER,

        /**
         * Boolean object type
         */
        BOOLEAN,

        /**
         * Byte object type
         */
        BYTE,

        /**
         * Null object type
         */
        NULL
    }
}
