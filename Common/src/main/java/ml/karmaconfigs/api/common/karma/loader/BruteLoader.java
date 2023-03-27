package ml.karmaconfigs.api.common.karma.loader;

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

import ml.karmaconfigs.api.common.karma.source.APISource;
import ml.karmaconfigs.api.common.karma.source.KarmaSource;
import ml.karmaconfigs.api.common.utils.JavaVM;
import ml.karmaconfigs.api.common.ResourceDownloader;
import ml.karmaconfigs.api.common.karma.loader.component.NameComponent;
import ml.karmaconfigs.api.common.utils.enums.Level;
import ml.karmaconfigs.api.common.data.file.FileUtilities;
import ml.karmaconfigs.api.common.data.path.PathUtilities;
import ml.karmaconfigs.api.common.utils.url.URLUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static ml.karmaconfigs.api.common.karma.KarmaAPI.source;

/**
 * Brute source loader
 * <p>
 * This new way of loading sources to the project class
 * path export all the modules to all the modules
 * "ExportAllToAll" using BurningWave utility class.
 * <p>
 * That way, there's no need to create any class loader
 * nor bootstrap
 */
public final class BruteLoader {

    private final static Set<String> failed = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private static boolean open = false;

    private final ClassLoader loader;

    /**
     * Initialize the brute loader
     *
     * @param ucl the main class loader
     */
    @SuppressWarnings("all")
    public BruteLoader(final ClassLoader ucl) {
        ClassLoader tmp = ucl;
        if (!(tmp instanceof URLClassLoader)) {
            tmp = Thread.currentThread().getContextClassLoader();

            if (!(tmp instanceof URLClassLoader))
                tmp = ucl;
        }

        loader = tmp;
        if (JavaVM.javaVersion() >= 9 && !open) {
            KarmaSource source = APISource.getOriginal(false);

            open = true;
            try {
                Path bw_file = source.getDataPath().resolve("runtime").resolve("BurningWave.jar");
                Path jv_file = source.getDataPath().resolve("runtime").resolve("JVMDriver.jar");

                if (!Files.exists(bw_file) && !Files.exists(jv_file)) {
                    URL bw_url = URLUtils.getOrBackup("https://github.com/KarmaConfigs/updates/raw/master/KarmaAPI/BurningWave.jar",
                            "https://karmadev.es/karma-repository/BurningWave.jar",
                            "https://karmaconfigs.ml/karma-repository/BurningWave.jar",
                            "https://karmarepo.ml/karma-repository/BurningWave.jar",
                            "https://backup.karmadev.es/karma-repository/BurningWave.jar",
                            "https://backup.karmaconfigs.ml/karma-repository/BurningWave.jar",
                            "https://backup.karmarepo.ml/karma-repository/BurningWave.jar");
                    URL jv_url = URLUtils.getOrBackup("https://github.com/KarmaConfigs/updates/raw/master/KarmaAPI/JVMDriver.jar",
                            "https://karmadev.es/karma-repository/JVMDriver.jar",
                            "https://karmaconfigs.ml/karma-repository/JVMDriver.jar",
                            "https://karmarepo.ml/karma-repository/JVMDriver.jar",
                            "https://backup.karmadev.es/karma-repository/JVMDriver.jar",
                            "https://backup.karmaconfigs.ml/karma-repository/JVMDriver.jar",
                            "https://backup.karmarepo.ml/karma-repository/JVMDriver.jar");

                    if (bw_url != null && jv_url != null) {

                        ResourceDownloader bw_downloader = new ResourceDownloader(bw_file, bw_url);
                        ResourceDownloader jvm_downloader = new ResourceDownloader(jv_file, jv_url);

                        bw_downloader.download();
                        jvm_downloader.download();
                    }
                }

                URL[] urls = {
                        new URL("jar:file:" + bw_file + "!/"),
                        new URL("jar:file:" + jv_file + "!/")
                };
                URLClassLoader cl = new URLClassLoader(urls, ucl);
                Class<?> loader = cl.loadClass("org.burningwave.core.assembler.StaticComponentContainer");
                Field modules = loader.getDeclaredField("Modules");

                Object module = modules.get(loader);
                Method exportAllToAll = module.getClass().getDeclaredMethod("exportAllToAll");
                exportAllToAll.invoke(module);
            } catch (Throwable ignored) {}
        }
    }

