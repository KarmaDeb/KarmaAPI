package ml.karmaconfigs.api.common.karma.panel.wrapper.post;

import ml.karmaconfigs.api.common.karma.panel.wrapper.post.filter.Filterable;
import ml.karmaconfigs.api.common.karma.panel.wrapper.post.meta.PostTopicContainer;

import java.util.Set;

/**
 * Page of posts wrapper
 */
public abstract class PostPageWrapper implements Filterable<PostTopicContainer> {

    /**
     * Get the current page
     *
     * @return the current page
     */
    public abstract int getPage();

    /**
     * Get the maximum amount of pages
     *
     * @return the maximum amount of pages
     */
    public abstract int getMaxPages();

    /**
     * Get all the page posts
     *
     * @return all the page posts
     */
    public abstract Set<PostWrapper> getPosts();

    /**
     * Get all the page posts
     *
     * @param filter the page filter
     * @return all the page posts
     */
    public abstract Set<PostWrapper> getPosts(final Filterable<PostWrapper> filter);
}
