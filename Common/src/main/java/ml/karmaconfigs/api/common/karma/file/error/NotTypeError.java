package ml.karmaconfigs.api.common.karma.file.error;

import ml.karmaconfigs.api.common.karma.file.element.types.Element;

/**
 * An error when a KarmaElement is tried to be cast
 * to another element which is not the same as the
 * original
 *
 * @author KarmaDev
 * @since 1.3.2-SNAPSHOT
 */
public class NotTypeError extends RuntimeException {

    /**
     * Initialize the error
     *
     * @param element the element that is being tried
     *                to be cast
     * @param type    the element cast target
     */
    public NotTypeError(final Element<?> element, final Class<? extends Element<?>> type) {
        super("The element " + element + " cannot be converted to " + type);
    }
}
