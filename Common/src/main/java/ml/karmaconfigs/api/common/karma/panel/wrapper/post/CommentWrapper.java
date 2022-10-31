package ml.karmaconfigs.api.common.karma.panel.wrapper.post;


import ml.karmaconfigs.api.common.karma.panel.wrapper.post.filter.Filterable;

import java.time.Instant;

/**
 * Post comment wrapper
 */
public abstract class CommentWrapper implements Filterable<CommentWrapper> {

    /**
     * Get the post where the comment is published
     *
     * @return the comment post
     */
    public abstract PostWrapper getPost();

    /**
     * Get the name of the user who posted the comment
     *
     * @return the comment owner
     */
    public abstract String getPublisher();

    /**
     * Get the comment content
     *
     * @return the comment
     */
    public abstract String getContent();

    /**
     * Get the date when the comment was posted
     *
     * @return the comment creation date
     */
    public abstract String getCreated();
}
