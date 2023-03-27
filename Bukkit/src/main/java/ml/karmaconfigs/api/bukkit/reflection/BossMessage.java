package ml.karmaconfigs.api.bukkit.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import ml.karmaconfigs.api.bukkit.KarmaPlugin;
import ml.karmaconfigs.api.bukkit.server.BukkitServer;
import ml.karmaconfigs.api.bukkit.server.Version;
import ml.karmaconfigs.api.common.karma.source.KarmaSource;
import ml.karmaconfigs.api.common.minecraft.boss.*;
import ml.karmaconfigs.api.common.string.StringUtils;
import ml.karmaconfigs.api.common.timer.SchedulerUnit;
import ml.karmaconfigs.api.common.timer.SourceScheduler;
import ml.karmaconfigs.api.common.timer.TimeCondition;
import ml.karmaconfigs.api.common.timer.scheduler.SimpleScheduler;
import ml.karmaconfigs.api.common.utils.enums.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

/**
 * Boss message
 */
@SuppressWarnings("unused")
public final class BossMessage extends BossProvider<Player> {

    private final KarmaSource source;

    private String message;

    private boolean health = false;
    private final double live_time;

    private static boolean legacy;

    private static Class<?> craft_world;

    private static Class<?> craft_player;

    private static Class<?> packet_connection;

    private static Class<?> packet_play_out_destroy;

    private static Constructor<?> wither_constructor;

    private static Constructor<?> entity_living_constructor;

    private static Constructor<?> packet_play_teleport_constructor;
    private static Constructor<?> packet_play_metadata;

    private static Method craft_world_handle;

    private static Method craft_player_handle;

    private static Method packet_connect_send;

    private static Method wither_set_location_method;

    private static Method wither_set_progress_method;

    private static final Map<UUID, SimpleScheduler> bar_schedulers = new ConcurrentHashMap<>();
    private static final Map<UUID, Integer> player_bars = new ConcurrentHashMap<>();
    private static final Map<UUID, Queue<BossMessage>> b_bars = new ConcurrentHashMap<>();

    private static final Map<Integer, BossMessage> boss_bars = new ConcurrentHashMap<>();

    private static final Map<Integer, Object> wither_objects = new ConcurrentHashMap<>();


    private BossColor color = BossColor.PURPLE;

    private BossType type = BossType.SOLID;

    private ProgressiveBar progress = ProgressiveBar.NONE;

    private double lived_time = 0.0D;

    private boolean cancelled = false;

    private static int total_ids = 0;

    private final int id;

    private SimpleScheduler bar_timer = null;

    /**
     * Initialize the boss message
     *
     * @param owner the message owner
     * @param _message the message
     * @param duration the message duration
     */
    public BossMessage(final KarmaSource owner, final String _message, final double duration) {
        source = owner;
        message = _message;
        live_time = duration;
        legacy = BukkitServer.isUnder(Version.v1_14);

        if (legacy)
            doReflectionStuff();

        id = ++total_ids;
    }

    /**
     * Set the boss bar color
     *
     * @param color_style the boss bar color
     * @return this boss bar instance
     */
    public BossMessage color(final BossColor color_style) {
        color = color_style;
        return this;
    }

    /**
     * Set the boss bar style
     *
     * @param type_style the boss bar style
     * @return this boss bar instance
     */
    @Override
    public BossProvider<Player> style(final BossType type_style) {
        type = type_style;
        return this;
    }

    /**
     * Set the boss bar progress type
     *
     * @param progress_style the boss bar progress type
     * @return this boss bar instance
     */
    @Override
    public BossProvider<Player> progress(final ProgressiveBar progress_style) {
        progress = progress_style;
        return this;
    }

    /**
     * Destroy the current boss bar
     */
    @Override
    public void cancel() {
        cancelled = true;
        if (bar_timer != null) {
            bar_timer.cancel();
        }
    }

