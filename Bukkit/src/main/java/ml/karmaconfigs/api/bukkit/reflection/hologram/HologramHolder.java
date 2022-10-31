package ml.karmaconfigs.api.bukkit.reflection.hologram;

import ml.karmaconfigs.api.bukkit.reflection.hologram.component.HologramComponent;
import ml.karmaconfigs.api.bukkit.reflection.hologram.policy.ViewPolicy;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Hologram holder
 */
public abstract class HologramHolder {

    /**
     * Get the hologram ID
     *
     * @return the hologram UUID
     */
    public abstract UUID getId();

    /**
     * Get the hologram location
     *
     * @return the hologram location
     */
    public abstract Location getLocation();

    /**
     * Teleport the hologram to the specified location
     *
     * @param location the new hologram location
     */
    public abstract void teleport(final Location location);

    /**
     * Get if the player can see the hologram
     *
     * @param player the player to check
     * @return if the player can see the hologram
     */
    public abstract boolean canSee(final OfflinePlayer player);

    /**
     * Update view policy for new users
     *
     * @param policy the new view policy
     */
    public abstract void updateViewPolicy(final ViewPolicy policy);

    /**
     * Update the view policy
     *
     * @param player the player to update view policy to
     * @param policy the new view policy
     */
    public abstract void updateViewPolicy(final OfflinePlayer player, final ViewPolicy policy);

    /**
     * Get the player view policy for this hologram
     *
     * @param player the player
     * @return the player view policy for this hologram
     */
    public abstract ViewPolicy getViewPolicy(final OfflinePlayer player);

    /**
     * Get the hologram default view policy
     *
     * @return the hologram view policy
     */
    public abstract ViewPolicy getViewPolicy();

    /**
     * Add a component to the hologram
     *
     * @param component the hologram component to add
     */
    public abstract void addComponent(final HologramComponent component);

    /**
     * Get the hologram component
     *
     * @param line the hologram line
     * @return the hologram component
     */
    public abstract @Nullable HologramComponent getComponent(final int line);

    /**
     * Remove a hologram component by index
     *
     * @param line the hologram component line
     * @return the hologram component that has been removed
     */
    public abstract @Nullable HologramComponent removeComponent(final int line);

    /**
     * Remove a hologram component
     *
     * @param component the component to remove
     */
    public abstract boolean removeComponent(final HologramComponent component);

    /**
     * Get the hologram components
     *
     * @return the hologram components
     */
    public abstract HologramComponent[] getComponents();

    /**
     * Update the hologram for everyone that
     * can see it
     */
    public abstract void update();

    /**
     * Save the hologram
     */
    public abstract void save();

    /**
     * Load from a hologram id
     *
     * @param hologram_id the hologram id
     * @return if the hologram could be loaded
     */
    public abstract boolean load(final UUID hologram_id);
}
