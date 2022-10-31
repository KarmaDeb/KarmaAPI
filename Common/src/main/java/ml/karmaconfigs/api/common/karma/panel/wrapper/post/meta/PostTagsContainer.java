package ml.karmaconfigs.api.common.karma.panel.wrapper.post.meta;

import ml.karmaconfigs.api.common.karma.panel.wrapper.post.filter.Filterable;

import java.util.HashSet;
import java.util.Set;

/**
 * Post tags container
 */
public abstract class PostTagsContainer implements Filterable<PostTagsContainer> {

    /**
     * Get the post tags
     *
     * @return the post tags
     */
    public abstract Set<PostTag> getTags();

    /**
     * Get the post tags
     *
     * @param filter the tag filter
     * @return the post tags
     */
    public abstract Set<PostTag> getTags(final Filterable<PostTag> filter);

    /**
     * Filter the object
     *
     * @param object the {@link PostTagsContainer} to filter
     * @return if {@link PostTagsContainer} passes the filter
     */
    @Override
    public boolean filter(final PostTagsContainer object) {
        Set<String> otherTags = new HashSet<>();
        object.getTags().forEach((tag) -> otherTags.add(tag.get().toLowerCase()));

        for (PostTag tag : getTags()) {
            if (!otherTags.contains(tag.get().toLowerCase())) {
                return false;
            }
        }

        return true;
    }
}
