package ml.karmaconfigs.api.bukkit;

import ml.karmaconfigs.api.common.karma.KarmaConfig;
import ml.karmaconfigs.api.common.timer.scheduler.Scheduler;
import ml.karmaconfigs.api.common.timer.worker.ScheduledTask;
import ml.karmaconfigs.api.common.timer.worker.event.TaskListener;
import ml.karmaconfigs.api.common.utils.enums.Level;
import org.bukkit.Bukkit;

import javax.swing.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Bukkit synchronized scheduler
 */
public class BukkitSyncScheduler extends Scheduler {

    private final static Map<KarmaPlugin, Set<TaskListener>> listeners = new ConcurrentHashMap<>();
    private final static Map<Integer, ScheduledTask> tasks = new ConcurrentHashMap<>();

    private final KarmaPlugin source;

    private static boolean executing_task = false;
    private static ScheduledExecutorService runner;
    private static int taskId = 0;
    private static int current_task = 0;

    BukkitSyncScheduler(final KarmaPlugin src) {
        source = src;
        boolean initialize = false;

        if (runner == null) {
            runner = Executors.newSingleThreadScheduledExecutor();
            initialize = true;
        }

        if (!runner.isShutdown() || runner.isTerminated()) {
            runner = Executors.newSingleThreadScheduledExecutor();
            initialize = true;
        }

        if (initialize) {
            runner.scheduleAtFixedRate(() -> {
                if (!executing_task) {
                    Integer[] ids = tasks.keySet().toArray(new Integer[0]);
                    Arrays.sort(ids);

                    current_task = ids[0];
                    if (tasks.containsKey(current_task)) {
                        ScheduledTask task = tasks.remove(current_task);
                        if (task != null) {
                            executing_task = true;
                            Set<TaskListener> registered = listeners.getOrDefault(source, Collections.newSetFromMap(new ConcurrentHashMap<>()));
                            registered.forEach((listener) -> listener.onSyncTaskStart(task));

                            Bukkit.getServer().getScheduler().runTask(source, task.getTask());
                            executing_task = false;

                            registered.forEach((listener) -> listener.onSyncTaskComplete(task));
                        }
                    }
                }
            }, 1, 1, TimeUnit.SECONDS);
        }
    }

    /**
     * Add a task listener
     *
     * @param listener the task listener
     */
    @Override
    public void addTaskListener(TaskListener listener) {
        Set<TaskListener> registered = listeners.getOrDefault(source, Collections.newSetFromMap(new ConcurrentHashMap<>()));
        registered.add(listener);

        listeners.put(source, registered);
    }

    /**
     * Remove a task listener
     *
     * @param listener the listener to remove
     */
    @Override
    public void removeTaskListener(TaskListener listener) {
        Set<TaskListener> registered = listeners.getOrDefault(source, Collections.newSetFromMap(new ConcurrentHashMap<>()));
        registered.remove(listener);

        listeners.put(source, registered);
    }

    /**
     * Queue another task to the scheduler
     *
     * @param name          the task name
     * @param paramRunnable the task to perform
     */
    @Override
    public void queue(String name, Runnable paramRunnable) {
        int task = taskId++;

        ScheduledTask tsk = new ScheduledTask(name, paramRunnable, task);
        tasks.put(task, tsk);

        Set<TaskListener> registered = listeners.getOrDefault(source, Collections.newSetFromMap(new ConcurrentHashMap<>()));
        registered.forEach((listener) -> listener.onSyncTaskSchedule(tsk));
    }

    /**
     * Get a scheduled task by name
     *
     * @param name the task name
     * @return the task
     */
    @Override
    public ScheduledTask[] getByName(String name) {
        return new ScheduledTask[0];
    }

    /**
     * Get a scheduled task by id
     *
     * @param id the task id
     * @return the task
     */
    @Override
    public ScheduledTask getById(int id) {
        return null;
    }

    /**
     * Get the current task id
     *
     * @return the current task id
     */
    @Override
    public int currentTask() {
        return current_task;
    }
}
