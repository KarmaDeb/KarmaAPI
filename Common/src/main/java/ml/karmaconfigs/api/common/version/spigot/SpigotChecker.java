package ml.karmaconfigs.api.common.version.spigot;

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

import com.google.gson.*;
import ml.karmaconfigs.api.common.karma.KarmaAPI;
import ml.karmaconfigs.api.common.timer.scheduler.BiLateScheduler;
import ml.karmaconfigs.api.common.timer.scheduler.LateScheduler;
import ml.karmaconfigs.api.common.timer.scheduler.worker.AsyncBiLateScheduler;
import ml.karmaconfigs.api.common.timer.scheduler.worker.AsyncLateScheduler;
import ml.karmaconfigs.api.common.string.StringUtils;
import ml.karmaconfigs.api.common.utils.url.HttpUtil;
import ml.karmaconfigs.api.common.utils.url.URLUtils;
import org.kefirsf.bb.BBProcessorFactory;
import org.kefirsf.bb.TextProcessor;

import java.net.URL;

/**
 * Karma spigot checker
 */
public final class SpigotChecker {

    private final int resource_id;

    private final static String fetch_version = "https://api.spigotmc.org/simple/0.2/index.php?action=getResource&id={0}";
    private final static String fetch_update = "https://api.spigotmc.org/simple/0.2/index.php?action=getResourceUpdates&id={0}&page={1}";
    private final static String update_url = "https://www.spigotmc.org/resources/{0}/update?update={1}";

    /**
     * Initialize the checker
     *
     * @param id the spigot resource id
     */
    public SpigotChecker(final int id) {
        resource_id = id;
    }

    /**
     * Get the project latest version
     *
     * @return the project latest versions
     */
    public String getLatest() {
        try {
            URL url = new URL(StringUtils.formatString(fetch_version, resource_id));
            HttpUtil utils = URLUtils.extraUtils(url);

            if (utils != null) {
                String response = utils.getResponse();

                if (!StringUtils.isNullOrEmpty(response)) {
                    Gson gson = new GsonBuilder().create();
                    JsonElement element = gson.fromJson(response, JsonElement.class);

                    if (element.isJsonObject()) {
                        JsonObject object = element.getAsJsonObject();

                        if (object.has("current_version")) {
                            return object.get("current_version").getAsString();
                        }
                    }
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Fetch the project latest's update
     *
     * @return the project latest's update
     */
    public BiLateScheduler<URL, String> fetchLatest() {
        BiLateScheduler<URL, String> result = new AsyncBiLateScheduler<>();

        KarmaAPI.source(false).async().queue("plugin_version_check", () -> {
            try {
                URL version_url = new URL(StringUtils.formatString(fetch_version, resource_id));
                HttpUtil version_utils = URLUtils.extraUtils(version_url);

                if (version_utils != null) {
                    String version_response = version_utils.getResponse();

                    if (!StringUtils.isNullOrEmpty(version_response)) {
                        Gson gson = new GsonBuilder().create();
                        JsonElement element = gson.fromJson(version_response, JsonElement.class);

                        if (element.isJsonObject()) {
                            JsonObject version_object = element.getAsJsonObject();

                            if (version_object.has("stats")) {
                                int updates = version_object.getAsJsonObject("stats").get("updates").getAsInt();
                                int last_page = (int) Math.ceil((double) updates / 10);

                                URL url = new URL(StringUtils.formatString(fetch_update, resource_id, last_page));
                                HttpUtil utils = URLUtils.extraUtils(url);

                                String response = "[]";
                                if (utils != null) {
                                    response = utils.getResponse();
                                    if (response.equals("[]")) {
                                        last_page--;

                                        url = new URL(StringUtils.formatString(fetch_update, resource_id, last_page));
                                        utils = URLUtils.extraUtils(url);

                                        if (utils != null) {
                                            response = utils.getResponse();
                                        }
                                    }
                                }

                                if (!response.equals("403 - Connection refused") && !response.equals("[]")) {
                                    JsonArray array = gson.fromJson(response, JsonArray.class);

                                    JsonElement last = array.get(array.size() - 1);
                                    if (last.isJsonObject()) {
                                        JsonObject object = last.getAsJsonObject();
                                        if (object.has("id")) {
                                            int updateId = object.get("id").getAsInt();
                                            String update_entry = object.get("message").getAsString();

                                            TextProcessor processor = BBProcessorFactory.getInstance().create();
                                            String real_changelog = processor.process(update_entry)
                                                    .replace("<br/>", "\n")
                                                    .replace("<li>", "\t- ")
                                                    .replaceAll("<.*?>", "");

                                            URL updateInfo = new URL(StringUtils.formatString(update_url, resource_id, updateId));
                                            result.complete(updateInfo, removeBBCode(real_changelog));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Throwable ex) {
                result.complete(null, "", ex);
            } finally {
                result.complete(null, "");
            }
        });

        return result;
    }

    /**
     * Remove the bbcode from the string
     *
     * @param original the original string
     * @return the string without bbcode
     */
    private String removeBBCode(final String original) {
        StringBuilder builder = new StringBuilder();
        int reading = 0;
        for (int i = 0; i < original.length(); i++) {
            char character = original.charAt(i);
            if (character == '[') {
                reading++;
            }
            if (reading == 0) {
                builder.append(character);
            }

            if (reading > 0) {
                if (character == ']') {
                    reading--;
                }
            }
        }

        return builder.toString();
    }

    /**
     * Get the project update URL
     *
     * @return the project update URL
     * @deprecated replaced by {@link SpigotChecker#fetchLatest()}
     */
    @Deprecated
    public LateScheduler<URL> getUpdateURL() {
        LateScheduler<URL> result = new AsyncLateScheduler<>();

        KarmaAPI.source(false).async().queue("plugin_version_check", () -> {
            try {
                URL version_url = new URL(StringUtils.formatString(fetch_version, resource_id));
                HttpUtil version_utils = URLUtils.extraUtils(version_url);

                if (version_utils != null) {
                    String version_response = version_utils.getResponse();

                    if (!StringUtils.isNullOrEmpty(version_response)) {
                        Gson gson = new GsonBuilder().create();
                        JsonElement element = gson.fromJson(version_response, JsonElement.class);

                        if (element.isJsonObject()) {
                            JsonObject version_object = element.getAsJsonObject();

                            if (version_object.has("stats")) {
                                int updates = version_object.getAsJsonObject("stats").get("updates").getAsInt();
                                int last_page = (int) Math.ceil((double) updates / 10);

                                URL url = new URL(StringUtils.formatString(fetch_update, resource_id, last_page));
                                HttpUtil utils = URLUtils.extraUtils(url);

                                if (utils != null) {
                                    String response = utils.getResponse();
                                    if (!response.equals("403 - Connection refused")) {
                                        JsonArray array = gson.fromJson(response, JsonArray.class);

                                        JsonElement last = array.get(array.size() - 1);
                                        if (last.isJsonObject()) {
                                            JsonObject object = last.getAsJsonObject();
                                            if (object.has("id")) {
                                                int updateId = object.get("id").getAsInt();

                                                URL updateInfo = new URL(StringUtils.formatString(update_url, resource_id, updateId));
                                                result.complete(updateInfo, null);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Throwable ex) {
                result.complete(null, ex);
            } finally {
                result.complete(null);
            }
        });

        return result;
    }
}
