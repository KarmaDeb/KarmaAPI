package ml.karmaconfigs.api.common.karma;

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

import ml.karmaconfigs.api.common.karma.loader.BruteLoader;
import ml.karmaconfigs.api.common.karma.loader.component.NameComponent;
import ml.karmaconfigs.api.common.karma.source.APISource;
import ml.karmaconfigs.api.common.karma.source.KarmaSource;
import ml.karmaconfigs.api.common.utils.enums.Level;
import ml.karmaconfigs.api.common.string.StringUtils;
import ml.karmaconfigs.api.common.utils.url.URLUtils;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Karma API
 */
public interface KarmaAPI extends Serializable {

    /**
     * Get the current API version
     *
     * @return the current API version
     */
    static String getVersion() {
        String version = "-1";
        try {
            InputStream in = KarmaAPI.class.getResourceAsStream("/api.properties");
            if (in != null) {
                Properties properties = new Properties();
                properties.load(in);
                version = properties.getProperty("version", "-1");
            }
        } catch (Throwable ignored) {
        }
        return version;
    }

    /**
     * Get the current API version
     *
     * @return the current API version
     */
    static String getPluginVersion() {
        String version = "-1";
        try {
            InputStream in = KarmaAPI.class.getResourceAsStream("/api.properties");
            if (in != null) {
                Properties properties = new Properties();
                properties.load(in);
                version = properties.getProperty("plugin_version", "-1");
            }
        } catch (Throwable ignored) {
        }
        return version;
    }

    /**
     * Get the current API version
     *
     * @return the current API version
     */
    static String getPluginName() {
        String version = "-1";
        try {
            InputStream in = KarmaAPI.class.getResourceAsStream("/api.properties");
            if (in != null) {
                Properties properties = new Properties();
                properties.load(in);
                version = properties.getProperty("plugin_name", "-1");
            }
        } catch (Throwable ignored) {
        }
        return version;
    }

    /**
     * Get the used java version to compile
     * the API
     *
     * @return the java version used to compile the API
     */
    static String getCompilerVersion() {
        String version = "16";
        try {
            InputStream in = KarmaAPI.class.getResourceAsStream("/api.properties");
            if (in != null) {
                Properties properties = new Properties();
                properties.load(in);
                version = properties.getProperty("java_version", "15");
            }
        } catch (Throwable ignored) {
        }
        return version;
    }

    /**
     * Get the API build date
     *
     * @return the API build date
     */
    static String getBuildDate() {
        String compile_date = "01-01-1999 00:00:00";
        try {
            InputStream in = KarmaAPI.class.getResourceAsStream("/api.properties");
            if (in != null) {
                Properties properties = new Properties();
                properties.load(in);
                compile_date = properties.getProperty("compile_date", "01-01-1999 00:00:00");
            }
        } catch (Throwable ignored) {
        }
        return compile_date;
    }

    /**
     * Get if the specified source jar is loaded
     *
     * @param source the source
     * @return if the source jar is loaded
     */
    static boolean isLoaded(final KarmaSource source) {
        JarFile jar = null;
        SourceMap map = new SourceMap(source);

        try {
            String stored = map.get();
            if (!StringUtils.isNullOrEmpty(stored)) {
                Class<?> clazz = KarmaAPI.class.getClassLoader().loadClass(stored);
                if (clazz != null) {
                    return true;
                }
            }
        } catch (Throwable ignored) {
        }

        try {
            File sourceJar = source.getSourceFile();
            jar = new JarFile(sourceJar);

            Enumeration<JarEntry> e = jar.entries();
            while (e.hasMoreElements()) {
                JarEntry jarEntry = e.nextElement();
                if (jarEntry.getName().endsWith(".class")) {
                    String className = jarEntry.getName()
                            .replace("/", ".")
                            .replace(".class", "");

                    try {
                        Class<?> clazz = KarmaAPI.class.getClassLoader().loadClass(className);
                        if (clazz != null) {
                            map.set(className);
                            return true;
                        }
                    } catch (Throwable ignored) {
                    }
                }
            }
        } catch (Throwable ignored) {
        } finally {
            try {
                if (jar != null)
                    jar.close();
            } catch (Throwable ignored) {
            }
        }

        return false;
    }

    /**
     * Get the API source
     *
     * @param force force default KarmaAPI
     * @return a KarmaSource
     */
    static KarmaSource source(final boolean force) {
        return APISource.getOriginal(force);
    }

