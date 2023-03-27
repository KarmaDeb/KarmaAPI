package ml.karmaconfigs.api.bukkit.nms.hologram.part.touch;

public interface TouchablePart {

    /**
     * Add a touch handler to this text part
     *
     * @param handlers the touch handlers
     */
    void addTouchHandler(final TouchHandler... handlers);

    /**
     * Remove a touch handler from this text part
     *
     * @param handlers the touch handlers to remove
     */
    void removeTouchHandler(final TouchHandler... handlers);

    /**
     * Get the text part touch handlers
     *
     * @return the hologram touch handlers
     */
    TouchHandler[] getTouchHandlers();
}
