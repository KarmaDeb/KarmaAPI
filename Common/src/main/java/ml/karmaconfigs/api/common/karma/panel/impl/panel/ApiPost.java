package ml.karmaconfigs.api.common.karma.panel.impl.panel;

import com.google.gson.*;
import ml.karmaconfigs.api.common.karma.panel.APIRequest;
import ml.karmaconfigs.api.common.karma.panel.wrapper.ProfileWrapper;
import ml.karmaconfigs.api.common.karma.panel.wrapper.PublicProfileWrapper;
import ml.karmaconfigs.api.common.karma.panel.wrapper.post.CommentWrapper;
import ml.karmaconfigs.api.common.karma.panel.wrapper.post.PostWrapper;
import ml.karmaconfigs.api.common.karma.panel.wrapper.post.filter.Filterable;
import ml.karmaconfigs.api.common.karma.panel.wrapper.post.meta.PostTagsContainer;
import ml.karmaconfigs.api.common.timer.scheduler.LateScheduler;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Initialize the API post
 */
public class ApiPost extends PostWrapper {

    private final JsonObject post;

    private ProfileWrapper current_profile;

    /**
     * Initialize the API post
     *
     * @param data the post data
     */
    public ApiPost(final JsonObject data) {
        post = data;
    }

    /**
     * Set the current profile
     *
     * @param profile the current post profile
     * @return this instance
     */
    public ApiPost withProfile(final ProfileWrapper profile) {
        current_profile = profile;

        return this;
    }

    /**
     * Get the post ID
     *
     * @return the post ID
     */
    @Override
    public String getId() {
        if (post.has("id")) {
            JsonElement post_data = post.get("id");
            if (post_data.isJsonPrimitive()) {
                JsonPrimitive primitive = post_data.getAsJsonPrimitive();

                if (primitive.isString()) {
                    return primitive.getAsString();
                }
            }
        }

        return null;
    }

