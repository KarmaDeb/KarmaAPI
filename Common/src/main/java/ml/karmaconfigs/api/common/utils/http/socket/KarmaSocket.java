package ml.karmaconfigs.api.common.utils.http.socket;

import okhttp3.WebSocket;

public abstract class KarmaSocket {

    /**
     * Get the socket name
     *
     * @return the socket name
     */
    public abstract String getName();

    /**
     * Update the socket name
     *
     * @param n the new socket name
     * @return the old socket name
     */
    public abstract String updateName(final String n);

    /**
     * Send a message to the web socket
     *
     * @param message the message to send
     * @return if the message could be sent
     */
    public abstract boolean send(final String message);

    /**
     * Connect to the web socket
     *
     * @param listener the socket listener
     */
    public abstract void connect(final SocketListener listener);

    /**
     * This method should be run instead of {@link WebSocket#close(int, String)}
     *
     * @return if the socket could be closed
     */
    public boolean close() {
        return close(1000, "Normal");
    }

    /**
     * This method should be run instead of {@link WebSocket#close(int, String)}
     *
     * @param code the close code ( defaults to 1000 at {@link KarmaSocket#close()}
     * @param reason the close reason ( defaults to 'Normal' at {@link KarmaSocket#close()}
     * @return if the socket could be closed
     */
    public abstract boolean close(final int code, final String reason);
}
