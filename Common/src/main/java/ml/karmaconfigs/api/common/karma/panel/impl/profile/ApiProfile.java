package ml.karmaconfigs.api.common.karma.panel.impl.profile;

import com.google.gson.*;
import ml.karmaconfigs.api.common.karma.KarmaSource;
import ml.karmaconfigs.api.common.karma.panel.HeaderURL;
import ml.karmaconfigs.api.common.karma.panel.wrapper.ProfileWrapper;
import ml.karmaconfigs.api.common.karma.panel.wrapper.PublicProfileWrapper;
import ml.karmaconfigs.api.common.karma.panel.wrapper.SettingsWrapper;
import ml.karmaconfigs.api.common.karma.panel.wrapper.post.PostWrapper;
import ml.karmaconfigs.api.common.timer.scheduler.BiLateScheduler;
import ml.karmaconfigs.api.common.timer.scheduler.LateScheduler;
import ml.karmaconfigs.api.common.timer.scheduler.worker.AsyncBiLateScheduler;
import ml.karmaconfigs.api.common.timer.scheduler.worker.AsyncLateScheduler;
import ml.karmaconfigs.api.common.utils.string.StringUtils;
import ml.karmaconfigs.api.common.utils.url.HttpUtil;
import ml.karmaconfigs.api.common.utils.url.Post;
import ml.karmaconfigs.api.common.utils.url.URLUtils;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 * API profile implementation for the
 * panel
 */
public final class ApiProfile extends ProfileWrapper {

    private final KarmaSource source;

    private final HeaderURL working_url;
    private final String name;
    private final String email;

    private String LAST_AUTH_MESSAGE = "{}";
    private String LAST_REGI_MESSAGE = "{}";

    private String LAST_PROF_MESSAGE = "{}";

    private String LAST_POST_DATA_MESSAGE = "{}";

    /**
     * Initialize the API profile
     *
     * @param s the source that will perform requests
     * @param w the working URL to perform API requests
     * @param n the profile name
     * @param e the profile email address
     */
    public ApiProfile(final KarmaSource s, final HeaderURL w, final String n, final String e) {
        source = s;

        working_url = w;
        name = n;
        email = e;
    }

    /**
     * Get the profile name
     *
     * @return the profile name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Get the profile email address
     *
     * @return the profile email address
     */
    @Override
    public String getEmail() {
        return email;
    }

    /**
     * Get the profile identifier
     *
     * @return the profile identifier
     */
    @Override
    public int getIdentifier() {
        if (!StringUtils.isNullOrEmpty(LAST_PROF_MESSAGE) && !LAST_PROF_MESSAGE.equalsIgnoreCase("{}")) {
            Gson gson = new GsonBuilder().create();
            JsonElement element = gson.fromJson(LAST_PROF_MESSAGE, JsonElement.class);

            if (element.isJsonObject()) {
                JsonObject object = element.getAsJsonObject();

                if (object.has("user_id")) {
                    JsonElement client = object.get("user_id");
                    if (client.isJsonPrimitive()) {
                        JsonPrimitive primitive = client.getAsJsonPrimitive();

                        if (primitive.isNumber()) {
                            return primitive.getAsNumber().intValue();
                        }
                    }
                }
            }
        }

        return -1;
    }

    /**
     * Get the profile unique identifier
     *
     * @return the profile unique identifier
     */
    @Override
    public UUID getUniqueIdentifier() {
        return null;
    }

    /**
     * Authenticate the user
     *
     * @param password the account password
     * @return if the account could be authenticated
     */
    @Override
    public LateScheduler<Boolean> authenticate(final String password) {
        LateScheduler<Boolean> result = new AsyncLateScheduler<>();

        source.async().queue("auth_user", () -> {
            HttpUtil util = URLUtils.extraUtils(URLUtils.append(working_url.getUrl(), "auth/login.php"));

            if (util != null) {
                LAST_AUTH_MESSAGE = util.getResponse(
                        Post.newPost()
                                .add("remember", 1)
                                .add("nosession", 0)
                                .add("stay", 1)
                                .add("close", 0)
                                .add("email", email)
                                .add("username", name)
                                .add("paramNoE", email)
                                .add("password", password),
                        working_url.getHeader()
                );

                result.complete(true);
            } else {
                result.complete(false, new IllegalStateException("Failed to create URL utilities for API authentication. Are we connected to the internet?"));
            }
        });

        return result;
    }

