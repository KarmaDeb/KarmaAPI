package ml.karmaconfigs.api.common.karma.file.element.types;

import ml.karmaconfigs.api.common.karma.file.element.types.primitive.PrimitiveType;

public interface ElementArray<T extends Element<?>> extends Element<T[]>, Iterable<T> {

    /**
     * Add elements to the array
     *
     * @param elements the elements to add
     */
    void add(final PrimitiveType<?>... elements);

    /**
     * Add elements to the array
     *
     * @param elements the elements to add
     */
    void add(final ElementPrimitive... elements);

    /**
     * Add elements to the array
     *
     * @param elements the elements to add
     */
    void add(final String... elements);

    /**
     * Add elements to the array
     *
     * @param elements the elements to add
     */
    void add(final Number... elements);

    /**
     * Add elements to the array
     *
     * @param elements the elements to add
     */
    void add(final Boolean... elements);

    /**
     * Add elements to the array
     *
     * @param elements the elements to add
     */
    void add(final Character... elements);

    /**
     * Add elements to the array
     *
     * @param elements the elements to add
     */
    void add(final Byte... elements);

    /**
     * Remove elements from the array
     *
     * @param elements the elements to remove
     */
    void remove(final PrimitiveType<?>... elements);

    /**
     * Add elements to the array
     *
     * @param elements the elements to add
     */
    void remove(final ElementPrimitive... elements);

    /**
     * Remove elements from the array
     *
     * @param elements the elements to remove
     */
    void remove(final String... elements);

    /**
     * Remove elements from the array
     *
     * @param elements the elements to remove
     */
    void remove(final Number... elements);

    /**
     * Remove elements from the array
     *
     * @param elements the elements to remove
     */
    void remove(final Boolean... elements);

    /**
     * Remove elements from the array
     *
     * @param elements the elements to remove
     */
    void remove(final Character... elements);

    /**
     * Remove elements from the array
     *
     * @param elements the elements to remove
     */
    void remove(final Byte... elements);

    /**
     * Check if the array contains the specified element
     *
     * @param element the element
     * @return if the array contains the element
     */
    boolean contains(final Element<?> element);

    /**
     * Check if the array contains all the specified elements
     *
     * @param elements the elements
     * @return if the array contains all the elements
     */
    boolean containsAll(final Element<?>... elements);

    /**
     * Get an element at the specified index
     *
     * @param index the element index
     * @return the element
     */
    Element<?> get(final int index);

    /**
     * Get this array but with its contents lower case
     *
     * @return the array lower case
     */
    ElementArray<T> contentsToLowerCase();

    /**
     * Get this array but with its contents upper case
     *
     * @return the array upper case
     */
    ElementArray<T> contentsToUpperCase();

    /**
     * Get the array size
     *
     * @return the array size
     */
    int getSize();

    /**
     * Get if the element is primitive
     *
     * @return if the element is primitive
     */
    @Override
    default boolean isPrimitive() {
        return false;
    }

    /**
     * Get if the element is an array
     *
     * @return if the element is an array
     */
    @Override
    default boolean isArray() {
        return true;
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

    /**
     * Get if the element is a null object
     *
     * @return if the element is a null object
     */
    @Override
    default boolean isElementNull() { return false; }
}
