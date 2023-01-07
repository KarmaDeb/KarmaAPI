package ml.karmaconfigs.api.velocity.listener;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.GameProfileRequestEvent;
import com.velocitypowered.api.util.GameProfile;
import ml.karmaconfigs.api.common.karma.source.Identifiable;
import ml.karmaconfigs.api.common.karma.source.KarmaSource;
import ml.karmaconfigs.api.common.string.StringUtils;
import ml.karmaconfigs.api.common.utils.uuid.UUIDUtil;

public class JoinHandler {

    private final Identifiable plugin;

    public JoinHandler(final Identifiable owner) {
        plugin = owner;
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onJoin(GameProfileRequestEvent e) {
        GameProfile original = e.getOriginalProfile();
        GameProfile current = e.getGameProfile();
        String userName = e.getUsername();

        ((KarmaSource) plugin).async().queue("oka_register_client", () -> {
            if (!StringUtils.isNullOrEmpty(original)) {
                UUIDUtil.registerMinecraftClient(plugin, original.getName());
            }
            if (!StringUtils.isNullOrEmpty(current)) {
                UUIDUtil.registerMinecraftClient(plugin, current.getName());
            }
            if (!StringUtils.isNullOrEmpty(userName)) {
                UUIDUtil.registerMinecraftClient(plugin, userName);
            }
        });
    }
}
