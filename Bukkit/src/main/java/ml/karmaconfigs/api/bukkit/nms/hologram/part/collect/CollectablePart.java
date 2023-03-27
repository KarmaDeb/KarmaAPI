package ml.karmaconfigs.api.bukkit.nms.hologram.part.collect;

import ml.karmaconfigs.api.bukkit.nms.hologram.part.touch.TouchHandler;

public interface CollectablePart {

    /**
     * Add a collect handler to this item part
     *
     * @param handlers the collect handlers
     */
    void addCollectHandler(final CollectHandler... handlers);

    /**
     * Remove a collect handler from this item part
     *
     * @param handlers the collect handlers to remove
     */
    void removeCollectHandler(final CollectHandler... handlers);

    /**
     * Get the item part collect handlers
     *
     * @return the hologram collect handlers
     */
    CollectHandler[] getCollectHandlers();
}
