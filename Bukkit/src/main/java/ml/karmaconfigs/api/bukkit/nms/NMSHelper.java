package ml.karmaconfigs.api.bukkit.nms;

import ml.karmaconfigs.api.bukkit.nms.hologram.Hologram;
import ml.karmaconfigs.api.bukkit.nms.hologram.interfaces.*;
import ml.karmaconfigs.api.bukkit.nms.hologram.line.HologramLine;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public final class NMSHelper {

    private static NetMinecraftHelper helper;

    public static void setHelper(final NetMinecraftHelper h) {
        helper = h;
    }

    public static boolean isSupported() {
        return helper != null;
    }
    public static MinecraftSlime invokeSlime(final World world, final double x, final double y, final double z, final HologramLine line) {
        return (helper != null ? helper.createSlime(world, x, y, z, line) : null);
    }

    public static MinecraftStand invokeStand(final World world, final double x, final double y, final double z, final HologramLine line) {
        return (helper != null ? helper.createStand(world, x, y, z, line) : null);
    }

    public static MinecraftHorse invokeHorse(final World world, final double x, final double y, final double z, final HologramLine line) {
        return (helper != null ? helper.createHorse(world, x, y, z, line) : null);
    }

    public static MinecraftWitherSkull invokeSkull(final World world, final double x, final double y, final double z, final HologramLine line) {
        return (helper != null ? helper.createSkull(world, x, y, z, line) : null);
    }

    public static MinecraftItem invokeItem(final World world, final double x, final double y, final double z, final HologramLine line, final ItemStack item) {
        return (helper != null ? helper.createItem(world, x, y, z, line, item) : null);

    }

    public static void invokeHologram(final Hologram hologram) {
        if (helper != null)
            helper.createHologram(hologram);
    }

    public static void revokeHologram(final Hologram hologram) {
        if (helper != null)
            helper.destroyHologram(hologram);
    }
}
