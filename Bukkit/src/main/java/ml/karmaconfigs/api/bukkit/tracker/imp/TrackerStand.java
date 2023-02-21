package ml.karmaconfigs.api.bukkit.tracker.imp;

import ml.karmaconfigs.api.bukkit.KarmaPlugin;
import ml.karmaconfigs.api.bukkit.tracker.AutoTracker;
import ml.karmaconfigs.api.bukkit.tracker.Tracker;
import ml.karmaconfigs.api.bukkit.tracker.event.*;
import ml.karmaconfigs.api.bukkit.tracker.property.PropertyValue;
import ml.karmaconfigs.api.bukkit.tracker.property.flag.TrackerFlag;
import ml.karmaconfigs.api.bukkit.util.LineOfSight;
import ml.karmaconfigs.api.bukkit.util.sight.PointToEntity;
import ml.karmaconfigs.api.bukkit.util.sight.SightPart;
import ml.karmaconfigs.api.common.utils.enums.Level;
import ml.karmaconfigs.api.common.string.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Tracker implementation for Armor Stands
 */
public class TrackerStand extends Tracker {

    private final static ArrayList<UUID> kill_queue = new ArrayList<>(25); //Should be enough

    //private final Map<String, Object> properties = new HashMap<>();
    private final Set<PropertyValue<?>> properties = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final Set<PropertyValue<?>> updated_properties = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final KarmaPlugin plugin;
    private final Location location;
    private final Location variable_location;

    private ArmorStand stand;
    private BukkitTask task;
    private BukkitTask track_task;
    private LivingEntity tracking;
    private boolean reset_time = false;
    private boolean reset_task = false;
    private AutoTracker tracker = null;

    private float yaw;
    private float pitch;

    private UUID last_killed = null;

    private LivingEntity last_track = null;

    /**
     * Initialize the tracker stand
     *
     * @param p the plugin owning this stand
     * @param l the stand location
     */
    public TrackerStand(final KarmaPlugin p, final Location l) {
        plugin = p;
        location = l.clone();
        variable_location = l.clone();
        location.setYaw(180);

        init();
    }

    /**
     * Get the tracking entity
     *
     * @return the tracking entity
     */
    @Override
    public LivingEntity getTracking() {
        return tracking;
    }

    /**
     * Get the tracker auto tracker
     *
     * @return the tracker auto tracker
     */
    @Override
    public AutoTracker getTracker() {
        return tracker;
    }

    /**
     * Set the tracker property
     * <p>
     * PLEASE NOTE: A correctly setup {@link Tracker} should have
     * all the expected properties with a default value. Otherwise, people
     * could be able to mess up. So this method should be only an alternative
     * to {@link Tracker#getProperty(TrackerFlag, String)} and then {@link PropertyValue#update(Object)} or
     * {@link PropertyValue#updateUnsafe(Object)} methods respectively
     *
     * @param newProperty the property
     * @param <T>         the property type
     * @return this instance
     * @throws IllegalStateException if the property value doesn't match with the stored property value.
     */
    @Override
    public <T> Tracker setProperty(final PropertyValue<T> newProperty) throws IllegalStateException {
        if (newProperty != null && newProperty.getValue() != null) {
            PropertyValue<T> prop = null;

            for (PropertyValue<?> property : properties) {
                if (property.getIdentifier().equals(newProperty.getIdentifier())) {
                    if (property.matches(newProperty.getValue())) {
                        PropertyValue<T> new_value = property.getFlag().makeProperty(newProperty.getName());
                        new_value.update(newProperty.getValue());
                        prop = new_value;
                        break;
                    } else {
                        throw new IllegalStateException("Cannot modify tracker property because values type doesn't match");
                    }
                }
            }

            if (prop == null) {
                updated_properties.add(newProperty);
            } else {
                updated_properties.add(prop);
            }
        }

        return this;
    }

    /**
     * Get the tracker property
     *
     * @param flag the flag to get for
     * @param name the property name
     * @param <T>  the property type
     * @return the tracker property
     */
    @Override
    public @SuppressWarnings("unchecked") <T> PropertyValue<T> getProperty(final TrackerFlag flag, final String name) {
        PropertyValue<T> prop = null;

        for (PropertyValue<?> property : properties) {
            if (property.getIdentifier().equals(flag.getPrefix() + "_" + name)) {
                try {
                    prop = (PropertyValue<T>) property;
                } catch (ClassCastException ex) {
                    return null;
                }
                break;
            }
        }

        return prop;
    }

    /**
     * Get the tracker line of sight
     *
     * @return the tracker line of sight
     */
    @Override
    public LineOfSight getLineOfSight() {
        boolean small = getProperty(TrackerFlag.PROPERTY_BOOLEAN, "small").getUnsafe();
        PointToEntity sight = new PointToEntity(stand.getLocation(), (tracking != null ? tracking : stand), SightPart.HEAD)
                .sourceOffset(0, (small ? 0.5 : 1.5), 0)
                .targetOffset(0, (tracking != null ? tracking.getEyeHeight() : (small ? 0.5 : 1.5)), 0);

        /*
        switch (part) {
            case HEAD:
                sight = new PointToEntity(stand.getLocation(), (tracking != null ? tracking : stand))
                    .sourceOffset(0, (small ? 0.5 : 1.5), 0)
                    .targetOffset(0, (tracking != null ? tracking.getEyeHeight() : (small ? 0.5 : 1.5)), 0);
                break;
            case BODY:
                sight = new PointToEntity(stand.getLocation(), (tracking != null ? tracking : stand))
                        .sourceOffset(0, (small ? 0.5 : 1.5), 0)
                        .targetOffset(0, (tracking != null ? tracking.getEyeHeight() / 2 : (small ? 0.25 : 0.75)), 0);
                break;
            case FEET:
            default:
                sight = new PointToEntity(stand.getLocation(), (tracking != null ? tracking : stand))
                        .sourceOffset(0, (small ? 0.5 : 1.5), 0)
                        .targetOffset(0, 0, 0);
                break;
        }*/

        return sight.ignore(stand).ignoreMiddle(true).precision(0.3);
    }

