package ml.karmaconfigs.api.common.utils.http.listener;

import ml.karmaconfigs.api.common.utils.http.socket.KarmaSocket;
import ml.karmaconfigs.api.common.utils.http.socket.SocketEvent;
import okhttp3.Response;
import okhttp3.WebSocket;
import org.jetbrains.annotations.Nullable;

/**
 * Event when a socket gets disconnected or the connection was failed
 */
public class SocketDisconnected extends SocketEvent {

    private final String name;
    private final KarmaSocket socket;
    private final String reason;
    private final Throwable error;

    public SocketDisconnected(final String n, final KarmaSocket s, final String r, final @Nullable Throwable e) {
        name = n;
        socket = s;
        reason = r;
        error = e;
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
     * Get the reason when disconnected
     *
     * @return the disconnect reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * Get the error
     *
     * @return the disconnect error
     */
    public @Nullable Throwable getError() {
        return error;
    }
}
