package ml.karmaconfigs.api.common.console;

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

import ml.karmaconfigs.api.common.karma.KarmaConfig;
import ml.karmaconfigs.api.common.karma.source.KarmaSource;
import ml.karmaconfigs.api.common.timer.SchedulerUnit;
import ml.karmaconfigs.api.common.timer.SourceScheduler;
import ml.karmaconfigs.api.common.timer.scheduler.SimpleScheduler;
import ml.karmaconfigs.api.common.console.prefix.PrefixConsoleData;
import ml.karmaconfigs.api.common.console.packet.ConsolePacket;
import ml.karmaconfigs.api.common.utils.enums.Level;
import ml.karmaconfigs.api.common.placeholder.GlobalPlaceholderEngine;
import ml.karmaconfigs.api.common.placeholder.util.PlaceholderEngine;
import ml.karmaconfigs.api.common.string.ListTransformation;
import ml.karmaconfigs.api.common.string.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Karma console
 */
public final class Console {

    /**
     * The custom message actions
     */
    private final static Map<KarmaSource, Consumer<String>> messageActions = new ConcurrentHashMap<>();
    private final static Map<KarmaSource, Set<PlaceholderEngine>> engines = new ConcurrentHashMap<>();

    private final static Map<KarmaSource, Map<Integer, String>> sequence = new ConcurrentHashMap<>();

    private final static Map<KarmaSource, SimpleScheduler> sequential_consoles = new ConcurrentHashMap<>();

    /**
     * The console source
     */
    private final KarmaSource source;

    /**
     * Initialize a new console
     *
     * @param src the console source
     */
    public Console(final KarmaSource src) {
        source = src;

        PlaceholderEngine global = new GlobalPlaceholderEngine(src);
        Set<PlaceholderEngine> stored = engines.getOrDefault(source, Collections.newSetFromMap(new ConcurrentHashMap<>()));
        stored.add(global);

        engines.put(source, stored);
    }

    /**
     * Initialize a new console
     *
     * @param src       the console source
     * @param onMessage the console message action
     */
    public Console(final KarmaSource src, final Consumer<String> onMessage) {
        this.source = src;

        if (onMessage != null) {
            boolean isNew = messageActions.getOrDefault(src, null) == null;

            messageActions.put(src, onMessage);
            if (isNew) {
                KarmaConfig config = new KarmaConfig();

                if (config.debug(Level.INFO)) {
                    send(StringUtils.formatString(src, "Using custom console message sender", Level.INFO));
                }
            }
        } else {
            messageActions.remove(src);
        }

        PlaceholderEngine global = new GlobalPlaceholderEngine(src);
        Set<PlaceholderEngine> stored = engines.getOrDefault(source, Collections.newSetFromMap(new ConcurrentHashMap<>()));
        stored.add(global);

        engines.put(source, stored);
    }

    /**
     * Update the console sequential status for
     * this source
     * <p>
     * SEQUENTIAL CONSOLE ALLOWS SOURCES TO FORCE THE
     * MESSAGE ORDER, USEFUL WHEN YOU RUN A LOT OF ASYNC
     * TASKS BUT WANT TO KEEP AN ORDER
     *
     * @param seq_status the console sequential status
     * @return this instance
     */
    public Console sequential(final boolean seq_status) {
        SimpleScheduler scheduler = sequential_consoles.getOrDefault(source, null);
        if (scheduler == null) {
            scheduler = new SourceScheduler(source, 1, SchedulerUnit.SECOND, true).multiThreading(false).restartAction(() -> {
                Map<Integer, String> execution_queue = sequence.getOrDefault(source, new ConcurrentHashMap<>());
                List<Integer> order = new ArrayList<>(execution_queue.keySet());
                if (!order.isEmpty()) {
                    Collections.sort(order);
                    int next_message = order.get(0);

                    String next = execution_queue.remove(next_message);
                    ConsolePacket packet = new ConsolePacket(Base64.getDecoder().decode(next));
                    readPacket(packet);

                    sequence.put(source, execution_queue);
                }
            });

            sequential_consoles.put(source, scheduler);
        }

        if (seq_status) {
            if (!scheduler.isRunning()) {
                scheduler.start();
            }
        } else {
            if (scheduler.isRunning()) {
                scheduler.pause();
            }
        }

        return this;
    }

    /**
     * Get the console prefix data
     *
     * @return this source prefix console data
     */
    public PrefixConsoleData getData() {
        return new PrefixConsoleData(this.source);
    }

