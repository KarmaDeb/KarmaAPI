package ml.karmaconfigs.api.common.utils.http.listener;

import ml.karmaconfigs.api.common.utils.http.socket.KarmaSocket;
import ml.karmaconfigs.api.common.utils.http.socket.SocketEvent;
import ml.karmaconfigs.api.common.utils.http.socket.SocketListener;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Socket handler
 */
public final class SocketEventHandler {

    private final static Map<KarmaSocket, Set<SocketListener>> listeners = new ConcurrentHashMap<>();

    /**
     * Add a listener
     *
     * @param socket the socket to listen at
     * @param listener the socket listener
     */
    public static void addListener(final KarmaSocket socket, final SocketListener listener) {
        Set<SocketListener> list = listeners.getOrDefault(socket, Collections.newSetFromMap(new ConcurrentHashMap<>()));
        list.add(listener);
        listeners.put(socket, list);
    }

    /**
     * Remove a listener
     *
     * @param socket the socket to listen at
     * @param listener the socket listener
     */
    public static void removeListener(final KarmaSocket socket, final SocketListener listener) {
        Set<SocketListener> list = listeners.getOrDefault(socket, Collections.newSetFromMap(new ConcurrentHashMap<>()));
        list.remove(listener);
        listeners.put(socket, list);
    }

    /**
     * Call an event on a socket
     *
     * @param socket the socket
     * @param event the event to call
     */
    public static void callEvent(final KarmaSocket socket, SocketEvent event) {
        Set<SocketListener> list = listeners.getOrDefault(socket, Collections.newSetFromMap(new ConcurrentHashMap<>()));
        for (SocketListener listener : list) {
            Method[] methods = listener.getClass().getDeclaredMethods();

            for (Method method : methods) {
                Parameter[] params = method.getParameters();

                if (params.length == 1) {
                    Parameter parameter = params[0];
                    if (parameter.getType().equals(event.getClass())) {
                        try {
                            method.invoke(listener, event);
                        } catch (Throwable ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
