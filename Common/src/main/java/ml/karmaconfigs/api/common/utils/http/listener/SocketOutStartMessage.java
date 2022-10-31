package ml.karmaconfigs.api.common.utils.http.listener;

import ml.karmaconfigs.api.common.utils.http.socket.KarmaSocket;
import ml.karmaconfigs.api.common.utils.http.socket.SocketEvent;
import okhttp3.WebSocket;

/**
 * Event when a message is about to be sent
 */
public class SocketOutStartMessage extends SocketEvent {

    private final String name;
    private final KarmaSocket socket;

    private final String original_message;
    private String message = null;

    public SocketOutStartMessage(final String n, final KarmaSocket s, final String m) {
        name = n;
        socket = s;
        original_message = m;
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
     * @param original force the original message
     * @return the message
     */
    public String getMessage(final boolean original) {
        if (!original) {
            return (message != null ? message : original_message);
        }

        return original_message;
    }

    /**
     * Get if the message has been modified
     *
     * @return if the message has been modified
     */
    public boolean isModified() {
        return message != null && !message.equals(original_message);
    }

    /**
     * Set the message
     *
     * @param m the new message
     */
    public void setMessage(final String m) {
        message = m;
    }
}
