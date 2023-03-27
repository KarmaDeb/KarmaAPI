package ml.karmaconfigs.api.common.minecraft.api;

/**
 * Response json container
 */
public interface JsonContainer {

    /**
     * Parse the response to json
     *
     * @param pretty prettify the output
     * @return the json response
     */
    String toJson(final boolean pretty);
}