    /**
     * Initialize the brute loader
     *
     * @param ucl the main class loader
     */
    public BruteLoader(final URLClassLoader ucl) {
        loader = ucl;

        if (!open) {
            KarmaSource source = APISource.getOriginal(false);

            open = true;
            try {
                Path bw_file = source.getDataPath().resolve("runtime").resolve("BurningWave.jar");
                Path jv_file = source.getDataPath().resolve("runtime").resolve("JVMDriver.jar");

                if (!Files.exists(bw_file) && !Files.exists(jv_file)) {
                    URL bw_url = URLUtils.getOrBackup("https://github.com/KarmaConfigs/updates/raw/master/KarmaAPI/BurningWave.jar",
                            "https://karmadev.es/karma-repository/BurningWave.jar",
                            "https://karmaconfigs.ml/karma-repository/BurningWave.jar",
                            "https://karmarepo.ml/karma-repository/BurningWave.jar",
                            "https://backup.karmadev.es/karma-repository/BurningWave.jar",
                            "https://backup.karmaconfigs.ml/karma-repository/BurningWave.jar",
                            "https://backup.karmarepo.ml/karma-repository/BurningWave.jar");
                    URL jv_url = URLUtils.getOrBackup("https://github.com/KarmaConfigs/updates/raw/master/KarmaAPI/JVMDriver.jar",
                            "https://karmadev.es/karma-repository/JVMDriver.jar",
                            "https://karmaconfigs.ml/karma-repository/JVMDriver.jar",
                            "https://karmarepo.ml/karma-repository/JVMDriver.jar",
                            "https://backup.karmadev.es/karma-repository/JVMDriver.jar",
                            "https://backup.karmaconfigs.ml/karma-repository/JVMDriver.jar",
                            "https://backup.karmarepo.ml/karma-repository/JVMDriver.jar");

                    if (bw_url != null && jv_url != null) {

                        ResourceDownloader bw_downloader = new ResourceDownloader(bw_file, bw_url);
                        ResourceDownloader jvm_downloader = new ResourceDownloader(jv_file, jv_url);

                        bw_downloader.download();
                        jvm_downloader.download();
                    }
                }

                URL[] urls = {
                        new URL("jar:file:" + bw_file + "!/"),
                        new URL("jar:file:" + jv_file + "!/")
                };
                URLClassLoader cl = new URLClassLoader(urls, ucl);
                Class<?> loader = cl.loadClass("org.burningwave.core.assembler.StaticComponentContainer");
                Field modules = loader.getDeclaredField("Modules");

                Object module = modules.get(loader);
                Method exportAllToAll = module.getClass().getDeclaredMethod("exportAllToAll");
                exportAllToAll.invoke(module);
            } catch (Throwable ignored) {}
        }
    }

    /**
     * Download and then inject the result
     * into the API
     *
     * @param downloadURL the source download URL
     * @param name        the source name, use {@link NameComponent#forFile(CharSequence, String, String...)}
     */
    public void downloadAndInject(final URL downloadURL, final NameComponent name) {
        //Dependencies will always be inside ./KarmaAPI/cache/dependencies/...
        name.addParentStart("dependencies");

        if (downloadURL != null) {
            ResourceDownloader downloader = ResourceDownloader.toCache(source(true), name.getName() + "." + name.findExtension(), downloadURL.toString(), name.getParents());
            if (!Files.exists(downloader.getDestPath())) {
                downloader.download();
            }

            add(downloader.getDestFile());
            if (failed.contains(name.getName())) {
                failed.remove(name.getName());

                source(false).console().send("Downloaded {0} after a failed download. The sources may work as usual from now, but a server/service restart may be also necessary", Level.WARNING, name.getName());
            }
        } else {
            if (!failed.contains(name.getName())) {
                failed.add(name.getName());

                source(false).console().send("Failed to download {0} because download URL is null. This may cause lot of issues in the future", Level.GRAVE, name.getName());
            }
        }
    }

