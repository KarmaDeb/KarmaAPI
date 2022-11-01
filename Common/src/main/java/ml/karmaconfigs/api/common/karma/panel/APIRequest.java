package ml.karmaconfigs.api.common.karma.panel;

import com.google.gson.*;
import ml.karmaconfigs.api.common.karma.KarmaSource;
import ml.karmaconfigs.api.common.karma.panel.impl.panel.ApiPost;
import ml.karmaconfigs.api.common.karma.panel.impl.profile.ApiProfile;
import ml.karmaconfigs.api.common.karma.panel.wrapper.ProfileWrapper;
import ml.karmaconfigs.api.common.karma.panel.wrapper.PublicProfileWrapper;
import ml.karmaconfigs.api.common.karma.panel.wrapper.post.PostPageWrapper;
import ml.karmaconfigs.api.common.karma.panel.wrapper.post.PostWrapper;
import ml.karmaconfigs.api.common.karma.panel.wrapper.post.filter.Filterable;
import ml.karmaconfigs.api.common.karma.panel.wrapper.post.meta.PostTopicContainer;
import ml.karmaconfigs.api.common.timer.scheduler.LateScheduler;
import ml.karmaconfigs.api.common.timer.scheduler.worker.AsyncLateScheduler;
import ml.karmaconfigs.api.common.utils.url.HttpUtil;
import ml.karmaconfigs.api.common.utils.url.Post;
import ml.karmaconfigs.api.common.utils.url.URLUtils;
import ml.karmaconfigs.api.common.utils.uuid.UUIDType;
import org.apache.http.message.BasicHeader;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class APIRequest {

    private final static String[] hosts = {
            "https://karmadev.es/2k22panel/api/",
            "https://karmaconfigs.ml/2k22panel/api/",
            "https://karmarepo.ml/2k22panel/api/",
            "https://backup.karmadev.es/2k22panel/api/",
            "https://backup.karmaconfigs.ml/2k22panel/api/",
            "https://backup.karmarepo.ml/2k22panel/api/"
    };

    private final HeaderURL working_url;

    private final KarmaSource source;

    private static PublicProfileWrapper current_client;

    /**
     * Initialize the API request
     *
     * @param src the source that will run requests
     * @param key the API key
     */
    public APIRequest(final KarmaSource src, final String key) {
        working_url = new HeaderURL(URLUtils.getOrBackup(hosts), new BasicHeader("access-key", key));

        source = src;
    }

    /**
     * Update the current profile
     * <p>
     * PLEASE NOTE: If you make a custom API executor, you should
     * always call to this method after you create or fetch a profile
     * in order to keep a correct execution of the KarmaAPI.
     *
     * @param profile the current profile
     */
    public void updateCurrentProfile(final PublicProfileWrapper profile) {
        current_client = profile;
    }

    /**
     * Create a new profile
     *
     * @param name  the profile name
     * @param email the profile email
     * @return a new profile
     */
    public ProfileWrapper createProfile(final String name, final String email) {
        ProfileWrapper wrapper = new ApiProfile(source, working_url, name, email);
        current_client = wrapper.asPublicProfile();

        return wrapper;
    }

    /**
     * Fetch a profile by its user ID
     *
     * @param userId the user ID to search for
     * @return the basic user info
     */
    public PublicProfileWrapper fetchProfile(final int userId) {
        return null;
    }

    /**
     * Search for posts
     *
     * @param filter the post filter
     * @return all the posts
     */
    public LateScheduler<PostPageWrapper> searchPost(final Filterable<PostTopicContainer> filter) {
        LateScheduler<PostPageWrapper> result = new AsyncLateScheduler<>();

        return result;
    }

    /**
     * Load a post by its ID
     *
     * @param postId the post id to load
     * @return the post data
     */
    public LateScheduler<PostWrapper> loadPost(final String postId) {
        LateScheduler<PostWrapper> result = new AsyncLateScheduler<>();

        source.async().queue("auth_user", () -> {
            HttpUtil util = URLUtils.extraUtils(working_url.getUrl());

            if (util != null) {
                String response = util.getResponse(
                        Post.newPost()
                                .add("method", "post")
                                .add("action", "fetch")
                                .add("post", postId),
                        working_url.getHeader()
                );

                Gson gson = new GsonBuilder().create();
                JsonElement element = gson.fromJson(response, JsonElement.class);

                if (element.isJsonObject()) {
                    JsonObject object = element.getAsJsonObject();
                    if (object.has("success")) {
                        JsonElement success_element = object.get("success");
                        if (success_element.isJsonPrimitive()) {
                            //System.out.println(response);
                            JsonPrimitive primitive = success_element.getAsJsonPrimitive();

                            if (primitive.isBoolean()) {
                                boolean success = primitive.getAsBoolean();

                                if (success) {
                                    if (object.has("0")) {
                                        object = object.getAsJsonObject("0");
                                        result.complete(new ApiPost(object));
                                    } else {
                                        JsonArray array = object.getAsJsonArray();
                                        object = array.get(0).getAsJsonObject();
                                        result.complete(new ApiPost(object));
                                    }
                                } else {
                                    result.complete(null);
                                }
                            }
                        }
                    }
                } else {
                    result.complete(null, new IllegalStateException("Failed to fetch post data. Json response was not an object!"));
                }
            } else {
                result.complete(null, new IllegalStateException("Failed to create URL utilities for API authentication. Are we connected to the internet?"));
            }
        });

        return result;
    }

    /**
     * Get the amount of minecraft accounts that are currently known by
     * the API
     *
     * @return the amount of known minecraft accounts
     */
    public LateScheduler<Integer> getMinecraftAccounts() {
        LateScheduler<Integer> result = new AsyncLateScheduler<>();

        source.async().queue("auth_user", () -> {
            HttpUtil util = URLUtils.extraUtils(working_url.getUrl());

            if (util != null) {
                String response = util.getResponse(
                        Post.newPost()
                                .add("method", "minecraft")
                                .add("action", "fetch")
                                .add("query", "*"),
                        working_url.getHeader()
                );

                Gson gson = new GsonBuilder().create();
                JsonElement element = gson.fromJson(response, JsonElement.class);

                if (element.isJsonObject()) {
                    JsonObject object = element.getAsJsonObject();
                    if (object.has("success")) {
                        JsonElement success_element = object.get("success");
                        if (success_element.isJsonPrimitive()) {
                            //System.out.println(response);
                            JsonPrimitive primitive = success_element.getAsJsonPrimitive();

                            if (primitive.isBoolean()) {
                                boolean success = primitive.getAsBoolean();

                                if (success) {
                                    result.complete(object.getAsJsonPrimitive("accounts").getAsInt());
                                } else {
                                    result.complete(null);
                                }
                            }
                        }
                    }
                } else {
                    result.complete(null, new IllegalStateException("Failed to fetch post data. Json response was not an object!"));
                }
            } else {
                result.complete(null, new IllegalStateException("Failed to create URL utilities for API authentication. Are we connected to the internet?"));
            }
        });

        return result;
    }

    /**
     * Get the client account UUID
     *
     * @param name the client name
     * @param type the UUID type
     * @return the client UUID
     */
    public LateScheduler<UUID> getMinecraftID(final String name, final UUIDType type) {
        LateScheduler<UUID> result = new AsyncLateScheduler<>();

        source.async().queue("auth_user", () -> {
            HttpUtil util = URLUtils.extraUtils(working_url.getUrl());

            if (util != null) {
                String response = util.getResponse(
                        Post.newPost()
                                .add("method", "minecraft")
                                .add("action", "fetch")
                                .add("query", name),
                        working_url.getHeader()
                );

                Gson gson = new GsonBuilder().create();
                JsonElement element = gson.fromJson(response, JsonElement.class);

                if (element.isJsonObject()) {
                    JsonObject object = element.getAsJsonObject();
                    if (object.has("success")) {
                        JsonElement success_element = object.get("success");
                        if (success_element.isJsonPrimitive()) {
                            //System.out.println(response);
                            JsonPrimitive primitive = success_element.getAsJsonPrimitive();

                            if (primitive.isBoolean()) {
                                boolean success = primitive.getAsBoolean();


                            }
                        }
                    }
                } else {
                    result.complete(null, new IllegalStateException("Failed to fetch post data. Json response was not an object!"));
                }
            } else {
                result.complete(null, new IllegalStateException("Failed to create URL utilities for API authentication. Are we connected to the internet?"));
            }
        });

        return result;
    }

    /**
     * Get the active client
     * <p>
     * PLEASE NOTE: For implementations in where there's a constant
     * call to {@link APIRequest#fetchProfile(int)} or {@link APIRequest#createProfile(String, String)}
     * this will return the last fetched/created profile, because we
     * assume there's not a 'current' one, but iteration of them
     *
     * @return the active client
     */
    public static @Nullable PublicProfileWrapper getCurrentClient() {
        return current_client;
    }
}
