package ml.karmaconfigs.api.bukkit;

/*
 * This file is part of KarmaAPI, licensed under the MIT License.
 *
 *  Copyright (c) karma (KarmaDev) <karmaconfigs@gmail.com>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

import ml.karmaconfigs.api.common.console.Console;
import ml.karmaconfigs.api.common.karma.file.element.types.Element;
import ml.karmaconfigs.api.common.karma.file.element.types.ElementPrimitive;
import ml.karmaconfigs.api.common.logger.Logger;
import ml.karmaconfigs.api.common.karma.source.APISource;
import ml.karmaconfigs.api.common.karma.source.Identifiable;
import ml.karmaconfigs.api.common.karma.KarmaAPI;
import ml.karmaconfigs.api.common.karma.source.KarmaSource;
import ml.karmaconfigs.api.common.karma.file.KarmaMain;
import ml.karmaconfigs.api.common.logger.KarmaLogger;
import ml.karmaconfigs.api.common.placeholder.GlobalPlaceholderEngine;
import ml.karmaconfigs.api.common.placeholder.util.Placeholder;
import ml.karmaconfigs.api.common.placeholder.util.PlaceholderEngine;
import ml.karmaconfigs.api.common.security.token.TokenGenerator;
import ml.karmaconfigs.api.common.string.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

/**
 * Karma plugin
 */
public abstract class KarmaPlugin extends JavaPlugin implements KarmaSource, Identifiable {

    /**
     * The plugin console
     */
    private final Console console;

    private String plugin_identifier = TokenGenerator.generateToken();

    /**
     * The plugin logger
     */
    private final KarmaLogger logger;

    /**
     * Initialize the KarmaPlugin
     */
    public KarmaPlugin() {
        KarmaAPI.install();

        if (!APISource.hasProvider(name())) {
            APISource.addProvider(this);
        }

        console = new Console(this, (msg) -> Bukkit.getServer().getConsoleSender().sendMessage(StringUtils.toColor(StringUtils.fromAnyOsColor(msg))));
        logger = new Logger(this);
        loadIdentifier("DEFAULT");
    }

    /**
     * Initialize the KarmaPlugin
     *
     * @param defineDefault if this source should be defined
     *                      as the default source
     * @throws SecurityException if the default module is already
     *                           set
     */
    public KarmaPlugin(final boolean defineDefault) throws SecurityException {
        KarmaAPI.install();

        if (!APISource.hasProvider(name())) {
            APISource.addProvider(this);
            if (defineDefault) {
                APISource.defineDefault(this);
            }
        }

        console = new Console(this, (msg) -> Bukkit.getServer().getConsoleSender().sendMessage(StringUtils.toColor(StringUtils.fromAnyOsColor(msg))));
        logger = new Logger(this);
        loadIdentifier("DEFAULT");
    }

    /**
     * Enable the KarmaPlugin
     */
    public abstract void enable();

    /**
     * On plugin enable
     */
    @Override
    public final void onEnable() {
        /*async = new BukkitAsyncScheduler<>(this);
        sync = new BukkitSyncScheduler<>(this);*/

        enable();
    }

    /**
     * Karma source name
     *
     * @return the source name
     */
    @Override
    public String name() {
        return getDescription().getName();
    }

    /**
     * Karma source version
     *
     * @return the source version
     */
    @Override
    public String version() {
        return getDescription().getVersion();
    }

    /**
     * Karma source description
     *
     * @return the source description
     */
    @Override
    public String description() {
        return getDescription().getDescription();
    }

    /**
     * Karma source authors
     *
     * @return the source authors
     */
    @Override
    public String[] authors() {
        return getDescription().getAuthors().toArray(new String[0]);
    }

    /**
     * Get the source out
     *
     * @return the source out
     */
    @Override
    public Console console() {
        return console;
    }

    /**
     * Get the plugin file logger
     *
     * @return the plugin file logger
     */
    @Override
    public @NotNull KarmaLogger logger() {
        return logger;
    }

    /**
     * Get the current identifier
     *
     * @return the current identifier
     */
    @Override
    public String getIdentifier() {
        return plugin_identifier;
    }

    /**
     * Store the identifier
     *
     * @param name the identifier name
     * @return if the identifier could be stored
     */
    @Override
    public boolean storeIdentifier(final String name) {
        KarmaMain main = new KarmaMain(APISource.getOriginal(true), "identifiers.kf");
        if (!main.exists())
            main.create();

        main.setRaw(name, plugin_identifier);

        return main.save();
    }

