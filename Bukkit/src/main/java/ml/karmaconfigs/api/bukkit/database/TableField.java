package ml.karmaconfigs.api.bukkit.database;

/**
 * Table field
 */
public interface TableField {

    /**
     * Get the table field name
     *
     * @return the table field name
     */
    String name();

    /**
     * Get the table field type
     *
     * @return the table field type
     */
    String type();

    /**
     * Get the table default value
     *
     * @return the table default value
     */
    String def();

    /**
     * Get the field extra params, such as NOT NULL, or
     * AUTO_INCREMENT
     *
     * @return the field extra params
     */
    String[] extraParams();
}