    /**
     * Display the boss bar to the specified player
     *
     * @param target the player to display to
     */
    @Override
    protected void displayBar(final Player target) {
        if (cancelled)
            cancelled = false;

        switch (progress) {
            case UP:
                lived_time = 0d;
                break;
            case DOWN:
                lived_time = live_time;
                break;
        }

        final KarmaPlugin plugin = KarmaPlugin.getABC();
        if (legacy) {
            try {
                int init_bars = player_bars.getOrDefault(target.getUniqueId(), 0);
                init_bars++;

                player_bars.put(target.getUniqueId(), init_bars);

                Location location = target.getLocation();
                Object c_world = craft_world.cast(location.getWorld());
                Object world_server = craft_world_handle.invoke(c_world);

                Object wither = wither_constructor.newInstance(world_server);
                Object c_player = craft_player.cast(target);
                Object entity_player = craft_player_handle.invoke(c_player);

                Field playerConnection = entity_player.getClass().getField("playerConnection");
                Object connection = playerConnection.get(entity_player);

                Method setInvisible = wither.getClass().getMethod("setInvisible", boolean.class);
                Method setVisible = wither.getClass().getMethod("setCustomNameVisible", boolean.class);
                Method setLocation = wither.getClass().getMethod("setLocation", double.class, double.class, double.class, float.class, float.class);
                setCustomName(wither, message);

                setVisible.invoke(wither, true);
                setInvisible.invoke(wither, false);
                setLocation.invoke(wither, location.getX(), location.getY() - 10, location.getZ(), location.getYaw(), location.getPitch());

                Object packetPlayOutEntityLiving = entity_living_constructor.newInstance(wither);

                packet_connect_send.invoke(packet_connection.cast(connection), packetPlayOutEntityLiving);
                try {
                    Method getId = wither.getClass().getMethod("getId");
                    Method getDataWatcher = wither.getClass().getMethod("getDataWatcher");
                    int id = (int) getId.invoke(wither);
                    Object dataWatcher = getDataWatcher.invoke(wither);

                    Object packetPlayOutEntityMetadata = packet_play_metadata.newInstance(id, dataWatcher, true);
                    packet_connect_send.invoke(packet_connection.cast(connection), packetPlayOutEntityMetadata); //Apply invisibility
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }

                wither_objects.put(id, wither);
                bar_timer = new SourceScheduler(source, live_time, SchedulerUnit.SECOND, false);
                bar_timer.condition(TimeCondition.OVER_OF, 2, (time) -> plugin.getServer().getScheduler().runTask(plugin, () -> {
                    try {
                        Location new_location = target.getLocation().clone().add(target.getLocation().getDirection().multiply(20));

                        wither_set_location_method.invoke(wither,
                                new_location.getX(),
                                new_location.getY()  - 10,
                                new_location.getZ(),

                                new_location.getYaw(),
                                new_location.getPitch());

                        Object packetPlayOutEntityTeleport = packet_play_teleport_constructor.newInstance(wither);
                        packet_connect_send.invoke(packet_connection.cast(connection), packetPlayOutEntityTeleport);
                    } catch (Throwable ex) {
                        bar_timer.cancel();
                        source.logger().scheduleLog(Level.GRAVE, ex);
                        source.logger().scheduleLog(Level.INFO, "Failed to run task on boss bar display");

                        bar_timer.cancel();
                    }
                })).endAction(() -> plugin.getServer().getScheduler().runTask(plugin, () -> {
                    try {
                        cancelled = true;
                        Constructor<?> packetPlayOutEntityDestroyConstructor = packet_play_out_destroy.getConstructor(int[].class);
                        Method getId = wither.getClass().getMethod("getId");

                        int id = (int) getId.invoke(wither);
                        Object packetPlayOutEntityDestroy = packetPlayOutEntityDestroyConstructor.newInstance(new int[]{id});
                        if (target.isOnline()) {
                            packet_connect_send.invoke(packet_connection.cast(connection), packetPlayOutEntityDestroy);
                        }

                        boss_bars.remove(id);
                        wither_objects.remove(id);
                        int bars = player_bars.getOrDefault(target.getUniqueId(), 0);
                        if (bars > 0)
                            bars--;

                        player_bars.put(target.getUniqueId(), bars);
                    } catch (Throwable ex) {
                        bar_timer.cancel();
                        source.logger().scheduleLog(Level.GRAVE, ex);
                        source.logger().scheduleLog(Level.INFO, "Failed to run task on boss bar end");
                    }
                })).cancelAction((when) -> plugin.getServer().getScheduler().runTask(plugin, () -> {
                    try {
                        cancelled = true;
                        Constructor<?> packetPlayOutEntityDestroyConstructor = packet_play_out_destroy.getConstructor(int[].class);
                        Method getId = wither.getClass().getMethod("getId");

                        int id = (int) getId.invoke(wither);
                        Object packetPlayOutEntityDestroy = packetPlayOutEntityDestroyConstructor.newInstance(new int[]{id});
                        if (target.isOnline()) {
                            packet_connect_send.invoke(packet_connection.cast(connection), packetPlayOutEntityDestroy);
                        }

                        boss_bars.remove(id);
                        wither_objects.remove(id);
                        int bars = player_bars.getOrDefault(target.getUniqueId(), 0);
                        if (bars > 0)
                            bars--;

                        player_bars.put(target.getUniqueId(), bars);
                    } catch (Throwable ex) {
                        bar_timer.cancel();
                        source.logger().scheduleLog(Level.GRAVE, ex);
                        source.logger().scheduleLog(Level.INFO, "Failed to run task on boss bar end");
                    }
                }));

                SimpleScheduler hp_timer = new SourceScheduler(source, 1, SchedulerUnit.SECOND, true);
                hp_timer.restartAction(() -> plugin.getServer().getScheduler().runTask(plugin, () -> {
                    if (!cancelled && target.isOnline()) {
                        try {
                            double percentage = 1000;
                            switch (progress) {
                                case UP:
                                    percentage = lived_time / live_time;
                                    lived_time++;
                                    break;
                                case DOWN:
                                    percentage = lived_time / live_time;
                                    lived_time--;
                                    break;
                            }
                            percentage = Math.max(0, percentage);
                            if (health) {
                                percentage *= 300;
                            }

                            try {
                                wither_set_progress_method.invoke(wither, percentage);
                            } catch (Throwable ex) {
                                try {
                                    wither_set_progress_method.invoke(wither, (float) percentage);
                                } catch (Throwable ex2) {
                                    source.logger().scheduleLog(Level.GRAVE, ex2);
                                    source.logger().scheduleLog(Level.INFO, "Failed to change health/progress to boss bar");
                                    return;
                                }
                            }

                            try {
                                Method getId = wither.getClass().getMethod("getId");
                                Method getDataWatcher = wither.getClass().getMethod("getDataWatcher");
                                int id = (int) getId.invoke(wither);
                                Object dataWatcher = getDataWatcher.invoke(wither);

                                Object packetPlayOutEntityMetadata = packet_play_metadata.newInstance(id, dataWatcher, true);
                                packet_connect_send.invoke(packet_connection.cast(connection), packetPlayOutEntityMetadata);
                            } catch (Throwable ex) {
                                ex.printStackTrace();
                            }
                        } catch (Throwable ex) {
                            bar_timer.cancel();
                            hp_timer.cancel();
                            source.logger().scheduleLog(Level.GRAVE, ex);
                            source.logger().scheduleLog(Level.INFO, "Failed to run task on boss bar HP modifier");
                        }
                    } else {
                        bar_timer.cancel();
                        hp_timer.cancel();
                    }
                }));

                bar_timer.start();
                hp_timer.start();
            } catch (Throwable ex) {
                source.logger().scheduleLog(Level.GRAVE, ex);
                source.logger().scheduleLog(Level.INFO, "Failed to display bar");
            }
        } else {
            BossBar wither = Bukkit.getServer().createBossBar(
                    StringUtils.toColor(message),
                    BarColor.valueOf(color.name()),
                    BarStyle.valueOf(type.name()));
            wither.addPlayer(target);

            wither.setVisible(true);
            wither_objects.put(id, wither);

            bar_timer = new SourceScheduler(source, live_time, SchedulerUnit.SECOND, false);
            bar_timer.endAction(() -> plugin.getServer().getScheduler().runTask(plugin, () -> {
                cancelled = true;
                boss_bars.remove(id);
                wither_objects.remove(id);

                wither.removePlayer(target);
                int bars = player_bars.getOrDefault(target.getUniqueId(), 0);
                if (bars > 0)
                    bars--;

                player_bars.put(target.getUniqueId(), bars);
                wither.setVisible(false);
            })).cancelAction((when) -> plugin.getServer().getScheduler().runTask(plugin, () -> {
                cancelled = true;
                boss_bars.remove(id);
                wither_objects.remove(id);

                wither.removePlayer(target);
                int bars = player_bars.getOrDefault(target.getUniqueId(), 0);
                if (bars > 0)
                    bars--;

                player_bars.put(target.getUniqueId(), bars);
                wither.setVisible(false);
            }));

            SimpleScheduler progress_scheduler = new SourceScheduler(source, 1, SchedulerUnit.SECOND, true);
            progress_scheduler.restartAction(() -> plugin.getServer().getScheduler().runTask(plugin, () -> {
                if (!cancelled && target.isOnline()) {
                    try {
                        wither.setColor(BarColor.valueOf(color.name()));
                        wither.setStyle(BarStyle.valueOf(type.name()));

                        double percentage = 1;
                        switch (progress) {
                            case UP:
                                percentage = lived_time / live_time;
                                lived_time++;
                                break;
                            case DOWN:
                                percentage = lived_time / live_time;
                                lived_time--;
                                break;
                        }

                        wither.setProgress(Math.max(0, percentage));
                    } catch (Throwable ex) {
                        bar_timer.cancel();
                        progress_scheduler.cancel();
                        source.logger().scheduleLog(Level.GRAVE, ex);
                        source.logger().scheduleLog(Level.INFO, "Failed to run task on boss bar HP modifier");
                    }
                } else {
                    bar_timer.cancel();
                    progress_scheduler.cancel();
                }
            }));

            bar_timer.start();
            progress_scheduler.start();
        }
    }

