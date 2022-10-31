package ml.karmaconfigs.api.common.karma.panel.wrapper.post;

import ml.karmaconfigs.api.common.karma.panel.wrapper.post.filter.Filterable;
import ml.karmaconfigs.api.common.karma.panel.wrapper.post.meta.PostTagsContainer;
import ml.karmaconfigs.api.common.timer.scheduler.LateScheduler;
import org.omg.CORBA.UNKNOWN;

import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import java.util.List;

/**
 * Post
 */
public abstract class PostWrapper implements Filterable<PostTagsContainer> {

    /**
     * Get the post ID
     *
     * @return the post ID
     */
    public abstract String getId();

    /**
     * Get the post title
     *
     * @return the post title
     */
    public abstract String getTitle();

    /**
     * Get the post content
     *
     * @return the post content
     */
    public abstract String getContent();

    /**
     * Get the post publication date
     *
     * @return the post date
     */
    public abstract String getCreated();

    /**
     * Get the post last modification date
     *
     * @return the post last modification date
     */
    public abstract String getModified();

    /**
     * Get if the current account (if logged in) liked the post.
     * Otherwise, returns false
     *
     * @return if the current account likes this post
     */
    public abstract boolean isLiked();

    /**
     * Get the post status
     *
     * @return the post status
     */
    public abstract Status getStatus();

    /**
     * Get the post owner
     *
     * @return the post owner
     */
    public abstract int getOwner();

    /**
     * Get the post status issuer
     *
     * @return the post status issuer
     */
    public abstract int getStatusIssuer();

    /**
     * Get the post visibility issuer
     *
     * @return the post visibility issuer
     */
    public abstract int getVisibilityIssuer();

    /**
     * Get the post comments
     *
     * @return the post comments
     */
    public abstract List<CommentWrapper> getComments();

    /**
     * Get the post comments
     *
     * @param filter the post comment filter
     * @return the post comments
     */
    public abstract List<CommentWrapper> getComments(final Filterable<CommentWrapper> filter);

    /**
     * Get the post likes
     *
     * @return the post likes
     */
    public abstract List<String> getLikes();

    /**
     * Get the post likes
     *
     * @param filter the post like filter
     * @return the post likes
     */
    public abstract List<String> getLikes(final Filterable<String> filter);

    /**
     * Add a comment to the post
     *
     * @param content the comment content
     * @return the added comment
     */
    public abstract LateScheduler<CommentWrapper> addComment(final String content);

    /**
     * Returns true if the new post like status is true, and returns false
     * if the new post like status is false
     *
     * @return the new post like status
     */
    public abstract LateScheduler<Boolean> toggleLike();

    /**
     * Post status
     */
    public enum Status {
        PENDING(0),
        ACTIVE(1),
        PRIVATE(2),
        DELETED(3),
        UNKNOWN(-1);

        private final int code;

        /**
         * Initialize the status
         *
         * @param c the status code
         */
        Status(final int c) {
            code = c;
        }

        /**
         * Get the status code
         *
         * @return the status code
         */
        public int getCode() {
            return code;
        }

        /**
         * Get the status from a code
         *
         * @param code the code
         * @return the status
         */
        public static Status fromCode(final int code) {
            switch (code) {
                case 0:
                    return PENDING;
                case 1:
                    return ACTIVE;
                case 2:
                    return PRIVATE;
                case 3:
                default:
                    return DELETED;
            }
        }
    }

    /**
     * Usually returns the post json
     *
     * @return the post as string
     */
    @Override
    public abstract String toString();
}
