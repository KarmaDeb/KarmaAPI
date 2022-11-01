package ml.karmaconfigs.api.common.karma.panel.wrapper;

import com.google.gson.JsonElement;
import ml.karmaconfigs.api.common.karma.panel.wrapper.post.PostWrapper;
import ml.karmaconfigs.api.common.timer.scheduler.BiLateScheduler;
import ml.karmaconfigs.api.common.timer.scheduler.LateScheduler;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

/**
 * KarmaAPI web panel profile wrapper
 */
public abstract class ProfileWrapper {

    /**
     * Get the profile name
     *
     * @return the profile name
     */
    public abstract String getName();

    /**
     * Get the profile email address
     *
     * @return the profile email address
     */
    public abstract String getEmail();

    /**
     * Get the profile identifier
     *
     * @return the profile identifier
     */
    public abstract int getIdentifier();

    /**
     * Get the profile unique identifier
     *
     * @return the profile unique identifier
     */
    public abstract UUID getUniqueIdentifier();

    /**
     * Authenticate the user
     *
     * @param password the account password
     * @return if the account could be authenticated
     */
    public abstract LateScheduler<Boolean> authenticate(final String password);

    /**
     * Register the user
     *
     * @param password      the account password
     * @param auto_validate if the account should try to validate himself right after
     *                      it gets created
     * @return the register result
     */
    public abstract LateScheduler<Boolean> create(final String password, final boolean auto_validate);

    /**
     * Get the current profile
     *
     * @return {@link Void} when the action is completed
     */
    public abstract LateScheduler<Void> fetchProfile();

    /**
     * Update profile
     *
     * @param settings the profile settings
     * @return if the profile could be updated
     */
    public abstract BiLateScheduler<Boolean, String> updateProfile(final SettingsWrapper settings);

    /**
     * Create a post
     *
     * @param post the post to create
     * @return if the post could be created and the created post
     */
    public abstract BiLateScheduler<Boolean, PostWrapper> createPost(final PostWrapper post);

    /**
     * Accept a post
     *
     * @param post the post to accept
     * @return if the post could be accepted
     */
    public abstract BiLateScheduler<Boolean, PostWrapper> acceptPost(final String post);

    /**
     * Decline a post
     *
     * @param post the post to decline
     * @return if the post could be declined
     */
    public abstract LateScheduler<Boolean> declinePost(final String post);

    /**
     * Update a post
     *
     * @param post     the post id
     * @param new_post the new post data
     * @return if the post could be updated and the new post
     */
    public abstract BiLateScheduler<Boolean, PostWrapper> updatePost(final String post, final PostWrapper new_post);

    /**
     * Get the profile settings
     *
     * @return the profile settings
     */
    @Nullable
    public abstract SettingsWrapper getSettings();

    /**
     * Get the profile created posts
     *
     * @return the profile created posts
     */
    public abstract Set<String> getCreatedPosts();

    /**
     * Get the profile commented posts
     *
     * @return the profile commented posts
     */
    public abstract Set<String> getCommentedPosts();

    /**
     * Get the profile liked posts
     *
     * @return the profile liked posts
     */
    public abstract Set<String> getLikedPosts();

    /**
     * Get the last auth message
     *
     * @return the last auth message
     */
    @Nullable
    public abstract JsonElement getLastAuth();

    /**
     * Get the last registration message
     *
     * @return the last registration message
     */
    @Nullable
    public abstract JsonElement getLastCreate();

    /**
     * Returns this profile as a public profile instance
     *
     * @return the profile as a public profile
     */
    public abstract PublicProfileWrapper asPublicProfile();
}
