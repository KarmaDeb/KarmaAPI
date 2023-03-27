package ml.karmaconfigs.api.common.minecraft.api;

import com.google.gson.*;
import ml.karmaconfigs.api.common.karma.KarmaAPI;
import ml.karmaconfigs.api.common.karma.source.APISource;
import ml.karmaconfigs.api.common.karma.source.KarmaSource;
import ml.karmaconfigs.api.common.minecraft.api.response.OKAHeadRequest;
import ml.karmaconfigs.api.common.minecraft.api.response.OKAServerRequest;
import ml.karmaconfigs.api.common.minecraft.api.response.data.CapeData;
import ml.karmaconfigs.api.common.minecraft.api.response.MultiOKARequest;
import ml.karmaconfigs.api.common.minecraft.api.response.OKARequest;
import ml.karmaconfigs.api.common.minecraft.api.response.data.SkinData;
import ml.karmaconfigs.api.common.minecraft.api.response.data.SrvRecord;
import ml.karmaconfigs.api.common.string.StringUtils;
import ml.karmaconfigs.api.common.timer.scheduler.LateScheduler;
import ml.karmaconfigs.api.common.timer.scheduler.worker.AsyncLateScheduler;
import ml.karmaconfigs.api.common.utils.enums.Level;
import ml.karmaconfigs.api.common.utils.url.HttpUtil;
import ml.karmaconfigs.api.common.utils.url.URLUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Online KarmaAPI caller
 */
@SuppressWarnings("unused")
public final class MineAPI {

    static {
        KarmaAPI.install();
    }

    private static long threadIndex = 0;

    private final static KarmaSource source = APISource.getOriginal(false);
    private final static ScheduledThreadPoolExecutor service = new ScheduledThreadPoolExecutor(10, new ThreadFactory() {
        @Override
        public Thread newThread(final @NotNull Runnable r) {
            return new Thread(r, "mine_api_executor-" + threadIndex++);
        }
    });
    private final static Gson gson = new GsonBuilder().create();

    public final static int VERY_SMALL = 64;
    public final static int SMALL = 128;
    public final static int MEDIUM = 256;
    public final static int BIG = 512;
    public final static int DEFAULT = 1024;

    static {
        service.setKeepAliveTime(1, TimeUnit.SECONDS);
        service.setMaximumPoolSize(15);
    }

    /**
     * Try to push the nick data
     *
     * @param nick the nick
     * @return the response
     */
    public static LateScheduler<OKARequest> publish(final String nick) {
        if (service.getPoolSize() + 1 >= service.getMaximumPoolSize()) {
            service.setMaximumPoolSize(service.getMaximumPoolSize() + 5);
        } else {
            if (service.getPoolSize() + 5 < service.getMaximumPoolSize()) {
                service.setMaximumPoolSize(Math.max(service.getMaximumPoolSize() - 5, 15));
            }
        }

        LateScheduler<OKARequest> task = new AsyncLateScheduler<>();
        service.schedule(() -> {
            OKARequest request = runMethod("push", nick);
            task.complete(request);
        }, 0, TimeUnit.SECONDS);

        return task;
    }

    /**
     * Try to push the nick data
     * synchronously
     *
     * @param nick the nick
     * @return the response
     */
    public static OKARequest publishAndWait(final String nick) {
        return runMethod("push", nick);
    }

    /**
     * Fetch a user information
     *
     * @param nick the nick
     * @return the user information
     */
    public static LateScheduler<OKARequest> fetch(final String nick) {
        if (service.getPoolSize() + 1 >= service.getMaximumPoolSize()) {
            service.setMaximumPoolSize(service.getMaximumPoolSize() + 5);
        } else {
            if (service.getPoolSize() + 5 < service.getMaximumPoolSize()) {
                service.setMaximumPoolSize(Math.max(service.getMaximumPoolSize() - 5, 15));
            }
        }

        LateScheduler<OKARequest> task = new AsyncLateScheduler<>();
        service.schedule(() -> {
            OKARequest request = runMethod("fetch", nick);
            task.complete(request);
        }, 0, TimeUnit.SECONDS);

        return task;
    }

