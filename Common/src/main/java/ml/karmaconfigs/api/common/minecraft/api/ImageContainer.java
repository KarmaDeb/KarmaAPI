package ml.karmaconfigs.api.common.minecraft.api;

import ml.karmaconfigs.api.common.data.path.PathUtilities;
import ml.karmaconfigs.api.common.minecraft.api.response.OKAHeadRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Response image container
 */
public interface ImageContainer {

    /**
     * Parse the image base64 to a
     * buffered image
     *
     * @return the image
     * @throws IOException as part of {@link javax.imageio.ImageIO#read(InputStream)}
     */
    BufferedImage toImage() throws IOException;

    /**
     * Export the head image into a file
     *
     * @param file the destination file
     * @throws IOException if the file failed to write or as part of {@link OKAHeadRequest#toImage()}
     */
    default void export(final Path file) throws IOException {
        BufferedImage image = toImage();
        PathUtilities.create(file);

        ImageIO.write(image, "png", Files.newOutputStream(file));
    }

    /**
     * Export the head image into a file
     *
     * @param file the destination file
     * @throws IOException if the file failed to write or as part of {@link OKAHeadRequest#toImage()}
     */
    default void export(final File file) throws IOException {
        BufferedImage image = toImage();
        ImageIO.write(image, "png", file);
    }

    /**
     * Export the head image into a file
     *
     * @param stream the destination stream
     * @throws IOException if the file failed to write or as part of {@link OKAHeadRequest#toImage()}
     */
    default void export(final OutputStream stream) throws IOException {
        BufferedImage image = toImage();
        ImageIO.write(image, "png", stream);
    }

    /**
     * Export the head image into a file
     *
     * @param paths the destination file
     * @return if the head image could be exported
     * @throws IOException if the file failed to write or as part of {@link OKAHeadRequest#toImage()}
     */
    Path export(final String... paths) throws IOException;
}
