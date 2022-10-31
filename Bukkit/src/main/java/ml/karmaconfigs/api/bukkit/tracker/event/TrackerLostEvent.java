package ml.karmaconfigs.api.bukkit.tracker.event;

import ml.karmaconfigs.api.bukkit.tracker.Tracker;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Tracker lost event, this event is fired when the
 * tracker is no longer able to track the target
 */
public class TrackerLostEvent extends Event {

    private final static HandlerList HANDLER_LIST = new HandlerList();

    private final Tracker tracker;
    private final LivingEntity target;

    /**
     * Initialize the event
     *
     * @param t the tracker
     * @param l the tracker target
     */
    public TrackerLostEvent(final Tracker t, final LivingEntity l) {
        tracker = t;
        target = l;
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
     * Get the tracker target
     *
     * @return the tracker target
     */
    public LivingEntity getTarget() {
        return target;
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
