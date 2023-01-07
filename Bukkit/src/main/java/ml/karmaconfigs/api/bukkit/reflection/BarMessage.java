package ml.karmaconfigs.api.bukkit.reflection;

/*
 * This file is part of KarmaAPI, licensed under the MIT License.
 *
 *  Copyright (c) karma (KarmaDev) <karmaconfigs@gmail.com>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

import ml.karmaconfigs.api.bukkit.KarmaPlugin;
import ml.karmaconfigs.api.bukkit.reflection.legacy.LegacyProvider;
import ml.karmaconfigs.api.bukkit.reflection.legacy.LegacyUtil;
import ml.karmaconfigs.api.bukkit.server.BukkitServer;
import ml.karmaconfigs.api.bukkit.server.Version;
import ml.karmaconfigs.api.common.string.StringUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Action bar message
 */
public final class BarMessage {

    private final static Map<UUID, BarMessage> data = new ConcurrentHashMap<>();
    private final static Map<UUID, UUID> last_send_id = new ConcurrentHashMap<>();

    private final Player player;

    private String message;

    private boolean sent = false;
    private boolean send = false;

    private int remaining = 0;

    private final UUID bar_id = UUID.randomUUID();

    /**
     * Initialize the ActionBar class
     *
     * @param p the player
     * @param m the message
     */
    public BarMessage(final Player p, @Nullable final String m) {
        player = p;
        message = m;
    }

    /**
     * Send the action bar
     */
    private void send() {
        String msg = StringUtils.toColor(message);
        if (BukkitServer.isOver(Version.v1_7_10)) {
            try {
                Constructor<?> constructor = Objects.requireNonNull(BukkitServer.getMinecraftClass("PacketPlayOutChat")).getConstructor(BukkitServer.getMinecraftClass("IChatBaseComponent"), byte.class);

                Object icbc = Objects.requireNonNull(BukkitServer.getMinecraftClass("IChatBaseComponent")).getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + msg + "\"}");
                Object packet = constructor.newInstance(icbc, (byte) 2);
                Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
                Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);

                playerConnection.getClass().getMethod("sendPacket", BukkitServer.getMinecraftClass("Packet")).invoke(playerConnection, packet);
                sent = true;
            } catch (Throwable ex) {
                try {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, UUID.randomUUID(), TextComponent.fromLegacyText(msg));
                    sent = true;
                } catch (Throwable exc) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
                    sent = true;
                }
            }
        } else {
            Bukkit.getServer().getScheduler().runTask(KarmaPlugin.getABC(), () -> {
                last_send_id.put(player.getUniqueId(), bar_id);

                LegacyProvider provider = LegacyUtil.getProvider();
                if (provider != null) {
                    provider.displayActionbarFor(player, msg);

                    Bukkit.getServer().getScheduler().runTaskLater(KarmaPlugin.getABC(), () -> {
                        if (last_send_id.getOrDefault(player.getUniqueId(), player.getUniqueId()).equals(bar_id)) {
                            provider.displayActionbarFor(player, null);
                        }
                    }, 20 * 2);
                }
            });
        }
    }

    /**
     * Send the message until you tell it to stop
     *
     * @param persistent if the message should be persistent
     *                   until you order stopping
     */
    public void send(final boolean persistent) {
        if (player != null && player.isOnline()) {
            BarMessage stored = data.getOrDefault(player.getUniqueId(), null);
            if (stored != null) {
                stored.stop();
            }

            send = persistent;
            ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
            timer.scheduleAtFixedRate(() -> {
                if (!send) {
                    stop();
                    timer.shutdown();
                }

                send();
            }, 0, 1, TimeUnit.SECONDS);

            data.put(player.getUniqueId(), this);
        }
    }

    /**
     * Send the message the specified amount of times
     *
     * @param repeats the amount of times to send it
     */
    public void send(final int repeats) {
        if (player != null && player.isOnline()) {
            BarMessage stored = data.getOrDefault(player.getUniqueId(), null);
            if (stored != null) {
                stored.stop();
                stored.remaining = 0;
            }

            remaining = repeats;
            send = true;
            ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
            AtomicInteger repeated = new AtomicInteger(0);

            timer.scheduleAtFixedRate(() -> {
                if (!send)
                    stop();

                send();
                if (repeated.incrementAndGet() >= remaining) {
                    timer.shutdown();
                    stop();
                }
            }, 0, 1, TimeUnit.SECONDS);

            if (stored != null) {
                data.put(player.getUniqueId(), this);
            }
        }
    }

    /**
     * Update the actionbar message
     *
     * @param _message the new message
     */
    public void setMessage(final String _message) {
        message = _message;
    }

    /**
     * Stop sending the action bar
     */
    public void stop() {
        send = false;
        last_send_id.remove(player.getUniqueId());
    }

    /**
     * Check if the bar has been sent
     *
     * @return if the bar has been sent
     */
    public boolean isSent() {
        return sent;
    }
}
