package ml.karmaconfigs.api.common.karma.panel.impl.profile;

import com.google.gson.*;
import ml.karmaconfigs.api.common.karma.panel.wrapper.PublicProfileWrapper;
import ml.karmaconfigs.api.common.karma.panel.wrapper.post.PostPageWrapper;
import ml.karmaconfigs.api.common.utils.string.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

/**
 * API profile implementation for the
 * panel
 */
public final class PublicApiProfile extends PublicProfileWrapper {

    private final JsonObject profile;

    /**
     * Initialize the public profile
     *
     * @param profile_data the profile data
     */
    public PublicApiProfile(final String profile_data) {
        Gson gson = new GsonBuilder().create();
        profile = gson.fromJson(profile_data, JsonObject.class);
    }

    /**
     * Get the profile name
     *
     * @return the profile name
     */
    @Override
    public String getName() {
        if (profile.has("name")) {
            JsonElement name = profile.get("name");
            if (name.isJsonPrimitive()) {
                JsonPrimitive primitive = name.getAsJsonPrimitive();
                return primitive.getAsString();
            }
        }

        return "[unknown]";
    }

    /**
     * Get the profile description, or null if the
     * profile is private
     *
     * @return the profile description or null if private
     */
    @Override
    public String getDescription() {
        if (profile.has("description")) {
            JsonElement description = profile.get("description");
            if (description.isJsonArray()) {
                StringBuilder descBuilder = new StringBuilder();

                JsonArray array = description.getAsJsonArray();
                for (JsonElement element : array) {
                    if (element.isJsonPrimitive()) {
                        JsonPrimitive primitive = element.getAsJsonPrimitive();

                        if (primitive.isString()) {
                            descBuilder.append(primitive).append("\n");
                        }
                    }
                }

                String desc = descBuilder.toString();
                return StringUtils.replaceLast(desc, "\n", "");
            }
        }

        return null;
    }

    /**
     * Get the profile unique identifier
     *
     * @return the profile unique identifier
     */
    @Override
    public String getUniqueIdentifier() {
        return null;
    }

    /**
     * Get if the profile is private
     *
     * @return if the profile is private or has
     * friends-only mode
     */
    @Override
    public boolean isPrivate() {
        return profile.has("description"); //If the profile is private, the API just won't send this element
    }

    /**
     * Get the profile picture
     *
     * @return the profile picture
     */
    @Override
    public Image getImage() {
        if (profile.has("image")) {
            JsonElement image_element = profile.get("image");
            if (image_element.isJsonPrimitive()) {
                JsonPrimitive primitive = image_element.getAsJsonPrimitive();

                if (primitive.isString()) {
                    String imageBase64 = primitive.getAsString();

                    byte[] imageData = Base64.getDecoder().decode(imageBase64.getBytes());
                    InputStream stream = new ByteArrayInputStream(imageData);

                    try {
                        return ImageIO.read(stream);
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        return null;
    }

    /**
     * Get the profile created posts
     *
     * @return the profile created posts
     */
    @Override
    public PostPageWrapper getCreatedPosts() {
        return null;
    }

    /**
     * Get the profile commented posts
     *
     * @return the profile commented posts
     */
    @Override
    public PostPageWrapper getCommentedPosts() {
        return null;
    }

    /**
     * Get the profile liked posts
     *
     * @return the profile liked posts
     */
    @Override
    public PostPageWrapper getLikedPosts() {
        return null;
    }

    /**
     * Usually returns the public profile json string
     *
     * @return the profile string
     */
    @Override
    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(profile);
    }
}
