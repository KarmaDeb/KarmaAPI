package ml.karmaconfigs.api.common.utils;

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

import ml.karmaconfigs.api.common.karma.KarmaSource;
import ml.karmaconfigs.api.common.karma.KarmaConfig;
import ml.karmaconfigs.api.common.utils.enums.Level;
import ml.karmaconfigs.api.common.utils.string.StringUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Karma console prefix data
 */
public final class PrefixConsoleData implements Serializable {

    private final KarmaConfig config = new KarmaConfig();

    /**
     * Map containing source => ok prefix
     */
    @Deprecated @ApiStatus.ScheduledForRemoval
    private static final HashMap<KarmaSource, String> okPrefix = new HashMap<>();
    /**
     * Map containing source => info prefix
     */
    @Deprecated @ApiStatus.ScheduledForRemoval
    private static final HashMap<KarmaSource, String> infoPrefix = new HashMap<>();
    /**
     * Map containing source => warning prefix
     */
    @Deprecated @ApiStatus.ScheduledForRemoval
    private static final HashMap<KarmaSource, String> warnPrefix = new HashMap<>();
    /**
     * Map containing source => grave prefix
     */
    @Deprecated @ApiStatus.ScheduledForRemoval
    private static final HashMap<KarmaSource, String> gravPrefix = new HashMap<>();

    /**
     * Map containing source => prefix data
     */
    private static final Map<KarmaSource, Map<Level, String>> prefix_data = new ConcurrentHashMap<>();

    /**
     * The prefix console data source
     */
    private final KarmaSource source;

    /**
     * Initialize the prefix console data
     *
     * @param p the source owner
     */
    public PrefixConsoleData(final @NotNull KarmaSource p) {
        this.source = p;
    }

    public void setPrefix(final Level level, final String prefix) {
        if (StringUtils.isNullOrEmpty(prefix)) {
            return;
        }

        Map<Level, String> prefixes = prefix_data.getOrDefault(source, new ConcurrentHashMap<>());
        prefixes.put(level, prefix);

        prefix_data.put(source, prefixes);
    }

    /**
     * Set the OK prefix
     *
     * @param prefix the prefix
     * @deprecated Use the method {@link PrefixConsoleData#setPrefix(Level, String)} instead
     */
    @Deprecated @ApiStatus.ScheduledForRemoval
    public void setOkPrefix(final @NotNull String prefix) {
        okPrefix.put(this.source, StringUtils.toAnyOsColor(prefix));
    }

    /**
     * Set the info prefix
     *
     * @param prefix the prefix
     * @deprecated Use the method {@link PrefixConsoleData#setPrefix(Level, String)} instead
     */
    @Deprecated @ApiStatus.ScheduledForRemoval
    public void setInfoPrefix(final @NotNull String prefix) {
        infoPrefix.put(this.source, StringUtils.toAnyOsColor(prefix));
    }

    /**
     * Set the warning prefix
     *
     * @param prefix the prefix
     * @deprecated Use the method {@link PrefixConsoleData#setPrefix(Level, String)} instead
     */
    @Deprecated @ApiStatus.ScheduledForRemoval
    public void setWarnPrefix(final @NotNull String prefix) {
        warnPrefix.put(this.source, StringUtils.toAnyOsColor(prefix));
    }

    /**
     * Set the grave prefix
     *
     * @param prefix the prefix
     * @deprecated Use the method {@link PrefixConsoleData#setPrefix(Level, String)} instead
     */
    @Deprecated @ApiStatus.ScheduledForRemoval
    public void setGravePrefix(final @NotNull String prefix) {
        gravPrefix.put(this.source, StringUtils.toAnyOsColor(prefix));
    }

    /**
     * Get the prefix
     *
     * @param level the prefix level
     * @return the prefix
     */
    public String getPrefix(final Level level) {
        Map<Level, String> prefixes = prefix_data.getOrDefault(source, new ConcurrentHashMap<>());
        String def = config.consolePrefix(source, level);

        return prefixes.getOrDefault(level, def);
    }

    /**
     * Get the OK prefix
     *
     * @return the prefix
     * @deprecated Use the method {@link PrefixConsoleData#getPrefix(Level)} instead
     */
    @Deprecated
    public String getOkPrefix() {
        return okPrefix.getOrDefault(this.source, StringUtils.toAnyOsColor(config.consolePrefix(source, Level.OK)));
    }

    /**
     * Get the info prefix
     *
     * @return the info prefix
     * @deprecated Use the method {@link PrefixConsoleData#getPrefix(Level)} instead
     */
    @Deprecated
    public String getInfoPrefix() {
        return infoPrefix.getOrDefault(this.source, StringUtils.toAnyOsColor(config.consolePrefix(source, Level.INFO)));
    }

    /**
     * Get the warning prefix
     *
     * @return the warning prefix
     * @deprecated Use the method {@link PrefixConsoleData#getPrefix(Level)} instead
     */
    @Deprecated
    public String getWarningPrefix() {
        return warnPrefix.getOrDefault(this.source, StringUtils.toAnyOsColor(config.consolePrefix(source, Level.WARNING)));
    }

    /**
     * Set the grave prefix
     *
     * @return the grave prefix
     * @deprecated Use the method {@link PrefixConsoleData#getPrefix(Level)} instead
     */
    @Deprecated
    public String getGravePrefix() {
        return gravPrefix.getOrDefault(this.source, StringUtils.toAnyOsColor(config.consolePrefix(source, Level.GRAVE)));
    }
}
