package ml.karmaconfigs.api.common.utils.http.listener;

import ml.karmaconfigs.api.common.utils.http.socket.KarmaSocket;
import ml.karmaconfigs.api.common.utils.http.socket.SocketEvent;

/**
 * Event when a message have been sent
 */
public class SocketOutSendMessage extends SocketEvent {

    private final String name;
    private final KarmaSocket socket;

    private final String message;

    public SocketOutSendMessage(final String n, final KarmaSocket s, final String m) {
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
     * Get the message
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }
}
