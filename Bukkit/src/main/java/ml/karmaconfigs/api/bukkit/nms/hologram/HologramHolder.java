package ml.karmaconfigs.api.bukkit.nms.hologram;

import ml.karmaconfigs.api.bukkit.nms.NMSHelper;
import ml.karmaconfigs.api.bukkit.nms.hologram.line.HologramItemLine;
import ml.karmaconfigs.api.bukkit.nms.hologram.line.HologramTextLine;
import ml.karmaconfigs.api.bukkit.nms.hologram.part.ItemPart;
import ml.karmaconfigs.api.bukkit.nms.hologram.part.Line;
import ml.karmaconfigs.api.bukkit.nms.hologram.part.TextPart;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class HologramHolder implements Hologram {

    private World world;

    private double x;
    private double y;
    private double z;

    private int chunkX;
    private int chunkZ;

    private final List<Line> lines = new ArrayList<>();

    private final long lifetime = System.currentTimeMillis();
    private boolean deleted;

    private boolean visibility = false;

    private final Set<UUID> canView = new HashSet<>();

    public HologramHolder(final Location location) {
        Objects.requireNonNull(location, "Hologram location cannot be null");
        Objects.requireNonNull(location.getWorld(), "Hologram location world cannot be null");
        updateLocation(location.getWorld(), location.getX(), location.getY(), location.getZ());
    }

    public boolean isInChunk(final Chunk chunk) {
        return chunk.getX() == chunkX && chunk.getZ() == chunkZ;
    }

    public boolean isInChunk(final int x, final int z) {
        return x == chunkX && z == chunkZ;
    }

    /**
     * Get the hologram location
     *
     * @return the hologram location
     */
    @Override
    public Location getLocation() {
        return new Location(world, x, y, z);
    }

    /**
     * Get the hologram world
     *
     * @return the hologram world
     */
    @Override
    public World getWorld() {
        return world;
    }

    /**
     * Get the hologram x position
     *
     * @return the hologram x position
     */
    @Override
    public double getX() {
        return x;
    }

    /**
     * Get the hologram y position
     *
     * @return the hologram y position
     */
    @Override
    public double getY() {
        return y;
    }

    /**
     * Get the hologram z position
     *
     * @return the hologram z position
     */
    @Override
    public double getZ() {
        return z;
    }

    /**
     * Update the hologram location
     *
     * @param w the hologram new world
     * @param x the hologram new x position
     * @param y the hologram new y position
     * @param z the hologram new z position
     */
    public void updateLocation(final World w, final double x, final double y, final double z) {
        Objects.requireNonNull(w, "World cannot be null");
        world = w;
        this.x = x;
        this.y = y;
        this.z = z;
        int x_floor = (int) x;
        int z_floor = (int) z;
        chunkX = (x_floor == x) ? x_floor : (x_floor - (int)(Double.doubleToRawLongBits(x) >>> 63L)) >> 4;
        chunkZ = (z_floor == z) ? z_floor : (z_floor - (int)(Double.doubleToRawLongBits(z) >>> 63L)) >> 4;
    }

    /**
     * Append a text line to the hologram
     *
     * @param text the text
     * @return the hologram part
     */
    @Override
    public TextPart append(final String text) {
        if (deleted) {
            throw new NullPointerException("Cannot append a line to removed hologram");
        }
        HologramTextLine line = new HologramTextLine(this, text);
        lines.add(line);
        update();

        return line;
    }

    /**
     * Append an item to the hologram
     *
     * @param item          the item to add
     * @param meta_modifier the item meta modifier
     * @return the hologram part
     */
    @Override
    public ItemPart append(final Material item, final Consumer<ItemMeta> meta_modifier) {
        if (deleted) {
            throw new NullPointerException("Cannot append a line to removed hologram");
        }

        ItemStack stack = new ItemStack(item, 1);
        if (meta_modifier != null) {
            ItemMeta meta = stack.getItemMeta();
            if (meta != null)
                meta_modifier.accept(meta);

            stack.setItemMeta(meta);
        }

        HologramItemLine line = new HologramItemLine(this, stack);
        lines.add(line);
        update();

        return line;
    }

    /**
     * Append an item to the hologram
     *
     * @param item          the item to add
     * @param amount        the amount of the item
     * @param meta_modifier the item meta modifier
     * @return the hologram part
     */
    @Override
    public ItemPart append(final Material item, final int amount, final Consumer<ItemMeta> meta_modifier) {
        if (deleted) {
            throw new NullPointerException("Cannot append a line to removed hologram");
        }

        ItemStack stack = new ItemStack(item, amount);
        if (meta_modifier != null) {
            ItemMeta meta = stack.getItemMeta();
            if (meta != null)
                meta_modifier.accept(meta);

            stack.setItemMeta(meta);
        }

        HologramItemLine line = new HologramItemLine(this, stack);
        lines.add(line);
        update();

        return line;
    }

    /**
     * Append an item to the hologram
     *
     * @param item          the item to add
     * @param data          ONLY FOR LEGACY; the material data
     * @param amount        the amount of the item
     * @param meta_modifier the item meta modifier
     * @return the hologram part
     */
    @Override
    @SuppressWarnings("deprecation")
    public ItemPart append(final Material item, final byte data, final int amount, final Consumer<ItemMeta> meta_modifier) {
        if (deleted) {
            throw new NullPointerException("Cannot append a line to removed hologram");
        }

        ItemStack stack = new ItemStack(item, amount, data);
        if (meta_modifier != null) {
            ItemMeta meta = stack.getItemMeta();
            if (meta != null)
                meta_modifier.accept(meta);

            stack.setItemMeta(meta);
        }

        HologramItemLine line = new HologramItemLine(this, stack);
        lines.add(line);
        update();

        return line;
    }

    /**
     * Append an item to the hologram
     *
     * @param item the item to add
     * @return the hologram part
     */
    @Override
    public ItemPart append(final ItemStack item) {
        if (deleted) {
            throw new NullPointerException("Cannot append a line to removed hologram");
        }

        HologramItemLine line = new HologramItemLine(this, item);
        lines.add(line);
        update();

        return line;
    }

    /**
     * Insert a text line to the hologram
     *
     * @param index the index to insert at
     * @param text  the text
     * @return the hologram part
     */
    @Override
    public TextPart insertAt(final int index, final String text) {
        if (deleted) {
            throw new NullPointerException("Cannot append a line to removed hologram");
        }

        HologramTextLine line = new HologramTextLine(this, text);
        if (lines.size() < index) {
            lines.set(index, line);
        } else {
            int diff = index - lines.size();
            while (diff-- != 0)
                append("");

            lines.set(index, line);
        }
        update();

        return line;
    }

    /**
     * Insert an item to the hologram
     *
     * @param index         the index to insert at
     * @param item          the item to add
     * @param meta_modifier the item meta modifier
     * @return the hologram part
     */
    @Override
    public ItemPart insertAt(final int index, final Material item, final Consumer<ItemMeta> meta_modifier) {
        if (deleted) {
            throw new NullPointerException("Cannot append a line to removed hologram");
        }

        ItemStack stack = new ItemStack(item, 1);
        if (meta_modifier != null) {
            ItemMeta meta = stack.getItemMeta();
            if (meta != null)
                meta_modifier.accept(meta);

            stack.setItemMeta(meta);
        }

        HologramItemLine line = new HologramItemLine(this, stack);
        if (lines.size() < index) {
            lines.set(index, line);
        } else {
            int diff = index - lines.size();
            while (diff-- != 0)
                append("");

            lines.set(index, line);
        }
        update();

        return line;
    }

    /**
     * Insert an item to the hologram
     *
     * @param index         the index to insert at
     * @param item          the item to add
     * @param amount        the amount of the item
     * @param meta_modifier the item meta modifier
     * @return the hologram part
     */
    @Override
    public ItemPart insertAt(final int index, final Material item, final int amount, final Consumer<ItemMeta> meta_modifier) {
        if (deleted) {
            throw new NullPointerException("Cannot append a line to removed hologram");
        }

        ItemStack stack = new ItemStack(item, amount);
        if (meta_modifier != null) {
            ItemMeta meta = stack.getItemMeta();
            if (meta != null)
                meta_modifier.accept(meta);

            stack.setItemMeta(meta);
        }

        HologramItemLine line = new HologramItemLine(this, stack);
        if (lines.size() < index) {
            lines.set(index, line);
        } else {
            int diff = index - lines.size();
            while (diff-- != 0)
                append("");

            lines.set(index, line);
        }
        update();

        return line;
    }

    /**
     * Insert an item to the hologram
     *
     * @param index         the index to insert at
     * @param item          the item to add
     * @param data          ONLY FOR LEGACY; the material data
     * @param amount        the amount of the item
     * @param meta_modifier the item meta modifier
     * @return the hologram part
     */
    @Override
    @SuppressWarnings("deprecation")
    public ItemPart insertAt(final int index, final Material item, final byte data, final int amount, final Consumer<ItemMeta> meta_modifier) {
        if (deleted) {
            throw new NullPointerException("Cannot append a line to removed hologram");
        }

        ItemStack stack = new ItemStack(item, amount, data);
        if (meta_modifier != null) {
            ItemMeta meta = stack.getItemMeta();
            if (meta != null)
                meta_modifier.accept(meta);

            stack.setItemMeta(meta);
        }

        HologramItemLine line = new HologramItemLine(this, stack);
        if (lines.size() < index) {
            lines.set(index, line);
        } else {
            int diff = index - lines.size();
            while (diff-- != 0)
                append("");

            lines.set(index, line);
        }
        update();

        return line;
    }

    /**
     * Insert an item to the hologram
     *
     * @param index the index to insert at
     * @param item  the item to add
     * @return the hologram part
     */
    @Override
    public ItemPart insertAt(final int index, final ItemStack item) {
        if (deleted) {
            throw new NullPointerException("Cannot append a line to removed hologram");
        }

        HologramItemLine line = new HologramItemLine(this, item);
        if (lines.size() < index) {
            lines.set(index, line);
        } else {
            int diff = index - lines.size();
            while (diff-- != 0)
                append("");

            lines.set(index, line);
        }
        update();

        return line;
    }

    /**
     * Get the hologram part at specified index
     *
     * @param index the part index
     * @return the part or null
     */
    @Override
    public @Nullable Line getPart(final int index) {
        if (index < lines.size()) {
            return lines.get(index);
        }

        return null;
    }

    /**
     * Get the index of a part
     *
     * @param part the part
     * @return the index of the part or -1
     */
    @Override
    public int getIndex(final Line part) {
        return lines.indexOf(part);
    }

    /**
     * Remove a hologram part
     *
     * @param index the part index to remove
     * @return the removed part
     */
    @Override
    public Line remove(final int index) {
        if (index < lines.size()) {
            return lines.remove(index);
        }

        return null;
    }

    /**
     * Remove an unknown part type
     *
     * @param unknown the unknown part type
     * @return if the part could be removed
     */
    @Override
    public boolean remove(final Line unknown) {
        return lines.remove(unknown);
    }

    /**
     * Remove the specified text part from the hologram
     *
     * @param part the part to remove
     * @return if the part could be removed or not
     */
    @Override
    public boolean removeText(final TextPart part) {
        return lines.remove(part);
    }

    /**
     * Remove the specified item part from the hologram
     *
     * @param part the part to remove
     * @return if the part could be removed or not
     */
    @Override
    public boolean removeItem(final ItemPart part) {
        return lines.remove(part);
    }

    /**
     * Clear the hologram
     */
    @Override
    public void clear() {
        canView.clear();
        NMSHelper.revokeHologram(this);
        lines.clear();
    }

    /**
     * Get the hologram size
     *
     * @return the hologram size
     */
    @Override
    public int size() {
        return lines.size();
    }

    /**
     * Get the hologram part separator
     *
     * @return the hologram part separator
     */
    @Override
    public double partSeparation() {
        return 0.02;
    }

    /**
     * Teleport the hologram at the specified location
     *
     * @param location the new hologram location
     */
    @Override
    public void teleport(final Location location) {
        Objects.requireNonNull(location, "Hologram location cannot be null");
        teleport(location.getWorld(), location.getX(), location.getY(), location.getZ());
    }

    /**
     * Teleport the hologram at the specified world
     *
     * @param world the new hologram world
     */
    @Override
    public void teleport(final World world) {
        teleport(world, x, y, z);
    }

    /**
     * Teleport the hologram at the specified coordinates
     *
     * @param x the x cord
     * @param y the y cord
     * @param z the z cord
     */
    @Override
    public void teleport(final double x, final double y, final double z) {
        teleport(world, x, y, z);
    }

    /**
     * Teleport the hologram at the specified world and coordinates
     *
     * @param w the hologram new world
     * @param x     the x cord
     * @param y     the y cord
     * @param z     the z cord
     */
    @Override
    public void teleport(final World w, final double x, final double y, final double z) {
        if (deleted) {
            throw new NullPointerException("Cannot move a removed hologram");
        }
        Objects.requireNonNull(world, "Cannot move an hologram to a null world");

        if (!w.getUID().equals(world.getUID())) {
            world = w;
            for (Line line : lines)
                line.deSpawn();
        }

        updateLocation(world, x, y, z);
        update();
    }

    /**
     * Move the hologram x
     *
     * @param x the new hologram x
     */
    @Override
    public void moveX(final double x) {
        teleport(world, x, y, z);
    }

    /**
     * Move the hologram y
     *
     * @param y the new hologram y
     */
    @Override
    public void moveY(final double y) {
        teleport(world, x, y, z);
    }

    /**
     * Move the hologram z
     *
     * @param z the new hologram z
     */
    @Override
    public void moveZ(final double z) {
        teleport(world, x, y, z);
    }

    /**
     * Get the default hologram visibility
     *
     * @return if the hologram is visible by default
     */
    @Override
    public boolean defaultVisibility() {
        return visibility;
    }

    /**
     * Set the hologram default visibility
     *
     * @param status the hologram default visibility
     */
    @Override
    public void setDefaultVisibility(final boolean status) {
        visibility = status;
    }

    /**
     * Show the hologram to the specified players
     *
     * @param players the players to show the hologram to
     */
    @Override
    public void show(final Player... players) {
        for (Player p : players)
            canView.add(p.getUniqueId());
    }

    /**
     * Hide the hologram to the specified players
     *
     * @param players the players to hide the hologram to
     */
    @Override
    public void hide(final Player... players) {
        for (Player player : players)
            canView.remove(player.getUniqueId());
    }

    /**
     * Get if the player can see the hologram
     *
     * @param player the player to check
     * @return if the player can see the hologram
     */
    @Override
    public boolean canSee(final Player player) {
        return canView.contains(player.getUniqueId());
    }

    /**
     * Reset the visibility for the specified players
     *
     * @param players the players to reset the visibility for
     */
    @Override
    public void resetVisibility(final Player... players) {
        for (Player p : players) {
            if (visibility) {
                canView.add(p.getUniqueId());
            } else {
                canView.remove(p.getUniqueId());
            }
        }
    }

    /**
     * Get the hologram lifetime
     *
     * @return the hologram lifetime
     */
    @Override
    public long getLifeTime() {
        return System.currentTimeMillis() - lifetime;
    }

    /**
     * Delete the hologram
     */
    @Override
    public void delete() {
        if (!deleted) {
            NMSHelper.revokeHologram(this);
            deleted = true;
            clear();
        }
    }

    /**
     * Get if the hologram exists, for example,
     * this returns false after {@link Hologram#delete()}
     *
     * @return if the hologram exists
     */
    @Override
    public boolean exists() {
        return !deleted;
    }

    /**
     * Update the hologram
     */
    @Override
    public void update() {
        NMSHelper.revokeHologram(this);

        if (world.isChunkLoaded(chunkX, chunkZ)) {
            double current_y = y;
            boolean first = true;

            for (Line line : lines) {
                current_y -= line.getHeight();
                if (first) {
                    first = false;
                } else {
                    current_y -= partSeparation();
                }

                if (line.exists()) {
                    line.teleport(x, current_y, z);
                    continue;
                }

                line.spawn(world, x, current_y, z);
            }
        }
    }

    /**
     * Refresh the hologram
     */
    @Override
    public void refresh() {
        NMSHelper.revokeHologram(this);
        for (Line line : lines) {
            line.deSpawn();
        }

        double current_y = y;
        boolean first = true;

        for (Line line : lines) {
            current_y -= line.getHeight();
            if (first) {
                first = false;
            } else {
                current_y -= partSeparation();
            }

            line.spawn(this.world, this.x, current_y, this.z);
        }
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @NotNull
    @Override
    public Iterator<Line> iterator() {
        return lines.iterator();
    }
}
