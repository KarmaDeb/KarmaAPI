package ml.karmaconfigs.api.common.utils.http.listener;

import ml.karmaconfigs.api.common.utils.http.socket.KarmaSocket;
import ml.karmaconfigs.api.common.utils.http.socket.SocketEvent;

/**
 * Event when the socket gives a response
 */
public class SocketInReceiveMessage extends SocketEvent {

    private final String name;
    private final KarmaSocket socket;
    private final String message;

    public SocketInReceiveMessage(final String n, final KarmaSocket s, final String m) {
        name = n;
        socket = s;
        message = m;
    }

    /**
     * Get the socket connection name
     *
     * @return the socket connection name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Get the web socket
     *
     * @return the web socket
     */
    @Override
    public KarmaSocket getSocket() {
        return socket;
    }

    /**
     * Get the response
     *
     * @return the response
     */
    public String getMessage() {
        return message;
    }
}
