package ml.karmaconfigs.api.bukkit.database;

import java.sql.Connection;

/**
 * Table selector
 */
public abstract class TableSelector {

    protected final Connection connection;
    protected final String table;

    /**
     * Initialize the table selector
     *
     * @param c the connection
     * @param t the table name
     */
    public TableSelector(final Connection c, final String t) {
        connection = c;
        table = t;
    }

    /**
     * Get a string
     *
     * @param path the string path
     * @return the string
     */
    public abstract String getString(final String path);

    /**
     * Get a string
     *
     * @param path the string path
     * @param def the string default value
     * @return the string
     */
    public abstract String getString(final String path, final String def);

    /**
     * Get an integer
     *
     * @param path the integer path
     * @return the integer
     */
    public abstract int getInteger(final String path);

    /**
     * Get an integer
     *
     * @param path the integer path
     * @param def the default value
     * @return the integer
     */
    public abstract int getInteger(final String path, final int def);

    /**
     * Get a double
     *
     * @param path the double path
     * @return the double
     */
    public abstract double getDouble(final String path);

    /**
     * Get a double
     *
     * @param path the double path
     * @param def the default value
     * @return the double
     */
    public abstract double getDouble(final String path, final double def);

    /**
     * Get a float
     *
     * @param path the float path
     * @return the float
     */
    public abstract float getFloat(final String path);

    /**
     * Get a float
     *
     * @param path the float path
     * @param def the default value
     * @return the float
     */
    public abstract float getFloat(final String path, final float def);

    /**
     * Get a long
     *
     * @param path the short path
     * @return the long
     */
    public abstract long getLong(final String path);

    /**
     * Get a long
     *
     * @param path the long path
     * @param def the default value
     * @return the long
     */
    public abstract long getLong(final String path, final long def);

    /**
     * Get a short
     *
     * @param path the short path
     * @return the short
     */
    public abstract short getShort(final String path);

    /**
     * Get a short
     *
     * @param path the short path
     * @param def the default value
     * @return the short
     */
    public abstract short getShort(final String path, final short def);

    /**
     * Get a byte
     *
     * @param path the byte path
     * @return the byte
     */
    public abstract byte getByte(final String path);

    /**
     * Get a byte
     *
     * @param path the byte path
     * @param def the default value
     * @return the byte
     */
    public abstract byte getByte(final String path, final byte def);

    /**
     * Get a boolean
     *
     * @param path the boolean path
     * @return the boolean
     */
    public boolean getBoolean(final String path) {
        return getByte(path) == 1;
    }

    /**
     * Get a boolean
     *
     * @param path the boolean path
     * @param def the default value
     * @return the boolean
     */
    public boolean getBoolean(final String path, final boolean def) {
        byte b = getByte(path);
        if (b == 0x00) {
            return def;
        }

        return b == 1;
    }
}