    /**
     * Load an identifier
     *
     * @param name the identifier name
     */
    @Override
    public void loadIdentifier(final String name) {
        KarmaMain main = new KarmaMain(APISource.getOriginal(true), "identifiers.kf");
        if (!main.exists())
            main.create();

        if (main.isSet(name)) {
            Element<?> element = main.get(name);
            if (element.isPrimitive()) {
                ElementPrimitive primitive = element.getAsPrimitive();
                if (primitive.isString()) {
                    plugin_identifier = primitive.asString();
                }
            }
        }
    }

    /**
     * Create a player placeholder
     *
     * @param key       the placeholder key
     * @param onRequest on placeholder request
     * @return the placeholder
     */
    public static Placeholder<String> createTextPlaceholder(final String key, final BiConsumer<Player, String> onRequest) {
        return new Placeholder<String>() {
            @Override
            public String getKey() {
                return key;
            }

            @Override
            public String getValue(@Nullable Object container) {
                if (container instanceof Player) {
                    Player player = (Player) container;
                    String original = "";
                    onRequest.accept(player, original);

                    return original;
                } else {
                    return StringUtils.toColor("&cContainer not a player");
                }
            }

            @Override
            public Class<?> getType() {
                return Player.class;
            }
        };
    }

    /**
     * Create a player placeholder
     *
     * @param key       the placeholder key
     * @param onRequest on placeholder request
     * @return the placeholder
     */
    public static Placeholder<Integer> createIntegerPlaceholder(final String key, final BiConsumer<Player, Integer> onRequest) {
        return new Placeholder<Integer>() {
            @Override
            public String getKey() {
                return key;
            }

            @Override
            public Integer getValue(@Nullable Object container) {
                if (container instanceof Player) {
                    Player player = (Player) container;
                    int original = Integer.MAX_VALUE;
                    onRequest.accept(player, original);

                    return original;
                } else {
                    return Integer.MIN_VALUE;
                }
            }

            @Override
            public Class<?> getType() {
                return Player.class;
            }
        };
    }

    /**
     * Create a player placeholder
     *
     * @param key       the placeholder key
     * @param onRequest on placeholder request
     * @return the placeholder
     */
    public static Placeholder<Double> createDoublePlaceholder(final String key, final BiConsumer<Player, Double> onRequest) {
        return new Placeholder<Double>() {
            @Override
            public String getKey() {
                return key;
            }

            @Override
            public Double getValue(@Nullable Object container) {
                if (container instanceof Player) {
                    Player player = (Player) container;
                    double original = Double.MAX_VALUE;
                    onRequest.accept(player, original);

                    return original;
                } else {
                    return Double.MIN_VALUE;
                }
            }

            @Override
            public Class<?> getType() {
                return Player.class;
            }
        };
    }

    /**
     * Create a player placeholder
     *
     * @param key       the placeholder key
     * @param onRequest on placeholder request
     * @return the placeholder
     */
    public static Placeholder<Float> createFloatPlaceholder(final String key, final BiConsumer<Player, Float> onRequest) {
        return new Placeholder<Float>() {
            @Override
            public String getKey() {
                return key;
            }

            @Override
            public Float getValue(@Nullable Object container) {
                if (container instanceof Player) {
                    Player player = (Player) container;
                    float original = Float.MAX_VALUE;
                    onRequest.accept(player, original);

                    return original;
                } else {
                    return Float.MIN_VALUE;
                }
            }

            @Override
            public Class<?> getType() {
                return Player.class;
            }
        };
    }

    /**
     * Create a player placeholder
     *
     * @param <T>       the placeholder type
     * @param key       the placeholder key
     * @param onRequest on placeholder request
     * @return the placeholder
     */
    public static <T> Placeholder<T> createAnyPlaceholder(final String key, final BiConsumer<Player, T> onRequest) {
        return new Placeholder<T>() {
            @Override
            public String getKey() {
                return key;
            }

            @Override
            public T getValue(@Nullable Object container) {
                if (container instanceof Player) {
                    Player player = (Player) container;
                    T original = null;
                    onRequest.accept(player, original);

                    return original;
                } else {
                    return null;
                }
            }

            @Override
            public Class<?> getType() {
                return Player.class;
            }
        };
    }

    /**
     * Get the ABC plugin
     *
     * @return the ABC plugin
     */
    public static KarmaPlugin getABC() {
        return (KarmaPlugin) Bukkit.getServer().getPluginManager().getPlugin("KarmaAPI");
    }

    /**
     * Register globally a player placeholder
     *
     * @param placeholders the player placeholder
     */
    public static void registerPlayerPlaceholder(final Placeholder<?>... placeholders) {
        PlaceholderEngine engine = new GlobalPlaceholderEngine(KarmaAPI.source(false));
        engine.protect();

        engine.register(placeholders);
    }
}
