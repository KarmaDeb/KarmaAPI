package ml.karmaconfigs.api.bukkit.nms.hologram.part;

import ml.karmaconfigs.api.bukkit.nms.hologram.part.touch.TouchablePart;

/**
 * Text part
 */
public interface TextPart extends Line, TouchablePart {

    /**
     * Get the hologram part
     *
     * @return the hologram part
     */
    String get();

    /**
     * Set the hologram part value
     *
     * @param param the value
     */
    void set(final String param);
}
