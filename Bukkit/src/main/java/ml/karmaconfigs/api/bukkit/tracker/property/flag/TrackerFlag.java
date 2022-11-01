package ml.karmaconfigs.api.bukkit.tracker.property.flag;

import ml.karmaconfigs.api.bukkit.tracker.property.PropertyValue;

/**
 * Tracker properties
 */
public enum TrackerFlag {
    /**
     * Entity property string, usually used for custom name
     */
    PROPERTY_STRING("property_text"),

    /**
     * Entity property boolean, for thins such as {@link org.bukkit.entity.Entity#setCustomNameVisible(boolean)}
     */
    PROPERTY_BOOLEAN("property_boolean"),

    /**
     * Entity property integer, for things such as {@link org.bukkit.entity.Entity#setFireTicks(int)}
     */
    PROPERTY_NUMBER("property_number"),

    /**
     * Entity property UUID, can be used with custom key for example "location_world" to update world
     */
    PROPERTY_UUID("property_uuid"),

    /**
     * Custom, for settings things such as "owner"
     */
    PROPERTY_OTHER("property_object"),

    /**
     * Entity property string, usually used for custom name
     */
    TRACKER_STRING("tracker_text"),

    /**
     * Entity property boolean, for thins such as {@link org.bukkit.entity.Entity#setCustomNameVisible(boolean)}
     */
    TRACKER_BOOLEAN("tracker_boolean"),

    /**
     * Entity property integer, for things such as {@link org.bukkit.entity.Entity#setFireTicks(int)}
     */
    TRACKER_NUMBER("tracker_number"),

    /**
     * Entity property UUID, can be used with custom key for example "location_world" to update world
     */
    TRACKER_UUID("tracker_uuid"),

    /**
     * Custom, for settings things such as "owner"
     */
    TRACKER_OTHER("tracker_object");

    private final String prefix;

    /**
     * Initialize the property
     *
     * @param k the property key
     */
    TrackerFlag(final String k) {
        prefix = k;
    }

    /**
     * Get the tracker prefix
     *
     * @return the tracker prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Create a property for this tracker flag
     *
     * @param <T> the property type
     * @return a new property for this flag
     */
    public <T> PropertyValue<T> makeProperty(final String name) {
        PropertyValue<?> value;

        switch (this) {
            case TRACKER_STRING:
            case PROPERTY_STRING:
                value = new PropertyString(this).setIdentifier(name);
                break;
            case TRACKER_BOOLEAN:
            case PROPERTY_BOOLEAN:
                value = new PropertyBoolean(this).setIdentifier(name);
                break;
            case TRACKER_NUMBER:
            case PROPERTY_NUMBER:
                value = new PropertyNumber(this).setIdentifier(name);
                break;
            case TRACKER_UUID:
            case PROPERTY_UUID:
                value = new PropertyUUID(this).setIdentifier(name);
                break;
            case PROPERTY_OTHER:
            case TRACKER_OTHER:
            default:
                value = new PropertyObject(this).setIdentifier(name);
                break;
        }

        return (PropertyValue<T>) value;
    }

    /**
     * Get the tracker flag from the specified key
     *
     * @param key the tracker key
     * @return the tracker flag
     */
    public static <T> PropertyValue<T> fromKey(final String key) {
        if (key.contains("_")) {
            String[] nData = key.split("_");

            if (nData.length >= 3) {
                String target = nData[0];
                String type = nData[1];

                if (target.equals("property")) {
                    String name = key.replaceFirst("property_" + type + "_", "");

                    switch (type.toLowerCase()) {
                        case "text":
                            return TrackerFlag.PROPERTY_STRING.makeProperty(name);
                        case "boolean":
                            return TrackerFlag.PROPERTY_BOOLEAN.makeProperty(name);
                        case "number":
                            return TrackerFlag.PROPERTY_NUMBER.makeProperty(name);
                        case "id":
                            return TrackerFlag.PROPERTY_UUID.makeProperty(name);
                        case "object":
                            return TrackerFlag.PROPERTY_OTHER.makeProperty(name);
                    }
                } else {
                    String name = key.replaceFirst("tracker_" + type + "_", "");

                    switch (type.toLowerCase()) {
                        case "text":
                            return TrackerFlag.TRACKER_STRING.makeProperty(name);
                        case "boolean":
                            return TrackerFlag.TRACKER_BOOLEAN.makeProperty(name);
                        case "number":
                            return TrackerFlag.TRACKER_NUMBER.makeProperty(name);
                        case "id":
                            return TrackerFlag.TRACKER_UUID.makeProperty(name);
                        case "object":
                            return TrackerFlag.TRACKER_OTHER.makeProperty(name);
                    }
                }
            }
        }

        return null;
    }
}
