package ml.karmaconfigs.api.common.minecraft.api.response.data;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.net.URL;

/**
 * Skin data
 */
@Value(staticConstructor = "of")
public class CapeData {

    @Getter
    URL url;
    @Getter
    String data;

    /**
     * Create an empty cape data
     *
     * @return an empty cape data
     */
    public static CapeData empty() {
        return CapeData.of(null, null);
    }
}
