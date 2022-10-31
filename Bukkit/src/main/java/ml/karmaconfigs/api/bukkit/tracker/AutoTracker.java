package ml.karmaconfigs.api.bukkit.tracker;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;

/**
 * Auto tracker
 */
public interface AutoTracker {

    /**
     * Track an entity
     *
     * @param tracker the tracker
     * @param max_radius the max search radius
     * @return the living entity, or null if none
     */
    default LivingEntity track(final Tracker tracker, final double max_radius) {
        LivingEntity entity = null;

        Collection<Entity> entities = tracker.getWorld().getNearbyEntities(tracker.getLocation(), max_radius, max_radius, max_radius);
        for (Entity ent : entities) {
            if (ent instanceof LivingEntity) {
                LivingEntity living = (LivingEntity) ent;
                if (!(living instanceof ArmorStand)) {
                    entity = living;
                    break;
                }
            }
        }

        return entity;
    }
}
