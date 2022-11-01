package ml.karmaconfigs.api.common.utils.http.listener;

import ml.karmaconfigs.api.common.utils.http.socket.KarmaSocket;
import ml.karmaconfigs.api.common.utils.http.socket.SocketEvent;
import okhttp3.Response;

/**
 * Event when the socket connects successfully
 */
public final class SocketConnected extends SocketEvent {

    private final String name;
    private final KarmaSocket socket;
    private final Response response;

    /**
     * Initialize the event
     *
     * @param n the connection name
     * @param s the connection socket
     * @param r the connection response
     */
    public SocketConnected(final String n, final KarmaSocket s, final Response r) {
        name = n;
        socket = s;
        response = r;
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
     * Get the connection response
     *
     * @return the connection response
     */
    public Response getResponse() {
        return response;
    }
}
