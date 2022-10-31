package ml.karmaconfigs.api.bukkit.reflection.hologram.component;

/**
 * Hologram text component
 */
public abstract class HTextComponent implements HologramComponent {

    /**
     * Get the hologram text component
     *
     * @return the hologram text component
     */
    public abstract String getText();

    /**
     * Update the component text
     *
     * @param text the new text
     */
    public abstract void updateText(final String text);
}
