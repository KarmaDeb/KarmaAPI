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

import ml.karmaconfigs.api.common.data.file.FileUtilities;
import ml.karmaconfigs.api.common.data.path.PathUtilities;
import ml.karmaconfigs.api.common.karma.file.KarmaMain;
import ml.karmaconfigs.api.common.karma.file.element.KarmaPrimitive;
import ml.karmaconfigs.api.common.karma.file.element.multi.KarmaArray;
import ml.karmaconfigs.api.common.karma.file.element.types.Element;
import ml.karmaconfigs.api.common.karma.file.element.types.ElementArray;
import ml.karmaconfigs.api.common.karma.file.element.types.ElementPrimitive;
import ml.karmaconfigs.api.common.karma.file.yaml.FileCopy;
import ml.karmaconfigs.api.common.karma.source.KarmaSource;
import ml.karmaconfigs.api.common.utils.enums.Level;
import ml.karmaconfigs.api.common.logger.web.WebTarget;
import ml.karmaconfigs.api.common.string.StringUtils;

/**
 * Karma configuration
 */
public final class KarmaConfig {

    private final static KarmaMain mn = KarmaMain.getConfiguration();

    /**
     * Get if the API should print the license to the console
     *
     * @return if the API prints the license to console
     */
    public boolean printLicense() {
        if (mn != null) {
            Element<?> element = mn.get("print_license", new KarmaPrimitive(false));

            if (element.isPrimitive()) {
                ElementPrimitive primitive = element.getAsPrimitive();
                if (primitive.isBoolean())
                    return primitive.asBoolean();
            }
        }

        return true;
    }

    /**
     * Set the print license status
     *
     * @param status the print license status
     */
    public void setPrintLicense(final boolean status) {
        if (mn != null) {
            mn.setRaw("print_license", status);
            mn.save();
        }
    }

    /**
     * Get if the API should print the license to the console
     *
     * @return if the API prints the license to console
     */
    public int requestCodeTimeout() {
        if (mn != null) {
            Element<?> element = mn.get("url.request_code_timeout", new KarmaPrimitive(1000));

            if (element.isPrimitive()) {
                ElementPrimitive primitive = element.getAsPrimitive();
                if (primitive.isNumber())
                    return primitive.asInteger();
            }
        }

        return 1000;
    }

    /**
     * Get if the API should print the license to the console
     *
     * @return if the API prints the license to console
     */
    public boolean requestCodeStrict() {
        if (mn != null) {
            Element<?> element = mn.get("url.code_strict", new KarmaPrimitive(1000));

            if (element.isPrimitive()) {
                ElementPrimitive primitive = element.getAsPrimitive();
                if (primitive.isBoolean())
                    return primitive.asBoolean();
            }
        }

        return false;
    }

    /**
     * Get the console default prefix
     *
     * @param source the source for the prefix
     * @param lvl    the level for the prefix
     * @return the console level prefix
     */
    public String consolePrefix(final KarmaSource source, final Level lvl) {
        String placeholder;

        switch (lvl) {
            case OK:
                placeholder = "&b[ &3%project% &b| &2OK &b] >> &9";
                if (mn != null) {
                    Element<?> element = mn.get("ok_prefix", null);
                    if (element != null && element.isPrimitive()) {
                        String rs = element.toString();
                        if (!StringUtils.isNullOrEmpty(rs))
                            placeholder = rs;
                    }
                }
                break;
            case INFO:
                placeholder = "&b[ &3%project% &b| &7INFO &b] >> &9";
                if (mn != null) {
                    Element<?> element = mn.get("info_prefix", null);
                    if (element != null && element.isPrimitive()) {
                        String rs = element.toString();
                        if (!StringUtils.isNullOrEmpty(rs))
                            placeholder = rs;
                    }
                }
                break;
            case WARNING:
                placeholder = "&b[ &3%project% &b| &6WARNING &b] >> &9";
                if (mn != null) {
                    Element<?> element = mn.get("warning_prefix", null);
                    if (element != null && element.isPrimitive()) {
                        String rs = element.toString();
                        if (!StringUtils.isNullOrEmpty(rs))
                            placeholder = rs;
                    }
                }

                break;
            case GRAVE:
            default:
                placeholder = "&b[ &3%project% &b| &cGRAVE &b] >> &9";
                if (mn != null) {
                    Element<?> element = mn.get("grave_prefix", null);
                    if (element != null && element.isPrimitive()) {
                        String rs = element.toString();
                        if (!StringUtils.isNullOrEmpty(rs))
                            placeholder = rs;
                    }
                }
                break;
        }

        return placeholder
                .replace("%project%", source.name())
                .replace("%version%", source.version())
                .replace("%author%", source.authors(false, " ; "))
                .replace("%level%", lvl.name().toUpperCase());
    }

