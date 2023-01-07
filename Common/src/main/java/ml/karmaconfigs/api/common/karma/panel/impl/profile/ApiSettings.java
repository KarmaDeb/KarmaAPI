package ml.karmaconfigs.api.common.karma.panel.impl.profile;

import com.google.gson.*;
import ml.karmaconfigs.api.common.karma.panel.wrapper.ProfileWrapper;
import ml.karmaconfigs.api.common.karma.panel.wrapper.SettingsWrapper;
import ml.karmaconfigs.api.common.string.StringUtils;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;

/**
 * Profile settings implementation
 */
public class ApiSettings extends SettingsWrapper {

    private final JsonObject profile_data;

    /**
     * Initialize the API settings
     *
     * @param s the profile string
     */
    public ApiSettings(final String s) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        profile_data = gson.fromJson(s, JsonObject.class);
    }

    /**
     * Get the profile visibility
     *
     * @return the profile visibility
     */
    @Override
    public Visibility getVisibility() {
        if (profile_data.has("settings")) {
            JsonElement settings_element = profile_data.get("settings");
            if (settings_element.isJsonObject()) {
                JsonObject settings = settings_element.getAsJsonObject();
                if (settings.has("visibility")) {
                    JsonElement visibility_element = settings.get("visibility");
                    if (visibility_element.isJsonPrimitive()) {
                        JsonPrimitive primitive = visibility_element.getAsJsonPrimitive();

                        if (primitive.isString()) {
                            String visibility = primitive.getAsString();
                            return Visibility.fromApiName(visibility);
                        }
                    }
                }
            }
        }

        return Visibility.INVALID;
    }

    /**
     * Get the profile description
     *
     * @return the profile description
     */
    @Override
    public String getDescription() {
        if (profile_data.has("profile")) {
            JsonElement profile_element = profile_data.get("profile");
            if (profile_element.isJsonObject()) {
                JsonObject profile = profile_element.getAsJsonObject();
                if (profile.has("description")) {
                    JsonElement description_element = profile.get("description");
                    if (description_element.isJsonArray()) {
                        JsonArray array = description_element.getAsJsonArray();

                        StringBuilder descriptionBuilder = new StringBuilder();
                        for (JsonElement element : array) {
                            if (element.isJsonPrimitive()) {
                                JsonPrimitive primitive = element.getAsJsonPrimitive();

                                if (primitive.isString()) {
                                    String line = primitive.getAsString();

                                    descriptionBuilder.append(line).append("\n");
                                }
                            }
                        }
                        String description = descriptionBuilder.toString();
                        return StringUtils.replaceLast(description, "\n", "");
                    }
                }
            }
        }

        return "";
    }

    /**
     * Get the profile image picture
     *
     * @return the profile image picture in a base64 string
     */
    @Override
    public Image getProfilePicture() {
        if (profile_data.has("profile")) {
            JsonElement profile_element = profile_data.get("profile");
            if (profile_element.isJsonObject()) {
                JsonObject profile = profile_element.getAsJsonObject();
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
            }
        }

        return null;
    }

    /**
     * Get the profile image picture
     *
     * @return the profile image picture in a base64 string
     */
    @Override
    public String getProfileRawPicture() {
        if (profile_data.has("profile")) {
            JsonElement profile_element = profile_data.get("profile");
            if (profile_element.isJsonObject()) {
                JsonObject profile = profile_element.getAsJsonObject();
                if (profile.has("image")) {
                    JsonElement image_element = profile.get("image");
                    if (image_element.isJsonPrimitive()) {
                        JsonPrimitive primitive = image_element.getAsJsonPrimitive();

                        if (primitive.isString()) {
                            return primitive.getAsString();
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * Broadcast if the profile is online or not
     *
     * @return if the profile should broadcast its status
     */
    @Override
    public boolean broadcastStatus() {
        if (profile_data.has("settings")) {
            JsonElement settings_element = profile_data.get("settings");
            if (settings_element.isJsonObject()) {
                JsonObject settings = settings_element.getAsJsonObject();
                if (settings.has("broadcast_status")) {
                    JsonElement broadcast_element = settings.get("broadcast_status");
                    if (broadcast_element.isJsonPrimitive()) {
                        JsonPrimitive primitive = broadcast_element.getAsJsonPrimitive();

                        if (primitive.isBoolean()) {
                            return primitive.getAsBoolean();
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * Broadcast registration date
     *
     * @return if the profile should broadcast when it was first created
     */
    @Override
    public boolean broadcastRegistration() {
        if (profile_data.has("settings")) {
            JsonElement settings_element = profile_data.get("settings");
            if (settings_element.isJsonObject()) {
                JsonObject settings = settings_element.getAsJsonObject();
                if (settings.has("broadcast_registration")) {
                    JsonElement broadcast_element = settings.get("broadcast_registration");
                    if (broadcast_element.isJsonPrimitive()) {
                        JsonPrimitive primitive = broadcast_element.getAsJsonPrimitive();

                        if (primitive.isBoolean()) {
                            return primitive.getAsBoolean();
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * Enable profile email notifications
     *
     * @return if the profile receives email notifications
     */
    @Override
    public boolean emailNotifications() {
        if (profile_data.has("settings")) {
            JsonElement settings_element = profile_data.get("settings");
            if (settings_element.isJsonObject()) {
                JsonObject settings = settings_element.getAsJsonObject();
                if (settings.has("email_notifications")) {
                    JsonElement email_element = settings.get("email_notifications");
                    if (email_element.isJsonPrimitive()) {
                        JsonPrimitive primitive = email_element.getAsJsonPrimitive();

                        if (primitive.isBoolean()) {
                            return primitive.getAsBoolean();
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * Set the account visibility
     *
     * @param policy the new visibility policy
     */
    @Override
    public void setVisibility(final Visibility policy) {
        JsonObject settings = new JsonObject();

        if (profile_data.has("settings")) {
            JsonElement settings_element = profile_data.get("settings");
            if (settings_element.isJsonObject()) {
                settings = settings_element.getAsJsonObject();
            }
        }

        settings.addProperty("visibility", policy.getValue());
        profile_data.add("settings", settings);
    }

    /**
     * Set the profile description
     *
     * @param description the profile new description
     */
    @Override
    public void setDescription(final String description) {
        JsonObject profile = new JsonObject();

        if (profile_data.has("profile")) {
            JsonElement profile_element = profile_data.get("profile");
            if (profile_element.isJsonObject()) {
                profile = profile_element.getAsJsonObject();
            }
        }

        JsonArray array = new JsonArray();
        String[] data = description.split("\n");
        for (String str : data) array.add(str);

        profile.add("description", array);
        profile_data.add("profile", profile);
    }

    /**
     * Set the profile picture
     *
     * @param image the profile picture
     */
    @Override
    public void setProfilePicture(final Image image) {
        JsonObject profile = new JsonObject();

        if (profile_data.has("profile")) {
            JsonElement profile_element = profile_data.get("profile");
            if (profile_element.isJsonObject()) {
                profile = profile_element.getAsJsonObject();
            }
        }

        try {
            BufferedImage buffer = new BufferedImage(120, 120, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = buffer.createGraphics();
            g2d.drawImage(image, 0, 0, 120, 120, null);
            g2d.dispose();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(buffer, "png", out);

            profile.addProperty("image", DatatypeConverter.printBase64Binary(out.toByteArray()));
            profile_data.add("profile", profile);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Share the account status and last visited page
     *
     * @param status if the account will share status and last visited
     *               page
     */
    @Override
    public void shareStatus(final boolean status) {
        JsonObject settings = new JsonObject();

        if (profile_data.has("settings")) {
            JsonElement settings_element = profile_data.get("settings");
            if (settings_element.isJsonObject()) {
                settings = settings_element.getAsJsonObject();
            }
        }

        settings.addProperty("broadcast_status", status);
        profile_data.add("settings", settings);
    }

    /**
     * Set the account share registration date status
     *
     * @param status if the account will display when it was first created
     */
    @Override
    public void shareRegistration(final boolean status) {
        JsonObject settings = new JsonObject();

        if (profile_data.has("settings")) {
            JsonElement settings_element = profile_data.get("settings");
            if (settings_element.isJsonObject()) {
                settings = settings_element.getAsJsonObject();
            }
        }

        settings.addProperty("broadcast_registration", status);
        profile_data.add("settings", settings);
    }

    /**
     * Set the account email notifications status
     *
     * @param status if the account will receive notifications via email
     */
    @Override
    public void emailNotifications(final boolean status) {
        JsonObject settings = new JsonObject();

        if (profile_data.has("settings")) {
            JsonElement settings_element = profile_data.get("settings");
            if (settings_element.isJsonObject()) {
                settings = settings_element.getAsJsonObject();
            }
        }

        settings.addProperty("email_notifications", status);
        profile_data.add("settings", settings);
    }

    /**
     * IMPORTANT!
     * <p>
     * This method should always return a json string, otherwise, calls
     * to {@link ProfileWrapper#updateProfile(SettingsWrapper)} will result
     * in a failed request
     *
     * @return the settings json string
     */
    @Override
    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(profile_data);
    }
}
