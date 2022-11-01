package ml.karmaconfigs.api.common.utils.http;

import ml.karmaconfigs.api.common.utils.http.listener.*;
import ml.karmaconfigs.api.common.utils.http.socket.SocketListener;

/**
 * Socket event adapter
 */
public abstract class SocketListenerAdapter implements SocketListener {

    /**
     * Socket listener
     *
     * @param event the event
     */
    public void onConnectionSuccess(final SocketConnected event) {
    }

    /**
     * Socket listener
     *
     * @param event the event
     */
    public void onDisconnection(final SocketDisconnected event) {
    }

    /**
     * Socket listener
     *
     * @param event the event
     */
    public void onMessageResponse(final SocketInReceiveMessage event) {
    }

    /**
     * Socket listener
     *
     * @param event the event
     */
    public void onMessageSend(final SocketOutSendMessage event) {
    }

    /**
     * Socket listener
     *
     * @param event the event
     */
    public void onMessagePrepare(final SocketOutStartMessage event) {
    }
}
