package ml.karmaconfigs.api.bukkit.reflection;

import ml.karmaconfigs.api.bukkit.KarmaPlugin;
import ml.karmaconfigs.api.bukkit.reflection.hologram.HologramFactory;
import ml.karmaconfigs.api.bukkit.reflection.hologram.HologramHolder;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class HoloManager {

    private final static Map<KarmaPlugin, Set<HologramHolder>> holders = new ConcurrentHashMap<>();
    private final static Map<KarmaPlugin, HologramFactory> factories = new ConcurrentHashMap<>();

    private final KarmaPlugin plugin;

    /**
     * Initialize the hologram manager
     *
     * @param source the plugin that will own this hologram manager
     */
    public HoloManager(final KarmaPlugin source) {
        plugin  = source;
    }

    /**
     * Update the plugin hologram factory
     *
     * @param factory the new hologram factory of the plugin
     */
    public void setFactory(final HologramFactory factory) {
        if (factory != null) {
            factories.put(plugin, factory);
        }
    }

    /**
     * Get the hologram factory
     *
     * @return the hologram factory
     */
    public HologramFactory getFactory() {
        HologramFactory factory = factories.getOrDefault(plugin, null);
        if (factory == null) {
            factory = factories.get(KarmaPlugin.getABC());
            factories.put(plugin, factory);
        }

        return factory;
    }

    /**
     * Register the hologram holder
     *
     * @param holder the holder
     */
    public void registerHolder(final HologramHolder holder) {
        if (holder != null) {
            Set<HologramHolder> h_holders = holders.getOrDefault(plugin, Collections.newSetFromMap(new ConcurrentHashMap<>()));
            h_holders.add(holder);

            holders.put(plugin, h_holders);
        }
    }

    public void removeHolder(final HologramHolder holder) {
        if (holder != null) {
            Set<HologramHolder> h_holders = holders.getOrDefault(plugin, Collections.newSetFromMap(new ConcurrentHashMap<>()));
            h_holders.remove(holder);

            holders.put(plugin, h_holders);
        }
    }

    /**
     * Get the plugin's hologram holder
     *
     * @return the plugin hologram holder
     */
    public Set<HologramHolder> getHolders() {
        Set<HologramHolder> h_holders = holders.getOrDefault(plugin, Collections.newSetFromMap(new ConcurrentHashMap<>()));
        return new HashSet<>(h_holders);
    }
}
