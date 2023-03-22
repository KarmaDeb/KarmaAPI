---
description: >-
  When you create a ItemFunction, you are able to specify actions to run when it
  gets clicked.
---

# ❓ ItemFunction actions

```java
/**
 * Run a custom action
 *
 * @param Runnable the action to perform
 * @return the action
 */
Action#run(Runnable): Action

/**
 * Replace the item
 *
 * @param ItemStack the new item
 * @return the action
 */
Action#replaceItem(ItemStack): Action

/**
 * Allow event interaction, please note this will allow player
 * to take items
 *
 * @return the action
 */
Action#allow(): Action

/**
 * Close the inventory
 *
 * @return the action
 */
Action#close(): Action

/**
 * Change the page
 *
 * @param Integer the new page
 * @return the action
 */
Action#changePage(Integer): Action

/**
 * Go to the next page
 *
 * @return the action
 */
Action#nextPage(): Action

/**
 * Go to the previous page
 *
 * @return the action
 */
Action#previousPage(): Action

/**
 * Run a custom action
 *
 * @param Runnable the action to perform
 * @param Long the ticks to wait before executing this action
 * @return the action
 */
Action#run(Runnable, Long): Action

/**
 * Close the inventory
 *
 * @param Long the ticks to wait before executing this action
 * @return the action
 */
Action#close(Long): Action

/**
 * Change the page
 *
 * @param Integer the new page
 * @param Long the ticks to wait before executing this action
 * @return the action
 */
Action#changePage(Integer, Long): Action

/**
 * Go to the next page
 *
 * @param Long the ticks to wait before executing this action
 * @return the action
 */
Action#nextPage(Long): Action

/**
 * Go to the previous page
 *
 * @param Long the ticks to wait before executing this action
 * @return the action
 */
Action#previousPage(Long): Action

/**
 * Run an action on the click event
 *
 * @param BiConsumer the click consumer
 * @return the action
 */
Action#handle(BiConsumer <Integer, InventoryView>): Action

/**
 * Run an action on the click event
 *
 * @param BiConsumer the click consumer
 * @param Supplier the cancel function
 * @return the action
 */
Action#handle(final BiConsumer <Integer, InventoryView> , Supplier <Boolean>): Action
```