    /**
     * Get the tracker line of sight with another entity
     *
     * @param target the entity to check with
     * @return the tracker line of sight with entity
     */
    @Override
    public LineOfSight getLineOfSight(final LivingEntity target) {
        boolean small = getProperty(TrackerFlag.PROPERTY_BOOLEAN, "small").getUnsafe();
        PointToEntity sight = new PointToEntity(stand.getLocation(), target, SightPart.HEAD)
                .sourceOffset(0, (small ? 0.5 : 1.5), 0)
                .targetOffset(0, (target != null ? target.getEyeHeight() : (small ? 0.5 : 1.5)), 0);

        /*
        switch (part) {
            case HEAD:
                sight = new PointToEntity(stand.getLocation(), target)
                        .sourceOffset(0, (small ? 0.5 : 1.5), 0)
                        .targetOffset(0, (target != null ? target.getEyeHeight() : (small ? 0.5 : 1.5)), 0);
                break;
            case BODY:
                sight = new PointToEntity(stand.getLocation(), (target != null ? target : stand))
                        .sourceOffset(0, (small ? 0.5 : 1.5), 0)
                        .targetOffset(0, (target != null ? target.getEyeHeight() / 2 : (small ? 0.25 : 0.75)), 0);
                break;
            case FEET:
            default:
                sight = new PointToEntity(stand.getLocation(), target)
                        .sourceOffset(0, (small ? 0.5 : 1.5), 0)
                        .targetOffset(0, 0, 0);
                break;
        }*/

        return sight.ignore(stand).ignoreMiddle(true).precision(0.3);
    }

    /**
     * Get the tracker location
     *
     * @return the tracker location
     */
    @Override
    public Location getLocation() {
        if (stand != null) {
            Location clone = stand.getLocation().clone();
            clone.setYaw(yaw);
            clone.setPitch(pitch);

            variable_location.setX(clone.getX());
            variable_location.setY(clone.getY());
            variable_location.setZ(clone.getZ());
            variable_location.setYaw(clone.getYaw());
            variable_location.setPitch(clone.getPitch());

            return clone;
        }

        return location;
    }

    /**
     * Get the tracker variable location
     *
     * @return the tracker variable location
     */
    @Override
    public Location getVariableLocation() {
        Location clone = stand.getLocation().clone();
        clone.setYaw(yaw);
        clone.setPitch(pitch);

        variable_location.setX(clone.getX());
        variable_location.setY(clone.getY());
        variable_location.setZ(clone.getZ());
        variable_location.setYaw(clone.getYaw());
        variable_location.setPitch(clone.getPitch());

        return variable_location;
    }

    /**
     * Get the tracker world
     *
     * @return the tracker world
     */
    @Override
    public World getWorld() {
        if (stand != null) {
            return stand.getWorld();
        }

        return location.getWorld();
    }

    /**
     * Get the tracker entity
     *
     * @return the tracker entity
     */
    @Override
    public LivingEntity getEntity() {
        return stand;
    }

    /**
     * Get the direction in where the player is
     *
     * @param trackEye use tracker eye location
     * @param tarEye use target eye location
     * @return the direction in where the player is
     */
    @Override
    public Vector getDirection(final boolean trackEye, final boolean tarEye) {
        if (stand != null && tracking != null) {
            Location standLocation = stand.getLocation().clone();
            if (trackEye) {
                boolean small = getProperty(TrackerFlag.PROPERTY_BOOLEAN, "small").getUnsafe();
                standLocation.add(0, (small ? 0.5 : 1.5), 0);
            }

            Location trackLocation = (tarEye ? tracking.getEyeLocation() : tracking.getLocation()).clone();

            return trackLocation.toVector().subtract(standLocation.toVector()).normalize();
        }

        return location.getDirection().normalize();
    }


    /**
     * Set up the tracker auto tracker. Once the auto
     * tracker is set. The method {@link Tracker#setTracking(LivingEntity)}
     * will be locked and no longer work unless we set our
     * auto tracker to null.
     *
     * @param auto the auto tracker
     */
    @Override
    public void setAutoTracker(final AutoTracker auto) {
        tracker = auto;
    }

    /**
     * Set the tracking entity
     *
     * @param entity the entity to track
     */
    @Override
    public void setTracking(final LivingEntity entity) {
        if (entity != null && tracker == null) {
            tracking = entity;
        }
    }

