package ml.karmaconfigs.api.common.utils.console;

import ml.karmaconfigs.api.common.utils.enums.Level;
import ml.karmaconfigs.api.common.utils.string.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

/**
 * Console packet, for sequential console
 */
public final class ConsolePacket {

    /**
     * The packet data
     */
    private final byte[] message;

    /**
     * Only for deserializing utilities
     */
    public ConsolePacket(final byte[] raw) {
        message = raw;
    }

    /**
     * Initialize the console packet
     *
     * @param string   the packet text
     * @param replaces the packet text replaces
     */
    public ConsolePacket(final CharSequence string, final Object... replaces) {
        StringBuilder replaceDataBuilder = new StringBuilder();
        for (Object replace : replaces)
            replaceDataBuilder.append(String.valueOf(replace).replace(",", "{coma}")).append(",");

        byte[] msgData = String.valueOf(string).getBytes(StandardCharsets.UTF_8);
        byte[] replaceData = StringUtils.replaceLast(",", "", replaceDataBuilder.toString()).getBytes(StandardCharsets.UTF_8);


        message = new byte[msgData.length + replaceData.length + 1];
        message[0] = 0; //First byte always indicates the message level, being 0 no message level
        message[1] = (byte) replaceData.length; //Second byte always indicates the amount of message replaces that are

        if (replaceData.length - 2 >= 0) System.arraycopy(replaceData, 2, message, 2, replaceData.length - 2);
        if (message.length - (replaceData.length + 2) >= 0)
            System.arraycopy(msgData, replaceData.length + 2, message, replaceData.length + 2, message.length - (replaceData.length + 2));
    }

    /**
     * Initialize the console packet
     *
     * @param string   the packet message
     * @param level    the packet level
     * @param replaces the packet text replaces
     */
    public ConsolePacket(final CharSequence string, final Level level, final Object[] replaces) {
        StringBuilder replaceDataBuilder = new StringBuilder();
        for (Object replace : replaces)
            replaceDataBuilder.append(String.valueOf(replace).replace(",", "{coma}")).append(",");

        byte[] msgData = String.valueOf(string).getBytes(StandardCharsets.UTF_8);
        byte[] replaceData = StringUtils.replaceLast(",", "", replaceDataBuilder.toString()).getBytes(StandardCharsets.UTF_8);

        message = new byte[msgData.length + replaceData.length + 1];
        message[0] = level.getByte(); //First byte always indicates the message level, being 0 no message level
        message[1] = (byte) replaceData.length; //Second byte always indicates the amount of message replaces that are

        if (replaceData.length - 2 >= 0) System.arraycopy(replaceData, 2, message, 2, replaceData.length - 2);
        if (message.length - (replaceData.length + 2) >= 0)
            System.arraycopy(msgData, replaceData.length + 2, message, replaceData.length + 2, message.length - (replaceData.length + 2));
    }

    /**
     * Get the packet message level
     *
     * @return the packet message level
     */
    public Level getLevel() {
        switch (message[0]) {
            case 1:
                return Level.OK;
            case 2:
                return Level.INFO;
            case 3:
                return Level.WARNING;
            case 4:
                return Level.GRAVE;
            case 0:
            default:
                return null;
        }
    }

    /**
     * Get the packet message
     *
     * @return the packet message
     */
    public String getMessage() {
        return new String(Arrays.copyOfRange(message, (int) message[2] + 2, message.length), StandardCharsets.UTF_8);
    }

    /**
     * Get the packet replaces
     *
     * @return the packet replaces
     */
    public Object[] getReplaces() {
        StringBuilder packer = new StringBuilder();
        int length = message[2];

        for (int i = 1; i < length; i++) {
            byte value = message[i];
            packer.append((char) value);
        }

        String unpacked = packer.toString();
        if (unpacked.contains(",")) {
            return packer.toString().split(",");
        } else {
            return new String[]{packer.toString()};
        }
    }

    /**
     * Get the packet data
     *
     * @return the packet data
     */
    public String serialize() {
        return Base64.getEncoder().encodeToString(message);
    }
}
