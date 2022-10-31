package ml.karmaconfigs.api.bukkit.tracker.event;

import ml.karmaconfigs.api.bukkit.tracker.Tracker;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Tracker tick event, is fired when the tracker
 * has a valid target, and it's on its line of sight ( or always
 * if specified in tracker properties )
 */
public class TrackerSecondEvent extends Event {

    private final static HandlerList HANDLER_LIST = new HandlerList();

    private final Tracker tracker;
    private final LivingEntity target;
    private final int second;

    /**
     * Initialize the tracker tick event
     *
     * @param t the tracker
     * @param l the target
     * @param s the second
     */
    public TrackerSecondEvent(final Tracker t, final LivingEntity l, final int s) {
        tracker = t;
        target = l;
        second = s;
    }

    /**
     * Get the tracker target
     *
     * @return the tracker target
     */
    public Tracker getTracker() {
        return tracker;
    }

    /**
     * Get the track target
     *
     * @return the track target
     */
    public LivingEntity getTarget() {
        return target;
    }

    /**
     * Get the time the entity has been tracked for
     *
     * @return the track time
     */
    public int getSecond() {
        return second;
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
