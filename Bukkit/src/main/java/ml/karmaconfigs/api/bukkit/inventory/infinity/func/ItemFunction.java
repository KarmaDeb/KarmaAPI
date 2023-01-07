package ml.karmaconfigs.api.bukkit.inventory.infinity.func;

import ml.karmaconfigs.api.bukkit.inventory.infinity.InventoryBook;
import ml.karmaconfigs.api.bukkit.inventory.infinity.InventoryPage;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public final class ItemFunction {

    private final InventoryBook book;

    private final Set<Action> click_actions = new LinkedHashSet<>();

    /**
     * Initialize the item function
     *
     * @param book the book inventory owner
     */
    public ItemFunction(final InventoryBook book) {
        this.book = book;
    }

    /**
     * On item click action
     *
     * @param action the action to perform
     * @return this instance
     */
    public ItemFunction onClick(final Action... action) {
        click_actions.addAll(Arrays.asList(action));
        return this;
    }

    public void triggerClick(final InventoryClickEvent click) {
        HumanEntity human = click.getWhoClicked();

        if (human instanceof Player) {
            Player player = (Player) human;

            Inventory inv = click.getClickedInventory();
            InventoryPage page = book.getPage(player);
            if (page != null) {
                if (page.isInventory(inv)) {
                    click.setCancelled(true);

                    for (Action rs : click_actions) {
                        rs.accept(book, click, player);
                    }
                }
            }
        }
    }
}
