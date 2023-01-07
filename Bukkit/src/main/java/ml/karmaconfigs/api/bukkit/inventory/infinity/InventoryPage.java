package ml.karmaconfigs.api.bukkit.inventory.infinity;

import ml.karmaconfigs.api.bukkit.KarmaPlugin;
import ml.karmaconfigs.api.bukkit.inventory.infinity.exceptions.NoIndexPageException;
import ml.karmaconfigs.api.bukkit.inventory.infinity.func.Action;
import ml.karmaconfigs.api.bukkit.inventory.infinity.func.FunctionalInventory;
import ml.karmaconfigs.api.bukkit.inventory.infinity.func.ItemFunction;
import ml.karmaconfigs.api.bukkit.reflection.skull.SkinSkull;
import ml.karmaconfigs.api.common.string.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Inventory page
 */
public final class InventoryPage implements FunctionalInventory, InventoryHolder, Listener {

    private final static String NEXT_VALUE = "ewogICJ0aW1lc3RhbXAiIDogMTY3Mjg2NTgyMDgwMSwKICAicHJvZmlsZUlkIiA6ICJiZTQxNTM0NTFiYjQ0MmQ5ODMwNzRhYjRmNDhmMWY5NCIsCiAgInByb2ZpbGVOYW1lIiA6ICJpdHNsZW1vbmNvbGFfIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzVhNDgzZGYwYTlkMGUwMTY2ZDk1MzIyMjM0YjM0N2FiOTk1MjVkMjExZTU4ODNjNTQzMjZkMjM2ZGM3Y2RkODMiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==";
    private final static String NEXT_SIGNATURE = "cqDeD5mSJ0FLL0WGpckpFuwD8N0z0KFt2wxxrFkaGaUqhmJrwjirvSYeKoaRrBRBZTRI+0meDptbkHattf9x5+S8vf7MHsdqOtUlTWvu2HEjU2SX8bKUQWEI//2NN/dci0P/C38z7ZKLpKwk4gvA/511ggxzZq3Z8ZVjVBjzbaE+/ZPA3E/YQXeGie6u2SknSYO/CPufOyX3I7Z7KuJqVPtA0G/XsP7ZEPITphjRbZ79xsFZSeOEG2eC74z0sY7omHKy0l8C9lGdehn2SMoSuLwdVitPD98Ipnwyg3Uzkq/GlBe4EbMBxl+oVtG+9tCr8aoXX1IPKjebCRBd5YB+hgXCOQWIRbcu3+QYfo9CX5lQ0vf5adfY7NxSx6TN9jVYjdW/84UOvb96V/QKwkVFVLTclzg/VDLlysgiL9eVIr51V1ZGw1eZZGW3p2g3h3FN8uuMCQf2jYMjquxp6GbFLdvISy4CaGHSLho6UUAg7EDvcDyl+H0FNfxXbqekqc1ZXzZy99oyX2P0MECiOlUT/lhaEAZdX7nC+w7thhrlfGCFnR6ld8l/dYrM6BYV75ZX02/RXbz3d6+DxFEWmS9pwdf98Y1zZ4V3fIyg8+lbdD2gjPkTzYMaoFBIEizFHsTahAXrElfZ92rI20Z0HD+hNmF94Gld1hGX99aYIMc2yTQ=";
    private final static String PREV_VALUE = "ewogICJ0aW1lc3RhbXAiIDogMTYwNjIyNDg3MjYyNCwKICAicHJvZmlsZUlkIiA6ICI3MmNiMDYyMWU1MTA0MDdjOWRlMDA1OTRmNjAxNTIyZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJNb3M5OTAiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGM5MDg2MmYzNTJjM2QzNmMxN2M4NDMxMTM2NDM5YjdjMGE5N2MxYmRlMjBjZmQ2MjM3YzIzOWIyNmZkMTk1IgogICAgfQogIH0KfQ==";
    private final static String PREV_SIGNATURE = "bJ+SQAGYlk4/P2HdXfCuaC1r7uLf9Mgvrf/BoUf5Pa1g9PbX2KfjNGUp8npZlcG4mVcSbGH1XpbPnOQcf6JNS7LjQzqyWB1m1GQoEu945PvVLvk1cxaIo4W4gqwlZirklSZ9qy3S2FNPPygTPh5L61cpujO/NPu6A9t7uZlnQkVu+AlFEelW+GBLGu9eVgBwkE3Ja1nsPjTwXNug9DNJZcH+fyVtkA+pK+++348Kmy9DLiABpYQDGnRZiB8i5oZwprfF2rYDJT+jyFGABjfAgThnFZmQS1k/2ScHxyncRnrUeMNkJ4fKx3LOst2iKPZ1zl3x+6oQBY9tz7j9Wa8KMFVqM1ph27epy7vn3SFHE0UVwSbj3MRRJ+yg7iZrz5f6+VUMEEKpgJv0LtzY5WO6xoSRZQB0LGjkXVK7bvN4TB0+1uzVTr/xSxstWQBZK8kmHc2t/fZNB/4GYdpdRBp2gk1u4J2DUL1yaoBbHB5+Jrtn0Tz6xOm+jQZXoe3pLt6xGkd+3wKm47AjRKd/AOQq6265+pD9kUxpUl/Hrj8jrws2Ksr2RqS2tykkKho6x3H35wzkY/oiNV08uzAuk6Uqp3bht4zVkA30wn/297obJnACJp5VCChr/t+xiLXQAeSVuNavgBdQMwufkWdZvwvo68C9crhdVec4TLLLDZbRejU=";
    private static ItemStack NEXT;
    private static ItemStack PREV;

