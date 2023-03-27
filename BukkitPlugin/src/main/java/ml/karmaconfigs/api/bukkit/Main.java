package ml.karmaconfigs.api.bukkit;

import ml.karmaconfigs.api.bukkit.legacy.Helper;
import ml.karmaconfigs.api.bukkit.legacy.command.TestCommand;
import ml.karmaconfigs.api.bukkit.listener.JoinHandler;
import ml.karmaconfigs.api.bukkit.modern.ModernHelper;
import ml.karmaconfigs.api.bukkit.nms.NMSHelper;
import ml.karmaconfigs.api.bukkit.old.OldHelper;
import ml.karmaconfigs.api.bukkit.reflection.BossMessage;
import ml.karmaconfigs.api.bukkit.server.BukkitServer;
import ml.karmaconfigs.api.bukkit.server.Version;
import ml.karmaconfigs.api.common.karma.source.APISource;
import ml.karmaconfigs.api.common.karma.KarmaConfig;
import ml.karmaconfigs.api.common.minecraft.api.MineAPI;
import ml.karmaconfigs.api.common.minecraft.boss.ProgressiveBar;
import ml.karmaconfigs.api.common.timer.SchedulerUnit;
import ml.karmaconfigs.api.common.timer.SourceScheduler;
import ml.karmaconfigs.api.common.timer.scheduler.SimpleScheduler;
import ml.karmaconfigs.api.common.utils.enums.Level;
import ml.karmaconfigs.api.common.data.path.PathUtilities;
import ml.karmaconfigs.api.common.placeholder.util.Placeholder;
import ml.karmaconfigs.api.common.string.StringUtils;
import ml.karmaconfigs.api.common.version.spigot.SpigotChecker;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import java.nio.file.Path;
import java.util.List;

@SuppressWarnings("unused")
public final class Main extends KarmaPlugin {

    private final SpigotChecker checker;

    public Main() {
        super(true);
        checker = new SpigotChecker(98542);
    }