    /**
     * Start the track task
     */
    @Override
    public void start() {
        if (task == null) {
            long track_period = Math.max(1, ((Number) getProperty(TrackerFlag.TRACKER_NUMBER, "trackPeriod").getUnsafe()).longValue());

            track_task = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
                boolean always_track = getProperty(TrackerFlag.TRACKER_BOOLEAN, "ignoreLineOfSight").getUnsafe();
                double offset = ((Number) getProperty(TrackerFlag.TRACKER_NUMBER, "angleOffset").getUnsafe()).doubleValue();
                double max_dist = Math.abs(((Number) getProperty(TrackerFlag.TRACKER_NUMBER, "scanDistance").getUnsafe()).doubleValue());
                boolean track_lock = getProperty(TrackerFlag.TRACKER_BOOLEAN, "leapAtTarget").getUnsafe();

                if (tracking != null) {
                    if (track_lock && last_track != null && last_track.isValid() && !last_track.isDead()) {
                        tracking = last_track;
                    }

                    last_track = tracking;

                    if (tracking.isDead() || !tracking.isValid()) {
                        tracking = null;
                    }

                    if (stand != null) {
                        if (stand.isDead() || !stand.isValid()) {
                            tracking = null;
                        }
                    }

                    if (tracking != null && stand != null) {
                        Location trackLocation = tracking.getEyeLocation().clone();
                        Location standLocation = stand.getLocation().clone();
                        standLocation.setY(standLocation.getY() + Math.max(1.5, tracking.getEyeHeight()));

                        LineOfSight sight = getLineOfSight();

                        SightPart part = sight.getLineOfSight(max_dist);
                        if (!part.equals(SightPart.NONE) || always_track) {
                            switch (part) {
                                case BODY:
                                    trackLocation.subtract(0d, tracking.getEyeHeight() / 2, 0d);
                                    break;
                                case FEET:
                                    trackLocation.subtract(0d, tracking.getEyeHeight() / 4, 0d);
                                    break;
                            }

                            Vector vector = trackLocation.toVector().subtract(standLocation.toVector()).normalize();

                            if (offset != 0) {
                                if (offset >= 0) {
                                    vector = trackLocation.add(0, offset, 0).toVector().subtract(standLocation.toVector()).normalize();
                                } else {
                                    vector = trackLocation.subtract(0, Math.abs(offset), 0).toVector().subtract(standLocation.toVector()).normalize();
                                }
                            }

                            double angle_x = vector.getY() * (-1);
                            double x = vector.getX();
                            double z = vector.getZ();
                            double angle_y = 180F - Math.toDegrees(Math.atan2(x, z));

                            yaw = (float) angle_y - 180F;
                            pitch = (180.0F - (float) Math.toDegrees(Math.acos(vector.getY()))) * (-1);

                            Location clone = stand.getLocation().clone();
                            clone.setYaw(yaw);
                            clone.setPitch(pitch);

                            variable_location.setX(clone.getX());
                            variable_location.setY(clone.getY());
                            variable_location.setZ(clone.getZ());
                            variable_location.setYaw(clone.getYaw());
                            variable_location.setPitch(clone.getPitch());

                            plugin.console().debug("Stand yaw: {0} (From: {1})", Level.INFO, yaw, (180 + yaw));
                            plugin.console().debug("Stand pitch: {0} (From: {1})", Level.INFO, pitch, (180 + pitch));

                            EulerAngle angle = new EulerAngle(angle_x, Math.toRadians(angle_y), 0);
                            stand.setHeadPose(angle);
                        }
                    }
                }
            }, 0, track_period);

            if (stand == null || stand.isDead() || !stand.isValid()) {
                if (stand != null) stand.remove();

                World world = location.getWorld();
                if (world != null) {
                    Event event = new TrackerSpawnEvent(this, stand != null);
                    Bukkit.getServer().getPluginManager().callEvent(event);

                    stand = world.spawn(location, ArmorStand.class);

                    stand.setNoDamageTicks(20 * 5); //5 seconds of spawn invulnerability

                    update();
                }
            }

            AtomicLong start = new AtomicLong(System.currentTimeMillis());
            AtomicInteger last_second = new AtomicInteger(0);
            long period = Math.max(1, ((Number) getProperty(TrackerFlag.TRACKER_NUMBER, "scanPeriod").getUnsafe()).longValue());

