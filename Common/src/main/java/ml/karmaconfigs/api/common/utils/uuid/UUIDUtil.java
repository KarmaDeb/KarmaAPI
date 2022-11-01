package ml.karmaconfigs.api.common.utils.uuid;

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
import ml.karmaconfigs.api.common.annotations.Unstable;
import ml.karmaconfigs.api.common.karma.Identifiable;
import ml.karmaconfigs.api.common.karma.KarmaAPI;
import ml.karmaconfigs.api.common.karma.KarmaSource;
import ml.karmaconfigs.api.common.timer.scheduler.LateScheduler;
import ml.karmaconfigs.api.common.timer.scheduler.worker.AsyncLateScheduler;
import ml.karmaconfigs.api.common.utils.enums.Level;
import ml.karmaconfigs.api.common.utils.string.StringUtils;
import ml.karmaconfigs.api.common.utils.url.HttpUtil;
import ml.karmaconfigs.api.common.utils.url.Post;
import ml.karmaconfigs.api.common.utils.url.URLUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Karma UUID fetcher
 */
public final class UUIDUtil {

    static {
        KarmaAPI.install();
    }

    /**
     * Register a minecraft client into the karma UUID
     * engine database API
     *
     * @param identifiable an identifiable source
     * @param name         the client name
     */
    public static void registerMinecraftClient(final Identifiable identifiable, final String name) {
        URL first = URLUtils.getOrBackup("https://karmadev.es/?nick=" + name,
                "https://karmaconfigs.ml/api/?nick=" + name,
                "https://karmarepo.ml/api/?nick=" + name,
                "https://backup.karmadev.es/?nick=" + name,
                "https://backup.karmaconfigs.ml/api/?nick=" + name,
                "https://backup.karmarepo.ml/api/?nick=" + name);

        if (first != null) {
            int response_code = URLUtils.getResponseCode(first.toString());

            if (response_code == HTTP_OK) {
                HttpUtil utils = URLUtils.extraUtils(first);

                if (utils != null) {
                    utils.push(Post.newPost().add("identifier", identifiable.getIdentifier()));
                }
            }
        }
    }

