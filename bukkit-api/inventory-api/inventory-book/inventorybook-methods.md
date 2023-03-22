---
description: The inventory book has some methods that may be usefull in some situations
---

# InventoryBook methods

```java
/**
 * Get the book by its ID
 *
 * @param UUID the book id
 * @return the book
 */
InventoryBook #getBook(UUID): InventoryBook

/**
 * Get the book unique ID
 *
 * @return the book unique ID
 */
InventoryBook #getUniqueId(): UUID

/**
 * Allow a player to close the inventory
 *
 * @param Player the player to allow
 */
InventoryBook #allowClose(Player): void

/**
 * Deny a player from closing the inventory
 *
 * @param Player the player to deny
 */
InventoryBook #denyClose(Player): void

/**
 * Get if a player can close the inventory
 *
 * @param Player the player to check
 * @return if the player can close the inventory
 */
InventoryBook #canClose(Player): Boolean

/**
 * Sorts the inventory pages, by shifting if there's a page jump.
 * For example, this method is useful after running the 
 * InventoryBook#removePage(Integer) method
 *
 * @return this instance
 */
InventoryBook #sort(): InventoryBook

/**
 * Add a page to the book
 *
 * @return this instance
 */
InventoryBook #addPage(): InventoryPage

/**
 * Add a page to the book
 *
 * @param String the page title
 * @return this instance
 */
InventoryBook #addPage(String): InventoryPage

/**
 * Set a page of the book
 *
 * @param Integer the page number
 * @return this instance
 */
InventoryBook #setPage(Integer): InventoryPage

/**
 * Set a page of the book
 *
 * @param Integer the page number
 * @param String the page title
 * @return this instance
 */
InventoryBook #setPage(Integer, String): InventoryPage

/**
 * Remove a page from the book
 *
 * @param Integer the page to remove
 * @return the removed page
 * @throws NoIndexPageException if the page does not exist
 */
InventoryBook #removePage(Integer): InventoryPage throws NoIndexPageException

/**
 * Get the page the player has open
 *
 * @param Player the player
 * @return the player page
 */
InventoryBook #getPage(Player): InventoryPage

/**
 * Get the previous page number for the player
 *
 * @param Player the player
 * @return the previous page index for the player
 */
InventoryBook #getPreviousPageIndex(Player): Integer

/**
 * Get the page index of the player
 *
 * @param Player the player
 * @return the page index
 */
InventoryBook #getPageIndex(Player): Integer

/**
 * Get the next page number for the player
 *
 * @param Player the player
 * @return the next page index for the player
 */
InventoryBook #getNextPageIndex(Player): Integer

/**
 * Get the page from its index
 *
 * @param Integer the page index
 * @return the page
 */
InventoryBook #getPage(Integer): InventoryPage

/**
 * Open the book to the player
 *
 * @param Player the player
 * @throws EmptyBookException if the book is empty
 * @throws NoIndexPageException if the book has no page 0
 */
InventoryBook #open(Player): void throws EmptyBookException, NoIndexPageException

/**
 * Open the book to the player
 *
 * @param Player the player
 * @param Integer the page index to open
 * @throws EmptyBookException if the book is empty
 * @throws NoIndexPageException if the book has no page X
 */
InventoryBook #open(Player, Integer): void throws EmptyBookException, NoIndexPageException

/**
 * Get the open page
 *
 * @return the open page
 */
public InventoryBook getPages(): Integer
```
