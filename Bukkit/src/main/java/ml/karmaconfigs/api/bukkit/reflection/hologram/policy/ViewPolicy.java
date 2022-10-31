package ml.karmaconfigs.api.bukkit.reflection.hologram.policy;

import org.bukkit.permissions.Permission;

/**
 * Hologram view policy
 */
public enum ViewPolicy {
    /**
     * Visible for everyone
     */
    VISIBLE(null),
    /**
     * Hidden for everyone
     */
    HIDDEN(null),
    /**
     * Permission based
     */
    PERMISSION("karmaapi.hologram.view");

    String permission;

    /**
     * Initialize the view policy
     *
     * @param perm the permission
     */
    ViewPolicy(final String perm) {
        permission = perm;
    }

    /**
     * Get the view permission
     *
     * @return the view permission
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Create a view policy for the specified permission
     *
     * @param permission the permission
     * @return the permission view policy
     */
    public static ViewPolicy forPermission(final Permission permission) {
        ViewPolicy value = PERMISSION;
        value.permission = permission.getName();

        return value;
    }
}