    /**
     * Fetch a user information
     * synchronously
     *
     * @param nick the nick
     * @return the response
     */
    public static OKARequest fetchAndWait(final String nick) {
        return runMethod("push", nick);
    }

    /**
     * Fetch a user information
     *
     * @param id the user unique id
     * @return the user information
     */
    public static LateScheduler<OKARequest> fetch(final UUID id) {
        if (service.getPoolSize() + 1 >= service.getMaximumPoolSize()) {
            service.setMaximumPoolSize(service.getMaximumPoolSize() + 5);
        } else {
            if (service.getPoolSize() + 5 < service.getMaximumPoolSize()) {
                service.setMaximumPoolSize(Math.max(service.getMaximumPoolSize() - 5, 15));
            }
        }

        LateScheduler<OKARequest> task = new AsyncLateScheduler<>();
        service.schedule(() -> {
            OKARequest request = runMethod("fetch", id.toString());
            task.complete(request);
        }, 0, TimeUnit.SECONDS);

        return task;
    }

    /**
     * Fetch a user information
     * synchronously
     *
     * @param id the user unique id
     * @return the response
     */
    public static OKARequest fetchAndWait(final UUID id) {
        return runMethod("fetch", id.toString());
    }

    /**
     * Fetch the head image of the client
     *
     * @param name the client name
     * @return the head image or null if none
     */
    public static LateScheduler<OKAHeadRequest> fetchHead(final String name) {
        if (service.getPoolSize() + 1 >= service.getMaximumPoolSize()) {
            service.setMaximumPoolSize(service.getMaximumPoolSize() + 5);
        } else {
            if (service.getPoolSize() + 5 < service.getMaximumPoolSize()) {
                service.setMaximumPoolSize(Math.max(service.getMaximumPoolSize() - 5, 15));
            }
        }

        LateScheduler<OKAHeadRequest> task = new AsyncLateScheduler<>();
        service.schedule(() -> {
            OKAHeadRequest request = fetchHeadAndWait(name, DEFAULT);
            task.complete(request);
        }, 0, TimeUnit.SECONDS);

        return task;
    }

    /**
     * Fetch the head image of the client
     *
     * @param name the client name
     * @return the head image or null if none
     */
    public static OKAHeadRequest fetchHeadAndWait(final String name) {
        return fetchHeadAndWait(name, DEFAULT);
    }

    /**
     * Fetch the head image of the client
     *
     * @param name the client name
     * @param request_size request_size the image size
     * @return the head image or null if none
     */
    public static LateScheduler<OKAHeadRequest> fetchHead(final String name, final int request_size) {
        if (service.getPoolSize() + 1 >= service.getMaximumPoolSize()) {
            service.setMaximumPoolSize(service.getMaximumPoolSize() + 5);
        } else {
            if (service.getPoolSize() + 5 < service.getMaximumPoolSize()) {
                service.setMaximumPoolSize(Math.max(service.getMaximumPoolSize() - 5, 15));
            }
        }

        LateScheduler<OKAHeadRequest> task = new AsyncLateScheduler<>();
        service.schedule(() -> {
            OKAHeadRequest request = fetchHeadAndWait(name, request_size);
            task.complete(request);
        }, 0, TimeUnit.SECONDS);

        return task;
    }
    
