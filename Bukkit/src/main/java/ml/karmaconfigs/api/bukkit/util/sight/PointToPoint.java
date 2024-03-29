package ml.karmaconfigs.api.bukkit.util.sight;

import ml.karmaconfigs.api.bukkit.util.LineOfSight;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Point to point line of sight
 */
public class PointToPoint extends LineOfSight {

    private final Location pointA;
    private final Location pointB;

    private final Set<UUID> ignored = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private final double[] a_offset = new double[]{0, 0, 0};
    private final double[] b_offset = new double[]{0, 0, 0};

    public PointToPoint(final Location a, final Location b) {
        pointA = a.clone();
        pointB = b.clone();
    }

    /**
     * Point A offset
     *
     * @param offset the point A offset
     * @return the point A offset
     */
    public PointToPoint a(final double... offset) {
        for (int i = 0; i < offset.length; i++) {
            if (i <= 2)
                a_offset[i] = offset[i];
        }

        return this;
    }

    /**
     * Point B offset
     *
     * @param offset the point B offset
     * @return the point B offset
     */
    public PointToPoint b(final double... offset) {
        for (int i = 0; i < offset.length; i++) {
            if (i <= 2)
                b_offset[i] = offset[i];
        }

        return this;
    }

    /**
     * Add an ignored entity
     *
     * @param entities the entities to ignore
     * @return the ignored entities
     */
    public PointToPoint ignore(final Entity... entities) {
        for (Entity entity : entities)
            ignored.add(entity.getUniqueId());

        return this;
    }

    /**
     * Get if line of sight
     *
     * @return if line of sight
     */
    @Override
    public boolean inLineOfSight() {
        Location standLocation = pointA.clone().add(a_offset[0], a_offset[1], a_offset[2]);
        Location trackLocation = pointB.clone().add(b_offset[0], b_offset[1], b_offset[2]);

        Vector direction = trackLocation.toVector().subtract(standLocation.toVector()).normalize();
        World world = standLocation.getWorld();
        assert world != null;

        boolean hit = false;
        for (double i = 0.1; i < 64; i += 0.1) {
            direction.multiply(i);
            standLocation.add(direction);

            Block block = standLocation.getBlock();
            if (block.getType().isOccluding() && block.getType().isSolid()) {
                break;
            } else {
                Entity ray_entity = null;

                Collection<Entity> entities = world.getEntities();
                for (Entity entity : entities) {
                    if (!ignored.contains(entity.getUniqueId())) {
                        Location ent_location = entity.getLocation().clone();
                        ent_location.setY(standLocation.clone().getY());

                        if (ent_location.distance(standLocation.clone()) <= 0.1) {
                            ray_entity = entity;
                            break;
                        }
                    }
                }

                standLocation.subtract(direction);
                direction.normalize();

                if (ray_entity != null) {
                    break;
                } else {
                    Location b_chk = pointB.clone();
                    b_chk.setY(standLocation.clone().getY());

                    if (b_chk.distance(standLocation.clone()) <= 0.1) {
                        hit = true;
                        break;
                    }
                }
            }
        }

        return hit;
    }

