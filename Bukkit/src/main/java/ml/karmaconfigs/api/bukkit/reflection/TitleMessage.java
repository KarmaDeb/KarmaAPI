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
import ml.karmaconfigs.api.bukkit.nms.NMSHelper;
import ml.karmaconfigs.api.bukkit.nms.hologram.Hologram;
import ml.karmaconfigs.api.bukkit.nms.hologram.HologramHolder;
import ml.karmaconfigs.api.bukkit.server.BukkitServer;
import ml.karmaconfigs.api.bukkit.server.Version;
import ml.karmaconfigs.api.common.string.StringUtils;
import ml.karmaconfigs.api.common.timer.SchedulerUnit;
import ml.karmaconfigs.api.common.timer.SourceScheduler;
import ml.karmaconfigs.api.common.timer.scheduler.SimpleScheduler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Title message
 */
public final class TitleMessage {

    private final Player player;
    private final String title, subtitle;

    private final static Map<UUID, Hologram> title_holograms = new ConcurrentHashMap<>();

    /**
     * Initialize the title class
     *
     * @param p the player
     * @param t the title text
     * @param s the subtitle text
     */
    public TitleMessage(final Player p, @Nullable String t, @Nullable String s) {
        player = p;
        if (t == null)
            t = "";
        title = StringUtils.toColor(t);
        if (s == null)
            s = "";
        subtitle = StringUtils.toColor(s);
    }