    /**
     * Add placeholder engine to parse automatically
     * placeholders on the messages
     *
     * @param engine the engine to add
     */
    public void addEngine(final PlaceholderEngine engine) {
        Set<PlaceholderEngine> stored = engines.getOrDefault(source, Collections.newSetFromMap(new ConcurrentHashMap<>()));
        stored.add(engine);

        engines.put(source, stored);
    }

    /**
     * Remove a placeholder engine
     *
     * @param engine the placeholder engine to remove
     */
    public void removeEngine(final PlaceholderEngine engine) {
        Set<PlaceholderEngine> added = engines.getOrDefault(source, Collections.newSetFromMap(new ConcurrentHashMap<>()));
        added.remove(engine);

        engines.put(source, added);
    }

    /**
     * Get the placeholder engines of the console
     *
     * @return the placeholder engines of the console
     */
    public Set<PlaceholderEngine> getEngines() {
        return engines.getOrDefault(source, Collections.newSetFromMap(new ConcurrentHashMap<>()));
    }

    /**
     * Send a message to the console
     *
     * @param message the message to send
     */
    public void send(final CharSequence message) {
        SimpleScheduler scheduler = sequential_consoles.getOrDefault(source, null);

        if (scheduler != null && scheduler.isRunning()) {
            ConsolePacket packet = new ConsolePacket(message);
            Map<Integer, String> sequences = sequence.get(source);
            sequences.put(sequences.size() + 1, packet.serialize());
            sequence.put(source, sequences);
        } else {
            Consumer<String> messageAction = messageActions.getOrDefault(source, null);

            String msg = String.valueOf(message);
            Set<PlaceholderEngine> added = engines.getOrDefault(source, Collections.newSetFromMap(new ConcurrentHashMap<>()));
            for (PlaceholderEngine engine : added)
                msg = engine.parse(msg);

            if (messageAction == null) {
                System.out.println("\033[0m" + StringUtils.toAnyOsColor(Colors.RESET.getCode() + msg + Colors.RESET.getCode()));
            } else {
                messageAction.accept(msg);
            }
        }
    }

    /**
     * Send a message to the console
     *
     * @param message  the message to send
     * @param replaces the message replaces
     */
    public void send(final CharSequence message, final Object... replaces) {
        SimpleScheduler scheduler = sequential_consoles.getOrDefault(source, null);

        if (scheduler != null && scheduler.isRunning()) {
            ConsolePacket packet = new ConsolePacket(message, replaces);
            Map<Integer, String> sequences = sequence.get(source);
            sequences.put(sequences.size() + 1, packet.serialize());
            sequence.put(source, sequences);
        } else {
            Consumer<String> messageAction = messageActions.getOrDefault(source, null);

            String tmpMessage = String.valueOf(message);
            for (int i = 0; i < replaces.length; i++) {
                String placeholder = "{" + i + "}";
                String value = String.valueOf(replaces[i]);
                tmpMessage = tmpMessage.replace(placeholder, value);
            }

            Set<PlaceholderEngine> added = engines.getOrDefault(source, Collections.newSetFromMap(new ConcurrentHashMap<>()));
            for (PlaceholderEngine engine : added)
                tmpMessage = engine.parse(tmpMessage);

            if (messageAction == null) {
                System.out.println("\033[0m" + StringUtils.toAnyOsColor(Colors.RESET.getCode() + tmpMessage + Colors.RESET.getCode()));
            } else {
                messageAction.accept(tmpMessage);
            }
        }
    }

    /**
     * Send a message to the console
     *
     * @param message the message to send
     * @param level   the message level
     */
    public void send(final @NotNull CharSequence message, final @NotNull Level level) {
        SimpleScheduler scheduler = sequential_consoles.getOrDefault(source, null);

        if (scheduler != null && scheduler.isRunning()) {
            ConsolePacket packet = new ConsolePacket(message, level, new Object[]{});
            Map<Integer, String> sequences = sequence.get(source);
            sequences.put(sequences.size() + 1, packet.serialize());
            sequence.put(source, sequences);
        } else {
            Consumer<String> messageAction = messageActions.getOrDefault(source, null);

            String tmpMessage = String.valueOf(message);

            PrefixConsoleData data = getData();
            String prefix = data.getPrefix(level);

            tmpMessage = StringUtils.stripColor(tmpMessage);
            Set<PlaceholderEngine> added = engines.getOrDefault(source, Collections.newSetFromMap(new ConcurrentHashMap<>()));
            for (PlaceholderEngine engine : added)
                tmpMessage = engine.parse(tmpMessage);

            if (messageAction == null) {
                if (tmpMessage.contains("\n")) {
                    for (String msg : tmpMessage.split("\n"))
                        send(msg);
                } else {
                    send(prefix + tmpMessage);
                }
            } else {
                if (tmpMessage.contains("\n"))
                    tmpMessage = StringUtils.listToString(Arrays.asList(tmpMessage.split("\n")), ListTransformation.NEW_LINES);

                messageAction.accept(prefix + tmpMessage);
            }
        }
    }

