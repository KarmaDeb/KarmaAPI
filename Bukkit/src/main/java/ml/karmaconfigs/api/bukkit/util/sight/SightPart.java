package ml.karmaconfigs.api.bukkit.util.sight;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

/**
 * Tracker line of sight part
 */
public enum SightPart {
    /**
     * Head part
     */
    HEAD,
    /**
     * Body part
     */
    BODY,
    /**
     * Feet part
     */
    FEET,
    /**
     * No line of sight
     */
    NONE;

    private Location location;
    private Location[] trace_locations;
    private Entity sight_entity;
    private Block sight_block;

    /**
     * Get the sight part hit location
     *
     * @return the hit location
     */
    public Location getHitLocation() {
        return location;
    }

    /**
     * Get the line of sight trace
     *
     * @return the line of sight trace locations
     */
    public Location[] getTrace() {
        return trace_locations.clone();
    }

    /**
     * Get the entity in the line of sight
     *
     * @return the entity
     */
    public Entity getEntity() {
        return sight_entity;
    }

    /**
     * Get the block in the line of sight
     *
     * @return the block
     */
    public Block getBlock() {
        return sight_block;
    }

    /**
     * Set the line of sight trace locations
     *
     * @param locations the traced locations
     * @return the current line of sight
     */
    public SightPart setTraceLocation(final Location... locations) {
        trace_locations = locations;
        return this;
    }

    /**
     * Set the sight part hit location
     *
     * @param hitLocation the hit location
     * @return the current line of sight
     */
    public SightPart setHitLocation(final Location hitLocation) {
        location = hitLocation;
        return this;
    }

    /**
     * Set the sight part hit entity
     *
     * @param entity the entity
     * @return the current line of sight
     */
    public SightPart setHitEntity(final Entity entity) {
        sight_entity = entity;
        return this;
    }

    /**
     * Set the sight part hit block
     *
     * @param block the block
     * @return the current line of sight
     */
    public SightPart setHitBlock(final Block block) {
        sight_block = block;
        return this;
    }
}
