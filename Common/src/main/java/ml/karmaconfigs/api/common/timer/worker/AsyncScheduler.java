package ml.karmaconfigs.api.common.timer.worker;

import ml.karmaconfigs.api.common.karma.KarmaSource;
import ml.karmaconfigs.api.common.timer.scheduler.Scheduler;
import ml.karmaconfigs.api.common.timer.worker.event.TaskListener;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Asynchronous scheduler
 *
 * @param <T> the karma source
 */
public class AsyncScheduler<T extends KarmaSource> extends Scheduler {

    private final static Map<KarmaSource, Set<TaskListener>> listeners = new ConcurrentHashMap<>();
    private final static Map<Integer, ScheduledTask> tasks = new ConcurrentHashMap<>();

    private final KarmaSource source;

    private static ScheduledExecutorService runner;
    private static int taskId = 0;
    private static int current_task = 0;

    public AsyncScheduler(final T src) {
        source = src;
        boolean initialize = false;

        if (runner == null) {
            //We will use the half of processors to have the best performance without using all CPU
            int threadCount = Math.abs(Runtime.getRuntime().availableProcessors() / 2);
            if (threadCount <= 0)
                threadCount = 1;

            runner = Executors.newScheduledThreadPool(threadCount);
            initialize = true;
        }

        if (!runner.isShutdown() || runner.isTerminated()) {
            //We will use the half of processors to have the best performance without using all CPU
            int threadCount = Math.abs(Runtime.getRuntime().availableProcessors() / 2);
            if (threadCount <= 0)
                threadCount = 1;

            runner = Executors.newScheduledThreadPool(threadCount);
            initialize = true;
        }

        if (initialize) {
            runner.scheduleAtFixedRate(() -> {
                Integer[] ids = tasks.keySet().toArray(new Integer[0]);
                Arrays.sort(ids);

                current_task = ids[0];
                if (tasks.containsKey(current_task)) {
                    ScheduledTask task = tasks.remove(current_task);
                    if (task != null) {
                        runner.execute(() -> {
                            Set<TaskListener> registered = listeners.getOrDefault(source, Collections.newSetFromMap(new ConcurrentHashMap<>()));
                            registered.forEach((listener) -> listener.onAsyncTaskStart(task));

                            task.getTask().run();

                            registered.forEach((listener) -> listener.onAsyncTaskComplete(task));
                        });
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
    public void addTaskListener(final TaskListener listener) {
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
    public void removeTaskListener(final TaskListener listener) {
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
    public void queue(final String name, final Runnable paramRunnable) {
        int task = taskId++;

        ScheduledTask tsk = new ScheduledTask(name, paramRunnable, task);
        tasks.put(task, tsk);

        Set<TaskListener> registered = listeners.getOrDefault(source, Collections.newSetFromMap(new ConcurrentHashMap<>()));
        registered.forEach((listener) -> listener.onAsyncTaskSchedule(tsk));
    }

    /**
     * Get a scheduled task by name
     *
     * @param name the task name
     * @return the task
     */
    @Override
    public ScheduledTask[] getByName(final String name) {
        List<ScheduledTask> fetched = new ArrayList<>();

        Integer[] ids = tasks.keySet().toArray(new Integer[0]);
        Arrays.sort(ids);

        for (int id : ids) {
            ScheduledTask tmp = tasks.getOrDefault(id, null);
            if (tmp != null && tmp.getName().equals(name))
                fetched.add(tmp);
        }

        return fetched.toArray(new ScheduledTask[0]);
    }

    /**
     * Get a scheduled task by id
     *
     * @param id the task id
     * @return the task
     */
    @Override
    public @Nullable ScheduledTask getById(final int id) {
        return tasks.getOrDefault(id, null);
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
