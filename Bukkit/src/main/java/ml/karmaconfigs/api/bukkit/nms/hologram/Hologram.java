package ml.karmaconfigs.api.bukkit.nms.hologram;

import ml.karmaconfigs.api.bukkit.nms.hologram.part.ItemPart;
import ml.karmaconfigs.api.bukkit.nms.hologram.part.Line;
import ml.karmaconfigs.api.bukkit.nms.hologram.part.TextPart;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * Hologram
 */
public interface Hologram extends Iterable<Line> {

    /**
     * Append a text line to the hologram
     *
     * @param text the text
     * @return the hologram part
     */
    TextPart append(final String text);

    /**
     * Append an item to the hologram
     *
     * @param item the item to add
     * @param meta_modifier the item meta modifier
     * @return the hologram part
     */
    ItemPart append(final Material item, final Consumer<ItemMeta> meta_modifier);

    /**
     * Append an item to the hologram
     *
     * @param item the item to add
     * @param amount the amount of the item
     * @param meta_modifier the item meta modifier
     * @return the hologram part
     */
    ItemPart append(final Material item, final int amount, final Consumer<ItemMeta> meta_modifier);

    /**
     * Append an item to the hologram
     *
     * @param item the item to add
     * @param amount the amount of the item
     * @param data ONLY FOR LEGACY; the material data
     * @param meta_modifier the item meta modifier
     * @return the hologram part
     */
    ItemPart append(final Material item, final byte data, final int amount, final Consumer<ItemMeta> meta_modifier);

    /**
     * Append an item to the hologram
     *
     * @param item the item to add
     * @return the hologram part
     */
    ItemPart append(final ItemStack item);

    /**
     * Insert a text line to the hologram
     *
     * @param index the index to insert at
     * @param text the text
     * @return the hologram part
     */
    TextPart insertAt(final int index, final String text);

    /**
     * Insert an item to the hologram
     *
     * @param index the index to insert at
     * @param item the item to add
     * @param meta_modifier the item meta modifier
     * @return the hologram part
     */
    ItemPart insertAt(final int index, final Material item, final Consumer<ItemMeta> meta_modifier);

    /**
     * Insert an item to the hologram
     *
     * @param index the index to insert at
     * @param item the item to add
     * @param amount the amount of the item
     * @param meta_modifier the item meta modifier
     * @return the hologram part
     */
    ItemPart insertAt(final int index, final Material item, final int amount, final Consumer<ItemMeta> meta_modifier);

    /**
     * Insert an item to the hologram
     *
     * @param index the index to insert at
     * @param item the item to add
     * @param amount the amount of the item
     * @param data ONLY FOR LEGACY; the material data
     * @param meta_modifier the item meta modifier
     * @return the hologram part
     */
    ItemPart insertAt(final int index, final Material item, final byte data, final int amount, final Consumer<ItemMeta> meta_modifier);

    /**
     * Insert an item to the hologram
     *
     * @param index the index to insert at
     * @param item the item to add
     * @return the hologram part
     */
    ItemPart insertAt(final int index, final ItemStack item);

    /**
     * Get the hologram part at specified index
     *
     * @param index the part index
     * @return the part or null
     */
    @Nullable
    Line getPart(final int index);

    /**
     * Get the index of a part
     *
     * @param part the part
     * @return the index of the part or -1
     */
    int getIndex(final Line part);

    /**
     * Remove a hologram part
     *
     * @param index the part index to remove
     * @return the removed part
     */
    Line remove(final int index);

    /**
     * Remove an unknown part type
     *
     * @param unknown the unknown part type
     * @return if the part could be removed
     */
    boolean remove(final Line unknown);

    /**
     * Remove the specified text part from the hologram
     *
     * @param part the part to remove
     * @return if the part could be removed or not
     */
    boolean removeText(final TextPart part);

    /**
     * Remove the specified item part from the hologram
     *
     * @param part the part to remove
     * @return if the part could be removed or not
     */
    boolean removeItem(final ItemPart part);

