package ml.karmaconfigs.api.bukkit.region.flag.types;

import ml.karmaconfigs.api.bukkit.region.flag.RegionFlag;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Region flag
 */
public final class EntityFlag extends RegionFlag<EntityType[]> {

    private final String key;
    private final Set<EntityType> types = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /**
     * Initialize the entity flag
     *
     * @param name    the flag name
     * @param initial the initial values
     */
    public EntityFlag(final String name, final EntityType... initial) {
        key = name;
        types.addAll(Arrays.asList(initial));
    }

    /**
     * Add an entity type
     *
     * @param type the entity type to add
     */
    public void addEntity(final EntityType type) {
        types.add(type);
    }

    /**
     * Remove an entity type
     *
     * @param type the entity type
     */
    public void removeEntity(final EntityType type) {
        types.remove(type);
    }

    /**
     * Get the region flag key
     *
     * @return the region flag key
     */
    @Override
    public String getKey() {
        return key;
    }

    /**
     * Get the region flag value
     *
     * @return the region flag value
     */
    @Override
    public EntityType[] getValue() {
        return types.toArray(new EntityType[0]);
    }

    /**
     * Update the flag
     *
     * @param newValue the new value
     */
    @Override
    public void update(final EntityType[] newValue) {
        types.clear();
        types.addAll(Arrays.asList(newValue));
    }

    /**
     * Update the flag unsafely
     *
     * @param newValue the new value
     * @return if the value could be set
     */
    @Override
    public boolean updateUnsafe(final Object newValue) {
        boolean valid = false;

        if (newValue instanceof Collection) {
            Collection<?> collection = (Collection<?>) newValue;

            for (Object object : collection) {
                EntityType type = null;

                if (object instanceof Entity) {
                    Entity entity = (Entity) object;
                    type = entity.getType();
                } else {
                    if (object instanceof EntityType) {
                        type = (EntityType) object;
                    }
                }

                if (type != null) {
                    valid = true;
                    types.add(type);
                }
            }

            return true;
        }
        if (newValue != null && newValue.getClass().isArray()) {
            assert newValue instanceof Object[];
            Object[] objects = (Object[]) newValue;

            for (Object object : objects) {
                EntityType type = null;

                if (object instanceof Entity) {
                    Entity entity = (Entity) object;
                    type = entity.getType();
                } else {
                    if (object instanceof EntityType) {
                        type = (EntityType) object;
                    }
                }

                if (type != null) {
                    valid = true;
                    types.add(type);
                }
            }
        }

        return valid;
    }

    /**
     * Get the flag type
     *
     * @return the flag type
     */
    @Override
    public Class<? extends EntityType[]> getType() {
        return EntityType[].class;
    }
}