    /**
     * Fetch the head image of the client
     *
     * @param name the client name
     * @param request_size request_size the image size
     * @return the head image or null if none
     */
    public static OKAHeadRequest fetchHeadAndWait(final String name, final int request_size) {
        URL url = URLUtils.getOrBackup(
                "https://api.karmadev.es/head/" + name + "/" + request_size + "/",
                "https://backup.karmadev.es/api/head/" + name + "/" + request_size + "/");

        if (url == null) {
            return OKAHeadRequest.empty();
        }

        URL modified = URLUtils.append(url, "?display=json");
        HttpUtil extra = URLUtils.extraUtils(modified);
        if (extra == null) {
            return OKAHeadRequest.empty();
        }

        String okaResponse = extra.getResponse();
        try {
            JsonObject object = gson.fromJson(okaResponse, JsonObject.class);
            if (object.has("message") && object.has("code")) {
                String message = object.get("message").getAsString();
                String code = object.get("code").getAsString();

                if (!code.equals("ERR_TOO_MANY_REQUESTS")) {
                    source.console().send("Failed to execute request {0} ({1} - {2})", Level.GRAVE, url, message, code);
                }

                return OKAHeadRequest.empty(okaResponse);
            }

            long id = object.get("id").getAsLong();
            int size = object.get("size").getAsInt();
            String rawUrl = object.get("url").getAsString();
            String value = object.getAsJsonObject("texture").get("value").getAsString();
            String signature = object.getAsJsonObject("texture").get("signature").getAsString();
            String head = object.get("image").getAsString();

            URL skinURL = new URL(rawUrl);
            return OKAHeadRequest.builder()
                    .uri(url.toURI())
                    .id(id)
                    .size(size)
                    .texture(SkinData.of(skinURL, value, signature, null))
                    .head(head)
                    .json(okaResponse).build();
        } catch (JsonSyntaxException | MalformedURLException | URISyntaxException ex) {
            source.logger().scheduleLog(Level.GRAVE, ex);
            source.logger().scheduleLog(Level.INFO, "Failed to fetch minecraft head from web API");
        }

        return OKAHeadRequest.empty();
    }

    /**
     * Fetch the server information
     *
     * @param address the server address
     * @param port the server port
     */
    public static LateScheduler<OKAServerRequest> fetchServer(final String address, final int port) {
        if (service.getPoolSize() + 1 >= service.getMaximumPoolSize()) {
            service.setMaximumPoolSize(service.getMaximumPoolSize() + 5);
        } else {
            if (service.getPoolSize() + 5 < service.getMaximumPoolSize()) {
                service.setMaximumPoolSize(Math.max(service.getMaximumPoolSize() - 5, 15));
            }
        }

        LateScheduler<OKAServerRequest> task = new AsyncLateScheduler<>();
        service.schedule(() -> {
            OKAServerRequest request = fetchServerAndWait(address, port);
            task.complete(request);
        }, 0, TimeUnit.SECONDS);

        return task;
    }

    /**
     * Fetch the server information
     *
     * @param address the server address
     */
    public static LateScheduler<OKAServerRequest> fetchServer(final String address) {
        if (service.getPoolSize() + 1 >= service.getMaximumPoolSize()) {
            service.setMaximumPoolSize(service.getMaximumPoolSize() + 5);
        } else {
            if (service.getPoolSize() + 5 < service.getMaximumPoolSize()) {
                service.setMaximumPoolSize(Math.max(service.getMaximumPoolSize() - 5, 15));
            }
        }

        LateScheduler<OKAServerRequest> task = new AsyncLateScheduler<>();
        service.schedule(() -> {
            OKAServerRequest request = fetchServerAndWait(address, 25565);
            task.complete(request);
        }, 0, TimeUnit.SECONDS);

        return task;
    }

    /**
     * Fetch the server information
     *
     * @param address the server address
     * @param port the server port
     */
    public static OKAServerRequest fetchServerAndWait(final String address, final int port) {
        URL url = URLUtils.getOrBackup(
                "https://api.karmadev.es/?address=" + address + "&port=" + port + "&display=json",
                "https://backup.karmadev.es/api/?address=" + address + "&port=" + port + "&display=json");

        if (url == null) {
            return OKAServerRequest.empty(address, port);
        }

        URL modified = URLUtils.append(url, "?display=json");
        HttpUtil extra = URLUtils.extraUtils(modified);
        if (extra == null) {
            return OKAServerRequest.empty(address, port);
        }

        String okaResponse = extra.getResponse();
        try {
            JsonObject object = gson.fromJson(okaResponse, JsonObject.class);
            if (object.has("message") && object.has("code")) {
                String message = object.get("message").getAsString();
                String code = object.get("code").getAsString();

                if (!code.equals("ERR_TOO_MANY_REQUESTS")) {
                    source.console().send("Failed to execute request {0} ({1} - {2})", Level.GRAVE, url, message, code);
                }

                return OKAServerRequest.empty(address, port, okaResponse);
            }

            long cache = object.get("cache").getAsLong();
            Instant instant = Instant.ofEpochMilli(cache);
            String platform = object.getAsJsonObject("version").get("platform").getAsString();
            int protocol = object.getAsJsonObject("version").get("protocol").getAsInt();
            int players = object.getAsJsonObject("players").get("online").getAsInt();
            int maxPlayers = object.getAsJsonObject("players").get("max").getAsInt();
            String[] motd = new String[2];
            JsonArray array = object.get("motd").getAsJsonArray();
            int index = 0;
            for (JsonElement element : array) {
                if (index > 1) {
                    break;
                }

                motd[index++] = element.getAsString();
            }
            String icon = object.get("icon").getAsString();
            String srvHost = object.getAsJsonObject("srv").get("host").getAsString();
            int srvPort = object.getAsJsonObject("srv").get("port").getAsInt();
            long latency = object.getAsJsonObject("srv").get("latency").getAsLong();

            return OKAServerRequest.builder()
                    .uri(url.toURI())
                    .cache(instant)
                    .platform(platform)
                    .protocol(protocol)
                    .onlinePlayers(players)
                    .maxPlayers(maxPlayers)
                    .motd(motd)
                    .icon(icon)
                    .address(InetSocketAddress.createUnresolved(address, port))
                    .srv(SrvRecord.of(srvHost, srvPort, latency))
                    .json(okaResponse).build();
        } catch (JsonSyntaxException | URISyntaxException ex) {
            source.logger().scheduleLog(Level.GRAVE, ex);
            source.logger().scheduleLog(Level.INFO, "Failed to fetch minecraft server from web API");
        }

        return OKAServerRequest.empty(address, port);
    }

