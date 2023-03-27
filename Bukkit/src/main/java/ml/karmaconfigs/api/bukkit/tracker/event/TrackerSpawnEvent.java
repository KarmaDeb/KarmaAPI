package ml.karmaconfigs.api.bukkit.tracker.event;

import ml.karmaconfigs.api.bukkit.tracker.Tracker;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event is fired when a tracker is
 * spawned or respawned
 */
public class TrackerSpawnEvent extends Event {

    private final static HandlerList HANDLER_LIST = new HandlerList();

    private final Tracker tracker;

    private boolean respawn = false;

    /**
     * Initialize the event
     *
     * @param t the tracker
     * @param r respawn status
     */
    public TrackerSpawnEvent(final Tracker t, final boolean r) {
        tracker = t;
        respawn = r;
    }

    /**
     * Get the tracker
     *
     * @return the tracker
     */
    public Tracker getTracker() {
        return tracker;
    }

    /**
     * Get if the spawn even is for a
     * respawn
     *
     * @return if the stand have spawned or respawned
     */
    public boolean isRespawn() {
        return respawn;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    /**
     * Get a list of event handlers
     *
     * @return a list of event handlers
     */
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
