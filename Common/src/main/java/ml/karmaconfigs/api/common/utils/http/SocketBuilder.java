package ml.karmaconfigs.api.common.utils.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import ml.karmaconfigs.api.common.JavaVM;
import ml.karmaconfigs.api.common.karma.KarmaAPI;
import ml.karmaconfigs.api.common.utils.http.listener.*;
import ml.karmaconfigs.api.common.utils.http.socket.KarmaSocket;
import ml.karmaconfigs.api.common.utils.http.socket.SocketListener;
import ml.karmaconfigs.api.common.utils.string.RandomString;
import ml.karmaconfigs.api.common.utils.string.StringUtils;
import ml.karmaconfigs.api.common.utils.string.util.TextContent;
import ml.karmaconfigs.api.common.utils.string.util.TextType;
import okhttp3.*;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

/**
 * Socket builder
 */
public final class SocketBuilder {

    private final static OkHttpClient client;

    static {
        KarmaAPI.install();

        client = new OkHttpClient.Builder()
                .callTimeout(15, TimeUnit.SECONDS)
                .build();
    }

    private String host = "127.0.0.1";
    private OkHttpClient forced_client = client;

    /**
     * Set the socket host target
     *
     * @param h the socket host
     * @return this instance
     */
    public SocketBuilder setHost(final String h) {
        host = h;
        return this;
    }

    public SocketBuilder setClient(final OkHttpClient client) {
        forced_client = client;
        return this;
    }

    /**
     * Create the karma socket
     *
     * @return the new socket
     */
    public KarmaSocket build() {
        return new SocketImp(host, (forced_client != null ? forced_client : client));
    }

    /**
     * Socket implementation
     */
    public static final class SocketImp extends KarmaSocket {

        private String name;
        private final String host;
        private final OkHttpClient client;

        private WebSocket created_socket = null;
        private boolean connected = false;

        SocketImp(final String h, final OkHttpClient c) {
            host = "ws://" + h + "/";
            client = c;
        }

        /**
         * Get the socket name
         *
         * @return the socket name
         */
        @Override
        public String getName() {
            return name;
        }

        /**
         * Update the socket name
         *
         * @param n the new socket name
         * @return the old socket name
         */
        @Override
        public String updateName(final String n) {
            final String old = name;
            name = n;
            return old;
        }

        /**
         * Send a message to the web socket
         *
         * @param message the message to send
         * @return if the message could be sent
         */
        @Override
        public boolean send(final String message) {
            if (created_socket != null) {
                //By default, we won't allow message modification
                SocketOutStartMessage start = new SocketOutStartMessage(name, this, message);
                SocketEventHandler.callEvent(this, start);

                if (start.isModified()) {
                    return false;
                }

                created_socket.send(message);

                SocketOutSendMessage send = new SocketOutSendMessage(name, this, message);
                SocketEventHandler.callEvent(this, send);
            }

            return false;
        }

        /**
         * Connect to the web socket
         *
         * @param listener the socket listener
         * @throws IllegalStateException if something goes wrong while socket channeling or socket
         * creation
         */
        @Override
        public void connect(final SocketListener listener) throws IllegalStateException {
            if (created_socket == null) {
                name = StringUtils.generateString(RandomString.createBuilder()
                        .withContent(TextContent.ONLY_LETTERS)
                        .withSize(16)
                        .withType(TextType.RANDOM_SIZE)).create();

                Request request = new Request.Builder()
                        .url(host)
                        .header("User-Agent", "KarmaAPI/" + KarmaAPI.getVersion() + " (" + JavaVM.getSystem().getName() + " " + JavaVM.osVersion() + "; " + JavaVM.osModel() + "; " + JavaVM.osArchitecture() + ") AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36")
                        .build();

                if (listener != null) {
                    SocketEventHandler.addListener(this, listener);
                }

                KarmaSocket ks = this;
                //Socket creation
                created_socket = client.newWebSocket(request, new WebSocketListener() {
                    @Override
                    public void onOpen(final @NotNull WebSocket webSocket, final @NotNull Response response) {
                        SocketConnected event = new SocketConnected(name, ks, response);
                        super.onOpen(webSocket, response);
                        SocketEventHandler.callEvent(ks, event);
                        connected = true;
                    }

                    @Override
                    public void onMessage(final @NotNull WebSocket webSocket, final @NotNull String text) {
                        try {
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            JsonElement element = gson.fromJson(text, JsonElement.class);

                            SocketInReceiveJson event = new SocketInReceiveJson(name, ks, element);
                            //super.onMessage(webSocket, text);
                            SocketEventHandler.callEvent(ks, event);
                        } catch (Throwable ex) {
                            SocketInReceiveMessage event = new SocketInReceiveMessage(name, ks, text);
                            //super.onMessage(webSocket, text);
                            SocketEventHandler.callEvent(ks, event);
                        }
                    }

                    @Override
                    public void onMessage(final @NotNull WebSocket webSocket, final @NotNull ByteString bytes) {
                        String text = bytes.utf8();

                        try {
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            JsonElement element = gson.fromJson(text, JsonElement.class);

                            SocketInReceiveJson event = new SocketInReceiveJson(name, ks, element);
                            //super.onMessage(webSocket, text);
                            SocketEventHandler.callEvent(ks, event);
                        } catch (Throwable ex) {
                            SocketInReceiveMessage event = new SocketInReceiveMessage(name, ks, text);
                            //super.onMessage(webSocket, text);
                            SocketEventHandler.callEvent(ks, event);
                        }
                    }

                    @Override
                    public void onClosed(final @NotNull WebSocket webSocket, final int code, final @NotNull String reason) {
                        SocketDisconnected event = new SocketDisconnected(name, ks, reason, null);
                        super.onClosed(webSocket, code, reason);
                        SocketEventHandler.callEvent(ks, event);

                        created_socket = null;
                        connected = false;
                    }

                    @Override
                    public void onFailure(final @NotNull WebSocket webSocket, final @NotNull Throwable t, final @Nullable Response response) {
                        String message = "";
                        if (!connected) {
                            message = "Failed to connect to server";
                        }
                        if (response != null) {
                            message = response.message();
                        }

                        SocketDisconnected event = new SocketDisconnected(name, ks, message, t);
                        super.onFailure(webSocket, t, response);
                        SocketEventHandler.callEvent(ks, event);

                        created_socket = null;
                        connected = false;
                    }
                });
            }
        }

        /**
         * This method should be run instead of {@link WebSocket#close(int, String)}
         *
         * @param code   the close code ( defaults to 0 at {@link KarmaSocket#close()}
         * @param reason the close reason ( defaults to 'Client disconnect request' at {@link KarmaSocket#close()}
         * @return if the socket could be closed
         */
        @Override
        public boolean close(final int code, final String reason) {
            if (created_socket != null) {
                created_socket.close(code, reason);
                created_socket = null;
            }

            return false;
        }
    }
}