    /**
     * Send a message to the console
     *
     * @param message  the message to send
     * @param level    the message level
     * @param replaces the message replaces
     */
    public void send(final @NotNull CharSequence message, final @NotNull Level level, final @NotNull Object... replaces) {
        SimpleScheduler scheduler = sequential_consoles.getOrDefault(source, null);

        if (scheduler != null && scheduler.isRunning()) {
            ConsolePacket packet = new ConsolePacket(message, level, replaces);
            Map<Integer, String> sequences = sequence.get(source);
            sequences.put(sequences.size() + 1, packet.serialize());
            sequence.put(source, sequences);
        } else {
            Consumer<String> messageAction = messageActions.getOrDefault(source, null);

            String tmpMessage = String.valueOf(message);

            PrefixConsoleData data = getData();
            String prefix = data.getPrefix(level);

            for (int i = 0; i < replaces.length; i++) {
                String placeholder = "{" + i + "}";
                String value = String.valueOf(replaces[i]);
                tmpMessage = tmpMessage.replace(placeholder, value);
            }
            tmpMessage = StringUtils.stripColor(tmpMessage);
            Set<PlaceholderEngine> added = engines.getOrDefault(source, Collections.newSetFromMap(new ConcurrentHashMap<>()));
            for (PlaceholderEngine engine : added)
                tmpMessage = engine.parse(tmpMessage);

            if (messageAction == null) {
                if (tmpMessage.contains("\n")) {
                    boolean first_message = true;

                    for (String msg : tmpMessage.split("\n")) {
                        send((first_message ? prefix : "") + msg);

                        first_message = false;
                    }
                } else {
                    send(prefix + tmpMessage);
                }
            } else {
                if (tmpMessage.contains("\n"))
                    tmpMessage = StringUtils.listToString(Arrays.asList(tmpMessage.split("\n")), ListTransformation.NEW_LINES);

                messageAction.accept(prefix + tmpMessage);
            }
        }
    }

    /**
     * Send a message to the console
     *
     * @param message the message to send
     * @param level   the message level
     */
    public void debug(final @NotNull CharSequence message, final @NotNull Level level) {
        KarmaConfig config = new KarmaConfig();

        if (config.debug(level)) {
            Consumer<String> messageAction = messageActions.getOrDefault(source, null);

            String tmpMessage = String.valueOf(message);

            PrefixConsoleData data = getData();
            String prefix = data.getPrefix(level);

            tmpMessage = StringUtils.stripColor(tmpMessage);
            Set<PlaceholderEngine> added = engines.getOrDefault(source, Collections.newSetFromMap(new ConcurrentHashMap<>()));
            for (PlaceholderEngine engine : added)
                tmpMessage = engine.parse(tmpMessage);

            if (messageAction == null) {
                if (tmpMessage.contains("\n")) {
                    for (String msg : tmpMessage.split("\n"))
                        send(msg);
                } else {
                    send(prefix + tmpMessage);
                }
            } else {
                if (tmpMessage.contains("\n"))
                    tmpMessage = StringUtils.listToString(Arrays.asList(tmpMessage.split("\n")), ListTransformation.NEW_LINES);

                messageAction.accept(prefix + tmpMessage);
            }
        }
    }

