package ml.karmaconfigs.api.common.triple;

import ml.karmaconfigs.api.common.triple.consumer.TriConsumer;

public interface TriTerable<A, B, C> {

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    TriTerator<A, B, C> iterator();

    /**
     * Performs the given action for each element of the {@code Iterable}
     * until all elements have been processed or the action throws an
     * exception.  Unless otherwise specified by the implementing class,
     * actions are performed in the order of iteration (if an iteration order
     * is specified).  Exceptions thrown by the action are relayed to the
     * caller.
     *
     *
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
     * @since 1.8
     */
    void forEach(final TriConsumer<? super A, ? super B, ? super C> action);
}
