package ml.karmaconfigs.api.bukkit.database;

import ml.karmaconfigs.api.bukkit.KarmaPlugin;
import ml.karmaconfigs.api.common.collection.triple.TriCollection;
import ml.karmaconfigs.api.common.collection.triple.TriCollector;
import ml.karmaconfigs.api.common.data.file.FileUtilities;
import ml.karmaconfigs.api.common.karma.loader.BruteLoader;
import ml.karmaconfigs.api.common.karma.loader.component.NameComponent;
import ml.karmaconfigs.api.common.utils.enums.Level;
import ml.karmaconfigs.api.common.utils.url.URLUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * Plugin database
 */
public abstract class PluginDatabase {

    private final KarmaPlugin plugin;
    private final String name;
    protected Connection connection;

    private final static TriCollection<KarmaPlugin, String, Connection> connections = new TriCollector<>();

    /**
     * Initialize the plugin database
     *
     * @param owner the plugin owner
     * @param nm the database name
     */
    public PluginDatabase(final KarmaPlugin owner, final String nm) {
        plugin = owner;
        name = nm;
        connection = connections.get(plugin, name);
    }

    /**
     * Connect to the plugin database
     *
     * @return if the connection was successfully
     */
    public boolean connect() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Throwable ex) {
            plugin.console().send("SQLite JDBC driver not found, downloading it...", Level.WARNING);

            BruteLoader loader = new BruteLoader(plugin.getClass().getClassLoader());
            String dl_url = "https://github.com/xerial/sqlite-jdbc/releases/download/3.40.0.0/sqlite-jdbc-3.40.0.0.jar";
            loader.downloadAndInject(URLUtils.getOrNull(dl_url), NameComponent.fromPath(plugin
                    .getDataPath().resolve("cache").resolve("dependencies").resolve("SQLITE.jar")));
        }

        if (connection == null) {
            try {
                String connection_url = "jdbc:sqlite:" + FileUtilities.getPrettyFile(plugin.getDataPath()
                        .resolve("data").resolve(name + ".db").toFile()).replaceAll("\\s", "%20");

                connection = DriverManager.getConnection(connection_url);
                connections.add(plugin, name, connection);

                plugin.console().send("Successfully created database connection for {0}", Level.INFO, name);
                return true;
            } catch (Throwable ex) {
                return false;
            }
        }

        return true;
    }

    /**
     * Create a table
     *
     * @param name the table name
     * @param fields the table fields
     * @return if the table was able to be created
     */
    protected boolean createTable(final String name, final TableField... fields) {
        if (connection != null) {
            PreparedStatement statement = null;
            try {
                StringBuilder commandBuilder = new StringBuilder("CREATE TABLE ").append(name).append(" (");
                for (int x = 0; x < fields.length; x++) {
                    TableField field = fields[x];
                    commandBuilder.append(field.name()).append(" ").append(field.type()).append(" default ").append(field.def());
                    String[] extra = field.extraParams();
                    if (extra.length > 0) {
                        for (int i = 0; i < extra.length; i++) {
                            String cmd = extra[i];
                            if (i == extra.length - 1) {
                                commandBuilder.append(cmd);
                            } else {
                                commandBuilder.append(cmd).append(" ");
                            }
                        }
                    }
                    if (x != fields.length - 1) {
                        commandBuilder.append(", ");
                    }
                }
                commandBuilder.append(")");

                statement = connection.prepareStatement(commandBuilder.toString());
                return statement.execute();
            } catch (Throwable ignored) {} finally {
                try {
                    if (statement != null)
                        statement.close();
                } catch (Throwable ignored) {}
            }
        }

        return false;
    }

    /**
     * Open a table selector
     *
     * @param name the table name
     * @return the table selector or null if not exists
     */
    public abstract TableSelector onTable(final String name);
}