    /**
     * Send a message to the console
     *
     * @param message the message to send
     * @param level   the message level
     */
    public void debugFile(final @NotNull CharSequence message, final @NotNull Level level) {
        KarmaConfig config = new KarmaConfig();

        if (config.fileDebug(level)) {
            Consumer<String> messageAction = messageActions.getOrDefault(source, null);

            String tmpMessage = String.valueOf(message);

            PrefixConsoleData data = getData();
            String prefix = data.getPrefix(level);

            tmpMessage = StringUtils.stripColor(tmpMessage);
            Set<PlaceholderEngine> added = engines.getOrDefault(source, Collections.newSetFromMap(new ConcurrentHashMap<>()));
            for (PlaceholderEngine engine : added)
                tmpMessage = engine.parse(tmpMessage);

            if (messageAction == null) {
                if (tmpMessage.contains("\n")) {
                    for (String msg : tmpMessage.split("\n"))
                        send(msg);
                } else {
                    send(prefix + tmpMessage);
                }
            } else {
                if (tmpMessage.contains("\n"))
                    tmpMessage = StringUtils.listToString(Arrays.asList(tmpMessage.split("\n")), ListTransformation.NEW_LINES);

                messageAction.accept(prefix + tmpMessage);
            }
        }
    }

    /**
     * Send a message to the console
     *
     * @param message the message to send
     * @param level   the message level
     */
    public void debugUtil(final @NotNull CharSequence message, final @NotNull Level level) {
        KarmaConfig config = new KarmaConfig();

        if (config.utilDebug(level)) {
            Consumer<String> messageAction = messageActions.getOrDefault(source, null);

            String tmpMessage = String.valueOf(message);

            PrefixConsoleData data = getData();
            String prefix = data.getPrefix(level);

            tmpMessage = StringUtils.stripColor(tmpMessage);
            Set<PlaceholderEngine> added = engines.getOrDefault(source, Collections.newSetFromMap(new ConcurrentHashMap<>()));
            for (PlaceholderEngine engine : added)
                tmpMessage = engine.parse(tmpMessage);

            if (messageAction == null) {
                if (tmpMessage.contains("\n")) {
                    for (String msg : tmpMessage.split("\n"))
                        send(msg);
                } else {
                    send(prefix + tmpMessage);
                }
            } else {
                if (tmpMessage.contains("\n"))
                    tmpMessage = StringUtils.listToString(Arrays.asList(tmpMessage.split("\n")), ListTransformation.NEW_LINES);

                messageAction.accept(prefix + tmpMessage);
            }
        }
    }

    /**
     * Send a message to the console
     *
     * @param message  the message to send
     * @param level    the message level
     * @param replaces the message replaces
     */
    public void debug(final @NotNull CharSequence message, final @NotNull Level level, final @NotNull Object... replaces) {
        KarmaConfig config = new KarmaConfig();

        if (config.debug(level)) {
            Consumer<String> messageAction = messageActions.getOrDefault(source, null);

            String tmpMessage = String.valueOf(message);

            PrefixConsoleData data = getData();
            String prefix = data.getPrefix(level);

            for (int i = 0; i < replaces.length; i++) {
                String placeholder = "{" + i + "}";
                String value = String.valueOf(replaces[i]);
                tmpMessage = tmpMessage.replace(placeholder, value);
            }
            tmpMessage = StringUtils.stripColor(tmpMessage);
            Set<PlaceholderEngine> added = engines.getOrDefault(source, Collections.newSetFromMap(new ConcurrentHashMap<>()));
            for (PlaceholderEngine engine : added)
                tmpMessage = engine.parse(tmpMessage);

            if (messageAction == null) {
                if (tmpMessage.contains("\n")) {
                    for (String msg : tmpMessage.split("\n"))
                        send(msg);
                } else {
                    send(prefix + tmpMessage);
                }
            } else {
                if (tmpMessage.contains("\n"))
                    tmpMessage = StringUtils.listToString(Arrays.asList(tmpMessage.split("\n")), ListTransformation.NEW_LINES);

                messageAction.accept(prefix + tmpMessage);
            }
        }
    }

    /**
     * Send a message to the console
     *
     * @param message  the message to send
     * @param level    the message level
     * @param replaces the message replaces
     */
    public void debugFile(final @NotNull CharSequence message, final @NotNull Level level, final @NotNull Object... replaces) {
        KarmaConfig config = new KarmaConfig();

        if (config.fileDebug(level)) {
            Consumer<String> messageAction = messageActions.getOrDefault(source, null);

            String tmpMessage = String.valueOf(message);

            PrefixConsoleData data = getData();
            String prefix = data.getPrefix(level);

            for (int i = 0; i < replaces.length; i++) {
                String placeholder = "{" + i + "}";
                String value = String.valueOf(replaces[i]);
                tmpMessage = tmpMessage.replace(placeholder, value);
            }
            tmpMessage = StringUtils.stripColor(tmpMessage);
            Set<PlaceholderEngine> added = engines.getOrDefault(source, Collections.newSetFromMap(new ConcurrentHashMap<>()));
            for (PlaceholderEngine engine : added)
                tmpMessage = engine.parse(tmpMessage);

            if (messageAction == null) {
                if (tmpMessage.contains("\n")) {
                    for (String msg : tmpMessage.split("\n"))
                        send(msg);
                } else {
                    send(prefix + tmpMessage);
                }
            } else {
                if (tmpMessage.contains("\n"))
                    tmpMessage = StringUtils.listToString(Arrays.asList(tmpMessage.split("\n")), ListTransformation.NEW_LINES);

                messageAction.accept(prefix + tmpMessage);
            }
        }
    }

