package ml.karmaconfigs.api.bukkit.reflection.legacy;

import org.bukkit.entity.Player;

/**
 * Legacy provider
 */
public abstract class LegacyProvider {

    /**
     * Display a title to a player
     *
     * @param player the player to show title to
     * @param title the title
     * @param subtitle the subtitle
     */
    public abstract void displayTitleFor(Player player, final String title, final String subtitle);

    /**
     * Display an actionbar for the player
     *
     * @param player the player to show actionbar to
     * @param text the actionbar text
     */
    public abstract void displayActionbarFor(Player player, final String text);
}
