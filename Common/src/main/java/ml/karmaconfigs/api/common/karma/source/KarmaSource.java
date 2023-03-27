package ml.karmaconfigs.api.common.karma.source;

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
import ml.karmaconfigs.api.common.logger.Logger;
import ml.karmaconfigs.api.common.karma.file.KarmaMain;
import ml.karmaconfigs.api.common.string.random.RandomString;
import ml.karmaconfigs.api.common.timer.scheduler.Scheduler;
import ml.karmaconfigs.api.common.timer.scheduler.SimpleScheduler;
import ml.karmaconfigs.api.common.timer.worker.AsyncScheduler;
import ml.karmaconfigs.api.common.timer.worker.SyncScheduler;
import ml.karmaconfigs.api.common.logger.KarmaLogger;
import ml.karmaconfigs.api.common.data.file.FileUtilities;
import ml.karmaconfigs.api.common.data.path.PathUtilities;
import ml.karmaconfigs.api.common.string.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Karma API source
 */
public interface KarmaSource extends Serializable {

    /**
     * Karma source name
     *
     * @return the source name
     */
    String name();

    /**
     * Karma source version
     *
     * @return the source version
     */
    String version();

    /**
     * Karma source description
     *
     * @return the source description
     */
    String description();

    /**
     * Karma source authors
     *
     * @return the source authors
     */
    String[] authors();

    /**
     * Karma source update URL
     *
     * @return the source update URL
     */
    String updateURL();

    /**
     * Stop all the source tasks
     */
    default void stopTasks() {
        SimpleScheduler.cancelFor(this);
    }

    /**
     * Get the authors using a custom separator
     *
     * @param firstSeparator if the first object should have separator
     * @param separator      the separator
     * @return the authors using the separator options
     */
    default String authors(final boolean firstSeparator, final String separator) {
        String[] authors = authors();
        StringBuilder builder = new StringBuilder();
        for (String author : authors)
            builder.append(separator).append(author);
        if (firstSeparator)
            return builder.toString();

        return builder.toString().replaceFirst(separator, "");
    }

    /**
     * Get the source file
     *
     * @return the source file
     */
    default File getSourceFile() {
        File mainJar = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " "));
        return FileUtilities.getFixedFile(mainJar);
    }

    /**
     * Get the source data path
     *
     * @return the source data path
     */
    default Path getDataPath() {
        File dataFolder, mainJar = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " "));
        File parent = mainJar.getParentFile();
        if (StringUtils.isNullOrEmpty(name())) {
            dataFolder = new File(parent, new RandomString().create());
        } else {
            dataFolder = new File(parent, name());
        }
        return dataFolder.toPath();
    }

    /**
     * Get a source file
     *
     * @param name the file name
     * @param path the file path
     * @return the source file
     */
    default KarmaMain loadFile(final String name, final String... path) {
        return new KarmaMain(this, name, path);
    }

    /**
     * Make a BufferedImage from an image
     *
     * @param source the image
     * @return the image buffered image
     */
    default BufferedImage bufferImage(final Image source) {
        BufferedImage buffered = new BufferedImage(source.getWidth(null), source.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = buffered.createGraphics();
        g2.drawImage(source, null, null);
        g2.dispose();

        return buffered;
    }

    /**
     * Save a resource
     *
     * @param stream the resource
     * @param name the resource name
     * @param directory the resource directory
     * @return the resource path
     */
    default Path saveResource(final InputStream stream, final String name, final String... directory) {
        Path home = getDataPath();

        for (String sub : directory) {
            home = home.resolve(sub);
        }

        home = home.resolve(name);
        PathUtilities.create(home);

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[512];
            int length;
            while ((length = stream.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }

            Files.write(home, out.toByteArray(), StandardOpenOption.CREATE);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        return home;
    }


    /**
     * Load a source resource
     *
     * @param name      the resource name
     * @param directory the resource path
     * @return the resource
     */
    @Nullable
    default InputStream loadResource(final String name, final String... directory) {
        Path home = getDataPath();

        for (String sub : directory) {
            home = home.resolve(sub);
        }

        home = home.resolve(name);
        if (Files.exists(home)) {
            try {
                byte[] data = Files.readAllBytes(home);
                return new ByteArrayInputStream(data);
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Get if the source is an instance of this
     *
     * @param source the source
     * @return if the source is this source
     */
    default boolean isSource(final KarmaSource source) {
        return (getClass().getName().equalsIgnoreCase(source.getClass().getName()) && name().equals(source.name()));
    }

    /**
     * Get if this source will migrate automatically karma files
     *
     * @return if this source migrates karma files
     */
    default boolean migrateLegacyKarmaFile() {
        return true;
    }

    /**
     * Get the source out
     *
     * @return the source out
     */
    default Console console() {
        Console simple = new Console(this);

        try {
            //Fixes a problem related with papermc and 'nag authors'
            Class<?> bukkit = Class.forName("org.bukkit.Bukkit");
            Method getServer = bukkit.getMethod("getServer");

            Object server = getServer.invoke(bukkit);

            Class<?> svClass = server.getClass();
            Method consoleSender = svClass.getMethod("getConsoleSender");

            Object commandSender = consoleSender.invoke(server);

            Class<?> consoleCommandSender = commandSender.getClass();
            Method sendMessage = consoleCommandSender.getMethod("sendMessage", String.class);

            return new Console(this, (msg) -> {
                try {
                    sendMessage.invoke(commandSender, StringUtils.toColor(msg));
                } catch (Throwable ex) {
                    simple.send(msg);
                }
            });
        } catch (Throwable ex) {
            return simple;
        }
    }

    /**
     * Get the source async scheduler
     *
     * @return the source async scheduler
     */
    default Scheduler async() {
        return new AsyncScheduler<>(this);
    }

    /**
     * Get the source sync scheduler
     *
     * @return the source sync scheduler
     */
    default Scheduler sync() {
        return new SyncScheduler<>(this);
    }

    /**
     * Get the source logger
     *
     * @return the source logger
     */
    default KarmaLogger logger() {
        return new Logger(this);
    }

    /**
     * Returns if the specified source is the same
     * as this one
     *
     * @param dst the source to check with
     * @return if the source is the same as this
     */
    default boolean srcEquals(final KarmaSource dst) {
        return dst.hashCode() == this.hashCode();
    }
}