    /**
     * Get if line of sight with max distance
     *
     * @param max_distance the maximum distance
     * @return if line of sight inside the range
     */
    @Override
    public boolean inLineOfSight(final double max_distance) {
        Location standLocation = pointA.clone().add(a_offset[0], a_offset[1], a_offset[2]);
        Location trackLocation = pointB.clone().add(b_offset[0], b_offset[1], b_offset[2]);

        Vector direction = trackLocation.toVector().subtract(standLocation.toVector()).normalize();
        World world = standLocation.getWorld();
        assert world != null;

        boolean hit = false;
        for (double i = 0.1; i < max_distance; i += 0.1) {
            direction.multiply(i);
            standLocation.add(direction);

            Block block = standLocation.getBlock();
            if (block.getType().isOccluding() && block.getType().isSolid()) {
                break;
            } else {
                Entity ray_entity = null;

                Collection<Entity> entities = world.getEntities();
                for (Entity entity : entities) {
                    if (!ignored.contains(entity.getUniqueId())) {
                        Location ent_location = entity.getLocation().clone();
                        ent_location.setY(standLocation.clone().getY());

                        if (ent_location.distance(standLocation.clone()) <= 0.1) {
                            ray_entity = entity;
                            break;
                        }
                    }
                }

                standLocation.subtract(direction);
                direction.normalize();

                if (ray_entity != null) {
                    break;
                } else {
                    Location b_chk = pointB.clone();
                    b_chk.setY(standLocation.clone().getY());

                    if (b_chk.distance(standLocation.clone()) <= 0.1) {
                        hit = true;
                        break;
                    }
                }
            }
        }

        return hit;
    }

    /**
     * Get line of sight
     *
     * @return the line of sight
     */
    @Override
    public Iterator<Location> getLineOfSight() {
        List<Location> locations = new ArrayList<>();

        Location standLocation = pointA.clone().add(a_offset[0], a_offset[1], a_offset[2]);
        Location trackLocation = pointB.clone().add(b_offset[0], b_offset[1], b_offset[2]);

        Vector direction = trackLocation.toVector().subtract(standLocation.toVector()).normalize();
        World world = standLocation.getWorld();
        assert world != null;

        for (double i = 0.1; i < 64; i += 0.1) {
            direction.multiply(i);
            standLocation.add(direction);

            locations.add(standLocation.clone());
            Block block = standLocation.getBlock();
            if (block.getType().isOccluding() && block.getType().isSolid()) {
                break;
            } else {
                Entity ray_entity = null;

                Collection<Entity> entities = world.getEntities();
                for (Entity entity : entities) {
                    if (!ignored.contains(entity.getUniqueId())) {
                        Location ent_location = entity.getLocation().clone();
                        ent_location.setY(standLocation.clone().getY());

                        if (ent_location.distance(standLocation.clone()) <= 0.1) {
                            ray_entity = entity;
                        }
                    }
                }

                standLocation.subtract(direction);
                direction.normalize();

                if (ray_entity != null) {
                    break;
                } else {
                    locations.add(standLocation.clone());
                }
            }
        }

        return locations.iterator();
    }

    /**
     * Get line of sight with max distance
     *
     * @param max_distance the maximum distance
     * @return the line of sight inside the range
     */
    @Override
    public Iterator<Location> getLineOfSight(final double max_distance) {
        List<Location> locations = new ArrayList<>();

        Location standLocation = pointA.clone().add(a_offset[0], a_offset[1], a_offset[2]);
        Location trackLocation = pointB.clone().add(b_offset[0], b_offset[1], b_offset[2]);

        Vector direction = trackLocation.toVector().subtract(standLocation.toVector()).normalize();
        World world = standLocation.getWorld();
        assert world != null;

        for (double i = 0.1; i < max_distance; i += 0.1) {
            direction.multiply(i);
            standLocation.add(direction);

            locations.add(standLocation.clone());
            Block block = standLocation.getBlock();
            if (block.getType().isOccluding() && block.getType().isSolid()) {
                break;
            } else {
                Entity ray_entity = null;

                Collection<Entity> entities = world.getEntities();
                for (Entity entity : entities) {
                    if (!ignored.contains(entity.getUniqueId())) {
                        Location ent_location = entity.getLocation().clone();
                        ent_location.setY(standLocation.clone().getY());

                        if (ent_location.distance(standLocation.clone()) <= 0.1) {
                            ray_entity = entity;
                        }
                    }
                }

                standLocation.subtract(direction);
                direction.normalize();

                if (ray_entity != null) {
                    break;
                } else {
                    locations.add(standLocation.clone());
                }
            }
        }

        return locations.iterator();
    }
}
