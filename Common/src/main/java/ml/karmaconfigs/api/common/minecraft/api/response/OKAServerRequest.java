package ml.karmaconfigs.api.common.minecraft.api.response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import ml.karmaconfigs.api.common.data.path.PathUtilities;
import ml.karmaconfigs.api.common.minecraft.api.ImageContainer;
import ml.karmaconfigs.api.common.minecraft.api.JsonContainer;
import ml.karmaconfigs.api.common.minecraft.api.response.data.SrvRecord;
import ml.karmaconfigs.api.common.security.token.TokenGenerator;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;

/**
 * Online KarmaAPI server request
 */
@Builder
public class OKAServerRequest implements ImageContainer, JsonContainer {

    @Getter @Nullable
    URI uri;

    @Getter @NonNull
    Instant cache;

    @Getter @NonNull
    String platform;
    @Getter
    int protocol;
    @Getter
    long onlinePlayers;
    @Getter
    long maxPlayers;
    @Getter @NonNull
    String[] motd;

    @Getter @NonNull
    String icon;

    @Getter @NonNull
    InetSocketAddress address;

    @Getter @NonNull
    SrvRecord srv;

    @NonNull
    String json;

    /**
     * Parse the image base64 to a
     * buffered image
     *
     * @return the image
     * @throws IOException as part of {@link javax.imageio.ImageIO#read(InputStream)}
     */
    @Override
    public BufferedImage toImage() throws IOException {
        if (icon.contains(",")) {
            String[] data = icon.split(",");
            String base = data[1];

            byte[] imageBytes = Base64.getDecoder().decode(base);
            try (ByteArrayInputStream stream = new ByteArrayInputStream(imageBytes)) {
                return ImageIO.read(stream);
            }
        }

        return null;
    }

    /**
     * Export the head image into a file
     *
     * @param paths the destination file
     * @return if the head image could be exported
     * @throws IOException if the file failed to write or as part of {@link OKAHeadRequest#toImage()}
     */
    @Override
    public Path export(final String... paths) throws IOException {
        BufferedImage image = toImage();

        Path file;
        switch (paths.length) {
            case 0:
                String path = TokenGenerator.generateToken() + ".png";
                if (uri != null) path = uri.getPath() + ".png";
                file = Paths.get(path);
                break;
            case 1:
                file = Paths.get(paths[0]);
                break;
            default:
                file = Paths.get(paths[0], Arrays.copyOfRange(paths, 1, paths.length));
                break;
        }

        PathUtilities.create(file);

        ImageIO.write(image, "png", Files.newOutputStream(file));
        return file;
    }

    /**
     * Parse the response to json
     *
     * @param pretty prettify the output
     * @return the json response
     */
    @Override
    public String toJson(final boolean pretty) {
        if (pretty) {
            try {
                Gson gson = new GsonBuilder().setPrettyPrinting().create(); //Not compatible
                JsonElement element = gson.fromJson(json, JsonElement.class);

                return gson.toJson(element);
            } catch (Throwable ignored) {}
        }

        return json;
    }

    /**
     * Build an empty server request
     *
     * @param address the address
     * @param port the port
     * @return the empty server request
     */
    public static OKAServerRequest empty(final String address, final int port) {
        return empty(address, port, "{}");
    }

    /**
     * Build an empty server request
     *
     * @param address the address
     * @param port the port
     * @param json the json request
     * @return the empty server request
     */
    public static OKAServerRequest empty(final String address, final int port, final String json) {
        return OKAServerRequest.builder()
                .cache(Instant.now())
                .platform("unknown")
                .protocol(0)
                .onlinePlayers(0)
                .maxPlayers(0)
                .motd(new String[0])
                .icon("")
                .address(InetSocketAddress.createUnresolved(address, port))
                .srv(SrvRecord.empty(port))
                .json((json != null ? json : "{}")).build();
    }
}