    @Override
    public void enable() {
        JoinHandler handler = new JoinHandler(this);
        getServer().getPluginManager().registerEvents(handler, this);

        console().send("Registered KarmaAPI bukkit listener for panel API utilities");

        MineAPI.size().whenComplete((result, error) -> {
            if (error == null) {
                console().send("Currently there are {0} accounts in the API", Level.INFO, result);
            } else {
                logger().scheduleLog(Level.GRAVE, error);
                logger().scheduleLog(Level.INFO, "Failed to fetch KarmaAPI panel minecraft accounts");
            }
        });

        try {
            SimpleScheduler scheduler = new SourceScheduler(this, 5, SchedulerUnit.MINUTE, true).multiThreading(true);
            scheduler.restartAction(() -> {

                checker.fetchLatest().whenComplete((url, changelog, error) -> {
                    if (error == null) {
                        String latest = checker.getLatest();
                        if (latest != null) {
                            String current = version();

                            if (!latest.equals(current)) {
                                console().send("There's an update for the KarmaAPI platform. We highly recommend you to update now! ({0} -> {1})", Level.WARNING, current, latest);
                                console().send("Download from: {0}", url);
                                console().send("Changelog:", Level.INFO);
                                console().send(changelog);
                            }
                        }
                    } else {
                        logger().scheduleLog(Level.GRAVE, error);
                        logger().scheduleLog(Level.INFO, "Failed to check for updates");
                    }
                });
            });

            Placeholder<String> namePlaceholder = KarmaPlugin.createTextPlaceholder("minecraft_name", (player, original) ->
                    original = StringUtils.stripColor(player.getName())
            );
            Placeholder<String> worldPlaceholder = KarmaPlugin.createTextPlaceholder("minecraft_world", (player, original) ->
                    original = player.getWorld().getName()
            );
            Placeholder<Double> healthPlaceholder = KarmaPlugin.createDoublePlaceholder("minecraft_health", (player, original) ->
                    original = player.getHealth()
            );
            Placeholder<Double> healthScalePlaceholder = KarmaPlugin.createDoublePlaceholder("minecraft_healthScale", (player, original) ->
                    original = player.getHealthScale()
            );
            Placeholder<Double> locationXPlaceholder = KarmaPlugin.createDoublePlaceholder("minecraft_x", (player, original) ->
                    original = player.getLocation().getX()
            );
            Placeholder<Double> locationYPlaceholder = KarmaPlugin.createDoublePlaceholder("minecraft_y", (player, original) ->
                    original = player.getLocation().getY()
            );
            Placeholder<Double> locationZPlaceholder = KarmaPlugin.createDoublePlaceholder("minecraft_z", (player, original) ->
                    original = player.getLocation().getZ()
            );
            Placeholder<Float> locationYawPlaceholder = KarmaPlugin.createFloatPlaceholder("minecraft_yaw", (player, original) ->
                    original = player.getLocation().getYaw()
            );
            Placeholder<Float> locationPitchPlaceholder = KarmaPlugin.createFloatPlaceholder("minecraft_pitch", (player, original) ->
                    original = player.getLocation().getPitch()
            );
            Placeholder<Integer> foodLevel = KarmaPlugin.createIntegerPlaceholder("minecraft_food", (player, original) ->
                    original = player.getFoodLevel()
            );

            KarmaPlugin.registerPlayerPlaceholder(namePlaceholder, worldPlaceholder, healthPlaceholder, healthScalePlaceholder, locationXPlaceholder, locationYPlaceholder, locationZPlaceholder, locationYawPlaceholder, locationPitchPlaceholder, foodLevel);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        KarmaConfig config = new KarmaConfig();
        if (config.printLicense()) {
            config.setPrintLicense(false);
            Path file = APISource.getOriginal(true).saveResource(Main.class.getResourceAsStream("/license.txt"), "license.txt");
            List<String> lines = PathUtilities.readAllLines(file);
            getLogger().warning("PLEASE READ CAREFULLY");
            for (String str : lines) {
                console().send(StringUtils.stripColor(str)); //We'll use provided logger for this...
            }

            console().send("\n&eContinuing in 10 seconds");
            synchronized (Thread.currentThread()) {
                try {
                    Thread.sleep(10000);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            }
        }

        PluginCommand test_boss = getCommand("test_boss");
        if (test_boss != null) {
            test_boss.setExecutor(((commandSender, command, s, strings) -> {
                if (commandSender.isOp()) {
                    if (commandSender instanceof Player) {
                        Player player = (Player) commandSender;
                        String message = "&cTest BossBar";
                        double duration = 10;
                        try {
                            duration = Double.parseDouble(strings[0]);
                            try {
                                StringBuilder builder = new StringBuilder();
                                for (int i = 1; i < strings.length; i++) {
                                    builder.append(strings[i]).append(" ");
                                }

                                message = builder.substring(0, builder.length() - 1);
                            } catch (Throwable ignored) {}
                        } catch (Throwable ex) {
                            try {
                                StringBuilder builder = new StringBuilder();
                                for (String string : strings) {
                                    builder.append(string).append(" ");
                                }

                                message = builder.substring(0, builder.length() - 1);
                            } catch (Throwable ignored) {}
                        }

                        BossMessage boss = new BossMessage(this, message, duration);
                        if (player.isSneaking()) {
                            boss.progress(ProgressiveBar.DOWN);
                        } else {
                            if (player.isFlying()) {
                                boss.progress(ProgressiveBar.UP);
                            }
                        }
                        boss.scheduleBar(player);
                    }
                }

                return false;
            }));
        }


        if (BukkitServer.isUnder(Version.v1_8)) {
            console().send("Initializing legacy support for titles and actionbars. This will create invisible entities in front of the player.", Level.WARNING);
            NMSHelper.setHelper(new Helper());
            PluginCommand test_legacy = getCommand("test_legacy");
            if (test_legacy != null) test_legacy.setExecutor(new TestCommand());
        } else {
            PluginCommand test_legacy = getCommand("test_legacy");
            if (test_legacy != null) {
                test_legacy.setExecutor((commandSender, command, s, strings) -> {
                    if (commandSender.isOp()) {
                        commandSender.sendMessage(StringUtils.toColor("&cNot legacy server!"));
                    }

                    return false;
                });
            }

            if (BukkitServer.isUnder(Version.v1_18)) {
                OldHelper old = OldHelper.getInstance();
                if (old != null) {
                    NMSHelper.setHelper(old);
                }
            } else {
                ModernHelper modern = ModernHelper.getInstance();
                if (modern != null) {
                    NMSHelper.setHelper(modern);
                }
            }
        }
    }

    @Override
    public void onDisable() {

    }

    @Override
    public String updateURL() {
        return "https://www.spigotmc.org/resources/karmaapi-platform.98542/";
    }
}