    /**
     * Clear the hologram
     */
    void clear();

    /**
     * Get the hologram size
     *
     * @return the hologram size
     */
    int size();

    /**
     * Get the hologram part separator
     *
     * @return the hologram part separator
     */
    double partSeparation();

    /**
     * Teleport the hologram at the specified location
     *
     * @param location the new hologram location
     */
    void teleport(final Location location);

    /**
     * Teleport the hologram at the specified world
     *
     * @param world the new hologram world
     */
    void teleport(final World world);

    /**
     * Teleport the hologram at the specified coordinates
     *
     * @param x the x cord
     * @param y the y cord
     * @param z the z cord
     */
    void teleport(final double x, final double y, final double z);

    /**
     * Teleport the hologram at the specified world and coordinates
     *
     * @param world the hologram new world
     * @param x the x cord
     * @param y the y cord
     * @param z the z cord
     */
    void teleport(final World world, final double x, final double y, final double z);

    /**
     * Update the hologram
     */
    void update();

    /**
     * Refresh the hologram
     */
    void refresh();

    /**
     * Move the hologram x
     *
     * @param x the new hologram x
     */
    void moveX(final double x);

    /**
     * Move the hologram y
     *
     * @param y the new hologram y
     */
    void moveY(final double y);

    /**
     * Move the hologram z
     *
     * @param z the new hologram z
     */
    void moveZ(final double z);

    /**
     * Get the default hologram visibility
     *
     * @return if the hologram is visible by default
     */
    boolean defaultVisibility();

    /**
     * Set the hologram default visibility
     *
     * @param status the hologram default visibility
     */
    void setDefaultVisibility(final boolean status);

    /**
     * Show the hologram to the specified players
     *
     * @param players the players to show the hologram to
     */
    void show(final Player... players);

    /**
     * Hide the hologram to the specified players
     *
     * @param players the players to hide the hologram to
     */
    void hide(final Player... players);

    /**
     * Show the hologram to the specified players
     *
     * @param players the players to show the hologram to
     */
    default void show(final Collection<Player> players) {
        show(players.toArray(new Player[0]));
    }

    /**
     * Hide the hologram to the specified players
     *
     * @param players the player to hide the hologram to
     */
    default void hide(final Collection<Player> players) {
        hide(players.toArray(new Player[0]));
    }

    /**
     * Get if the player can see the hologram
     *
     * @param player the player to check
     * @return if the player can see the hologram
     */
    boolean canSee(final Player player);

    /**
     * Get the players that can see this hologram
     *
     * @return the players that have view access
     */
    default Player[] getCanSee() {
        List<Player> canSee = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (canSee(player)) {
                canSee.add(player);
            }
        }

        return canSee.toArray(new Player[0]);
    }

    /**
     * Reset the visibility for the specified players
     *
     * @param players the players to reset the visibility for
     */
    void resetVisibility(final Player... players);

    /**
     * Reset the visibility for all the players
     */
    default void resetVisibility() {
        resetVisibility(Bukkit.getServer().getOnlinePlayers().toArray(new Player[0]));
    }

    /**
     * Get the hologram location
     *
     * @return the hologram location
     */
    Location getLocation();

    /**
     * Get the hologram world
     *
     * @return the hologram world
     */
    World getWorld();

    /**
     * Get the hologram x position
     *
     * @return the hologram x position
     */
    double getX();

    /**
     * Get the hologram y position
     *
     * @return the hologram y position
     */
    double getY();

    /**
     * Get the hologram z position
     *
     * @return the hologram z position
     */
    double getZ();

    /**
     * Get the hologram lifetime
     *
     * @return the hologram lifetime
     */
    long getLifeTime();

    /**
     * Delete the hologram
     */
    void delete();

    /**
     * Get if the hologram exists, for example,
     * this returns false after {@link Hologram#delete()}
     *
     * @return if the hologram exists
     */
    boolean exists();
}
