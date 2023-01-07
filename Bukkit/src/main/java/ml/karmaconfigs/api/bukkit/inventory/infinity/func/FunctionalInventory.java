package ml.karmaconfigs.api.bukkit.inventory.infinity.func;

import ml.karmaconfigs.api.bukkit.inventory.infinity.InventoryPage;

public interface FunctionalInventory {

    /**
     * On close actions
     *
     * @param action the actions to perform
     * @return this instance
     */
    InventoryPage onClose(final Action... action);

    /**
     * On open actions
     *
     * @param actions the actions to perform
     * @return this instance
     */
    InventoryPage onOpen(final Action... actions);
}