    /**
     * Display the boss bar to the specified players
     *
     * @param players the players to display to
     * @deprecated This can result in bar duplicates; Use {@link BossProvider#displayBar(Object) single target} alternative
     */
    @Deprecated
    @Override
    @SuppressWarnings("all")
    protected void displayBar(final Collection<Player> players) {
        players.forEach(this::displayBar);
    }

    /**
     * Schedule the bar to the specified players
     *
     * @param players the players to display to
     */
    @Override
    public void scheduleBar(final Collection<Player> players) {
        scheduleBar(players.toArray(new Player[0]));
    }

    /**
     * Schedule the bar to the specified player
     *
     * @param players the player to display to
     */
    @Override
    public void scheduleBar(final Player... players) {
        for (Player player : players) {
            Queue<BossMessage> player_b_bars = b_bars.getOrDefault(player.getUniqueId(), new ConcurrentLinkedQueue<>());
            player_b_bars.add(this);
            b_bars.put(player.getUniqueId(), player_b_bars);
            boss_bars.put(id, this);

            /*
                if (!player_b_bars.isEmpty() && bars < (legacy ? 1 : 4)) {
                    BossMessage boss = player_b_bars.get(0);
                    boss.displayBar(players);
                    player_b_bars.remove(boss);
                    b_bars.put(player.getUniqueId(), player_b_bars);
                } else {
                    if (player_b_bars.isEmpty()) {
                        scheduler.cancel();
                    }
                }*/

            int max_bars = (legacy ? 1 : 4);
            SimpleScheduler scheduler = bar_schedulers.getOrDefault(player.getUniqueId(), null);
            if (scheduler == null) {
                scheduler = new SourceScheduler(source, 1, SchedulerUnit.SECOND, true);
                scheduler.restartAction(() -> {
                    Player instance = Bukkit.getPlayer(player.getUniqueId());
                    if (instance != null && instance.isOnline()) {
                        int bars = player_bars.getOrDefault(player.getUniqueId(), 0);
                        if (bars < max_bars) {
                            Queue<BossMessage> queue = b_bars.getOrDefault(player.getUniqueId(), new ConcurrentLinkedQueue<>());
                            BossMessage next = queue.poll();
                            if (next != null) {
                                next.displayBar(instance);
                            }
                        }
                    }
                }).start();

                bar_schedulers.put(player.getUniqueId(), scheduler);
            }
        }
    }

