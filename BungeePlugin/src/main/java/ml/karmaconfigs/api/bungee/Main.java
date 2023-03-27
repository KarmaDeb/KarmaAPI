package ml.karmaconfigs.api.bungee;

import ml.karmaconfigs.api.bungee.listener.JoinHandler;
import ml.karmaconfigs.api.common.karma.source.APISource;
import ml.karmaconfigs.api.common.karma.KarmaConfig;
import ml.karmaconfigs.api.common.minecraft.api.MineAPI;
import ml.karmaconfigs.api.common.timer.SchedulerUnit;
import ml.karmaconfigs.api.common.timer.SourceScheduler;
import ml.karmaconfigs.api.common.timer.scheduler.SimpleScheduler;
import ml.karmaconfigs.api.common.utils.enums.Level;
import ml.karmaconfigs.api.common.data.path.PathUtilities;
import ml.karmaconfigs.api.common.placeholder.util.Placeholder;
import ml.karmaconfigs.api.common.string.StringUtils;
import ml.karmaconfigs.api.common.version.spigot.SpigotChecker;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Server;

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
        getProxy().getPluginManager().registerListener(this, handler);

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
            Placeholder<String> serverPlaceholder = KarmaPlugin.createTextPlaceholder("minecraft_server", (player, original) -> {
                Server server = player.getServer();
                if (server != null) {
                    ServerInfo info = server.getInfo();

                    if (info != null) {
                        original = StringUtils.stripColor(info.getName());
                    }
                }
            });

            KarmaPlugin.registerPlayerPlaceholder(namePlaceholder, serverPlaceholder);
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
    }

    @Override
    public void onDisable() {

    }

    @Override
    public String updateURL() {
        return "https://www.spigotmc.org/resources/karmaapi-platform.98542/";
    }
}
