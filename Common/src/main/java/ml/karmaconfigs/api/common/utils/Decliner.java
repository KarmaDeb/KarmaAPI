package ml.karmaconfigs.api.common.utils;

/**
 * Decliner interface. This can be used when you need
 * to constantly send a value which can be declined by a third
 * party, so you can send more options to it
 *
 * @param <T> the object type
 */
public interface Decliner<T> {

    /**
     * Provide with the item
     *
     * @param object the item to provide with
     */
    void provide(final T object);

    /**
     * Decline the object
     */
    void decline();

    /**
     * Get if the item has been accepted
     *
     * @return if the item has been accepted
     */
    boolean isDeclined();

    /**
     * Accept the current object
     *
     * @return the current object
     */
    T fetch();
}
