package ml.karmaconfigs.api.common.karma;

import ml.karmaconfigs.api.common.utils.security.token.TokenGenerator;

/**
 * Identifiable object
 */
public interface Identifiable {

    /**
     * Get the current identifier
     *
     * @return the current identifier
     */
    String getIdentifier();

    /**
     * Store the identifier
     *
     * @param name the identifier name
     * @return if the identifier could be stored
     */
    boolean storeIdentifier(final String name);

    /**
     * Load an identifier
     *
     * @param name the identifier name
     */
    void loadIdentifier(final String name);

    /**
     * Generate an identifier
     *
     * @return the generated identifier
     */
    default String generateIdentifier() {
        return TokenGenerator.generateToken();
    }
}