    /**
     * Get if the specified level should be debugged in
     * {@link FileCopy}
     *
     * @param lvl the level
     * @return if the level is allowed to debug
     */
    public boolean fileDebug(final Level lvl) {
        if (mn != null) {
            Element<?> element = mn.get("file_copy_debug", new KarmaPrimitive(false));

            if (element.isPrimitive()) {
                ElementPrimitive primitive = element.getAsPrimitive();
                if (primitive.isBoolean()) {
                    boolean result = primitive.asBoolean();

                    if (result) {
                        Element<?> levels = mn.get("file_copy_levels", new KarmaArray(
                                new KarmaPrimitive("WARNING"),
                                new KarmaPrimitive("INFO")
                        ));

                        if (levels.isArray()) {
                            ElementArray<ElementPrimitive> array = ((KarmaArray) levels).contentsToLowerCase();
                            return array.contains(new KarmaPrimitive(lvl.name().toLowerCase()));
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Get if the specified level should be debugged in
     * {@link FileUtilities} or {@link PathUtilities}
     *
     * @param lvl the level
     * @return if the level is allowed to debug
     */
    public boolean utilDebug(final Level lvl) {
        if (mn != null) {
            Element<?> element = mn.get("file_util_debug", new KarmaPrimitive(false));

            if (element.isPrimitive()) {
                ElementPrimitive primitive = element.getAsPrimitive();
                if (primitive.isBoolean()) {
                    boolean result = primitive.asBoolean();

                    if (result) {
                        Element<?> levels = mn.get("file_util_levels", new KarmaArray(
                                new KarmaPrimitive("OK"),
                                new KarmaPrimitive("INFO"),
                                new KarmaPrimitive("WARNING"),
                                new KarmaPrimitive("GRAVE")
                        ));

                        if (levels.isArray()) {
                            ElementArray<ElementPrimitive> array = ((KarmaArray) levels).contentsToLowerCase();
                            return array.contains(new KarmaPrimitive(lvl.name().toLowerCase()));
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Get if the specified level should be debugged in
     * any part of the API.
     * {@link KarmaConfig#fileDebug(Level)} and {@link KarmaConfig#utilDebug(Level)}
     * should not be affected by this
     *
     * @param lvl the level
     * @return if the level is allowed to debug
     */
    public boolean debug(final Level lvl) {
        if (mn != null) {
            Element<?> element = mn.get("debug", new KarmaPrimitive(false));

            if (element.isPrimitive()) {
                ElementPrimitive primitive = element.getAsPrimitive();
                if (primitive.isBoolean()) {
                    boolean result = primitive.asBoolean();
                    if (result) {
                        Element<?> levels = mn.get("debug_levels", new KarmaArray(
                                new KarmaPrimitive("OK"),
                                new KarmaPrimitive("INFO"),
                                new KarmaPrimitive("WARNING"),
                                new KarmaPrimitive("GRAVE")));

                        if (levels.isArray()) {
                            ElementArray<ElementPrimitive> array = ((KarmaArray) levels).contentsToLowerCase();
                            return array.contains(new KarmaPrimitive(lvl.name().toLowerCase()));
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Get if the specified level should be logged in
     * any part of the API.
     *
     * @param lvl the level
     * @return if the level is allowed to log
     */
    public boolean log(final Level lvl) {
        if (mn != null) {
            Element<?> element = mn.get("logging", new KarmaPrimitive(true));
            if (element.isPrimitive()) {
                ElementPrimitive primitive = element.getAsPrimitive();
                if (primitive.isBoolean()) {
                    boolean result = primitive.asBoolean();
                    if (result) {
                        Element<?> levels = mn.get("logging_levels", new KarmaArray(
                                new KarmaPrimitive("OK"),
                                new KarmaPrimitive("INFO"),
                                new KarmaPrimitive("WARNING"),
                                new KarmaPrimitive("GRAVE")));

                        if (levels.isArray()) {
                            ElementArray<ElementPrimitive> array = ((KarmaArray) levels).contentsToLowerCase();
                            return array.contains(new KarmaPrimitive(lvl.name().toLowerCase()));
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * Get the access key of the web log target
     *
     * @param target the web target
     * @return the log API access key
     */
    public String getAccessKey(final WebTarget target) {
        if (mn != null) {
            Element<?> key = mn.get("paste_credentials." + target.name());
            if (key != null && key.isPrimitive())
                return key.toString();
        }

        return "";
    }
}
