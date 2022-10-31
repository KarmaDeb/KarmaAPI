package ml.karmaconfigs.api.bukkit.tracker.property.flag;

import ml.karmaconfigs.api.bukkit.tracker.property.PropertyValue;

import java.io.Serializable;

/**
 * Property implementation
 */
class PropertyObject extends PropertyValue<Object> {

    private Object val = "";
    private String name = "";

    private final TrackerFlag flag;

    /**
     * Initialize the property
     *
     * @param fl the property owner
     */
    public PropertyObject(final TrackerFlag fl) {
        flag = fl;
    }

    /**
     * Get if the object matches the value type
     *
     * @param obj the object
     * @return if the object matches the value type
     */
    @Override
    public boolean matches(final Object obj) {
        return obj != null;
    }

    /**
     * Update the value
     *
     * @param value the new value
     */
    @Override
    public void update(final Object value) {
        if (value != null) {
            val = value;
        }
    }

    /**
     * Update the value without knowing exactly
     * the property value type
     *
     * @param value the value to insert
     */
    @Override
    public void updateUnsafe(final Object value) {
        if (matches(value)) {
            val = value;
        }
    }

    /**
     * Set the property identifier
     *
     * @param n the property identifier name
     * @return this instance
     */
    @Override
    public PropertyValue<Object> setIdentifier(final String n) {
        name = n;
        return this;
    }

    /**
     * Get the value
     *
     * @return the value
     */
    @Override
    public Object getValue() {
        return val;
    }

    /**
     * Get the value forcing it to be
     * the desired value
     *
     * @return the forced value
     */
    @Override
    public <U> U getUnsafe() {
        return (U) val;
    }

    /**
     * Get the property identifier
     *
     * @return the property identifier
     */
    @Override
    public String getIdentifier() {
        return flag.getPrefix() + "_" + name;
    }

    /**
     * Get the property name
     *
     * @return the property name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Get the property flag
     *
     * @return the property flag
     */
    @Override
    public TrackerFlag getFlag() {
        return flag;
    }
}
