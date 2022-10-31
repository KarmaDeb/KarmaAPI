package ml.karmaconfigs.api.common.karma.panel.wrapper.post.filter;

/**
 * Simple name filter
 */
public class SimpleNameFilter implements Filterable<String> {

    private final String name;

    /**
     * Initialize the simple name
     *
     * @param n the name
     */
    public SimpleNameFilter(final String n) {
        name = n;
    }

    /**
     * Filter the object
     *
     * @param object the {@link String} to filter
     * @return if {@link String} passes the filter
     */
    @Override
    public boolean filter(final String object) {
        return name.equals(object);
    }
}