    /**
     * Get the API size
     *
     * @return the API size
     */
    public static Long sizeAndWait() {
        URL url = URLUtils.getOrBackup(
                "https://api.karmadev.es/fetch/@all",
                "https://backup.karmadev.es/api/fetch/@all");

        if (url == null) {
            return -1L;
        }

        HttpUtil extra = URLUtils.extraUtils(url);
        if (extra == null) {
            return -1L;
        }

        String okaResponse = extra.getResponse();
        try {
            JsonObject object = gson.fromJson(okaResponse, JsonObject.class);
            if (object.has("message") && object.has("code")) {
                String message = object.get("message").getAsString();
                String code = object.get("code").getAsString();

                if (!code.equals("ERR_TOO_MANY_REQUESTS")) {
                    source.console().send("Failed to execute request {0} ({1} - {2})", Level.GRAVE, url, message, code);
                }
                return -1L;
            }

            return object.get("stored").getAsLong();
        } catch (JsonSyntaxException ex) {
            source.logger().scheduleLog(Level.GRAVE, ex);
            source.logger().scheduleLog(Level.INFO, "Failed to fetch minecraft data from web API");
        }

        return -1L;
    }

    /**
     * Get the API size
     *
     * @return the API size
     */
    public static LateScheduler<Long> size() {
        if (service.getPoolSize() + 1 >= service.getMaximumPoolSize()) {
            service.setMaximumPoolSize(service.getMaximumPoolSize() + 5);
        } else {
            if (service.getPoolSize() + 5 < service.getMaximumPoolSize()) {
                service.setMaximumPoolSize(Math.max(service.getMaximumPoolSize() - 5, 15));
            }
        }

        LateScheduler<Long> task = new AsyncLateScheduler<>();
        service.schedule(() -> {
            long amount = sizeAndWait();
            task.complete(amount);
        }, 0, TimeUnit.SECONDS);

        return task;
    }

    /**
     * Get the API max pages
     *
     * @return the API pages
     */
    public static Long pagesAndWait() {
        URL url = URLUtils.getOrBackup(
                "https://api.karmadev.es/fetch/@all",
                "https://backup.karmadev.es/api/fetch/@all");

        if (url == null) {
            return -1L;
        }

        HttpUtil extra = URLUtils.extraUtils(url);
        if (extra == null) {
            return -1L;
        }

        String okaResponse = extra.getResponse();
        try {
            JsonObject object = gson.fromJson(okaResponse, JsonObject.class);
            if (object.has("message") && object.has("code")) {
                String message = object.get("message").getAsString();
                String code = object.get("code").getAsString();

                if (!code.equals("ERR_TOO_MANY_REQUESTS")) {
                    source.console().send("Failed to execute request {0} ({1} - {2})", Level.GRAVE, url, message, code);
                }

                return -1L;
            }

            return object.get("pages").getAsLong();
        } catch (JsonSyntaxException ex) {
            source.logger().scheduleLog(Level.GRAVE, ex);
            source.logger().scheduleLog(Level.INFO, "Failed to fetch minecraft data from web API");
        }

        return -1L;
    }

