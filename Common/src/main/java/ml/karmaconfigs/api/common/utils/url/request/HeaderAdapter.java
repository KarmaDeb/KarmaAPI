package ml.karmaconfigs.api.common.utils.url.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class HeaderAdapter {

    @Getter @NotNull
    private final String key;
    @Getter @NotNull
    private final String value;

    /**
     * Create a new header
     *
     * @param key the header key
     * @param value the header value
     * @return the new header
     */
    public static HeaderAdapter newHeader(final @NotNull String key, final @NotNull String value) {
        return new HeaderAdapter(key, value);
    }
}
