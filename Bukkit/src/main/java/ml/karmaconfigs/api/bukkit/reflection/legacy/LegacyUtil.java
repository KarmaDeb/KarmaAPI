package ml.karmaconfigs.api.bukkit.reflection.legacy;

import org.jetbrains.annotations.Nullable;

public final class LegacyUtil {

    private static LegacyProvider provider;

    public static void setProvider(final LegacyProvider l) {
        if (provider == null)
            provider = l;
    }

    /**
     * Get the legacy provider
     *
     * @return the legacy provider
     */
    @Nullable
    public static LegacyProvider getProvider() {
        return provider;
    }
}
