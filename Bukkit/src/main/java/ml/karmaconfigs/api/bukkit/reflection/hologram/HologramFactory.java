package ml.karmaconfigs.api.bukkit.reflection.hologram;

import ml.karmaconfigs.api.bukkit.reflection.hologram.component.HologramComponent;
import org.bukkit.Location;

/**
 * Hologram factory
 */
public interface HologramFactory {

    /**
     * Create a hologram
     *
     * @param component the hologram component
     * @return the new hologram
     */
    HologramHolder createHologram(final Location location, final HologramComponent... component);
}
