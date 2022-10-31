package ml.karmaconfigs.api.common.karma.panel.wrapper.post.meta;

import ml.karmaconfigs.api.common.karma.panel.wrapper.post.filter.Filterable;

/**
 * Post tag
 */
public interface PostTag extends Filterable<PostTag> {

    /**
     * Get the post tag
     *
     * @return the post tag
     */
    String get();
}