    /**
     * Fetch minecraft UUID
     *
     * @param name the minecraft name
     * @param type the uuid type
     * @return the minecraft uuid
     */
    public static UUID fetch(final String name, final UUIDType type) {
        switch (type) {
            case ONLINE:
                try {
                    URL url = URLUtils.getOrBackup(
                            "https://karmadev.es/api/?nick=" + name,
                            "https://karmaconfigs.ml/api/?nick=" + name,
                            "https://karmarepo.ml/api/?nick=" + name,
                            "https://backup.karmadev.es/api/?nick=" + name,
                            "https://backup.karmaconfigs.ml/api/?nick=" + name,
                            "https://backup.karmarepo.ml/api/?nick=" + name);

                    if (url != null) {
                        String result = null;

                        InputStream response = url.openStream();
                        InputStreamReader responseReader = new InputStreamReader(response, StandardCharsets.UTF_8);
                        BufferedReader reader = new BufferedReader(responseReader);

                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        JsonObject json = gson.fromJson(reader, JsonObject.class);

                        if (json.has("online")) {
                            JsonArray online = json.getAsJsonArray("online");
                            for (JsonElement sub : online) {
                                if (sub.isJsonObject()) {
                                    JsonObject data = sub.getAsJsonObject();
                                    if (data.has("data")) {
                                        JsonElement onlineData = data.get("data");
                                        if (onlineData.isJsonObject()) {
                                            data = onlineData.getAsJsonObject();

                                            if (data.has("short")) {
                                                result = data.get("short").getAsString();
                                                break;
                                            }
                                        }
                                    }
                                }
                            }

                            if (StringUtils.isNullOrEmpty(result)) {
                                JsonArray offline = json.getAsJsonArray("offline");
                                for (JsonElement sub : offline) {
                                    if (sub.isJsonObject()) {
                                        JsonObject data = sub.getAsJsonObject();
                                        if (data.has("data")) {
                                            JsonElement offlineData = data.get("data");
                                            if (offlineData.isJsonObject()) {
                                                data = offlineData.getAsJsonObject();

                                                if (data.has("short")) {
                                                    result = data.get("short").getAsString();
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        reader.close();
                        responseReader.close();
                        response.close();

                        if (result != null && result.equalsIgnoreCase("Playernotfound!"))
                            result = null;

                        return fromTrimmed(result);
                    }
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }

                return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes());
            case OFFLINE:
            default:
                return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes());
        }
    }

    /**
     * Fetch the client nick
     *
     * @param uuid the UUID to search for
     * @return the nick or null if not available
     * in karma UUID engine database API
     */
    public static String fetchNick(final UUID uuid) {
        String result = null;
        try {
            URL url = URLUtils.getOrBackup(
                    "https://karmadev.es/api/?fetch=" + uuid,
                    "https://karmaconfigs.ml/api/?fetch=" + uuid,
                    "https://karmarepo.ml/api/?fetch=" + uuid,
                    "https://backup.karmadev.es/api/?fetch=" + uuid,
                    "https://backup.karmaconfigs.ml/api/?fetch=" + uuid,
                    "https://backup.karmarepo.ml/api/?fetch=" + uuid);

            if (url != null) {
                HttpUtil utils = URLUtils.extraUtils(url);
                if (utils != null) {
                    String response = utils.getResponse();

                    if (!StringUtils.isNullOrEmpty(response)) {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        JsonObject json = gson.fromJson(response, JsonObject.class);

                        //For UUID cases, it's impossible to give more than 2 results
                        if (json.has("name")) {
                            JsonElement element = json.get("name");
                            if (element.isJsonPrimitive()) {
                                JsonPrimitive primitive = element.getAsJsonPrimitive();
                                if (primitive.isString()) {
                                    result = primitive.getAsString();

                                    //There's a player named "Unknown", the error query is unknown, not Unknown, so we must make sure the result is unknown and not Unknown
                                    if (result.equals("unknown"))
                                        result = null;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        return result;
    }

    /**
     * Generate an online karma response for the specified name
     *
     * @param name the name
     * @return the name OKA response
     */
    @Nullable
    public static OKAResponse fetchOKA(final String name) {
        OKAResponse result = null;

        if (!name.equalsIgnoreCase("@all")) {
            try {
                URL url = URLUtils.getOrBackup(
                        "https://karmadev.es/api/?fetch=" + name,
                        "https://karmaconfigs.ml/api/?fetch=" + name,
                        "https://karmarepo.ml/api/?fetch=" + name,
                        "https://backup.karmadev.es/api/?fetch=" + name,
                        "https://backup.karmaconfigs.ml/api/?fetch=" + name,
                        "https://backup.karmarepo.ml/api/?fetch=" + name);

                if (url != null) {
                    HttpUtil utils = URLUtils.extraUtils(url);
                    if (utils != null) {
                        String response = utils.getResponse();

                        if (!StringUtils.isNullOrEmpty(response)) {
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            JsonObject json = gson.fromJson(response, JsonObject.class);

                            //More than 1 result
                            if (json.has("stored")) {
                                JsonArray data = json.getAsJsonArray("fetched");
                                for (JsonElement element : data) {
                                    if (element.isJsonObject()) {
                                        JsonObject object = element.getAsJsonObject();

                                        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                                            String nick = entry.getKey();
                                            UUID off = null;
                                            UUID on = null;

                                            JsonObject info = entry.getValue().getAsJsonObject();

                                            JsonArray offline = info.getAsJsonArray("offline");
                                            JsonArray online = info.getAsJsonArray("online");

                                            JsonObject offlineData = offline.get(0).getAsJsonObject().get("data").getAsJsonObject();
                                            JsonObject onlineData = online.get(0).getAsJsonObject().get("data").getAsJsonObject();

                                            try {
                                                off = UUID.fromString(offlineData.get("id").getAsString());
                                            } catch (Throwable ignored) {
                                            }
                                            try {
                                                on = UUID.fromString(onlineData.get("id").getAsString());
                                            } catch (Throwable ignored) {
                                            }

                                            if (nick.equals(name)) {
                                                result = new OKAResponse(name, off, on);
                                                break;
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (json.has("name")) {
                                    String nick = json.get("name").getAsString();
                                    UUID off = null;
                                    UUID on = null;

                                    JsonArray offline = json.get("offline").getAsJsonArray();
                                    JsonArray online = json.get("online").getAsJsonArray();

                                    JsonObject offlineData = offline.get(0).getAsJsonObject().get("data").getAsJsonObject();
                                    JsonObject onlineData = online.get(0).getAsJsonObject().get("data").getAsJsonObject();
                                    try {
                                        off = UUID.fromString(offlineData.get("id").getAsString());
                                    } catch (Throwable ignored) {
                                    }
                                    try {
                                        on = UUID.fromString(onlineData.get("id").getAsString());
                                    } catch (Throwable ignored) {
                                    }

                                    result = new OKAResponse(nick, off, on);
                                }
                            }
                        }
                    }
                }
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }

        return result;
    }

    /**
     * Generate an online karma response for the specified uuid
     *
     * @param id the uuid
     * @return the name OKA response
     */
    @Nullable
    public static OKAResponse fetchOKAID(final UUID id) {
        return fetchOKA(id.toString().replace("-", ""));
    }

    /**
     * Get the stored users amount
     *
     * @return the stored users amount in the OKA database
     */
    public static LateScheduler<Integer> getStored() {
        LateScheduler<Integer> result = new AsyncLateScheduler<>();
        KarmaSource api = KarmaAPI.source(false);

        api.console().debug("Fetching number of registered accounts", Level.INFO);
        api.async().queue("oka_fetch_accounts", () -> {
            api.console().debug("Started task oka_fetch_accounts", Level.INFO);

            int stored = -1;
            Throwable error = null;

            try {
                URL url = URLUtils.getOrBackup(
                        "https://karmadev.es/api/?fetch=@size",
                        "https://karmaconfigs.ml/api/?fetch=@size",
                        "https://karmarepo.ml/api/?fetch=@size",
                        "https://backup.karmadev.es/api/?fetch=@size",
                        "https://backup.karmaconfigs.ml/api/?fetch=@size",
                        "https://backup.karmarepo.ml/api/?fetch=@size");

                if (url != null) {
                    api.console().debug("Using URL: {0}", Level.INFO, url);

                    HttpUtil utils = URLUtils.extraUtils(url);
                    if (utils != null) {
                        api.console().debug("HTTP utilities validated at {0}", Level.INFO, url);
                        String response = utils.getResponse();

                        if (!StringUtils.isNullOrEmpty(response)) {
                            api.console().debug("Response is valid at {0}", Level.INFO, url);

                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            JsonObject json = gson.fromJson(response, JsonObject.class);

                            if (json.has("stored")) {
                                api.console().debug("Stored element found at {0}", Level.INFO, url);

                                JsonElement element = json.get("stored");
                                if (element.isJsonPrimitive()) {
                                    api.console().debug("Stored is primitive at {0}", Level.INFO, url);

                                    JsonPrimitive primitive = element.getAsJsonPrimitive();
                                    if (primitive.isNumber()) {
                                        api.console().debug("Stored is number at {0}", Level.INFO, url);

                                        stored = primitive.getAsNumber().intValue();

                                        api.console().debug("Stored accounts: {0}", Level.INFO, stored);
                                    } else {
                                        api.console().debug("Stored element was not a number at {0}", Level.GRAVE, url);
                                    }
                                } else {
                                    api.console().debug("Stored element was not primitive at {0}", Level.GRAVE, url);
                                }
                            } else {
                                api.console().debug("Stored element not found at {0}", Level.GRAVE, url);
                            }
                        } else {
                            api.console().debug("API response was null or empty at {0}!", Level.GRAVE, url);
                        }
                    } else {
                        api.console().debug("HTTP utilities returned null for {0}", Level.GRAVE, url);
                    }
                } else {
                    api.console().debug("Failed to fetch URL", Level.GRAVE);
                }
            } catch (Throwable ex) {
                ex.printStackTrace();
                api.console().debug("An error occurred ({0})", Level.GRAVE, ex.fillInStackTrace());
                error = ex;
            }

            result.complete(stored, error);
            api.console().debug("Completing oka_fetch_accounts", Level.WARNING);
        });

        return result;
    }

    /**
     * Fetch all stored users in the OKA database
     *
     * @return all the stored users
     * @deprecated This method will be removed as fetching all accounts is currently
     * inviable by the API
     */
    @Deprecated
    @Unstable(reason = "This does not work as currently, fetching all accounts results in a very long wait for the API.")
    public static @ApiStatus.ScheduledForRemoval LateScheduler<Set<OKAResponse>> fetchAll(final int page) {
        LateScheduler<Set<OKAResponse>> result = new AsyncLateScheduler<>();

        KarmaAPI.source(false).async().queue("oka_fetch_clients", () -> {
            Set<OKAResponse> okaData = new HashSet<>();

            try {
                URL url = URLUtils.getOrBackup(
                        "https://karmadev.es/api/?fetch=@all&page=" + page,
                        "https://karmaconfigs.ml/api/?fetch=@all&page=" + page,
                        "https://karmarepo.ml/api/?fetch=@all&page=" + page,
                        "https://backup.karmadev.es/api/?fetch=@all&page=" + page,
                        "https://backup.karmaconfigs.ml/api/?fetch=@all&page=" + page,
                        "https://backup.karmarepo.ml/api/?fetch=@all&page=" + page);

                if (url != null) {
                    HttpUtil utils = URLUtils.extraUtils(url);
                    if (utils != null) {
                        String response = utils.getResponse();

                        if (!StringUtils.isNullOrEmpty(response)) {
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            JsonObject json = gson.fromJson(response, JsonObject.class);

                            if (json.has("fetched")) {
                                JsonElement element = json.get("fetched");
                                if (element.isJsonArray()) {
                                    JsonArray array = element.getAsJsonArray();

                                    for (JsonElement data : array) {
                                        if (data.isJsonObject()) {
                                            JsonObject obj = data.getAsJsonObject();

                                            obj.entrySet().forEach((account) -> {
                                                String nick = account.getKey();
                                                UUID off = null;
                                                UUID on = null;

                                                JsonObject info = account.getValue().getAsJsonObject();

                                                JsonArray offline = info.getAsJsonArray("offline");
                                                JsonArray online = info.getAsJsonArray("online");

                                                JsonObject offlineData = offline.get(0).getAsJsonObject().get("data").getAsJsonObject();
                                                JsonObject onlineData = online.get(0).getAsJsonObject().get("data").getAsJsonObject();

                                                try {
                                                    off = UUID.fromString(offlineData.get("id").getAsString());
                                                } catch (Throwable ignored) {
                                                }
                                                try {
                                                    on = UUID.fromString(onlineData.get("id").getAsString());
                                                } catch (Throwable ignored) {
                                                }

                                                OKAResponse tmp = new OKAResponse(nick, off, on);
                                                okaData.add(tmp);
                                            });
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Throwable ignored) {
            }

            result.complete(okaData);
        });

        return result;
    }

    /**
     * Get a UUID from a trimmed UUID
     *
     * @param id the trimmed UUID
     * @return the full UUID
     */
    @Nullable
    public static UUID fromTrimmed(final String id) {
        UUID result;
        if (!StringUtils.isNullOrEmpty(id)) {
            if (!id.contains("-")) {
                StringBuilder builder = new StringBuilder(id);
                builder.insert(20, "-");
                builder.insert(16, "-");
                builder.insert(12, "-");
                builder.insert(8, "-");
                result = UUID.fromString(builder.toString());
            } else {
                result = UUID.fromString(id);
            }
        } else {
            result = null;
        }

        return result;
    }
}
