package ml.karmaconfigs.api.common.karma.panel.wrapper;

import java.awt.*;

/**
 * Profile settings wrapper
 */
public abstract class SettingsWrapper {

    /**
     * Get the profile visibility
     *
     * @return the profile visibility
     */
    public abstract Visibility getVisibility();

    /**
     * Get the profile description
     *
     * @return the profile description
     */
    public abstract String getDescription();

    /**
     * Get the profile image picture
     *
     * @return the profile image picture in a base64 string
     */
    public abstract Image getProfilePicture();

    /**
     * Get the profile image picture
     *
     * @return the profile image picture in a base64 string
     */
    public abstract String getProfileRawPicture();

    /**
     * Broadcast if the profile is online or not
     *
     * @return if the profile should broadcast its status
     */
    public abstract boolean broadcastStatus();

    /**
     * Broadcast registration date
     *
     * @return if the profile should broadcast when it was first created
     */
    public abstract boolean broadcastRegistration();

    /**
     * Enable profile email notifications
     *
     * @return if the profile receives email notifications
     */
    public abstract boolean emailNotifications();

    /**
     * Set the account visibility
     *
     * @param policy the new visibility policy
     */
    public abstract void setVisibility(final Visibility policy);

    /**
     * Set the profile description
     *
     * @param description the profile new description
     */
    public abstract void setDescription(final String description);

    /**
     * Set the profile picture
     *
     * @param image the profile picture
     */
    public abstract void setProfilePicture(final Image image);

    /**
     * Share the account status and last visited page
     *
     * @param status if the account will share status and last visited
     *               page
     */
    public abstract void shareStatus(final boolean status);

    /**
     * Set the account share registration date status
     *
     * @param status if the account will display when it was first created
     */
    public abstract void shareRegistration(final boolean status);

    /**
     * Set the account email notifications status
     *
     * @param status if the account will receive notifications via email
     */
    public abstract void emailNotifications(final boolean status);

    /**
     * IMPORTANT!
     * <p>
     * This method should always return a json string, otherwise, calls
     * to {@link ProfileWrapper#updateProfile(SettingsWrapper)} will result
     * in a failed request
     *
     * @return the settings json string
     */
    @Override
    public abstract String toString();

    /**
     * Profile visibility
     */
    public enum Visibility {
        PUBLIC("public"),
        PRIVATE("private"),
        FRIENDS_ONLY("friends"),
        INVALID("public");

        private final String value;

        /**
         * Initialize the visibility setting
         *
         * @param api_name the visibility api name
         */
        Visibility(final String api_name) {
            value = api_name;
        }

        /**
         * Get the visibility api name
         *
         * @return the visibility value
         */
        public String getValue() {
            return value;
        }

        public static Visibility fromApiName(final String api_name) {
            switch (api_name.toLowerCase()) {
                case "public":
                    return Visibility.PUBLIC;
                case "private":
                    return Visibility.PRIVATE;
                case "friends":
                    return Visibility.FRIENDS_ONLY;
                default:
                    return Visibility.INVALID;
            }
        }
    }
}