    /**
     * Get the amount of bars that exist
     *
     * @return the amount of bars created
     */
    @Deprecated
    public int getBarsAmount() {
        AtomicInteger final_size = new AtomicInteger();
        player_bars.values().forEach(final_size::addAndGet);
        return final_size.get();
    }

    /**
     * Get the amount of bars that exist
     *
     * @param source the bar source
     * @return the amount of bars created
     */
    @Override
    public int getBarsAmount(final Player source) {
        return player_bars.getOrDefault(source.getUniqueId(), 0);
    }

    /**
     * Get the current boss bar id
     *
     * @return the current boss bar id
     */
    @Override
    public int getBarId() {
        return id;
    }

    /**
     * Update the boss bar
     *
     * @param title  the new boss bar text
     * @param restart restart the bar progress
     * @return if the boss bar could be updated
     */
    @Override
    public boolean update(final String title, final boolean restart) {
        try {
            if (!title.equals(message)) {
                if (legacy) {
                    Object wither = wither_objects.getOrDefault(id, null);
                    if (wither != null) {
                        setCustomName(wither, title);

                        try {
                            Method getId = wither.getClass().getMethod("getId");
                            Method getDataWatcher = wither.getClass().getMethod("getDataWatcher");
                            int id = (int) getId.invoke(wither);
                            Object dataWatcher = getDataWatcher.invoke(wither);

                            Object packetPlayOutEntityMetadata = packet_play_metadata.newInstance(id, dataWatcher, true);
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                try {
                                    Object c_player = craft_player.cast(player);
                                    Object entity_player = craft_player_handle.invoke(c_player);

                                    Field playerConnection = entity_player.getClass().getField("playerConnection");
                                    Object connection = playerConnection.get(entity_player);

                                    packet_connect_send.invoke(packet_connection.cast(connection), packetPlayOutEntityMetadata);
                                } catch (Throwable ignored) {}
                            }
                        } catch (Throwable ex) {
                            ex.printStackTrace();
                        }
                    }
                } else {
                    BossBar bar = (BossBar) wither_objects.getOrDefault(id, null);
                    if (bar != null) {
                        bar.setTitle(StringUtils.toColor(title));
                        bar.getPlayers().forEach(bar::addPlayer);
                    }
                }

                message = title;
            }

            return true;
        } catch (Throwable ex) {
            source.logger().scheduleLog(Level.GRAVE, ex);
            source.logger().scheduleLog(Level.INFO, "Failed to update boss bar");
        }

