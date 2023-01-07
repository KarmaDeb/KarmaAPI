package ml.karmaconfigs.api.bukkit.inventory.infinity;

import ml.karmaconfigs.api.bukkit.inventory.infinity.exceptions.EmptyBookException;
import ml.karmaconfigs.api.bukkit.inventory.infinity.exceptions.NoIndexPageException;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class InventoryBook {

    private final static Map<UUID, InventoryBook> instances = new ConcurrentHashMap<>();

    private final Map<InventoryBook, Map<Integer, InventoryPage>> pages = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> open_pages = new ConcurrentHashMap<>();

    private final Set<UUID> close_whitelist = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final UUID id;
    public final String TITLE;

    /**
     * Initialize the inventory book
     *
     * @param title the book title
     */
    public InventoryBook(final String title) {
        id = UUID.randomUUID();
        TITLE = title;

        instances.put(id, this);
    }

    /**
     * Get the book by its ID
     *
     * @param id the book id
     * @return the book
     */
    @Nullable
    public static InventoryBook getBook(final UUID id) {
        return instances.getOrDefault(id, null);
    }

    /**
     * Get the book unique ID
     *
     * @return the book unique ID
     */
    public UUID getUniqueId() {
        return id;
    }

    /**
     * Allow a player to close the inventory
     *
     * @param player the player to allow
     */
    public void allowClose(final Player player) {
        close_whitelist.add(player.getUniqueId());
    }

    /**
     * Deny a player from closing the inventory
     *
     * @param player the player to deny
     */
    public void denyClose(final Player player) {
        close_whitelist.remove(player.getUniqueId());
    }

    /**
     * Get if a player can close the inventory
     *
     * @param player the player to check
     * @return if the player can close the inventory
     */
    public boolean canClose(final Player player) {
        return close_whitelist.contains(player.getUniqueId());
    }

    /**
     * Sorts the inventory pages, by shifting if there's a page jump.
     * For example, this method is useful after running {@link InventoryBook#removePage(int) removePage}
     * method
     *
     * @return this instance
     */
    public InventoryBook sort() {
        Map<Integer, InventoryPage> map = pages.getOrDefault(this, new ConcurrentHashMap<>());
        List<Integer> page_count = new ArrayList<>(map.keySet());
        Collections.sort(page_count);

        int current_null = -1;

        for (int i = 0; i < page_count.get(page_count.size() - 1); i++) {
            InventoryPage key = map.get(i);

            if (key == null) {
                if (current_null == -1) {
                    current_null = i;
                }
            } else {
                if (current_null != -1) {
                    map.put(current_null, map.get(i));
                    map.remove(i);

                    i = current_null;
                    current_null = -1;
                }
            }
        }

        pages.put(this, map);

        return this;
    }

    /**
     * Add a page to the book
     *
     * @return this instance
     */
    public InventoryPage addPage() {
        return addPage(TITLE);
    }

    /**
     * Add a page to the book
     *
     * @param title the page title
     * @return this instance
     */
    public InventoryPage addPage(final String title) {
        Map<Integer, InventoryPage> map = pages.getOrDefault(this, new ConcurrentHashMap<>());
        List<Integer> page_count = new ArrayList<>(map.keySet());
        Collections.sort(page_count);

        InventoryPage page;
        if (page_count.isEmpty()) {
            page = new InventoryPage(0, this, title);
            map.put(0, page);
        } else {
            page = new InventoryPage(page_count.get(page_count.size() - 1) + 1, this, title);
            map.put(page.getPage(), page);
        }
        pages.put(this, map);

        return page;
    }

    /**
     * Set a page of the book
     *
     * @param page the page number
     * @return this instance
     */
    public InventoryPage setPage(final int page) {
        return setPage(page, TITLE);
    }

    /**
     * Set a page of the book
     *
     * @param page the page number
     * @param title the page title
     * @return this instance
     */
    public InventoryPage setPage(final int page, final String title) {
        Map<Integer, InventoryPage> map = pages.getOrDefault(this, new ConcurrentHashMap<>());

        InventoryPage new_page = new InventoryPage(page, this, title);
        map.put(page, new_page);

        pages.put(this, map);

        return new_page;
    }

    /**
     * Remove a page from the book
     *
     * @param page the page to remove
     * @return the removed page
     * @throws NoIndexPageException if the page does not exist
     */
    public InventoryPage removePage(final int page) throws NoIndexPageException {
        Map<Integer, InventoryPage> map = pages.getOrDefault(this, new ConcurrentHashMap<>());
        if (map.containsKey(page)) {
            InventoryPage p = map.remove(page);
            HandlerList.unregisterAll(p);
            p.closeAll();

            return p;
        } else {
            throw new NoIndexPageException(page);
        }
    }

    /**
     * Get the page the player has open
     *
     * @param player the player
     * @return the player page
     */
    public InventoryPage getPage(final Player player) {
        int page = open_pages.getOrDefault(player.getUniqueId(), -1);
        if (page >= 0) {
            Map<Integer, InventoryPage> page_map = pages.getOrDefault(this, new ConcurrentHashMap<>());
            return page_map.getOrDefault(page, null);
        }

        return null;
    }

    /**
     * Get the previous page number for the player
     *
     * @param player the player
     * @return the previous page index for the player
     */
    public int getPreviousPageIndex(final Player player) {
        int current_page = open_pages.getOrDefault(player.getUniqueId(), -1);
        if (current_page > 0) {
            List<Integer> numbers = new ArrayList<>(pages.getOrDefault(this, new ConcurrentHashMap<>()).keySet());
            Collections.sort(numbers);
            if (!numbers.isEmpty()) {
                int indexOf = numbers.indexOf(current_page);
                try {
                    return numbers.get(indexOf - 1);
                } catch (IndexOutOfBoundsException ignored) {}
            }
        }

        return -1;
    }

    /**
     * Get the page index of the player
     *
     * @param player the player
     * @return the page index
     */
    public int getPageIndex(final Player player) {
        return open_pages.getOrDefault(player.getUniqueId(), -1);
    }

    /**
     * Get the next page number for the player
     *
     * @param player the player
     * @return the next page index for the player
     */
    public int getNextPageIndex(final Player player) {
        int current_page = open_pages.getOrDefault(player.getUniqueId(), -1);
        if (current_page < pages.size()) {
            List<Integer> numbers = new ArrayList<>(pages.getOrDefault(this, new ConcurrentHashMap<>()).keySet());
            Collections.sort(numbers);
            if (!numbers.isEmpty()) {
                int indexOf = numbers.indexOf(current_page);
                try {
                    return numbers.get(indexOf + 1);
                } catch (IndexOutOfBoundsException ignored) {}
            }
        }

        return -1;
    }

    /**
     * Get the page from its index
     *
     * @param index the page index
     * @return the page
     */
    public InventoryPage getPage(final int index) {
        Map<Integer, InventoryPage> page_map = pages.getOrDefault(this, new ConcurrentHashMap<>());
        return page_map.getOrDefault(index, null);
    }

    /**
     * Open the book to the player
     *
     * @param player the player
     * @throws EmptyBookException if the book is empty
     * @throws NoIndexPageException if the book has no page 0
     */
    public void open(final Player player) throws EmptyBookException, NoIndexPageException {
        open(player, 0);
    }

    /**
     * Open the book to the player
     *
     * @param player the player
     * @throws EmptyBookException if the book is empty
     * @throws NoIndexPageException if the book has no page X
     */
    public void open(final Player player, final int page) throws EmptyBookException, NoIndexPageException {
        if (pages.containsKey(this)) {
            Map<Integer, InventoryPage> page_map = pages.getOrDefault(this, new ConcurrentHashMap<>());
            InventoryPage p = page_map.getOrDefault(page, null);
            if (p == null) {
                throw new NoIndexPageException(page);
            }

            player.openInventory(p.getInventory());
            open_pages.put(player.getUniqueId(), page);
        } else {
            throw new EmptyBookException();
        }
    }

    /**
     * Get the open page
     *
     * @return the open page
     */
    public int getPages() {
        return pages.size();
    }
}
