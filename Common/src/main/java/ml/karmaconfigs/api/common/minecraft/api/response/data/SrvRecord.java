package ml.karmaconfigs.api.common.minecraft.api.response.data;

import lombok.Getter;
import lombok.Value;

/**
 * Server srv record
 */
@Value(staticConstructor = "of")
public class SrvRecord {

    @Getter
    String host;

    @Getter
    int port;

    @Getter
    long latency;

    /**
     * Create an empty server record
     *
     * @return the record
     */
    public static SrvRecord empty(final int port) {
        return new SrvRecord("127.0.0.1", port, 0);
    }
}