    /**
     * Get the API max pages
     *
     * @return the API pages
     */
    public static LateScheduler<Long> pages() {
        if (service.getPoolSize() + 1 >= service.getMaximumPoolSize()) {
            service.setMaximumPoolSize(service.getMaximumPoolSize() + 5);
        } else {
            if (service.getPoolSize() + 5 < service.getMaximumPoolSize()) {
                service.setMaximumPoolSize(Math.max(service.getMaximumPoolSize() - 5, 15));
            }
        }

        LateScheduler<Long> task = new AsyncLateScheduler<>();
        service.schedule(() -> {
            long amount = pagesAndWait();
            task.complete(amount);
        }, 0, TimeUnit.SECONDS);

        return task;
    }

    /**
     * Get all the information
     *
     * @param page the information page
     * @return the information
     */
    public static MultiOKARequest fetchAllAndWait(final long page) {
        URL url = URLUtils.getOrBackup(
                "https://api.karmadev.es/fetch/@all",
                "https://backup.karmadev.es/api/fetch/@all");

        if (url == null) {
            return MultiOKARequest.empty();
        }

        HttpUtil extra = URLUtils.extraUtils(url);
        if (extra == null) {
            return MultiOKARequest.empty();
        }

        String okaResponse = extra.getResponse();
        try {
            JsonObject multiObject = gson.fromJson(okaResponse, JsonObject.class);
            if (multiObject.has("message") && multiObject.has("code")) {
                String message = multiObject.get("message").getAsString();
                String code = multiObject.get("code").getAsString();

                if (!code.equals("ERR_TOO_MANY_REQUESTS")) {
                    source.console().send("Failed to execute request {0} ({1} - {2})", Level.GRAVE, url, message, code);
                }
                return MultiOKARequest.empty();
            }

            long stored = multiObject.get("stored").getAsLong();
            long pages = multiObject.get("pages").getAsLong();

            List<OKARequest> accounts = new ArrayList<>();
            JsonObject fetched = multiObject.getAsJsonObject("fetched");
            String host = URLUtils.getDomainName(url);

            for (String nick : fetched.keySet()) {
                try {
                    URL virtualID = new URL("https://" + host + "/api/fetch/" + nick);
                    JsonObject object = fetched.get(nick).getAsJsonObject();
                    object.addProperty("name", nick);

                    String raw = gson.toJson(object);

                    OKARequest request = build(virtualID, raw, object);
                    accounts.add(request);
                } catch (URISyntaxException | MalformedURLException ignored) {}
            }

            return MultiOKARequest.builder()
                    .stored(stored)
                    .page(Math.max(1, page))
                    .pages(pages)
                    .fetched(accounts.size())
                    .accounts(accounts.toArray(new OKARequest[0])).build();
        } catch (JsonSyntaxException ex) {
            source.logger().scheduleLog(Level.GRAVE, ex);
            source.logger().scheduleLog(Level.INFO, "Failed to fetch minecraft data from web API");
        }

        return MultiOKARequest.empty();
    }

    /**
     * Get all the information
     *
     * @param page the information page
     * @return the information
     */
    public static LateScheduler<MultiOKARequest> fetchAll(final long page) {
        if (service.getPoolSize() + 1 >= service.getMaximumPoolSize()) {
            service.setMaximumPoolSize(service.getMaximumPoolSize() + 5);
        } else {
            if (service.getPoolSize() + 5 < service.getMaximumPoolSize()) {
                service.setMaximumPoolSize(Math.max(service.getMaximumPoolSize() - 5, 15));
            }
        }

        LateScheduler<MultiOKARequest> task = new AsyncLateScheduler<>();
        service.schedule(() -> {
            MultiOKARequest request = fetchAllAndWait(page);
            task.complete(request);
        }, 0, TimeUnit.SECONDS);

        return task;
    }