    /**
     * Send the title
     */
    public void send() {
        if (BukkitServer.isOver(Version.v1_8)) {
            try {
                Object chatTitle = Objects.requireNonNull(BukkitServer.getMinecraftClass("IChatBaseComponent")).getDeclaredClasses()[0].getMethod("a", String.class)
                        .invoke(null, "{\"text\": \"" + title + "\"}");
                Constructor<?> titleConstructor = Objects.requireNonNull(BukkitServer.getMinecraftClass("PacketPlayOutTitle")).getConstructor(
                        Objects.requireNonNull(BukkitServer.getMinecraftClass("PacketPlayOutTitle")).getDeclaredClasses()[0], BukkitServer.getMinecraftClass("IChatBaseComponent"),
                        int.class, int.class, int.class);
                Object packet = titleConstructor.newInstance(
                        Objects.requireNonNull(BukkitServer.getMinecraftClass("PacketPlayOutTitle")).getDeclaredClasses()[0].getField("TITLE").get(null), chatTitle,
                        20 * 2, 20 * 5, 20 * 2);

                Object chatsTitle = Objects.requireNonNull(BukkitServer.getMinecraftClass("IChatBaseComponent")).getDeclaredClasses()[0].getMethod("a", String.class)
                        .invoke(null, "{\"text\": \"" + subtitle + "\"}");
                Constructor<?> timingTitleConstructor = Objects.requireNonNull(BukkitServer.getMinecraftClass("PacketPlayOutTitle")).getConstructor(
                        Objects.requireNonNull(BukkitServer.getMinecraftClass("PacketPlayOutTitle")).getDeclaredClasses()[0], BukkitServer.getMinecraftClass("IChatBaseComponent"),
                        int.class, int.class, int.class);
                Object timingPacket = timingTitleConstructor.newInstance(
                        Objects.requireNonNull(BukkitServer.getMinecraftClass("PacketPlayOutTitle")).getDeclaredClasses()[0].getField("SUBTITLE").get(null), chatsTitle,
                        20 * 2, 20 * 5, 20 * 2);

                Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
                Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);

                playerConnection.getClass().getMethod("sendPacket", BukkitServer.getMinecraftClass("Packet")).invoke(playerConnection, packet);
                playerConnection.getClass().getMethod("sendPacket", BukkitServer.getMinecraftClass("Packet")).invoke(playerConnection, timingPacket);
            } catch (Throwable ex) {
                player.sendTitle(title, subtitle, 20 * 2, 20 * 5, 20 * 2);
            }
        } else {
            if (title_holograms.containsKey(player.getUniqueId())) {
                Hologram existing = title_holograms.getOrDefault(player.getUniqueId(), null);
                if (existing != null) existing.delete();
            }

            //Display title and subtitle using entities
            Location location = player.getEyeLocation().clone();
            Vector front = location.getDirection().multiply(2);
            location.add(front).add(0, 0.25, 0);

            Hologram title_hologram = new HologramHolder(location);
            title_hologram.show(player);
            title_hologram.append(StringUtils.toColor(title));
            title_hologram.append(StringUtils.toColor(subtitle));

            NMSHelper.invokeHologram(title_hologram);
            title_holograms.put(player.getUniqueId(), title_hologram);

            KarmaPlugin plugin = KarmaPlugin.getABC();
            SimpleScheduler scheduler = new SourceScheduler(plugin, 5000, SchedulerUnit.MILLISECOND, false);
            scheduler.changeAction((second) -> Bukkit.getServer().getScheduler().runTask(plugin, () -> {
                if (title_hologram.exists()) {
                    Location new_location = player.getEyeLocation().clone();
                    Vector new_front = new_location.getDirection().multiply(2);
                    new_location.add(new_front).add(0, 0.25, 0);

                    title_hologram.teleport(new_location);
                } else {
                    scheduler.cancel();
                }
            }));
            scheduler.endAction(() -> {
                title_hologram.delete();
                title_holograms.remove(player.getUniqueId());
            });
            scheduler.start();
        }
    }

    /**
     * Send the title
     *
     * @param showIn the time that will take to
     *               completely show the title
     * @param keepIn the time to keep in
     * @param hideIn the time that will take to
     *               completely hide the title
     */
    public void send(final int showIn, final int keepIn, final int hideIn) {
        if (BukkitServer.isOver(Version.v1_8)) {
            try {
                Object chatTitle = Objects.requireNonNull(BukkitServer.getMinecraftClass("IChatBaseComponent")).getDeclaredClasses()[0].getMethod("a", String.class)
                        .invoke(null, "{\"text\": \"" + title + "\"}");
                Constructor<?> titleConstructor = Objects.requireNonNull(BukkitServer.getMinecraftClass("PacketPlayOutTitle")).getConstructor(
                        Objects.requireNonNull(BukkitServer.getMinecraftClass("PacketPlayOutTitle")).getDeclaredClasses()[0], BukkitServer.getMinecraftClass("IChatBaseComponent"),
                        int.class, int.class, int.class);
                Object packet = titleConstructor.newInstance(
                        Objects.requireNonNull(BukkitServer.getMinecraftClass("PacketPlayOutTitle")).getDeclaredClasses()[0].getField("TITLE").get(null), chatTitle,
                        20 * showIn, 20 * keepIn, 20 * hideIn);

                Object chatsTitle = Objects.requireNonNull(BukkitServer.getMinecraftClass("IChatBaseComponent")).getDeclaredClasses()[0].getMethod("a", String.class)
                        .invoke(null, "{\"text\": \"" + subtitle + "\"}");
                Constructor<?> timingTitleConstructor = Objects.requireNonNull(BukkitServer.getMinecraftClass("PacketPlayOutTitle")).getConstructor(
                        Objects.requireNonNull(BukkitServer.getMinecraftClass("PacketPlayOutTitle")).getDeclaredClasses()[0], BukkitServer.getMinecraftClass("IChatBaseComponent"),
                        int.class, int.class, int.class);
                Object timingPacket = timingTitleConstructor.newInstance(
                        Objects.requireNonNull(BukkitServer.getMinecraftClass("PacketPlayOutTitle")).getDeclaredClasses()[0].getField("SUBTITLE").get(null), chatsTitle,
                        20 * showIn, 20 * keepIn, 20 * hideIn);

                Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
                Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);

                playerConnection.getClass().getMethod("sendPacket", BukkitServer.getMinecraftClass("Packet")).invoke(playerConnection, packet);
                playerConnection.getClass().getMethod("sendPacket", BukkitServer.getMinecraftClass("Packet")).invoke(playerConnection, timingPacket);
            } catch (Throwable ex) {
                player.sendTitle(title, subtitle, 20 * showIn, 20 * keepIn, 20 * hideIn);
            }
        } else {
            if (title_holograms.containsKey(player.getUniqueId())) {
                Hologram existing = title_holograms.getOrDefault(player.getUniqueId(), null);
                if (existing != null) existing.delete();
            }

            //Display title and subtitle using entities
            Location location = player.getEyeLocation().clone();
            Vector front = location.getDirection().multiply(2);
            location.add(front).add(0, 0.25, 0);

            Hologram title_hologram = new HologramHolder(location);
            title_hologram.show(player);
            title_hologram.append(StringUtils.toColor(title));
            title_hologram.append(StringUtils.toColor(subtitle));

            NMSHelper.invokeHologram(title_hologram);
            title_holograms.put(player.getUniqueId(), title_hologram);

            KarmaPlugin plugin = KarmaPlugin.getABC();
            SimpleScheduler scheduler = new SourceScheduler(plugin, TimeUnit.SECONDS.toMillis(keepIn), SchedulerUnit.MILLISECOND, false);
            scheduler.changeAction((second) -> Bukkit.getServer().getScheduler().runTask(plugin, () -> {
                if (title_hologram.exists()) {
                    Location new_location = player.getEyeLocation().clone();
                    Vector new_front = new_location.getDirection().multiply(2);
                    new_location.add(new_front).add(0, 0.25, 0);

                    title_hologram.teleport(new_location);
                } else {
                    scheduler.cancel();
                }
            }));
            scheduler.endAction(() -> {
                title_hologram.delete();
                title_holograms.remove(player.getUniqueId());
            });
            scheduler.start();
        }
    }
}