    /**
     * Install KarmaAPI dependencies
     */
    static void install() {
        KarmaConfig config = new KarmaConfig();
        BruteLoader loader = null;

        try {
            loader = new BruteLoader((URLClassLoader) source(false).getClass().getClassLoader());
        } catch (Throwable ex) {
            try {
                loader = new BruteLoader((URLClassLoader) Thread.currentThread().getContextClassLoader());
            } catch (Throwable exc) {
                if (config.debug(Level.GRAVE)) {
                    source(false).console().send("Failed to install KarmaAPI dependencies because of {0}", Level.GRAVE, ex.fillInStackTrace());

                    for (StackTraceElement element : ex.getStackTrace()) {
                        source(false).console().send("&c             {0}", element);
                    }
                }
            }
        }

        if (loader != null) {
            KarmaSource source = source(true);

            try {
                Class.forName("org.slf4j.LoggerFactory");
            } catch (Throwable ex) {
                if (config.debug(Level.WARNING)) {
                    source(false).console().send("Log4j dependency not found, downloading it...");
                }

                Path expected_file = source.getDataPath().resolve("dependencies").resolve("Log4j.jar");
                if (!Files.exists(expected_file)) {
                    loader.downloadAndInject(
                            URLUtils.getOrBackup("https://github.com/KarmaConfigs/updates/raw/master/KarmaAPI/Log4j.jar",
                                    "https://karmadev.es/karma-repository/Log4j.jar",
                                    "https://karmaconfigs.ml/karma-repository/Log4j.jar",
                                    "https://karmarepo.ml/karma-repository/Log4j.jar",
                                    "https://backup.karmadev.es/karma-repository/Log4j.jar",
                                    "https://backup.karmaconfigs.ml/karma-repository/Log4j.jar",
                                    "https://backup.karmarepo.ml/karma-repository/Log4j.jar"),
                            NameComponent.forFile("Log4j", "jar"));
                } else {
                    loader.add(expected_file);
                }
            }

            try {
                Class<?> clazz = Class.forName("com.google.gson.GsonBuilder");
                clazz.getMethod("setLenient");
            } catch (Throwable ex) {
                if (config.debug(Level.WARNING)) {
                    source(false).console().send("Google GSON dependency not found ( or very old version of it is running ) for UUID utilities, downloading it...", Level.WARNING);
                }

                Path expected_file = source.getDataPath().resolve("dependencies").resolve("GoogleGSON.jar");
                if (!Files.exists(expected_file)) {
                    loader.downloadAndInject(
                            URLUtils.getOrBackup("https://github.com/KarmaConfigs/updates/raw/master/KarmaAPI/GoogleGSON.jar",
                                    "https://karmadev.es/karma-repository/GoogleGSON.jar",
                                    "https://karmaconfigs.ml/karma-repository/GoogleGSON.jar",
                                    "https://karmarepo.ml/karma-repository/GoogleGSON.jar",
                                    "https://backup.karmadev.es/karma-repository/GoogleGSON.jar",
                                    "https://backup.karmaconfigs.ml/karma-repository/GoogleGSON.jar",
                                    "https://backup.karmarepo.ml/karma-repository/GoogleGSON.jar"),
                            NameComponent.forFile("GoogleGSON", "jar"));
                } else {
                    loader.add(expected_file);
                }
            }

            try {
                Class.forName("org.apache.hc.client5.http.impl.classic.HttpClients");
            } catch (Throwable ex) {
                if (config.debug(Level.WARNING)) {
                    source(false).console().send("Apache HTTP 5 not found for URL utilities, downloading it...", Level.WARNING);
                }

                Path expected_file = source.getDataPath().resolve("dependencies").resolve("ApacheHTTP5.jar");
                if (!Files.exists(expected_file)) {
                    loader.downloadAndInject(
                            URLUtils.getOrBackup("https://github.com/KarmaConfigs/updates/raw/master/KarmaAPI/ApacheHTTP5.jar",
                                    "https://karmadev.es/karma-repository/ApacheHTTP5.jar",
                                    "https://karmaconfigs.ml/karma-repository/ApacheHTTP5.jar",
                                    "https://karmarepo.ml/karma-repository/ApacheHTTP5.jar",
                                    "https://backup.karmadev.es/karma-repository/ApacheHTTP5.jar",
                                    "https://backup.karmaconfigs.ml/karma-repository/ApacheHTTP5.jar",
                                    "https://backup.karmarepo.ml/karma-repository/ApacheHTTP5.jar"),
                            NameComponent.forFile("ApacheHTTP5", "jar"));
                } else {
                    loader.add(expected_file);
                }
            }

            try {
                Class.forName("org.apache.hc.core5.http.Header");
            } catch (Throwable ex) {
                if (config.debug(Level.WARNING)) {
                    source(false).console().send("Apache HTTP Core 5 not found for URL utilities, downloading it...", Level.WARNING);
                }

                Path expected_file = source.getDataPath().resolve("dependencies").resolve("ApacheCore5.jar");
                if (!Files.exists(expected_file)) {
                    loader.downloadAndInject(
                            URLUtils.getOrBackup("https://github.com/KarmaConfigs/updates/raw/master/KarmaAPI/ApacheCore5.jar",
                                    "https://karmadev.es/karma-repository/ApacheCore5.jar",
                                    "https://karmaconfigs.ml/karma-repository/ApacheCore5.jar",
                                    "https://karmarepo.ml/karma-repository/ApacheCore5.jar",
                                    "https://backup.karmadev.es/karma-repository/ApacheCore5.jar",
                                    "https://backup.karmaconfigs.ml/karma-repository/ApacheCore5.jar",
                                    "https://backup.karmarepo.ml/karma-repository/ApacheCore5.jar"),
                            NameComponent.forFile("ApacheCore5", "jar"));
                } else {
                    loader.add(expected_file);
                }
            }

            try {
                Class.forName("org.apache.hc.core5.http2.H2Error");
            } catch (Throwable ex) {
                if (config.debug(Level.WARNING)) {
                    source(false).console().send("Apache HTTP2 Core 5 not found for URL utilities, downloading it...", Level.WARNING);
                }

                Path expected_file = source.getDataPath().resolve("dependencies").resolve("Apache2Core5.jar");
                if (!Files.exists(expected_file)) {
                    loader.downloadAndInject(
                            URLUtils.getOrBackup("https://github.com/KarmaConfigs/updates/raw/master/KarmaAPI/Apache2Core5.jar",
                                    "https://karmadev.es/karma-repository/Apache2Core5.jar",
                                    "https://karmaconfigs.ml/karma-repository/Apache2Core5.jar",
                                    "https://karmarepo.ml/karma-repository/Apache2Core5.jar",
                                    "https://backup.karmadev.es/karma-repository/Apache2Core5.jar",
                                    "https://backup.karmaconfigs.ml/karma-repository/Apache2Core5.jar",
                                    "https://backup.karmarepo.ml/karma-repository/Apache2Core5.jar"),
                            NameComponent.forFile("Apache2Core5", "jar"));
                } else {
                    loader.add(expected_file);
                }
            }

            /*try {
                Class.forName("org.apache.http.impl.client.HttpClients");
            } catch (Throwable ex) {
                if (config.debug(Level.WARNING)) {
                    source(false).console().send("Apache HTTP Client Components not found for URL utilities, downloading it...", Level.WARNING);
                }

                Path expected_file = source.getDataPath().resolve("dependencies").resolve("ApacheHTTPClient.jar");
                if (!Files.exists(expected_file)) {
                    loader.downloadAndInject(
                            URLUtils.getOrBackup("https://github.com/KarmaConfigs/updates/raw/master/KarmaAPI/ApacheHTTPClient.jar",
                                    "https://karmadev.es/karma-repository/ApacheHTTPClient.jar",
                                    "https://karmaconfigs.ml/karma-repository/ApacheHTTPClient.jar",
                                    "https://karmarepo.ml/karma-repository/ApacheHTTPClient.jar",
                                    "https://backup.karmadev.es/karma-repository/ApacheHTTPClient.jar",
                                    "https://backup.karmaconfigs.ml/karma-repository/ApacheHTTPClient.jar",
                                    "https://backup.karmarepo.ml/karma-repository/ApacheHTTPClient.jar"),
                            NameComponent.forFile("ApacheHTTPClient", "jar"));
                } else {
                    loader.add(expected_file);
                }
            }*/

            try {
                Class.forName("org.apache.commons.logging.LogFactory");
            } catch (Throwable ex) {
                if (config.debug(Level.WARNING)) {
                    source(false).console().send("Apache Commons Logging not found for other utilities, downloading it...", Level.WARNING);
                }

                Path expected_file = source.getDataPath().resolve("dependencies").resolve("ApacheLogging.jar");
                if (!Files.exists(expected_file)) {
                    loader.downloadAndInject(
                            URLUtils.getOrBackup("https://github.com/KarmaConfigs/updates/raw/master/KarmaAPI/ApacheLogger.jar",
                                    "https://karmadev.es/karma-repository/ApacheLogger.jar",
                                    "https://karmaconfigs.ml/karma-repository/ApacheLogger.jar",
                                    "https://karmarepo.ml/karma-repository/ApacheLogger.jar",
                                    "https://backup.karmadev.es/karma-repository/ApacheLogger.jar",
                                    "https://backup.karmaconfigs.ml/karma-repository/ApacheLogger.jar",
                                    "https://backup.karmarepo.ml/karma-repository/ApacheLogger.jar"),
                            NameComponent.forFile("ApacheLogging", "jar")
                    );
                } else {
                    loader.add(expected_file);
                }
            }

            try {
                Class.forName("okio.Buffer");
            } catch (Throwable ex) {
                if (config.debug(Level.WARNING)) {
                    source(false).console().send("Okio JVM not found for other utilities, downloading it...", Level.WARNING);
                }

                Path expected_file = source.getDataPath().resolve("dependencies").resolve("OkioJVM.jar");
                if (!Files.exists(expected_file)) {
                    loader.downloadAndInject(
                            URLUtils.getOrBackup("https://github.com/KarmaConfigs/updates/raw/master/KarmaAPI/OkioJVM.jar",
                                    "https://karmadev.es/karma-repository/OkioJVM.jar",
                                    "https://karmaconfigs.ml/karma-repository/OkioJVM.jar",
                                    "https://karmarepo.ml/karma-repository/OkioJVM.jar",
                                    "https://backup.karmadev.es/karma-repository/OkioJVM.jar",
                                    "https://backup.karmaconfigs.ml/karma-repository/OkioJVM.jar",
                                    "https://backup.karmarepo.ml/karma-repository/OkioJVM.jar"),
                            NameComponent.forFile("OkioJVM", "jar")
                    );
                } else {
                    loader.add(expected_file);
                }
            }


            try {
                Class.forName("okhttp3.Request");
            } catch (Throwable ex) {
                if (config.debug(Level.WARNING)) {
                    source(false).console().send("OkHTTP3 client not found for other utilities, downloading it...", Level.WARNING);
                }

                Path expected_file = source.getDataPath().resolve("dependencies").resolve("OkHTTP3.jar");
                if (!Files.exists(expected_file)) {
                    loader.downloadAndInject(
                            URLUtils.getOrBackup("https://github.com/KarmaConfigs/updates/raw/master/KarmaAPI/OkHTTP3.jar",
                                    "https://karmadev.es/karma-repository/OkHTTP3.jar",
                                    "https://karmaconfigs.ml/karma-repository/OkHTTP3.jar",
                                    "https://karmarepo.ml/karma-repository/OkHTTP3.jar",
                                    "https://backup.karmadev.es/karma-repository/OkHTTP3.jar",
                                    "https://backup.karmaconfigs.ml/karma-repository/OkHTTP3.jar",
                                    "https://backup.karmarepo.ml/karma-repository/OkHTTP3.jar"),
                            NameComponent.forFile("OkHTTP3", "jar")
                    );
                } else {
                    loader.add(expected_file);
                }
            }
        }
    }
}

/**
 * Sources class map
 */
class SourceMap {

    private final static Map<KarmaSource, String> map = new HashMap<>();

    private final KarmaSource source;

    /**
     * Initialize the source class map
     *
     * @param src the source
     */
    public SourceMap(final KarmaSource src) {
        source = src;
    }

    /**
     * Set the source class. This class is any class
     * found inside the jar file
     *
     * @param clazz the clazz path
     */
    public void set(final String clazz) {
        if (!StringUtils.isNullOrEmpty(clazz))
            map.put(source, clazz);
    }

    /**
     * Get the source class
     *
     * @return the source class path
     */
    @Nullable
    public String get() {
        return map.getOrDefault(source, null);
    }
}