    /**
     * Get a UUID from a trimmed UUID
     *
     * @param id the trimmed UUID
     * @return the full UUID
     */
    @Nullable
    public static UUID fromTrimmed(final String id) {
        UUID result = null;
        try {
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
            }
        } catch (Throwable ignored) {}

        return result;
    }

    private static OKARequest runMethod(final String method, final String argument) {
        if (method.equalsIgnoreCase("push")) {
            UUID id = fromTrimmed(argument);
            if (id != null) {
                return OKARequest.empty();
            }
        }

        URL url = URLUtils.getOrBackup(
                "https://api.karmadev.es/" + method + "/" + argument,
                "https://backup.karmadev.es/api/" + method + "/" + argument);

        if (url == null) {
            source.logger().scheduleLog(Level.GRAVE, "Failed to execute {0} on {1} because URL was null", method, argument);
            return OKARequest.empty();
        }

        HttpUtil extra = URLUtils.extraUtils(url);
        if (extra == null) {
            source.logger().scheduleLog(Level.GRAVE, "Failed to execute {0} on {1} because extra URL utilities couldn't be created", method, argument);
            return OKARequest.empty();
        }

        String okaResponse = extra.getResponse();
        try {
            JsonObject object = gson.fromJson(okaResponse, JsonObject.class);
            return build(url, okaResponse, object);
        } catch (JsonSyntaxException | URISyntaxException ex) {
            source.logger().scheduleLog(Level.GRAVE, ex);
            source.logger().scheduleLog(Level.INFO, "Failed to push minecraft data into web API");
        }

        return OKARequest.empty(okaResponse);
    }

    private static OKARequest build(final URL url, final String raw, final JsonObject object) throws URISyntaxException {
        if (object.has("message") && object.has("code")) {
            String message = object.get("message").getAsString();
            String code = object.get("code").getAsString();

            if (!code.equals("ERR_TOO_MANY_REQUESTS")) {
                source.console().send("Failed to execute request {0} ({1} - {2})", Level.GRAVE, url, message, code);
            }
            return OKARequest.empty(raw);
        }

        long id = object.get("id").getAsLong();
        String genName = object.get("name").getAsString();
        UUID nameId = fromTrimmed(genName);
        if (nameId != null) {
            return OKARequest.empty(raw);
        }

        String creation = object.get("created").getAsString();

        String offline_uuid = object.get("offline")
                .getAsJsonArray()
                .get(0)
                .getAsJsonObject()
                .getAsJsonObject("data")
                .get("id").getAsString();
        String online_uuid = object.get("online")
                .getAsJsonArray()
                .get(0)
                .getAsJsonObject()
                .getAsJsonObject("data")
                .get("id").getAsString();
        SkinData skin = SkinData.empty();
        CapeData cape = CapeData.empty();

        JsonArray properties = object.getAsJsonArray("properties");
        for (JsonElement element : properties) {
            JsonObject subElement = element.getAsJsonObject();
            String name = subElement.get("name").getAsString();

            if (name.equalsIgnoreCase("skin")) {
                if (subElement.has("url") && subElement.has("data") && subElement.has("value") && subElement.has("signature")) {
                    try {
                        URL skinURL = new URL(subElement.get("url").getAsString());
                        String data = subElement.get("data").getAsString();
                        String value = subElement.get("value").getAsString();
                        String signature = subElement.get("signature").getAsString();

                        skin = SkinData.of(skinURL, data, value, signature);
                    } catch (MalformedURLException ignored) {}
                }
            } else {
                if (subElement.has("url") && subElement.has("data")) {
                    try {
                        URL capeURL = new URL(subElement.get("url").getAsString());
                        String data = subElement.get("data").getAsString();

                        cape = CapeData.of(capeURL, data);
                    } catch (MalformedURLException ignored) {}
                }
            }
        }

        UUID offline = null;
        UUID online = null;
        try {
            offline = UUID.fromString(offline_uuid);
            if (!online_uuid.equalsIgnoreCase("unknown")) {
                online = UUID.fromString(online_uuid);
            }
        } catch (IllegalArgumentException ignored) {}

        return OKARequest.builder()
                .uri(url.toURI())
                .id(id)
                .nick(genName)
                .creation(creation)
                .offline(offline)
                .online(online)
                .skin(skin)
                .cape(cape)
                .json(raw)
                .build();
    }
}
