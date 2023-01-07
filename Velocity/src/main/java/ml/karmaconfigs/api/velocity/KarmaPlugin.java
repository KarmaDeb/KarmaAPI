package ml.karmaconfigs.api.velocity;

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

import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import ml.karmaconfigs.api.common.console.Colors;
import ml.karmaconfigs.api.common.logger.Logger;
import ml.karmaconfigs.api.common.karma.source.APISource;
import ml.karmaconfigs.api.common.karma.source.Identifiable;
import ml.karmaconfigs.api.common.karma.KarmaAPI;
import ml.karmaconfigs.api.common.karma.source.KarmaSource;
import ml.karmaconfigs.api.common.karma.file.KarmaMain;
import ml.karmaconfigs.api.common.karma.file.element.KarmaElement;
import ml.karmaconfigs.api.common.karma.file.element.KarmaObject;
import ml.karmaconfigs.api.common.logger.KarmaLogger;
import ml.karmaconfigs.api.common.placeholder.GlobalPlaceholderEngine;
import ml.karmaconfigs.api.common.placeholder.util.Placeholder;
import ml.karmaconfigs.api.common.placeholder.util.PlaceholderEngine;
import ml.karmaconfigs.api.common.security.token.TokenGenerator;
import ml.karmaconfigs.api.common.string.StringUtils;
import ml.karmaconfigs.api.velocity.loader.VelocityBridge;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

/**
 * Karma plugin for BungeeCord, to make easier for plugin developers to implement
 * the KarmaAPI in their BungeeCord plugins
 */
public abstract class KarmaPlugin implements KarmaSource, Identifiable {

    /**
     * Velocity plugin
     */
    private final PluginContainer container;

    /**
     * Plugin console
     */
    private final Colors console;

    /**
     * The plugin logger
     */
    private final KarmaLogger logger;

    private String plugin_identifier = TokenGenerator.generateToken();

    /**
     * Initialize the KarmaPlugin
     *
     * @param c the container
     */
    public KarmaPlugin(final PluginContainer c) {
        container = c;

        if (!APISource.hasProvider(name())) {
            APISource.addProvider(this);
        }

        console = new Colors(this, (msg) -> VelocityBridge.getServer().getConsoleCommandSource().sendMessage(Component.text().content(StringUtils.toColor(StringUtils.fromAnyOsColor(msg))).build()));
        logger = new Logger(this);
        loadIdentifier("DEFAULT");
    }

    /**
     * Initialize the KarmaPlugin
     *
     * @param c             the container
     * @param defineDefault if this source should be defined
     *                      as the default source
     * @throws SecurityException if the default module is already
     *                           set
     */
    public KarmaPlugin(final PluginContainer c, final boolean defineDefault) throws SecurityException {
        container = c;

        if (!APISource.hasProvider(name())) {
            APISource.addProvider(this);
            if (defineDefault) {
                APISource.defineDefault(this);
            }
        }

        console = new Colors(this, (msg) -> VelocityBridge.getServer().getConsoleCommandSource().sendMessage(Component.text().content(StringUtils.toColor(StringUtils.fromAnyOsColor(msg))).build()));
        logger = new Logger(this);
        loadIdentifier("DEFAULT");
    }

    /**
     * Enable the KarmaPlugin
     */
    public abstract void enable();

    /**
     * Disable the KarmaPlugin
     */
    public abstract void disable();

    /**
     * Get the velocity server
     *
     * @return the velocity server
     */
    public final ProxyServer getServer() {
        return VelocityBridge.getServer();
    }

    /**
     * Get the velocity plugin
     *
     * @return the velocity plugin
     */
    public final PluginContainer getContainer() {
        return container;
    }

    /**
     * Karma source name
     *
     * @return the source name
     */
    @Override
    public String name() {
        AtomicReference<String> name = new AtomicReference<>("");
        getContainer().getDescription().getName().ifPresent(name::set);

        return name.get();
    }

    /**
     * Karma source version
     *
     * @return the source version
     */
    @Override
    public String version() {
        AtomicReference<String> version = new AtomicReference<>("");
        getContainer().getDescription().getVersion().ifPresent(version::set);

        return version.get();
    }

    /**
     * Karma source description
     *
     * @return the source description
     */
    @Override
    public String description() {
        AtomicReference<String> description = new AtomicReference<>("");
        getContainer().getDescription().getDescription().ifPresent(description::set);

        return description.get();
    }

    /**
     * Karma source authors
     *
     * @return the source authors
     */
    @Override
    public String[] authors() {
        return getContainer().getDescription().getAuthors().toArray(new String[0]);
    }

    /**
     * Get the source out
     *
     * @return the source out
     */
    @Override
    public Colors console() {
        return console;
    }

    /**
     * Get the plugin file logger
     *
     * @return the plugin file logger
     */
    @Override
    public KarmaLogger logger() {
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

        main.set(name, new KarmaObject(plugin_identifier));

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
            KarmaElement element = main.get(name);
            if (element.isString()) {
                plugin_identifier = element.getObjet().getString();
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
                    String original = "placeholder " + key + " value";
                    onRequest.accept(player, original);

                    return original;
                } else {
                    return StringUtils.toColor("&ccontainer not a player");
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
