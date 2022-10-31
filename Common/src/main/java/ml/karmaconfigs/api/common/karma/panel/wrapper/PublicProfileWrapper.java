package ml.karmaconfigs.api.common.karma.panel.wrapper;

import ml.karmaconfigs.api.common.karma.panel.wrapper.post.PostPageWrapper;

import java.awt.*;

/**
 * Public profile wrapper for public profiles
 */
public abstract class PublicProfileWrapper {

    /**
     * Get the profile name
     *
     * @return the profile name
     */
    public abstract String getName();

    /**
     * Get the profile description, or null if the
     * profile is private
     *
     * @return the profile description or null if private
     */
    public abstract String getDescription();

    /**
     * Get the profile unique identifier
     *
     * @return the profile unique identifier
     */
    public abstract String getUniqueIdentifier();

    /**
     * Get if the profile is private
     *
     * @return if the profile is private or has
     * friends-only mode
     */
    public abstract boolean isPrivate();

    /**
     * Get the profile picture
     *
     * @return the profile picture
     */
    public abstract Image getImage();

    /**
     * Get the profile created posts
     *
     * @return the profile created posts
     */
    public abstract PostPageWrapper getCreatedPosts();

    /**
     * Get the profile commented posts
     *
     * @return the profile commented posts
     */
    public abstract PostPageWrapper getCommentedPosts();

    /**
     * Get the profile liked posts
     *
     * @return the profile liked posts
     */
    public abstract PostPageWrapper getLikedPosts();

    /**
     * Usually returns the public profile json string
     *
     * @return the profile string
     */
    @Override
    public abstract String toString();
}