            task = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
                if (reset_task) {
                    reset_task = false;

                    task.cancel();
                    track_task.cancel();
                    task = null;
                    track_task = null;
                    reset_time = true;

                    start();
                } else {
                    if (reset_time) {
                        reset_time = false;
                        start.set(System.currentTimeMillis());
                    }

                    boolean always_track = getProperty(TrackerFlag.TRACKER_BOOLEAN, "ignoreLineOfSight").getUnsafe();
                    double max_dist = Math.abs(((Number) getProperty(TrackerFlag.TRACKER_NUMBER, "scanDistance").getUnsafe()).doubleValue());
                    boolean track_lock = getProperty(TrackerFlag.TRACKER_BOOLEAN, "leapAtTarget").getUnsafe();

                    LivingEntity[] multi;
                    if (tracker != null && (stand != null && !stand.isDead() && stand.isValid())) {
                        multi = tracker.track(this, max_dist);
                    } else {
                        multi = new LivingEntity[]{tracking, last_track};
                    }

                    tracking = null;
                    for (LivingEntity entity : multi) {
                        if (entity != null) {
                            LineOfSight e_sight = getLineOfSight(entity);

                            if (!e_sight.getLineOfSight(max_dist).equals(SightPart.NONE) || always_track) {
                                tracking = entity;
                                break;
                            }
                        }
                    }
                    if (track_lock && last_track != null && last_track.isValid() && !last_track.isDead()) {
                        LineOfSight t_sight = getLineOfSight(last_track);

                        if (!t_sight.getLineOfSight(max_dist).equals(SightPart.NONE) || always_track) {
                            tracking = last_track;
                        }
                    }

                    if (tracking != null) {
                        if (last_track != null) {
                            if (!tracking.getUniqueId().equals(last_track.getUniqueId())) {
                                Event event = new TrackerLostEvent(this, last_track);
                                Bukkit.getServer().getPluginManager().callEvent(event);

                                last_track = tracking;
                            }
                        } else {
                            last_track = tracking;
                        }

                        if (tracking.isDead() || !tracking.isValid()) {
                            if (!kill_queue.contains(tracking.getUniqueId())) {
                                last_killed = tracking.getUniqueId();
                                last_track = null;
                                kill_queue.add(last_killed);

                                Event event = new TrackerTargetDiedEvent(this, tracking);
                                Bukkit.getServer().getPluginManager().callEvent(event);
                            }

                            tracking = null;
                        }

                        if (stand != null) {
                            if (stand.isDead() || !stand.isValid()) {
                                Event event = new TrackerDiedEvent(this);
                                Bukkit.getServer().getPluginManager().callEvent(event);

                                tracking = null;
                            }
                        }

                        if (tracking != null && stand != null) {
                            long end = System.currentTimeMillis();
                            int seconds = (int) (end - start.get()) / 1000;

                            Event event = new TrackerTickEvent(this, tracking, 1);
                            Bukkit.getServer().getPluginManager().callEvent(event);
                            if (last_second.get() != seconds) {
                                last_second.set(seconds);

                                Event second = new TrackerSecondEvent(this, tracking, seconds);
                                Bukkit.getServer().getPluginManager().callEvent(second);
                            }
                        }
                    } else {
                        if (last_killed != null) {
                            kill_queue.remove(last_killed);
                            last_killed = null;
                            kill_queue.trimToSize();
                            last_track = null;
                        }
                    }
                }
            }, 0, period);
        }
    }

    /**
     * Update the tracker
     */
    @Override
    public void update() {
        if (stand != null) {
            boolean updateLocation = false;

            for (PropertyValue<?> property : updated_properties) {
                String keyName = property.getIdentifier();

                switch (keyName) {
                    case "tracker_boolean_scanPeriod":
                        reset_task = true;
                        break;
                    case "property_number_x":
                        location.setX(property.getUnsafe());
                        updateLocation = true;
                        break;
                    case "property_number_y":
                        location.setY(property.getUnsafe());
                        updateLocation = true;
                        break;
                    case "property_number_z":
                        location.setZ(property.getUnsafe());
                        updateLocation = true;
                        break;
                    case "property_uuid_world":
                        World world = plugin.getServer().getWorld((UUID) property.getUnsafe());
                        if (world != null) {
                            location.setWorld(world);
                            updateLocation = true;
                        }
                        break;
                    case "property_boolean_small":
                        stand.setSmall(property.getUnsafe());
                        break;
                    case "property_boolean_basePlate":
                        stand.setBasePlate(property.getUnsafe());
                        break;
                    case "property_boolean_marker":
                        try {
                            Method setMarker = stand.getClass().getMethod("setMarker", boolean.class);

                            boolean marker = property.getUnsafe();
                            setMarker.invoke(stand, marker);
                        } catch (Throwable ignored) {
                        }
                        break;
                    case "property_boolean_invincible":
                        try {
                            Method setInvulnerable = stand.getClass().getMethod("setInvulnerable", boolean.class);

                            boolean invulnerable = property.getUnsafe();
                            setInvulnerable.invoke(stand, invulnerable);
                        } catch (Throwable ex) {
                            stand.setNoDamageTicks((property.getUnsafe() ? Integer.MAX_VALUE : 0));
                        }
                        break;
                    case "property_boolean_showName":
                        stand.setCustomNameVisible(property.getUnsafe());
                        break;
                    case "property_boolean_arms":
                        stand.setArms(property.getUnsafe());
                        break;
                    case "property_boolean_takeoffItems":
                        stand.setCanPickupItems(property.getUnsafe());
                        break;
                    case "property_text_customName":
                        stand.setCustomName(StringUtils.toColor((String) property.getUnsafe()));
                        break;
                    case "property_object_leftArmAngle":
                        stand.setLeftArmPose(property.getUnsafe());
                        break;
                    case "property_object_rightArmAngle":
                        stand.setRightArmPose(property.getUnsafe());
                        break;
                    case "property_object_leftLegAngle":
                        stand.setLeftLegPose(property.getUnsafe());
                        break;
                    case "property_object_rightLegAngle":
                        stand.setRightLegPose(property.getUnsafe());
                        break;
                    case "property_object_bodyAngle":
                        stand.setBodyPose(property.getUnsafe());
                        break;
                    case "property_object_equipmentLeftArm":
                        try {
                            EntityEquipment equipment = stand.getEquipment();
                            Method setItemInOffHand = equipment.getClass().getMethod("setItemInOffHand", ItemStack.class, boolean.class);

                            ItemStack stack = property.getUnsafe();
                            setItemInOffHand.invoke(equipment, stack, true);
                        } catch (Throwable ex) {
                            try {
                                EntityEquipment equipment = stand.getEquipment();
                                Method setItemInOffHand = equipment.getClass().getMethod("setItemInOffHand", ItemStack.class);

                                ItemStack stack = property.getUnsafe();
                                setItemInOffHand.invoke(equipment, stack);
                            } catch (Throwable exc) {
                                try {
                                    EntityEquipment equipment = stand.getEquipment();
                                    Method setItem = equipment.getClass().getMethod("setItem", EquipmentSlot.class, ItemStack.class);

                                    ItemStack stack = property.getUnsafe();
                                    setItem.invoke(equipment, EquipmentSlot.valueOf("OFF_HAND"), stack);
                                } catch (Throwable ignored) {
                                }
                            }
                        }
                        break;
                    case "property_object_equipmentRightArm":
                        try {
                            EntityEquipment equipment = stand.getEquipment();
                            Method setItemInOffHand = equipment.getClass().getMethod("setItemInMainHand", ItemStack.class, boolean.class);

                            ItemStack stack = property.getUnsafe();
                            setItemInOffHand.invoke(equipment, stack, true);
                        } catch (Throwable ex) {
                            try {
                                EntityEquipment equipment = stand.getEquipment();
                                Method setItemInOffHand = equipment.getClass().getMethod("setItemInMainHand", ItemStack.class);

                                ItemStack stack = property.getUnsafe();
                                setItemInOffHand.invoke(equipment, stack);
                            } catch (Throwable exc) {
                                try {
                                    EntityEquipment equipment = stand.getEquipment();
                                    Method setItem = equipment.getClass().getMethod("setItem", EquipmentSlot.class, ItemStack.class);

                                    ItemStack stack = property.getUnsafe();
                                    setItem.invoke(equipment, EquipmentSlot.valueOf("HAND"), stack);
                                } catch (Throwable exce) {
                                    try {
                                        EntityEquipment equipment = stand.getEquipment();
                                        Method setItem = equipment.getClass().getMethod("setItem", EquipmentSlot.class, ItemStack.class);

                                        ItemStack stack = property.getUnsafe();
                                        setItem.invoke(equipment, EquipmentSlot.valueOf("MAIN_HAND"), stack);
                                    } catch (Throwable excep) {
                                        try {
                                            Method setItem = stand.getClass().getMethod("setItemInHand", ItemStack.class);

                                            ItemStack stack = property.getUnsafe();
                                            setItem.invoke(stand, stack);
                                        } catch (Throwable ignored) {
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    case "property_object_equipmentHelmet":
                        try {
                            EntityEquipment equipment = stand.getEquipment();
                            Method setEquipment = equipment.getClass().getMethod("setHelmet", ItemStack.class, boolean.class);

                            ItemStack stack = property.getUnsafe();
                            setEquipment.invoke(equipment, stack, true);
                        } catch (Throwable ex) {
                            try {
                                EntityEquipment equipment = stand.getEquipment();
                                Method setEquipment = equipment.getClass().getMethod("setHelmet", ItemStack.class);

                                ItemStack stack = property.getUnsafe();
                                setEquipment.invoke(equipment, stack);
                            } catch (Throwable exc) {
                                try {
                                    EntityEquipment equipment = stand.getEquipment();
                                    Method setEquipment = equipment.getClass().getMethod("setItem", EquipmentSlot.class, ItemStack.class);

                                    ItemStack stack = property.getUnsafe();
                                    setEquipment.invoke(equipment, EquipmentSlot.valueOf("HEAD"), stack);
                                } catch (Throwable exce) {
                                    try {
                                        EntityEquipment equipment = stand.getEquipment();
                                        Method setEquipment = equipment.getClass().getMethod("setItem", EquipmentSlot.class, ItemStack.class);

                                        ItemStack stack = property.getUnsafe();
                                        setEquipment.invoke(equipment, EquipmentSlot.valueOf("HELMET"), stack);
                                    } catch (Throwable excep) {
                                        try {
                                            Method setEquipment = stand.getClass().getMethod("setHelmet", ItemStack.class);

                                            ItemStack stack = property.getUnsafe();
                                            setEquipment.invoke(stand, stack);
                                        } catch (Throwable ignored) {
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    case "property_object_equipmentChestplate":
                        try {
                            EntityEquipment equipment = stand.getEquipment();
                            Method setEquipment = equipment.getClass().getMethod("setChestplate", ItemStack.class, boolean.class);

                            ItemStack stack = property.getUnsafe();
                            setEquipment.invoke(equipment, stack, true);
                        } catch (Throwable ex) {
                            try {
                                EntityEquipment equipment = stand.getEquipment();
                                Method setEquipment = equipment.getClass().getMethod("setChestplate", ItemStack.class);

                                ItemStack stack = property.getUnsafe();
                                setEquipment.invoke(equipment, stack);
                            } catch (Throwable exc) {
                                try {
                                    EntityEquipment equipment = stand.getEquipment();
                                    Method setEquipment = equipment.getClass().getMethod("setItem", EquipmentSlot.class, ItemStack.class);

                                    ItemStack stack = property.getUnsafe();
                                    setEquipment.invoke(equipment, EquipmentSlot.valueOf("CHESTPLATE"), stack);
                                } catch (Throwable exce) {
                                    try {
                                        Method setEquipment = stand.getClass().getMethod("setChestplate", ItemStack.class);

                                        ItemStack stack = property.getUnsafe();
                                        setEquipment.invoke(stand, stack);
                                    } catch (Throwable ignored) {
                                    }
                                }
                            }
                        }
                        break;
                    case "property_object_equipmentLeggings":
                        try {
                            EntityEquipment equipment = stand.getEquipment();
                            Method setEquipment = equipment.getClass().getMethod("setLeggings", ItemStack.class, boolean.class);

                            ItemStack stack = property.getUnsafe();
                            setEquipment.invoke(equipment, stack, true);
                        } catch (Throwable ex) {
                            try {
                                EntityEquipment equipment = stand.getEquipment();
                                Method setEquipment = equipment.getClass().getMethod("setLeggings", ItemStack.class);

                                ItemStack stack = property.getUnsafe();
                                setEquipment.invoke(equipment, stack);
                            } catch (Throwable exc) {
                                try {
                                    EntityEquipment equipment = stand.getEquipment();
                                    Method setEquipment = equipment.getClass().getMethod("setItem", EquipmentSlot.class, ItemStack.class);

                                    ItemStack stack = property.getUnsafe();
                                    setEquipment.invoke(equipment, EquipmentSlot.valueOf("LEGGINGS"), stack);
                                } catch (Throwable exce) {
                                    try {
                                        Method setEquipment = stand.getClass().getMethod("setLeggings", ItemStack.class);

                                        ItemStack stack = property.getUnsafe();
                                        setEquipment.invoke(stand, stack);
                                    } catch (Throwable ignored) {
                                    }
                                }
                            }
                        }
                        break;
                    case "property_object_equipmentBoots":
                        try {
                            EntityEquipment equipment = stand.getEquipment();
                            Method setEquipment = equipment.getClass().getMethod("setBoots", ItemStack.class, boolean.class);

                            ItemStack stack = property.getUnsafe();
                            setEquipment.invoke(equipment, stack, true);
                        } catch (Throwable ex) {
                            try {
                                EntityEquipment equipment = stand.getEquipment();
                                Method setEquipment = equipment.getClass().getMethod("setBoots", ItemStack.class);

                                ItemStack stack = property.getUnsafe();
                                setEquipment.invoke(equipment, stack);
                            } catch (Throwable exc) {
                                try {
                                    EntityEquipment equipment = stand.getEquipment();
                                    Method setEquipment = equipment.getClass().getMethod("setItem", EquipmentSlot.class, ItemStack.class);

                                    ItemStack stack = property.getUnsafe();
                                    setEquipment.invoke(equipment, EquipmentSlot.valueOf("BOOTS"), stack);
                                } catch (Throwable exce) {
                                    try {
                                        Method setEquipment = stand.getClass().getMethod("setBoots", ItemStack.class);

                                        ItemStack stack = property.getUnsafe();
                                        setEquipment.invoke(stand, stack);
                                    } catch (Throwable ignored) {
                                    }
                                }
                            }
                        }
                        break;
                }

                PropertyValue<?> stored_property = getProperty(property.getFlag(), property.getName());
                if (stored_property != null) {
                    stored_property.update(property.getUnsafe());
                } else {
                    properties.add(property);
                }
            }

            if (updateLocation) stand.teleport(location);
        }
    }

    /**
     * Reset the tracking time
     */
    @Override
    public void resetTime() {
        reset_time = true;
    }

    /**
     * Destroy the tracking stand
     */
    @Override
    public void destroy() {
        stand.remove();
    }

    /**
     * Kill the tracker entity
     *
     * @deprecated As of build of 16/10/2022 this does the same as
     * {@link Tracker#destroy()}. Previously, destroy would kill the
     * tracker entity, and also remove from memory. Now it will be only
     * killed to keep a solid respawn value at {@link TrackerSpawnEvent}
     */
    @Deprecated
    public @ApiStatus.ScheduledForRemoval(inVersion = "1.3.4-SNAPSHOT") void kill() {
        stand.remove();
    }

    /**
     * If the tracker gets killed, this method
     * should "re-spawn" it
     */
    @Override
    public void respawn() {
        if (stand == null || stand.isDead() || !stand.isValid()) {
            if (stand != null) stand.remove();

            World world = location.getWorld();
            if (world != null) {
                Event event = new TrackerSpawnEvent(this, stand != null);
                Bukkit.getServer().getPluginManager().callEvent(event);

                stand = world.spawn(location, ArmorStand.class);

                stand.setNoDamageTicks(20 * 5); //5 seconds of spawn invulnerability
                stand.setMetadata("tracker", new FixedMetadataValue(plugin, id));

                update();
            }
        }
    }

    /**
     * Initialize the stand
     */
    protected void init() {
        PropertyValue<Number> LOCATION_X = TrackerFlag.PROPERTY_NUMBER.makeProperty("x");
        PropertyValue<Number> LOCATION_Y = TrackerFlag.PROPERTY_NUMBER.makeProperty("y");
        PropertyValue<Number> LOCATION_Z = TrackerFlag.PROPERTY_NUMBER.makeProperty("z");
        PropertyValue<Number> LOCATION_YAW = TrackerFlag.PROPERTY_NUMBER.makeProperty("yaw");
        PropertyValue<Number> LOCATION_PITCH = TrackerFlag.PROPERTY_NUMBER.makeProperty("pitch");
        PropertyValue<UUID> LOCATION_WORLD = TrackerFlag.PROPERTY_UUID.makeProperty("world");

        PropertyValue<Boolean> STAND_SMALL = TrackerFlag.PROPERTY_BOOLEAN.makeProperty("small");
        PropertyValue<Boolean> STAND_BASE_PLATE = TrackerFlag.PROPERTY_BOOLEAN.makeProperty("basePlate");
        PropertyValue<Boolean> STAND_MARKER = TrackerFlag.PROPERTY_BOOLEAN.makeProperty("marker");
        PropertyValue<Boolean> STAND_INVINCIBLE = TrackerFlag.PROPERTY_BOOLEAN.makeProperty("invincible");
        PropertyValue<Boolean> STAND_INVISIBLE = TrackerFlag.PROPERTY_BOOLEAN.makeProperty("invisible");
        PropertyValue<Boolean> STAND_SHOW_NAME = TrackerFlag.PROPERTY_BOOLEAN.makeProperty("showName");
        PropertyValue<Boolean> STAND_ARMS = TrackerFlag.PROPERTY_BOOLEAN.makeProperty("arms");
        PropertyValue<Boolean> STAND_PICKUP_ITEMS = TrackerFlag.PROPERTY_BOOLEAN.makeProperty("takeoffItems");

        PropertyValue<String> STAND_NAME = TrackerFlag.PROPERTY_STRING.makeProperty("customName");

        PropertyValue<Object> STAND_ANGLE_LA = TrackerFlag.PROPERTY_OTHER.makeProperty("leftArmAngle");
        PropertyValue<Object> STAND_ANGLE_RA = TrackerFlag.PROPERTY_OTHER.makeProperty("rightArmAngle");
        PropertyValue<Object> STAND_ANGLE_LL = TrackerFlag.PROPERTY_OTHER.makeProperty("leftLegAngle");
        PropertyValue<Object> STAND_ANGLE_RL = TrackerFlag.PROPERTY_OTHER.makeProperty("rightLegAngle");
        PropertyValue<Object> STAND_ANGLE_BO = TrackerFlag.PROPERTY_OTHER.makeProperty("bodyAngle");

        PropertyValue<Object> STAND_EQUIP_LA = TrackerFlag.PROPERTY_OTHER.makeProperty("equipmentLeftArm");
        PropertyValue<Object> STAND_EQUIP_RA = TrackerFlag.PROPERTY_OTHER.makeProperty("equipmentRightArm");
        PropertyValue<Object> STAND_EQUIP_HE = TrackerFlag.PROPERTY_OTHER.makeProperty("equipmentHelmet");
        PropertyValue<Object> STAND_EQUIP_CH = TrackerFlag.PROPERTY_OTHER.makeProperty("equipmentChestplate");
        PropertyValue<Object> STAND_EQUIP_LE = TrackerFlag.PROPERTY_OTHER.makeProperty("equipmentLeggings");
        PropertyValue<Object> STAND_EQUIP_BO = TrackerFlag.PROPERTY_OTHER.makeProperty("equipmentBoots");

        PropertyValue<Boolean> TRACKER_ALWAYS = TrackerFlag.TRACKER_BOOLEAN.makeProperty("ignoreLineOfSight");
        PropertyValue<Number> TRACKER_OFFSET = TrackerFlag.TRACKER_NUMBER.makeProperty("angleOffset");
        PropertyValue<Number> TRACKER_MAX_DISTANCE = TrackerFlag.TRACKER_NUMBER.makeProperty("scanDistance");
        PropertyValue<Boolean> TRACKER_LOCK = TrackerFlag.TRACKER_BOOLEAN.makeProperty("leapAtTarget");
        PropertyValue<Number> TRACKER_PERIOD = TrackerFlag.TRACKER_NUMBER.makeProperty("scanPeriod");
        PropertyValue<Number> TRACK_PERIOD = TrackerFlag.TRACKER_NUMBER.makeProperty("trackPeriod");

        assert location.getWorld() != null;

        LOCATION_X.update(location.getX());
        LOCATION_Y.update(location.getY());
        LOCATION_Z.update(location.getZ());
        LOCATION_YAW.update(location.getYaw());
        LOCATION_PITCH.update(location.getPitch());
        LOCATION_WORLD.update(location.getWorld().getUID());

        STAND_SMALL.update(false);
        STAND_BASE_PLATE.update(false);
        STAND_MARKER.update(true);
        STAND_INVINCIBLE.update(true);
        STAND_INVISIBLE.update(false);
        STAND_SHOW_NAME.update(true);
        STAND_ARMS.update(true);
        STAND_PICKUP_ITEMS.update(false);

        STAND_NAME.update("");

        STAND_ANGLE_LA.update(new EulerAngle(0, 0, 0));
        STAND_ANGLE_RA.update(new EulerAngle(0, 0, 0));
        STAND_ANGLE_LL.update(new EulerAngle(0, 0, 0));
        STAND_ANGLE_RL.update(new EulerAngle(0, 0, 0));
        STAND_ANGLE_BO.update(new EulerAngle(0, 0, 0));

        STAND_EQUIP_LA.update(new ItemStack(Material.AIR));
        STAND_EQUIP_RA.update(new ItemStack(Material.AIR));
        STAND_EQUIP_HE.update(new ItemStack(Material.AIR));
        STAND_EQUIP_CH.update(new ItemStack(Material.AIR));
        STAND_EQUIP_LE.update(new ItemStack(Material.AIR));
        STAND_EQUIP_BO.update(new ItemStack(Material.AIR));

        TRACKER_ALWAYS.update(false);
        TRACKER_OFFSET.update(0);
        TRACKER_MAX_DISTANCE.update(32);
        TRACKER_LOCK.update(true);
        TRACKER_PERIOD.update(randomPeriod(20));
        TRACK_PERIOD.update(5);

        properties.add(LOCATION_X);
        properties.add(LOCATION_Y);
        properties.add(LOCATION_Z);
        properties.add(LOCATION_YAW);
        properties.add(LOCATION_PITCH);
        properties.add(LOCATION_WORLD);

        properties.add(STAND_SMALL);
        properties.add(STAND_BASE_PLATE);
        properties.add(STAND_MARKER);
        properties.add(STAND_INVINCIBLE);
        properties.add(STAND_INVISIBLE);
        properties.add(STAND_SHOW_NAME);
        properties.add(STAND_ARMS);
        properties.add(STAND_PICKUP_ITEMS);

        properties.add(STAND_NAME);

        properties.add(STAND_ANGLE_LA);
        properties.add(STAND_ANGLE_RA);
        properties.add(STAND_ANGLE_LL);
        properties.add(STAND_ANGLE_RL);
        properties.add(STAND_ANGLE_BO);

        properties.add(STAND_EQUIP_LA);
        properties.add(STAND_EQUIP_RA);
        properties.add(STAND_EQUIP_HE);
        properties.add(STAND_EQUIP_CH);
        properties.add(STAND_EQUIP_LE);
        properties.add(STAND_EQUIP_BO);

        properties.add(TRACKER_ALWAYS);
        properties.add(TRACKER_OFFSET);
        properties.add(TRACKER_MAX_DISTANCE);
        properties.add(TRACKER_LOCK);
        properties.add(TRACKER_PERIOD);
        properties.add(TRACK_PERIOD);
    }

    /**
     * Tracker property
     */
    @SuppressWarnings("unused")
    public enum Property {
        /**
         * Tracker property
         */
        POSITION_X("x", TrackerFlag.PROPERTY_NUMBER),
        /**
         * Tracker property
         */
        POSITION_Y("y", TrackerFlag.PROPERTY_NUMBER),
        /**
         * Tracker property
         */
        POSITION_Z("z", TrackerFlag.PROPERTY_NUMBER),
        /**
         * Tracker property
         */
        POSITION_YAW("yaw", TrackerFlag.PROPERTY_NUMBER),
        /**
         * Tracker property
         */
        POSITION_PITCH("pitch", TrackerFlag.PROPERTY_NUMBER),
        /**
         * Tracker property
         */
        POSITION_WORLD("world", TrackerFlag.PROPERTY_UUID),
        /**
         * Tracker property
         */
        TRACKER_SMALL("small", TrackerFlag.PROPERTY_BOOLEAN),
        /**
         * Tracker property
         */
        TRACKER_BASE("basePlate", TrackerFlag.PROPERTY_BOOLEAN),
        /**
         * Tracker property
         */
        TRACKER_NO_HITBOX("marker", TrackerFlag.PROPERTY_BOOLEAN),
        /**
         * Tracker property
         */
        TRACKER_INVINCIBLE("invincible", TrackerFlag.PROPERTY_BOOLEAN),
        /**
         * Tracker property
         */
        TRACKER_INVISIBLE("invisible", TrackerFlag.PROPERTY_BOOLEAN),
        /**
         * Tracker property
         */
        TRACKER_SHOW_NAME("showName", TrackerFlag.PROPERTY_BOOLEAN),
        /**
         * Tracker property
         */
        TRACKER_ARMS("arms", TrackerFlag.PROPERTY_BOOLEAN),
        /**
         * Tracker property
         */
        TRACKER_CAN_TAKE_ITEMS("takeoffItems", TrackerFlag.PROPERTY_BOOLEAN),
        /**
         * Tracker property
         */
        TRACKER_NAME("customName", TrackerFlag.PROPERTY_STRING),
        /**
         * Tracker property
         */
        ANGLE_LEFT_ARM("leftArmAngle", TrackerFlag.PROPERTY_OTHER),
        /**
         * Tracker property
         */
        ANGLE_RIGHT_ARM("rightArmAngle", TrackerFlag.PROPERTY_OTHER),
        /**
         * Tracker property
         */
        ANGLE_LEFT_LEG("leftLegAngle", TrackerFlag.PROPERTY_OTHER),
        /**
         * Tracker property
         */
        ANGLE_RIGHT_LEG("rightLegAngle", TrackerFlag.PROPERTY_OTHER),
        /**
         * Tracker property
         */
        ANGLE_BODY("bodyAngle", TrackerFlag.PROPERTY_OTHER),
        /**
         * Tracker property
         */
        EQUIP_LEFT_ARM("equipmentLeftArm", TrackerFlag.PROPERTY_OTHER),
        /**
         * Tracker property
         */
        EQUIP_RIGHT_ARM("equipmentRightArm", TrackerFlag.PROPERTY_OTHER),
        /**
         * Tracker property
         */
        EQUIP_HELMET("equipmentHelmet", TrackerFlag.PROPERTY_OTHER),
        /**
         * Tracker property
         */
        EQUIP_CHESTPLATE("equipmentChestplate", TrackerFlag.PROPERTY_OTHER),
        /**
         * Tracker property
         */
        EQUIP_LEGGINGS("equipmentLeggings", TrackerFlag.PROPERTY_OTHER),
        /**
         * Tracker property
         */
        EQUIP_BOOTS("equipmentBoots", TrackerFlag.PROPERTY_OTHER),
        /**
         * Tracker property
         */
        ALWAYS_TRACK("ignoreLineOfSight", TrackerFlag.TRACKER_BOOLEAN),
        /**
         * Tracker property
         */
        OFFSET("angleOffset", TrackerFlag.TRACKER_NUMBER),
        /**
         * Tracker property
         */
        DISTANCE("scanDistance", TrackerFlag.TRACKER_NUMBER),
        /**
         * Tracker property
         */
        LOCK_TARGET("leapAtTarget", TrackerFlag.TRACKER_BOOLEAN),
        /**
         * Tracker property
         */
        PERIOD("scanPeriod", TrackerFlag.TRACKER_NUMBER),
        /**
         * Tracker property
         */
        LOOK_PERIOD("trackPeriod", TrackerFlag.TRACKER_NUMBER);

        private final String name;
        private final TrackerFlag type;

        /**
         * Initialize the property
         *
         * @param n the property name
         * @param t the property type
         */
        Property(final String n, final TrackerFlag t) {
            name = n;
            type = t;
        }

        /**
         * Get the property name
         *
         * @return the property name
         */
        public final String getName() {
            return name;
        }

        /**
         * Get the property type
         *
         * @return the property flag
         */
        public final TrackerFlag getFlag() {
            return type;
        }

        /**
         * Create a new value
         *
         * @return a new property value
         */
        public final <T> PropertyValue<T> createValue() {
            return type.makeProperty(name);
        }
    }
}
