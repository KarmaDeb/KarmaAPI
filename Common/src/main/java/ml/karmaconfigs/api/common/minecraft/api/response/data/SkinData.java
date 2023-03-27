package ml.karmaconfigs.api.common.minecraft.api.response.data;

import lombok.Getter;
import lombok.Value;

import java.net.URL;

/**
 * Skin data
 */
@Value(staticConstructor = "of")
public class SkinData {

    @Getter
    URL url;
    @Getter
    String value;
    @Getter
    String signature;
    @Getter
    String data;

    /**
     * Create an empty skin data
     *
     * @return an empty skin data
     */
    public static SkinData empty() {
        return SkinData.of(null, null, null, null);
    }
}
