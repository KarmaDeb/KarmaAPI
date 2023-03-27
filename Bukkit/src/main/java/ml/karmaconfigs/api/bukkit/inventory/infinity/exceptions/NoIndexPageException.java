package ml.karmaconfigs.api.bukkit.inventory.infinity.exceptions;

/**
 * This exception is thrown when an inventory book is tried
 * to open and doesn't have an index page
 */
public class NoIndexPageException extends Exception {

    /**
     * Initialize the exception
     * @param index the page index
     */
    public NoIndexPageException(final int index) {
        super("Cannot open inventory book which doesn't has index page (" + index + ") to player");
    }
}
