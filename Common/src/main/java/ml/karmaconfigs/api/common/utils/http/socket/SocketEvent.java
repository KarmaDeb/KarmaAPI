package ml.karmaconfigs.api.common.utils.http.socket;

/**
 * Socket event
 */
public abstract class SocketEvent {

    /**
     * Get the socket connection name
     *
     * @return the socket connection name
     */
    public abstract String getName();

    /**
     * Get the web socket
     *
     * @return the web socket
     */
    public abstract KarmaSocket getSocket();
}