        return false;
    }

    /**
     * Set the boss bar display time
     *
     * @param time the boss bar display time
     * @return the boss bar display time
     */
    @Override
    public BossProvider<Player> displayTime(final double time) {
        cancel();
        return new BossMessage(source, message, time)
                .color(color).style(type).progress(progress);
    }

    /**
     * Get if the boss bar is valid
     *
     * @return if the boss bar is valid
     */
    @Override
    public boolean isValid() {
        return wither_objects.containsKey(id);
    }

    /**
     * Get if the boss bar is cancelled
     *
     * @return if the boss bar is cancelled
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Get the boss bar title
     *
     * @return the boss bar title
     */
    @Override
    public String getTitle() {
        return message;
    }

    /**
     * Get the boss bar color
     *
     * @return the boss bar color
     */
    @Override
    public BossColor getColor() {
        return color;
    }

    /**
     * Get the boss bar style
     *
     * @return the boss bar style
     */
    @Override
    public BossType getStyle() {
        return type;
    }

    /**
     * Initialize for reflection
     */
    private void doReflectionStuff() {
        try {
            craft_world = BukkitServer.getBukkitClass("CraftWorld");
            Class<?> entity_wither = BukkitServer.getMinecraftClass("EntityWither");
            if (entity_wither != null) {
                Class<?> packet_entity_living_out = BukkitServer.getMinecraftClass("PacketPlayOutSpawnEntityLiving");
                if (packet_entity_living_out != null) {
                    craft_player = BukkitServer.getBukkitClass("entity.CraftPlayer");
                    Class<?> packet_class = BukkitServer.getMinecraftClass("Packet");
                    packet_connection = BukkitServer.getMinecraftClass("PlayerConnection");
                    packet_play_out_destroy = BukkitServer.getMinecraftClass("PacketPlayOutEntityDestroy");

                    Class<?> dataWatcher = BukkitServer.getMinecraftClass("DataWatcher");
                    Class<?> entityMetadata = BukkitServer.getMinecraftClass("PacketPlayOutEntityMetadata");
                    if (entityMetadata != null) {
                        packet_play_metadata = entityMetadata.getConstructor(int.class, dataWatcher, boolean.class);
                        Class<?> packet_play_teleport = BukkitServer.getMinecraftClass("PacketPlayOutEntityTeleport");
                        if (packet_play_teleport != null) {
                            wither_constructor = entity_wither.getConstructor(BukkitServer.getMinecraftClass("World"));
                            entity_living_constructor = packet_entity_living_out.getConstructor(BukkitServer.getMinecraftClass("EntityLiving"));
                            packet_play_teleport_constructor = packet_play_teleport.getConstructor(BukkitServer.getMinecraftClass("Entity"));
                            craft_world_handle = craft_world.getMethod("getHandle");
                            craft_player_handle = craft_player.getMethod("getHandle");
                            packet_connect_send = packet_connection.getMethod("sendPacket", packet_class);
                            wither_set_location_method = entity_wither.getMethod("setLocation", double.class, double.class, double.class, float.class, float.class);
                            try {
                                wither_set_progress_method = entity_wither.getMethod("setProgress", double.class);
                            } catch (Throwable ex) {
                                try {
                                    wither_set_progress_method = entity_wither.getMethod("setProgress", float.class);
                                } catch (Throwable ex2) {
                                    health = true;
                                    try {
                                        wither_set_progress_method = entity_wither.getMethod("setHealth", double.class);
                                    } catch (Throwable ex3) {
                                        wither_set_progress_method = entity_wither.getMethod("setHealth", float.class);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Get a boss message from its id
     *
     * @param id the boss id
     * @return the boss id
     * @throws BossNotFoundException if there's no boss bar with the specified ID
     */
    public static BossMessage fromId(final int id) throws BossNotFoundException {
        BossMessage provider = boss_bars.getOrDefault(id, null);
        if (provider != null) {
            return provider;
        } else {
            throw new BossNotFoundException(id, BossMessage.boss_bars.keySet());
        }
    }

    /**
     * Set the custom name of the wither
     *
     * @param wither the wither object
     * @param message the name
     * @throws InvocationTargetException as part of the method
     * @throws InstantiationException as part of the method
     * @throws IllegalAccessException as part of the method
     */
    private void setCustomName(final Object wither, final String message) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Method customName = null;
        boolean component = false;
        try {
            customName = wither.getClass().getMethod("setCustomName", String.class);
        } catch (NoSuchMethodException e) {
            Class<?> iChatBaseComponent = BukkitServer.getMinecraftClass("IChatBaseComponent");
            try {
                customName = wither.getClass().getMethod("setCustomName", iChatBaseComponent);
                component = true;
            } catch (NoSuchMethodException ex) {
                ex.printStackTrace();
            }
        }

        if (customName != null) {
            if (component) {
                Class<?> chatMessage = BukkitServer.getMinecraftClass("ChatMessage");
                if (chatMessage != null) {
                    try {
                        Constructor<?> constructor = chatMessage.getConstructor(String.class, Object[].class);
                        Object chatMessageComponent = constructor.newInstance(StringUtils.toColor(message), new Object[0]);
                        customName.invoke(wither, chatMessageComponent);
                    } catch (NoSuchMethodException ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                customName.invoke(wither, StringUtils.toColor(message));
            }
        } else {
            source.console().send("Cannot name a boss bar with invalid name method", Level.GRAVE);
        }
    }
}