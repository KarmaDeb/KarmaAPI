package ml.karmaconfigs.api.common.triple;

import ml.karmaconfigs.api.common.triple.consumer.TriConsumer;

import java.util.NoSuchElementException;
import java.util.Objects;

public interface TriTerator<A, B, C> {

    /**
     * Returns true if the iteration has more elements. (In other words, returns true if next would return an element rather than throwing an exception.)
     * Returns:
     *
     * @return true if the iteration has more elements
     */
    boolean hasNext();

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    A nextKey();

    /**
     * Get the current value, should be called after {@link TriTerator#nextKey()}
     *
     * @return the current value
     */
    B value();

    /**
     * Get the current secondary value, should be called after {@link TriTerator#nextKey()}
     *
     * @return the current secondary value
     */
    C secondary();

    /**
     * Removes from the underlying collection the last element returned
     * by this iterator (optional operation).  This method can be called
     * only once per call to {@link #nextKey()}.  The behavior of an iterator
     * is unspecified if the underlying collection is modified while the
     * iteration is in progress in any way other than by calling this
     * method.
     *
     * {@link UnsupportedOperationException} and performs no other action.
     *
     * @throws UnsupportedOperationException if the {@code remove}
     *         operation is not supported by this iterator
     *
     * @throws IllegalStateException if the {@code next} method has not
     *         yet been called, or the {@code remove} method has already
     *         been called after the last call to the {@code next}
     *         method
     */
    default void remove() {
        throw new UnsupportedOperationException("remove");
    }

    /**
     * Performs the given action for each remaining element until all elements
     * have been processed or the action throws an exception.  Actions are
     * performed in the order of iteration, if that order is specified.
     * Exceptions thrown by the action are relayed to the caller.
     *
     *
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
     * @since 1.8
     */
    default void forEachRemaining(final TriConsumer<? super A, ? super B, ? super C> action) {
        Objects.requireNonNull(action);
        while (hasNext()) {
            A next = nextKey();

            action.accept(next, value(), secondary());
        }
    }
}