    /**
     * Register the user
     *
     * @param password      the account password
     * @param auto_validate if the account should try to validate himself right after
     *                      it gets created
     * @return the register result
     */
    @Override
    public LateScheduler<Boolean> create(final String password, final boolean auto_validate) {
        LateScheduler<Boolean> result = new AsyncLateScheduler<>();

        source.async().queue("create_user", () -> {
            HttpUtil util = URLUtils.extraUtils(URLUtils.append(working_url.getUrl(), "auth/register.php"));

            if (util != null) {
                LAST_REGI_MESSAGE = util.getResponse(
                        Post.newPost()
                                .add("remember", 1)
                                .add("nosession", 0)
                                .add("stay", 1)
                                .add("close", 0)
                                .add("email", email)
                                .add("username", name)
                                .add("paramNoE", email)
                                .add("password", password),
                        working_url.getHeader()
                );

                if (auto_validate) {
                    JsonElement element = getLastCreate();
                    util = URLUtils.extraUtils(URLUtils.append(working_url.getUrl(), "auth/login.php"));

                    if (util != null) {
                        if (element != null) {
                            if (element.isJsonObject()) {
                                JsonObject object = element.getAsJsonObject();
                                if (object.has("success") && object.has("token")) {
                                    JsonElement successElement = object.get("success");
                                    JsonElement tokenElement = object.get("token");

                                    if (successElement.isJsonPrimitive() && tokenElement.isJsonPrimitive()) {
                                        JsonPrimitive successPrimitive = successElement.getAsJsonPrimitive();
                                        JsonPrimitive tokenPrimitive = tokenElement.getAsJsonPrimitive();

                                        if (successPrimitive.isBoolean() && tokenPrimitive.isString()) {
                                            boolean success = successPrimitive.getAsBoolean();
                                            String token = tokenPrimitive.getAsString();

                                            if (success) {
                                                LAST_AUTH_MESSAGE = util.getResponse(
                                                        Post.newPost()
                                                                .add("remember", 1)
                                                                .add("nosession", 0)
                                                                .add("stay", 1)
                                                                .add("close", 0)
                                                                .add("email", email)
                                                                .add("username", name)
                                                                .add("paramNoE", email)
                                                                .add("password", password)
                                                                .add("token", token),
                                                        working_url.getHeader()
                                                );

                                                result.complete(true);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        result.complete(false);
                    } else {
                        result.complete(false, new IllegalStateException("Failed to create URL utilities for API authentication ( account validation ). Are we connected to the internet?"));
                    }
                } else {
                    result.complete(true);
                }
            } else {
                result.complete(false, new IllegalStateException("Failed to create URL utilities for API authentication. Are we connected to the internet?"));
            }
        });

        return result;
    }

    /**
     * Get the current profile
     *
     * @return {@link Void} when the action is completed
     */
    @Override
    public LateScheduler<Void> fetchProfile() {
        LateScheduler<Void> result = new AsyncLateScheduler<>();

        source.async().queue("fetch_profile", () -> {
            HttpUtil util = URLUtils.extraUtils(working_url.getUrl());

            if (util != null) {
                String response = util.getResponse(
                        Post.newPost()
                                .add("method", "account")
                                .add("action", "fetch")
                                .add("query", "user_id,settings,image,description,liked_posts,commented_posts,created_posts"),
                        working_url.getHeader()
                );

                Gson gson = new GsonBuilder().create();
                JsonObject settings_element = gson.fromJson(response, JsonObject.class);
                JsonObject tmp_prof_object = new JsonObject();

                if (settings_element.has("success")) {
                    JsonElement success_element = settings_element.get("success");
                    if (success_element.isJsonPrimitive()) {
                        JsonPrimitive primitive = success_element.getAsJsonPrimitive();

                        if (primitive.isBoolean()) {
                            boolean success = primitive.getAsBoolean();

                            if (success) {
                                settings_element.remove("success");
                                settings_element.remove("message");

                                //LAST_PROF_MESSAGE = gson.toJson(settings_element);

                                if (settings_element.has("profile")) {
                                    JsonElement tmp_profile = settings_element.get("profile");
                                    if (tmp_profile.isJsonObject()) {
                                        JsonObject tmp_profile_obj = tmp_profile.getAsJsonObject();

                                        JsonElement created_posts = tmp_profile_obj.remove("created_posts");
                                        JsonElement liked_posts = tmp_profile_obj.remove("liked_posts");
                                        JsonElement commented_posts = tmp_profile_obj.remove("commented_posts");

                                        settings_element.add("profile", tmp_profile_obj);
                                        LAST_PROF_MESSAGE = gson.toJson(settings_element);

                                        tmp_prof_object.add("created_posts", created_posts);
                                        tmp_prof_object.add("liked_posts", liked_posts);
                                        tmp_prof_object.add("commented_posts", commented_posts);

                                        LAST_POST_DATA_MESSAGE = gson.toJson(tmp_prof_object);
                                    }
                                }
                            } else {
                                result.complete(null, new IllegalStateException("Failed to fetch user profile"));
                            }
                        }
                    }
                }

                result.complete(null);
            } else {
                result.complete(null, new IllegalStateException("Failed to create URL utilities for API authentication. Are we connected to the internet?"));
            }
        });

        return result;
    }

    /**
     * Update profile
     *
     * @param settings the profile settings
     * @return if the profile could be updated
     */
    @Override
    public BiLateScheduler<Boolean, String> updateProfile(final SettingsWrapper settings) {
        BiLateScheduler<Boolean, String> result = new AsyncBiLateScheduler<>();

        source.async().queue("update_profile", () -> {
            HttpUtil util = URLUtils.extraUtils(working_url.getUrl());

            if (util != null) {
                String response = util.getResponse(
                        Post.newPost()
                                .add("method", "account")
                                .add("action", "update")
                                .add("content", settings),
                        working_url.getHeader()
                );

                Gson gson = new GsonBuilder().create();
                JsonElement element = gson.fromJson(response, JsonElement.class);

                if (element != null) {
                    if (element.isJsonObject()) {
                        JsonObject object = element.getAsJsonObject();
                        if (object.has("success") && object.has("message")) {
                            JsonElement successElement = object.get("success");
                            JsonElement messageElement = object.get("message");

                            if (successElement.isJsonPrimitive() && messageElement.isJsonPrimitive()) {
                                JsonPrimitive successPrimitive = successElement.getAsJsonPrimitive();
                                JsonPrimitive messagePrimitive = messageElement.getAsJsonPrimitive();

                                if (successPrimitive.isBoolean() && messagePrimitive.isString()) {
                                    boolean success = successPrimitive.getAsBoolean();
                                    String message = messagePrimitive.getAsString();

                                    result.complete(success, message);
                                }
                            }
                        }
                    }
                }

                result.complete(false, "Unknown error");
            } else {
                result.complete(false, "Error", new IllegalStateException("Failed to create URL utilities for API authentication. Are we connected to the internet?"));
            }
        });

        return result;
    }

    /**
     * Create a post
     *
     * @param post the post to create
     * @return if the post could be created and the created post
     */
    @Override
    public BiLateScheduler<Boolean, PostWrapper> createPost(PostWrapper post) {
        return null;
    }

    /**
     * Accept a post
     *
     * @param post the post to accept
     * @return if the post could be accepted
     */
    @Override
    public BiLateScheduler<Boolean, PostWrapper> acceptPost(String post) {
        return null;
    }

    /**
     * Decline a post
     *
     * @param post the post to decline
     * @return if the post could be declined
     */
    @Override
    public LateScheduler<Boolean> declinePost(String post) {
        return null;
    }

    /**
     * Update a post
     *
     * @param post     the post id
     * @param new_post the new post data
     * @return if the post could be updated and the new post
     */
    @Override
    public BiLateScheduler<Boolean, PostWrapper> updatePost(String post, PostWrapper new_post) {
        return null;
    }

    /**
     * Get the profile settings
     *
     * @return the profile settings
     */
    @Override
    public SettingsWrapper getSettings() {
        if (!StringUtils.isNullOrEmpty(LAST_PROF_MESSAGE) && !LAST_PROF_MESSAGE.equalsIgnoreCase("{}")) {
            return new ApiSettings(LAST_PROF_MESSAGE);
        }

        return null;
    }

    /**
     * Get the profile created posts
     *
     * @return the profile created posts
     */
    @Override
    public Set<String> getCreatedPosts() {
        if (LAST_POST_DATA_MESSAGE != null) {
            Set<String> posts = new LinkedHashSet<>();
            //For this type of post data no pagination is made. So we will add the data directly

            Gson gson = new GsonBuilder().create();
            JsonElement element = gson.fromJson(LAST_POST_DATA_MESSAGE, JsonElement.class);

            if (element.isJsonObject()) {
                JsonObject object = element.getAsJsonObject();
                if (object.has("created_posts")) {
                    JsonElement created_posts_element = object.get("created_posts");

                    if (created_posts_element.isJsonArray()) {
                        JsonArray created_posts = created_posts_element.getAsJsonArray();

                        for (JsonElement postData : created_posts) {
                            if (postData.isJsonObject()) {
                                JsonObject postObject = postData.getAsJsonObject();

                                if (postObject.has("post")) {
                                    JsonElement post = postObject.get("post");

                                    if (post.isJsonPrimitive()) {
                                        JsonPrimitive primitive = post.getAsJsonPrimitive();

                                        if (primitive.isString()) {
                                            posts.add(primitive.getAsString());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return posts;
        }

        return null;
    }

    /**
     * Get the profile commented posts
     *
     * @return the profile commented posts
     */
    @Override
    public Set<String> getCommentedPosts() {
        if (LAST_POST_DATA_MESSAGE != null) {
            Set<String> posts = new LinkedHashSet<>();
            //For this type of post data no pagination is made. So we will add the data directly

            Gson gson = new GsonBuilder().create();
            JsonElement element = gson.fromJson(LAST_POST_DATA_MESSAGE, JsonElement.class);

            if (element.isJsonObject()) {
                JsonObject object = element.getAsJsonObject();
                if (object.has("commented_posts")) {
                    JsonElement commented_posts_element = object.get("commented_posts");

                    if (commented_posts_element.isJsonArray()) {
                        JsonArray commented_posts = commented_posts_element.getAsJsonArray();

                        for (JsonElement postData : commented_posts) {
                            if (postData.isJsonObject()) {
                                JsonObject postObject = postData.getAsJsonObject();

                                if (postObject.has("post")) {
                                    JsonElement post = postObject.get("post");

                                    if (post.isJsonPrimitive()) {
                                        JsonPrimitive primitive = post.getAsJsonPrimitive();

                                        if (primitive.isString()) {
                                            posts.add(primitive.getAsString());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return posts;
        }

        return null;
    }

    /**
     * Get the profile liked posts
     *
     * @return the profile liked posts
     */
    @Override
    public Set<String> getLikedPosts() {
        if (LAST_POST_DATA_MESSAGE != null) {
            Set<String> posts = new LinkedHashSet<>();
            //For this type of post data no pagination is made. So we will add the data directly

            Gson gson = new GsonBuilder().create();
            JsonElement element = gson.fromJson(LAST_POST_DATA_MESSAGE, JsonElement.class);

            if (element.isJsonObject()) {
                JsonObject object = element.getAsJsonObject();
                if (object.has("liked_posts")) {
                    JsonElement liked_posts_element = object.get("liked_posts");

                    if (liked_posts_element.isJsonObject()) {
                        JsonObject liked_posts = liked_posts_element.getAsJsonObject();

                        posts.addAll(liked_posts.keySet());
                    }
                }
            }

            return posts;
        }

        return null;
    }

    /**
     * Get the last auth message
     *
     * @return the last auth message
     */
    @Override
    public @Nullable JsonElement getLastAuth() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.fromJson(LAST_AUTH_MESSAGE, JsonElement.class);
    }

    /**
     * Get the last registration message
     *
     * @return the last registration message
     */
    @Override
    public @Nullable JsonElement getLastCreate() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.fromJson(LAST_REGI_MESSAGE, JsonElement.class);
    }

    /**
     * Returns this profile as a public profile instance
     *
     * @return the profile as a public profile
     */
    @Override
    public PublicProfileWrapper asPublicProfile() {
        JsonObject object = new JsonObject();
        object.addProperty("name", name);
        object.addProperty("email", email);
        SettingsWrapper wrapper = getSettings();
        if (wrapper != null) {
            String rawImage = wrapper.getProfileRawPicture();

            if (rawImage != null) {
                object.addProperty("image", rawImage);
            }
        }

        Gson gson = new GsonBuilder().create();
        try {
            JsonElement element = gson.fromJson(LAST_POST_DATA_MESSAGE, JsonElement.class);

            if (element.isJsonObject()) {
                JsonObject tmpObject = element.getAsJsonObject();
                if (tmpObject.has("created_posts")) {
                    JsonElement created_posts_element = object.get("created_posts");
                    object.add("created_posts", created_posts_element);
                }
                if (tmpObject.has("liked_posts")) {
                    JsonElement created_posts_element = object.get("liked_posts");
                    object.add("liked_posts", created_posts_element);
                }
                if (tmpObject.has("commented_posts")) {
                    JsonElement created_posts_element = object.get("commented_posts");
                    object.add("commented_posts", created_posts_element);
                }
            }
        } catch (Throwable ignored) {
        }

        return new PublicApiProfile(gson.toJson(object));
    }
}
