package ml.karmaconfigs.api.bukkit.legacy;

import ml.karmaconfigs.api.bukkit.legacy.horse.NMSHorse;
import ml.karmaconfigs.api.bukkit.reflection.legacy.LegacyProvider;
import ml.karmaconfigs.api.common.string.StringUtils;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class LegacyImp extends LegacyProvider {

    private final Map<UUID, NMSHorse> title_entities = new ConcurrentHashMap<>();
    private final Map<UUID, NMSHorse> subtitle_entities = new ConcurrentHashMap<>();
    private final Map<UUID, NMSHorse> actionbar_entities = new ConcurrentHashMap<>();

    private static boolean added = false;

    /**
     * Display a title to a player
     *
     * @param player   the player to show title to
     * @param title    the title
     * @param subtitle the subtitle
     */
    @Override
    public void displayTitleFor(final Player player, final String title, final String subtitle) {
        /*NMSHorse title_existing = title_entities.getOrDefault(player.getUniqueId(), null);
        NMSHorse subtitle_existing = subtitle_entities.getOrDefault(player.getUniqueId(), null);

        if (title_existing != null) {
            if (StringUtils.isNullOrEmpty(title)) {
                PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(title_existing.getId());
                for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                    CraftPlayer craft = (CraftPlayer) online;
                    craft.getHandle().playerConnection.sendPacket(destroyPacket);
                }

                Entity vehicle = title_existing.vehicle;
                if (vehicle != null) {
                    ((CraftWorld) player.getWorld()).getHandle().removeEntity(vehicle);
                }

                title_existing.die();
            } else {
                title_existing.setText(StringUtils.toColor(title));
            }
        } else {
            if (!StringUtils.isNullOrEmpty(title)) {
                Location playerLocation = player.getEyeLocation();
                 Location front_margin = playerLocation.add(playerLocation.getDirection());

                title_existing = new NMSHorse(front_margin.getWorld());
                title_existing.teleportTo(front_margin, false);

                title_existing.setText(StringUtils.toColor(title));
                NMSSkull vehicle = new NMSSkull(playerLocation.getWorld());
                vehicle.teleportTo(front_margin, false);

                ((CraftWorld) playerLocation.getWorld()).getHandle().addEntity(vehicle);
                PacketPlayOutSpawnEntityLiving spawn = new PacketPlayOutSpawnEntityLiving(title_existing);
                CraftPlayer craft = (CraftPlayer) player;
                craft.getHandle().playerConnection.sendPacket(spawn);

                title_entities.put(player.getUniqueId(), title_existing);
                title_existing.setPassengerOfNMS(vehicle);
            }
        }
        if (subtitle_existing != null) {
            if (StringUtils.isNullOrEmpty(subtitle)) {
                PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(subtitle_existing.getId());
                for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                    CraftPlayer craft = (CraftPlayer) online;
                    craft.getHandle().playerConnection.sendPacket(destroyPacket);
                }

                Entity vehicle = subtitle_existing.vehicle;
                if (vehicle != null) {
                    ((CraftWorld) player.getWorld()).getHandle().removeEntity(vehicle);
                }

                subtitle_existing.die();
            } else {
                subtitle_existing.setText(StringUtils.toColor(subtitle));
            }
        } else {
            if (!StringUtils.isNullOrEmpty(subtitle)) {
                Location playerLocation = player.getEyeLocation();
                 Location front_margin = playerLocation.add(playerLocation.getDirection());

                subtitle_existing = new NMSHorse(front_margin.getWorld());
                subtitle_existing.teleportTo(front_margin, false);

                subtitle_existing.setText(StringUtils.toColor(subtitle));

                NMSSkull vehicle = new NMSSkull(playerLocation.getWorld());
                vehicle.teleportTo(front_margin, false);

                ((CraftWorld) playerLocation.getWorld()).getHandle().addEntity(vehicle);
                PacketPlayOutSpawnEntityLiving spawn = new PacketPlayOutSpawnEntityLiving(subtitle_existing);
                CraftPlayer craft = (CraftPlayer) player;
                craft.getHandle().playerConnection.sendPacket(spawn);

                subtitle_entities.put(player.getUniqueId(), subtitle_existing);
                subtitle_existing.setPassengerOfNMS(vehicle);
            }
        }*/
    }

    /**
     * Display an actionbar for the player
     *
     * @param player the player to show actionbar to
     * @param text   the actionbar text
     */
    @Override
    public void displayActionbarFor(final Player player, final String text) {
        NMSHorse entity = actionbar_entities.getOrDefault(player.getUniqueId(), null);
        if (entity != null) {
            entity.die();
        }

        if (text != null) {
            Location loc = player.getEyeLocation();
            loc.setY(loc.getY() + loc.getY() - 1.25);

            CraftWorld cw = (CraftWorld) player.getWorld();
            entity = new NMSHorse(player.getWorld(), loc);
            cw.getHandle().addEntity(entity);

            NMSHorse finalHorse = entity;
            Bukkit.getServer().getOnlinePlayers().forEach((online) -> {
                if (!online.equals(player)) {
                    CraftPlayer cp = (CraftPlayer) online;
                    EntityPlayer connection = cp.getHandle();

                    PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(finalHorse.getId());
                    connection.playerConnection.sendPacket(destroy);
                }
            });

            actionbar_entities.put(player.getUniqueId(), entity);
            entity.setCustomNameVisible(true);
            entity.setCustomName(StringUtils.toColor(text));
        } else {
            if (entity != null) entity.die();

            actionbar_entities.remove(player.getUniqueId());
        }
    }
}
