---
description: The inventory page has some methods that may be usefull in some situations
---

# ❓ InventoryPage methods

```java
/**
 * Set the inventory title
 *
 * @param String the new inventory title
 * @return this page
 */
InventoryPage#title(String): InventoryPage

/**
 * Set the next page item name
 *
 * @param String the new item name
 * @return this page
 */
InventoryPage#nextItemName(String): InventoryPage

/**
 * Set the previous page item name
 *
 * @param String the new item name
 * @return this page
 */
InventoryPage#previousItemName(String): InventoryPage

/**
 * Add an item to the inventory page
 *
 * @param ItemStack the item to add
 * @return the item function
 */
InventoryPage#addItem(ItemStack): ItemFunction

/**
 * Set the item index
 *
 * @param Integer the item index
 * @param ItemStack the item to replace with
 * @return the item function
 */
InventoryPage#setItem(Integer, ItemStack): ItemFunction

/**
 * Update the inventory title
 */
InventoryPage#updateTitle(): void

/**
 * Remove an item
 *
 * @param ItemStack the item to remove
 */
InventoryPage#removeItem(ItemStack): void

/**
 * Get the index of an item
 *
 * @param ItemStack the item to get index for
 * @return the item index
 */
InventoryPage#getIndex(ItemStack): Integer

/**
 * Get the inventory book owning this page
 *
 * @return the inventory book
 */
InventoryPage#getBook(): InventoryBook

/**
 * Get the page number
 *
 * @return the page
 */
InventoryPage#getPage(): Integer

/**
 * Destroy the page
 *
 * @throws ConcurrentModificationException if the book already removed this page
 */
InventoryPage#destroy(): void throws ConcurrentModificationException

/**
 * Close the page for all the players viewing it
 */
InventoryPage#closeAll(): void

/**
 * Get if this inventory is the same as the other
 *
 * @param Inventory the other inventory
 * @return if the inventories are the same
 */
InventoryPage#isInventory(Inventory): Boolean

/**
 * Get the object's inventory.
 *
 * @return The inventory.
 */
InventoryPage#getInventory(): Inventory

/**
 * Prevent this inventory to be closed manually
 *
 * @return this instance
 */
InventoryPage#preventClose(): InventoryPage

/**
 * Allow closing this inventory
 *
 * @return this instance
 */
InventoryPage#allowClose(): InventoryPage

/**
 * On close actions
 *
 * @param Action the actions to perform
 * @return this instance
 */
InventoryPage#onClose(Action...action): InventoryPage

/**
 * On open actions
 *
 * @param Action the actions to perform
 * @return this instance
 */
InventoryPage#onOpen(Action...actions): InventoryPage
```
