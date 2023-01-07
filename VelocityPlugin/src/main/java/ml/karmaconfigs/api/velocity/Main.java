package ml.karmaconfigs.api.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import ml.karmaconfigs.api.common.karma.source.APISource;
import ml.karmaconfigs.api.common.karma.source.Identifiable;
import ml.karmaconfigs.api.common.karma.KarmaConfig;
import ml.karmaconfigs.api.common.karma.source.KarmaSource;
import ml.karmaconfigs.api.common.karma.file.KarmaMain;
import ml.karmaconfigs.api.common.karma.file.element.KarmaElement;
import ml.karmaconfigs.api.common.karma.file.element.KarmaObject;
import ml.karmaconfigs.api.common.timer.SchedulerUnit;
import ml.karmaconfigs.api.common.timer.SourceScheduler;
import ml.karmaconfigs.api.common.timer.scheduler.SimpleScheduler;
import ml.karmaconfigs.api.common.utils.enums.Level;
import ml.karmaconfigs.api.common.data.path.PathUtilities;
import ml.karmaconfigs.api.common.security.token.TokenGenerator;
import ml.karmaconfigs.api.common.string.StringUtils;
import ml.karmaconfigs.api.common.utils.uuid.UUIDUtil;
import ml.karmaconfigs.api.common.version.spigot.SpigotChecker;
import ml.karmaconfigs.api.velocity.listener.JoinHandler;
import ml.karmaconfigs.api.velocity.loader.VelocityBridge;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.nio.file.Path;
import java.util.List;

@Plugin(
        id = "anotherbarelycodedkarmaplugin",
        name = "ABCKarmaPlugin",
        version = "1.3.3-13",
        description = "A plugin that creates a bridge between the KarmaAPI and Bukkit",
        url = "https://karmaconfigs.ml",
        authors = {"KarmaDev"}
)
public class Main implements KarmaSource, Identifiable {

    private final ProxyServer instance;
    private final Logger logger;
    private final SpigotChecker checker;

    private VelocityBridge bridge;
    private String version = "1.3.3-7";

    private String plugin_identifier = TokenGenerator.generateToken();

    @Inject
    public Main(ProxyServer server, Logger logs) {
        instance = server;
        logger = logs;
        checker = new SpigotChecker(98542);

        loadIdentifier("DEFAULT");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        instance.getPluginManager().getPlugin("anotherbarelycodedkarmaplugin").ifPresent(container -> container.getDescription().getVersion().ifPresent((containerVersion) -> {
            version = containerVersion;

            JoinHandler handler = new JoinHandler(this);
            instance.getEventManager().register(container, handler);

            console().send("Registered KarmaAPI bukkit listener for panel API utilities");

            UUIDUtil.getStored().whenComplete((result, error) -> {
                if (error == null) {
                    console().send("Currently there are {0} accounts in the API", Level.INFO, result);
                } else {
                    logger().scheduleLog(Level.GRAVE, error);
                    logger().scheduleLog(Level.INFO, "Failed to fetch KarmaAPI panel minecraft accounts");
                }
            });

            bridge = new VelocityBridge(this, instance, container);
            bridge.start();

            SimpleScheduler scheduler = new SourceScheduler(this, 5, SchedulerUnit.MINUTE, true).multiThreading(true);
            scheduler.restartAction(() -> {
                checker.getUpdateURL().whenComplete((url, error) -> {
                    if (error == null) {
                        String latest = checker.getLatest();
                        String current = version();

                        if (!latest.equals(current)) {
                            console().send("There's an update for the KarmaAPI platform. We highly recommend you to update now! ({0} -> {1})", Level.WARNING, current, latest);
                            console().send("Download from: {0}", url);
                        }
                    } else {
                        logger().scheduleLog(Level.GRAVE, error);
                        logger().scheduleLog(Level.INFO, "Failed to check for updates");
                    }
                });
            });

            KarmaConfig config = new KarmaConfig();
            if (config.printLicense()) {
                Path file = APISource.getOriginal(true).saveResource(Main.class.getResourceAsStream("/license.txt"), "license.txt");
                List<String> lines = PathUtilities.readAllLines(file);
                logger.warn("PLEASE READ CAREFULLY");
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
        }));
    }

    @Subscribe
    public void onProxyInitialization(ProxyShutdownEvent e) {
        if (bridge != null)
            bridge.stop();
    }

    @Override
    public String name() {
        return "AnotherBarelyCodedKarmaPlugin";
    }

    @Override
    public String version() {
        return version;
    }

    @Override
    public String description() {
        return "A plugin that creates a bridge between the KarmaAPI and Bukkit";
    }

    @Override
    public String[] authors() {
        return new String[]{"KarmaDev"};
    }

    @Override
    public String updateURL() {
        return "https://www.spigotmc.org/resources/karmaapi-platform.98542/";
    }

    /**
     * Get the current identifier
     *
     * @return the current identifier
     */
    @Override
    public String getIdentifier() {
        return plugin_identifier;
    }

    /**
     * Store the identifier
     *
     * @param name the identifier name
     * @return if the identifier could be stored
     */
    @Override
    public boolean storeIdentifier(final String name) {
        KarmaMain main = new KarmaMain(APISource.getOriginal(true), "identifiers.kf");
        if (!main.exists())
            main.create();

        main.set(name, new KarmaObject(plugin_identifier));

        return main.save();
    }

    /**
     * Load an identifier
     *
     * @param name the identifier name
     */
    @Override
    public void loadIdentifier(final String name) {
        KarmaMain main = new KarmaMain(APISource.getOriginal(true), "identifiers.kf");
        if (!main.exists())
            main.create();

        if (main.isSet(name)) {
            KarmaElement element = main.get(name);
            if (element.isString()) {
                plugin_identifier = element.getObjet().getString();
            }
        }
    }
}