    private final int page;
    private final InventoryBook book;
    private final Inventory inventory;

    private final Map<Integer, ItemFunction> functions = new ConcurrentHashMap<>();
    private final Set<Action> open_actions = new LinkedHashSet<>();
    private final Set<Action> close_actions = new LinkedHashSet<>();

    private boolean canClose = true;

    /**
     * Initialize the inventory page
     *
     * @param page the page number
     * @param book the inventory book
     */
    @SuppressWarnings("deprecation")
    public InventoryPage(final int page, final InventoryBook book, final String title) {
        this.page = page;
        this.book = book;

        KarmaPlugin registrar = KarmaPlugin.getABC();

        inventory = registrar.getServer().createInventory(this, 54, StringUtils.toColor(title));

        if (NEXT == null || PREV == null) {
            NEXT = SkinSkull.createSkull(NEXT_VALUE, NEXT_SIGNATURE, (meta) -> {
                meta.setDisplayName(StringUtils.toColor("&aNext"));
                meta.addItemFlags(ItemFlag.values());
            });
            PREV = SkinSkull.createSkull(PREV_VALUE, PREV_SIGNATURE, (meta) -> {
                meta.setDisplayName(StringUtils.toColor("&cPrevious"));
                meta.addItemFlags(ItemFlag.values());
            });
        }

        setItem(53, NEXT).onClick(Action.nextPage(0));

        ItemStack spacer;
        try {
            spacer = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), (byte) 15);
        } catch (Throwable ex) {
            spacer = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        }
        ItemMeta meta = spacer.getItemMeta();
        assert meta != null;
        meta.setDisplayName(StringUtils.toColor("&0 "));
        meta.addItemFlags(ItemFlag.values());

        spacer.setItemMeta(meta);
        for (int i = 46; i < 53; i++) {
            setItem(i, spacer);
        }

        setItem(45, PREV).onClick(Action.previousPage(0));

        registrar.getServer().getPluginManager().registerEvent(InventoryClickEvent.class, this, EventPriority.HIGHEST, (listener, event) -> {
            assert event instanceof InventoryClickEvent;
            InventoryClickEvent click = (InventoryClickEvent) event;

            if (click.getClickedInventory() != null) {
                if (click.getClickedInventory().getHolder() == this) {
                    click.setCancelled(true);

                    ItemFunction function = functions.getOrDefault(click.getSlot(), null);
                    if (function != null) {
                        function.triggerClick(click);
                    }
                }
            }
        }, registrar, false);
        registrar.getServer().getPluginManager().registerEvent(InventoryMoveItemEvent.class, this, EventPriority.HIGHEST, (listener, event) -> {
            assert event instanceof InventoryMoveItemEvent;
            InventoryMoveItemEvent e = (InventoryMoveItemEvent) event;

            e.setCancelled(e.getDestination().getHolder() == this || e.getInitiator().getHolder() == this);
        }, registrar, true);
        registrar.getServer().getPluginManager().registerEvent(InventoryDragEvent.class, this, EventPriority.HIGHEST, (listener, event) -> {
            assert event instanceof InventoryDragEvent;
            InventoryDragEvent e = (InventoryDragEvent) event;

            e.setCancelled(e.getInventory().getHolder() == this);
        }, registrar, true);
        registrar.getServer().getPluginManager().registerEvent(InventoryOpenEvent.class, this, EventPriority.HIGHEST, (listener, event) -> {
            assert event instanceof InventoryOpenEvent;
            InventoryOpenEvent open = (InventoryOpenEvent) event;
            if (open.getInventory().getHolder() == this) {
                HumanEntity human = open.getPlayer();

                if (human instanceof Player) {
                    Player player = (Player) human;

                    for (Action action : open_actions) {
                        action.accept(book, open, player);
                    }
                }
            }
        }, registrar, false);
        registrar.getServer().getPluginManager().registerEvent(InventoryCloseEvent.class, this, EventPriority.HIGHEST, (listener, event) -> {
            assert event instanceof InventoryCloseEvent;
            InventoryCloseEvent close = (InventoryCloseEvent) event;

            if (close.getInventory().getHolder() == this) {
                HumanEntity human = close.getPlayer();

                if (human instanceof Player) {
                    Player player = (Player) human;
                    if (canClose || book.canClose(player)) {
                        for (Action action : close_actions) {
                            action.accept(book, close, player);
                        }
                    } else {
                        KarmaPlugin.getABC().getServer().getScheduler().runTaskLater(KarmaPlugin.getABC(), () -> {
                            try {
                                book.open(player, page);
                            } catch (Throwable ignored) {
                            }
                        }, 10);
                    }
                }
            }
        }, registrar, false);
    }

    /**
     * Add an item to the inventory page
     *
     * @param item the item to add
     * @return the item function
     */
    public ItemFunction addItem(final ItemStack item) {
        ItemFunction function = null;
        for (int slot = 0; slot < 54; slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (stack == null) {
                inventory.setItem(slot, item);
                function = new ItemFunction(book);
                functions.put(slot, function);
                break;
            }
        }

        return function;
    }

    /**
     * Set the item index
     *
     * @param index the item index
     * @param item the item to replace with
     * @return the item function
     */
    public ItemFunction setItem(final int index, final ItemStack item) {
        Objects.requireNonNull(item, "Cannot set null item for inventory");

        if (index > 54 || index < 0) {
            throw new IllegalArgumentException("Cannot set item to index " + index + ". Only from 0 to 54");
        }

        inventory.setItem(index, item);
        ItemFunction function = new ItemFunction(book);
        functions.put(index, function);

        return function;
    }

    /**
     * Remove an item
     *
     * @param item the item to remove
     */
    public void removeItem(final ItemStack item) {
        inventory.remove(item);
    }

    /**
     * Get the index of an item
     *
     * @param item the item to get index for
     * @return the item index
     */
    public int getIndex(final ItemStack item) {
        int slot = -1;

        for (int tmp_slot = 0; tmp_slot < 54; tmp_slot++) {
            ItemStack stack = inventory.getItem(tmp_slot);
            if (stack != null && stack.isSimilar(item)) {
                slot = tmp_slot;
                break;
            }
        }

        return slot;
    }

    /**
     * Get the inventory book owning this page
     *
     * @return the inventory book
     */
    public InventoryBook getBook() {
        return book;
    }

    /**
     * Get the page number
     *
     * @return the page
     */
    public int getPage() {
        return page;
    }

    /**
     * Destroy the page
     *
     * @throws ConcurrentModificationException if the book already removed this page
     */
    public void destroy() throws ConcurrentModificationException {
        HandlerList.unregisterAll(this);
        try {
            book.removePage(page);
            List<HumanEntity> viewers = new ArrayList<>(inventory.getViewers());
            viewers.forEach(HumanEntity::closeInventory);
        } catch (NoIndexPageException e) {
            throw new ConcurrentModificationException();
        }
    }

    /**
     * Close the page for all the players viewing it
     */
    public void closeAll() {
        List<HumanEntity> viewers = new ArrayList<>(inventory.getViewers());
        viewers.forEach((entity) -> {
            if (entity instanceof Player) {
                Player player = (Player) entity;
                book.allowClose(player);

                entity.closeInventory();

                book.denyClose(player);
            }
        });
    }

    /**
     * Get if this inventory is the same as the other
     *
     * @param i the other inventory
     * @return if the inventories are the same
     */
    public boolean isInventory(final Inventory i) {
        if (inventory == null || i == null)
            return false;

        if (!inventory.getViewers().isEmpty() && !i.getViewers().isEmpty()) {
            InventoryView instance_view = inventory.getViewers().get(0).getOpenInventory();
            InventoryView check_view = i.getViewers().get(0).getOpenInventory();

            String instance_title = instance_view.getTitle();
            String check_title = check_view.getTitle();

            return instance_title.equals(check_title);
        } else {
            //We shouldn't do that...
            if (inventory.getHolder() != null && i.getHolder() != null) {
                InventoryHolder instance_holder = inventory.getHolder();
                InventoryHolder check_holder = i.getHolder();

                return instance_holder.equals(check_holder);
            } else {
                return inventory.equals(i);
            }
        }
    }

    /**
     * Get the object's inventory.
     *
     * @return The inventory.
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Prevent this inventory to be closed manually
     *
     * @return this instance
     */
    public InventoryPage preventClose() {
        canClose = false;
        return this;
    }

    /**
     * Allow closing this inventory
     *
     * @return this instance
     */
    public InventoryPage allowClose() {
        canClose = true;
        return this;
    }

    /**
     * On close actions
     *
     * @param action the actions to perform
     * @return this instance
     */
    @Override
    public InventoryPage onClose(final Action... action) {
        close_actions.addAll(Arrays.asList(action));
        return this;
    }

    /**
     * On open actions
     *
     * @param actions the actions to perform
     * @return this instance
     */
    @Override
    public InventoryPage onOpen(final Action... actions) {
        open_actions.addAll(Arrays.asList(actions));
        return this;
    }
}