    /**
     * Get the post title
     *
     * @return the post title
     */
    @Override
    public String getTitle() {
        if (post.has("post")) {
            JsonElement post_data = post.get("post");
            if (post_data.isJsonObject()) {
                JsonObject object = post_data.getAsJsonObject();

                if (object.has("title")) {
                    JsonElement post_info = object.get("title");
                    if (post_info.isJsonPrimitive()) {
                        JsonPrimitive primitive = post_info.getAsJsonPrimitive();

                        if (primitive.isString()) {
                            return primitive.getAsString().replaceAll("\r\n", "");
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * Get the post content
     *
     * @return the post content
     */
    @Override
    public String getContent() {
        if (post.has("post")) {
            JsonElement post_data = post.get("post");
            if (post_data.isJsonObject()) {
                JsonObject object = post_data.getAsJsonObject();

                if (object.has("content")) {
                    JsonElement post_info = object.get("content");
                    if (post_info.isJsonPrimitive()) {
                        JsonPrimitive primitive = post_info.getAsJsonPrimitive();

                        if (primitive.isString()) {
                            return primitive.getAsString().replace("[/lb]", "\n").replaceAll("<.*?>", "");
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * Get the post publication date
     *
     * @return the post date
     */
    @Override
    public String getCreated() {
        if (post.has("date")) {
            JsonElement post_data = post.get("date");
            if (post_data.isJsonPrimitive()) {
                JsonPrimitive primitive = post_data.getAsJsonPrimitive();

                if (primitive.isString()) {
                    return primitive.getAsString();
                }
            }
        }

        return null;
    }

    /**
     * Get the post last modification date
     *
     * @return the post last modification date
     */
    @Override
    public String getModified() {
        if (post.has("modified")) {
            JsonElement post_data = post.get("modified");
            if (post_data.isJsonPrimitive()) {
                JsonPrimitive primitive = post_data.getAsJsonPrimitive();

                if (primitive.isString()) {
                    return primitive.getAsString();
                }
            }
        }

        return null;
    }

    /**
     * Get if the current account (if logged in) liked the post.
     * Otherwise, returns false
     *
     * @return if the current account likes this post
     */
    @Override
    public boolean isLiked() {
        PublicProfileWrapper current = APIRequest.getCurrentClient();

        if (current != null) {
            if (post.has("post")) {
                JsonElement post_data = post.get("post");
                if (post_data.isJsonObject()) {
                    JsonObject object = post_data.getAsJsonObject();

                    if (object.has("metadata")) {
                        JsonElement post_meta = object.get("metadata");
                        if (post_meta.isJsonObject()) {
                            JsonObject metadata = post_meta.getAsJsonObject();
                            if (metadata.has("likes")) {
                                JsonElement element = metadata.get("likes");

                                if (element.isJsonArray()) {
                                    JsonArray array = element.getAsJsonArray();
                                    return array.contains(new JsonPrimitive(current.getName()));
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Get the post status
     *
     * @return the post status
     */
    @Override
    public Status getStatus() {
        if (post.has("status")) {
            JsonElement post_data = post.get("status");
            if (post_data.isJsonObject()) {
                JsonObject object = post_data.getAsJsonObject();

                if (object.has("code")) {
                    JsonElement element = object.get("code");

                    if (element.isJsonPrimitive()) {
                        JsonPrimitive primitive = element.getAsJsonPrimitive();

                        if (primitive.isNumber()) {
                            int status_code = primitive.getAsNumber().intValue();

                            switch (status_code) {
                                case 0:
                                    return Status.PENDING;
                                case 1:
                                    return Status.ACTIVE;
                                case 2:
                                    return Status.PRIVATE;
                                case 3:
                                    return Status.DELETED;
                                default:
                                    Status status = Status.UNKNOWN;
                                    try {
                                        Field code = status.getClass().getField("code");
                                        code.setAccessible(true);

                                        Field modifiersField = Field.class.getDeclaredField("modifiers");
                                        modifiersField.setAccessible(true);
                                        int original_modifiers = code.getModifiers();

                                        modifiersField.setInt(code, code.getModifiers() & ~Modifier.FINAL);

                                        code.set(status, status_code);

                                        modifiersField.setInt(code, original_modifiers);
                                        modifiersField.setAccessible(false);
                                        code.setAccessible(false);
                                        //Restore everything, as if nothing had happen
                                    } catch (Throwable ignored) {}

                                    return status;
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * Get the post owner
     *
     * @return the post owner
     */
    @Override
    public int getOwner() {
        if (post.has("owner")) {
            JsonElement post_data = post.get("owner");
            if (post_data.isJsonObject()) {
                JsonObject object = post_data.getAsJsonObject();

                if (object.has("id")) {
                    JsonElement element = object.get("id");

                    if (element.isJsonPrimitive()) {
                        JsonPrimitive primitive = element.getAsJsonPrimitive();

                        if (primitive.isNumber()) {
                            return primitive.getAsNumber().intValue();
                        }
                    }
                }
            }
        }

        return 0;
    }

    /**
     * Get the post status issuer
     *
     * @return the post status issuer
     */
    @Override
    public int getStatusIssuer() {
        if (post.has("status")) {
            JsonElement post_data = post.get("status");
            if (post_data.isJsonObject()) {
                JsonObject object = post_data.getAsJsonObject();

                if (object.has("admin")) {
                    JsonElement element = object.get("admin");

                    if (element.isJsonPrimitive()) {
                        JsonPrimitive primitive = element.getAsJsonPrimitive();

                        if (primitive.isNumber()) {
                            return primitive.getAsNumber().intValue();
                        }
                    }
                }
            }
        }

        return 69; //Believe it or not, that's the panel account id
    }

    /**
     * Get the post visibility issuer
     *
     * @return the post visibility issuer
     */
    @Override
    public int getVisibilityIssuer() {
        if (post.has("status")) {
            JsonElement post_data = post.get("status");
            if (post_data.isJsonObject()) {
                JsonObject object = post_data.getAsJsonObject();

                if (object.has("admin")) {
                    JsonElement element = object.get("admin");

                    if (element.isJsonPrimitive()) {
                        JsonPrimitive primitive = element.getAsJsonPrimitive();

                        if (primitive.isNumber()) {
                            return primitive.getAsNumber().intValue();
                        }
                    }
                }
            }
        }

        return 69;
    }

    /**
     * Get the post comments
     *
     * @return the post comments
     */
    @Override
    public List<CommentWrapper> getComments() {
        return null;
    }

    /**
     * Get the post comments
     *
     * @param filter the post comment filter
     * @return the post comments
     */
    @Override
    public List<CommentWrapper> getComments(Filterable<CommentWrapper> filter) {
        return null;
    }

    /**
     * Get the post likes
     *
     * @return the post likes
     */
    @Override
    public List<String> getLikes() {
        List<String> likes = new ArrayList<>();

        if (post.has("post")) {
            JsonElement post_data = post.get("post");
            if (post_data.isJsonObject()) {
                JsonObject object = post_data.getAsJsonObject();

                if (object.has("metadata")) {
                    JsonElement post_meta = object.get("metadata");
                    if (post_meta.isJsonObject()) {
                        JsonObject metadata = post_meta.getAsJsonObject();
                        if (metadata.has("likes")) {
                            JsonElement element = metadata.get("likes");

                            if (element.isJsonArray()) {
                                JsonArray array = element.getAsJsonArray();

                                array.forEach((array_item) -> {
                                    if (array_item.isJsonPrimitive()) {
                                        JsonPrimitive array_primitive = array_item.getAsJsonPrimitive();
                                        if (array_primitive.isString()) {
                                            likes.add(array_primitive.getAsString());
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }

        return likes;
    }

    /**
     * Get the post likes
     *
     * @param filter the post like filter
     * @return the post likes
     */
    @Override
    public List<String> getLikes(final Filterable<String> filter) {
        List<String> likes = new ArrayList<>();

        if (post.has("post")) {
            JsonElement post_data = post.get("post");
            if (post_data.isJsonObject()) {
                JsonObject object = post_data.getAsJsonObject();

                if (object.has("metadata")) {
                    JsonElement post_meta = object.get("metadata");
                    if (post_meta.isJsonObject()) {
                        JsonObject metadata = post_meta.getAsJsonObject();
                        if (metadata.has("likes")) {
                            JsonElement element = metadata.get("likes");

                            if (element.isJsonArray()) {
                                JsonArray array = element.getAsJsonArray();

                                array.forEach((array_item) -> {
                                    if (array_item.isJsonPrimitive()) {
                                        JsonPrimitive array_primitive = array_item.getAsJsonPrimitive();
                                        if (array_primitive.isString() && filter.filter(array_primitive.getAsString())) {
                                            likes.add(array_primitive.getAsString());
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }

        return likes;
    }

    /**
     * Add a comment to the post
     *
     * @param content the comment content
     * @return the added comment
     */
    @Override
    public LateScheduler<CommentWrapper> addComment(String content) {
        return null;
    }

    /**
     * Returns true if the new post like status is true, and returns false
     * if the new post like status is false
     *
     * @return the new post like status
     */
    @Override
    public LateScheduler<Boolean> toggleLike() {
        return null;
    }

    /**
     * Usually returns the post json
     *
     * @return the post as string
     */
    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(post);
    }

    /**
     * Filter the object
     *
     * @param object the {@link PostTagsContainer} to filter
     * @return if {@link PostTagsContainer} passes the filter
     */
    @Override
    public boolean filter(final PostTagsContainer object) {
        return false;
    }
}
