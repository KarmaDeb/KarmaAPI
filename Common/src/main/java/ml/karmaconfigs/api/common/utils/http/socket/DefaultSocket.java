package ml.karmaconfigs.api.common.utils.http.socket;

import okhttp3.Request;
import okhttp3.WebSocket;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;

/**
 * Default socket
 */
public class DefaultSocket implements WebSocket {

    private final WebSocket master;
    private final KarmaSocket socket;

    /**
     * Initialize the default socket
     *
     * @param s the web socket
     * @param k the karma socket
     */
    public DefaultSocket(final WebSocket s, final KarmaSocket k) {
        master = s;
        socket = k;
    }

    /**
     * Get the socket request
     *
     * @return the socket request
     */
    @Override
    public @NotNull Request request() {
        return master.request();
    }

    /**
     * Get the socket queue size
     *
     * @return the socket queue size
     */
    @Override
    public long queueSize() {
        return master.queueSize();
    }

    /**
     * Send a message to the socket
     *
     * @param s the socket message
     * @return if the message could be sent
     */
    @Override
    public boolean send(final @NotNull String s) {
        return master.send(s);
    }

    /**
     * Send a message to the socket
     *
     * @param byteString the byte string message
     * @return if the message could be sent
     */
    @Override
    public boolean send(final @NotNull ByteString byteString) {
        return master.send(byteString);
    }

    /**
     * Close the socket
     *
     * @param i the close code
     * @param s the close reason
     * @return if the socket could be closed
     */
    @Override
    public boolean close(final int i, final String s) {
        return socket.close(i, s);
    }

    /**
     * Cancel the socket connection
     */
    @Override
    public void cancel() {
        master.cancel();
    }
}