    /**
     * Tries to add the specified source to the
     * application classpath
     *
     * @param source the source to add
     * @return if the source could be added
     */
    public boolean add(final URL source) {
        try {
            Method method;
            if (loader instanceof URLClassLoader) {
                method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                method.setAccessible(true);
                method.invoke(loader, source);
            } else {
                method = ClassLoader.class.getDeclaredMethod("addClass", Class.class);
                Method define = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
                Method load = ClassLoader.class.getMethod("loadClassData", String.class);
                method.setAccessible(true);
                define.setAccessible(true);
                load.setAccessible(true);

                try (JarFile jarFile = new JarFile(source.getFile())) {
                    Enumeration<JarEntry> e = jarFile.entries();

                    URL[] urls = {new URL("jar:file:" + FileUtilities.getPrettyFile(new File(source.getFile())) + "!/")};
                    try (URLClassLoader cl = URLClassLoader.newInstance(urls)) {
                        while (e.hasMoreElements()) {
                            JarEntry je = e.nextElement();
                            if (je.isDirectory() || !je.getName().endsWith(".class")) {
                                continue;
                            }

                            String className = je.getName().substring(0, je.getName().length() - 6);
                            className = className.replace('/', '.');
                            if (!className.endsWith("module-info")) {
                                Class<?> clazz = cl.loadClass(className);
                                method.invoke(loader, clazz);
                                byte[] data = (byte[]) load.invoke(loader, className);
                                define.invoke(loader, className, data, 0, data.length);
                            }
                        }

                        load.setAccessible(false);
                        define.setAccessible(false);
                    }
                }
            }
            method.setAccessible(false);

            return true;
        } catch (Throwable ex) {
            return false;
        }
    }

    /**
     * Tries to add the specified source to the
     * application classpath
     *
     * @param source the source to add
     * @return if the source could be added
     */
    public boolean add(final File source) {
        try {
            Method method;
            if (loader instanceof URLClassLoader) {
                method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                method.setAccessible(true);
                method.invoke(loader, source.toURI().toURL());
            } else {
                method = ClassLoader.class.getDeclaredMethod("addClass", Class.class);
                Method define = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
                Method load = ClassLoader.class.getMethod("loadClassData", String.class);
                method.setAccessible(true);
                define.setAccessible(true);
                load.setAccessible(true);

                try (JarFile jarFile = new JarFile(source)) {
                    Enumeration<JarEntry> e = jarFile.entries();

                    URL[] urls = {new URL("jar:file:" + FileUtilities.getPrettyFile(source) + "!/")};
                    try (URLClassLoader cl = URLClassLoader.newInstance(urls)) {
                        while (e.hasMoreElements()) {
                            JarEntry je = e.nextElement();
                            if (je.isDirectory() || !je.getName().endsWith(".class")) {
                                continue;
                            }

                            String className = je.getName().substring(0, je.getName().length() - 6);
                            if (!className.endsWith("module-info")) {
                                Class<?> clazz = cl.loadClass(className);
                                method.invoke(loader, clazz);
                                byte[] data = (byte[]) load.invoke(loader, className);
                                define.invoke(loader, className, data, 0, data.length);
                            }
                        }

                        load.setAccessible(false);
                        define.setAccessible(false);
                    }
                }
            }
            method.setAccessible(false);

            return true;
        } catch (Throwable ex) {
            return false;
        }
    }

    /**
     * Tries to add the specified source to the
     * application classpath
     *
     * @param source the source to add
     * @return if the source could be added
     */
    public boolean add(final Path source) {
        try {
            Method method;

            if (loader instanceof URLClassLoader) {
                method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                method.setAccessible(true);
                method.invoke(loader, source.toUri().toURL());
            } else {
                method = ClassLoader.class.getDeclaredMethod("addClass", Class.class);
                Method define = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
                Method load = ClassLoader.class.getMethod("loadClassData", String.class);
                method.setAccessible(true);
                define.setAccessible(true);
                load.setAccessible(true);

                try (JarFile jarFile = new JarFile(source.toFile())) {
                    Enumeration<JarEntry> e = jarFile.entries();

                    URL[] urls = {new URL("jar:file:" + PathUtilities.getPrettyPath(source) + "!/")};
                    try (URLClassLoader cl = URLClassLoader.newInstance(urls)) {
                        while (e.hasMoreElements()) {
                            JarEntry je = e.nextElement();
                            if (je.isDirectory() || !je.getName().endsWith(".class")) {
                                continue;
                            }

                            String className = je.getName().substring(0, je.getName().length() - 6);
                            if (!className.endsWith("module-info")) {
                                Class<?> clazz = cl.loadClass(className);
                                method.invoke(loader, clazz);
                                byte[] data = (byte[]) load.invoke(loader, className);
                                define.invoke(loader, className, data, 0, data.length);
                            }
                        }

                        load.setAccessible(false);
                        define.setAccessible(false);
                    }
                }
            }
            method.setAccessible(false);

            return true;
        } catch (Throwable ex) {
            return false;
        }
    }

    /**
     * Get the used class loader
     *
     * @return the class loader
     */
    public ClassLoader getLoader() {
        return loader;
    }
}