    /**
     * Send a message to the console
     *
     * @param message  the message to send
     * @param level    the message level
     * @param replaces the message replaces
     */
    public void debugUtil(final @NotNull CharSequence message, final @NotNull Level level, final @NotNull Object... replaces) {
        KarmaConfig config = new KarmaConfig();

        if (config.utilDebug(level)) {
            Consumer<String> messageAction = messageActions.getOrDefault(source, null);

            String tmpMessage = String.valueOf(message);

            PrefixConsoleData data = getData();
            String prefix = data.getPrefix(level);

            for (int i = 0; i < replaces.length; i++) {
                String placeholder = "{" + i + "}";
                String value = String.valueOf(replaces[i]);
                tmpMessage = tmpMessage.replace(placeholder, value);
            }
            tmpMessage = StringUtils.stripColor(tmpMessage);
            Set<PlaceholderEngine> added = engines.getOrDefault(source, Collections.newSetFromMap(new ConcurrentHashMap<>()));
            for (PlaceholderEngine engine : added)
                tmpMessage = engine.parse(tmpMessage);

            if (messageAction == null) {
                if (tmpMessage.contains("\n")) {
                    for (String msg : tmpMessage.split("\n"))
                        send(msg);
                } else {
                    send(prefix + tmpMessage);
                }
            } else {
                if (tmpMessage.contains("\n"))
                    tmpMessage = StringUtils.listToString(Arrays.asList(tmpMessage.split("\n")), ListTransformation.NEW_LINES);

                messageAction.accept(prefix + tmpMessage);
            }
        }
    }

    /**
     * Read the console packet
     *
     * @param packet the packet to read
     */
    private void readPacket(final ConsolePacket packet) {
        String message = packet.getMessage();
        Object[] replaces = packet.getReplaces();
        Level level = packet.getLevel();

        Consumer<String> messageAction = messageActions.getOrDefault(source, null);

        if (level == null) {
            for (int i = 0; i < replaces.length; i++) {
                String placeholder = "{" + i + "}";
                String value = String.valueOf(replaces[i]);
                message = message.replace(placeholder, value);
            }

            Set<PlaceholderEngine> added = engines.getOrDefault(source, Collections.newSetFromMap(new ConcurrentHashMap<>()));
            for (PlaceholderEngine engine : added)
                message = engine.parse(message);

            if (messageAction == null) {
                System.out.println("\033[0m" + StringUtils.toAnyOsColor(Colors.RESET.getCode() + message + Colors.RESET.getCode()));
            } else {
                messageAction.accept(message);
            }
        } else {
            PrefixConsoleData data = getData();
            String prefix = data.getPrefix(level);

            for (int i = 0; i < replaces.length; i++) {
                String placeholder = "{" + i + "}";
                String value = String.valueOf(replaces[i]);
                message = message.replace(placeholder, value);
            }
            message = StringUtils.stripColor(message);
            Set<PlaceholderEngine> added = engines.getOrDefault(source, Collections.newSetFromMap(new ConcurrentHashMap<>()));
            for (PlaceholderEngine engine : added)
                message = engine.parse(message);

            if (messageAction == null) {
                if (message.contains("\n")) {
                    boolean first_message = true;

                    for (String msg : message.split("\n")) {
                        System.out.println("\033[0m" + StringUtils.toAnyOsColor(Colors.RESET.getCode() + (first_message ? prefix : "") + msg + Colors.RESET.getCode()));

                        first_message = false;
                    }
                } else {
                    System.out.println("\033[0m" + StringUtils.toAnyOsColor(Colors.RESET.getCode() + prefix + message + Colors.RESET.getCode()));
                }
            } else {
                if (message.contains("\n"))
                    message = StringUtils.listToString(Arrays.asList(message.split("\n")), ListTransformation.NEW_LINES);

                messageAction.accept(prefix + message);
            }
        }
    }
}
