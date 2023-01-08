package ml.karmaconfigs.api.common.triple;

import ml.karmaconfigs.api.common.triple.consumer.TriConsumer;

import java.util.NoSuchElementException;

public class TriAbstract {

    /**
     * Iterable implementation for {@link TriTerable}
     *
     * @param <A> the iterable key type
     * @param <B> the iterable value type
     * @param <C> the iterable sub value type
     */
    public static class AbstractIterable<A, B, C> implements TriTerable<A, B, C> {

        private final TriEntry<? super A, ? super B, ? super C>[] entries;

        /**
         * Initialize the abstract iterable
         *
         * @param entries the iterable entries
         */
        @SafeVarargs
        public AbstractIterable(TriEntry<? super A, ? super B, ? super C>... entries) {
            this.entries = entries;
        }

        /**
         * Returns an iterator over elements of type {@code T}.
         *
         * @return an Iterator.
         */
        @Override
        public TriTerator<A, B, C> iterator() {
            return new AbstractIterator<>(entries);
        }

        /**
         * Performs the given action for each element of the {@code Iterable}
         * until all elements have been processed or the action throws an
         * exception.  Unless otherwise specified by the implementing class,
         * actions are performed in the order of iteration (if an iteration order
         * is specified).  Exceptions thrown by the action are relayed to the
         * caller.
         *
         * @param action The action to be performed for each element
         * @throws NullPointerException if the specified action is null
         * @implSpec <p>The default implementation behaves as if:
         * <pre>{@code
         *     for (T t : this)
         *         action.accept(t);
         * }</pre>
         * @since 1.8
         */
        @Override
        @SuppressWarnings("unchecked")
        public void forEach(final TriConsumer<? super A, ? super B, ? super C> action) {
            for (TriEntry<? super A, ? super B, ? super C> entry : entries) {
                action.accept((A) entry.getKey(), (B) entry.getValue(), (C) entry.getSub());
            }
        }
    }

    /**
     * Iterable implementation for {@link TriTerator}
     *
     * @param <A> the iterator key type
     * @param <B> the iterator value type
     * @param <C> the iterator sub value type
     */
    public static class AbstractIterator<A, B, C> implements TriTerator<A, B, C> {

        private final A[] keys;
        private final B[] values;
        private final C[] secondaries;

        private int current_index;

        @SuppressWarnings("unchecked")
        public AbstractIterator(TriEntry<? super A, ? super B, ? super C>... entries) {
            keys = (A[]) new Object[entries.length];
            values = (B[]) new Object[entries.length];
            secondaries = (C[]) new Object[entries.length];

            int index = 0;
            for (TriEntry<? super A, ? super B, ? super C> entry : entries) {
                keys[index++] = (A) entry.getKey();
                values[index] = (B) entry.getValue();
                secondaries[index] = (C) entry.getSub();
            }


        }

        /**
         * Returns true if the iteration has more elements. (In other words, returns true if next would return an element rather than throwing an exception.)
         * Returns:
         *
         * @return true if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return current_index < keys.length;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public A nextKey() throws NoSuchElementException {
            if (current_index < keys.length) {
                return keys[current_index++];
            } else {
                throw new NoSuchElementException();
            }
        }

        /**
         * Get the current value, should be called after {@link TriTerator#nextKey()}
         *
         * @return the current value
         */
        @Override
        public B value() {
            return values[current_index];
        }

        /**
         * Get the current secondary value, should be called after {@link TriTerator#nextKey()}
         *
         * @return the current secondary value
         */
        @Override
        public C secondary() {
            return secondaries[current_index];
        }
    }
}
