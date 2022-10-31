package ml.karmaconfigs.api.common.karma.panel.wrapper.post.meta;

import ml.karmaconfigs.api.common.karma.panel.wrapper.post.filter.Filterable;


/**
 * Post topic container
 */
public interface PostTopicContainer extends Filterable<PostTopicContainer> {

    /**
     * Get the topic value
     *
     * @return the topic value
     */
    String get();
}
