package ml.karmaconfigs.api.common.karma.panel.wrapper.post.filter;

public interface Filterable<T> {

    /**
     * Filter the object
     *
     * @param object the {@link T} to filter
     * @return if {@link T} passes the filter
     */
    boolean filter(T object);
}
