package ml.karmaconfigs.api.common.karma.file.element.multi;

import ml.karmaconfigs.api.common.karma.file.element.KarmaPrimitive;
import ml.karmaconfigs.api.common.karma.file.element.types.Element;
import ml.karmaconfigs.api.common.karma.file.element.types.ElementMap;
import ml.karmaconfigs.api.common.karma.file.element.types.ElementPrimitive;
import ml.karmaconfigs.api.common.karma.file.element.types.primitive.BytePrimitive;
import ml.karmaconfigs.api.common.karma.file.element.types.primitive.CharacterPrimitive;
import ml.karmaconfigs.api.common.karma.file.element.types.primitive.PrimitiveType;
import ml.karmaconfigs.api.common.karma.file.element.types.primitive.StringPrimitive;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class KarmaMap implements ElementMap<ElementPrimitive> {

    private final Map<String, ElementPrimitive> values = new LinkedHashMap<>();
    private final Map<ElementPrimitive, String> reverse = new LinkedHashMap<>();

    private final Lock readLock;
    private final Lock writeLock;

    public KarmaMap() {
        ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
        readLock = rwLock.readLock();
        writeLock = rwLock.writeLock();
    }

    /**
     * Load a java map into the karma map
     * <br>
     * NOTE: KarmaMap does not allow sub maps and will be skipped
     * <br>
     * @param map the java map
     * @param sort sort the items
     * @return the karma map
     */
    public static KarmaMap fromJavaMap(final Map<String, Object> map, final boolean sort) {
        KarmaMap element = new KarmaMap();
        Set<String> keys = map.keySet();
        if (sort) {
            keys = map.keySet().stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));
        }

        for (String key : keys) {
            Object elm = map.get(key);
            if (elm instanceof String) {
                element.put(key, new KarmaPrimitive((String) elm));
                continue;
            }
            if (elm instanceof Boolean) {
                element.put(key, new KarmaPrimitive((Boolean) elm));
                continue;
            }
            if (elm instanceof Character) {
                element.put(key, new KarmaPrimitive((Character) elm));
                continue;
            }
            if (elm instanceof Byte) {
                element.put(key, new KarmaPrimitive((Byte) elm));
                continue;
            }
            if (elm instanceof Number) {
                element.put(key, new KarmaPrimitive((Number) elm));
                continue;
            }
            if (elm instanceof Byte[]) {
                Byte[] data = (Byte[]) elm;
                for (byte b : data) {
                    element.put(key, new KarmaPrimitive(b));
                }
                continue;
            }
            if (elm instanceof PrimitiveType) {
                element.put(key, new KarmaPrimitive((PrimitiveType<?>) elm));
                continue;
            }
            if (elm == null) {
                element.put(key, KarmaPrimitive.forNull());
                continue;
            }
            try {
                element.put(key, new KarmaPrimitive(elm.toString()));
            } catch (Throwable ignored) {
                element.put(key, new KarmaPrimitive(String.valueOf(elm)));
            }
        }

        return element;
    }

    /**
     * Get the element
     *
     * @return the element
     */
    @Override
    public Map<String, ElementPrimitive> getValue() {
        return null;
    }

    /**
     * Set a map value
     *
     * @param key   the value key
     * @param value the elements to add
     */
    @Override
    public void put(final String key, final PrimitiveType<?> value) {
        put(key, new KarmaPrimitive(value));
    }

    /**
     * Set a map value
     *
     * @param key   the value key
     * @param value the elements to add
     */
    @Override
    public void put(final String key, final ElementPrimitive value) {
        writeLock.lock();
        try {
            ElementPrimitive old = values.put(key, value.onMap(this));
            if (old != null) reverse.remove(old);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Set a map value
     *
     * @param key   the value key
     * @param value the elements to add
     */
    @Override
    public void put(final String key, final String value) {
        put(key, new KarmaPrimitive(value));
    }

    /**
     * Set a map value
     *
     * @param key   the value key
     * @param value the elements to add
     */
    @Override
    public void put(final String key, final Number value) {
        put(key, new KarmaPrimitive(value));
    }

    /**
     * Set a map value
     *
     * @param key   the value key
     * @param value the elements to add
     */
    @Override
    public void put(final String key, final Boolean value) {
        put(key, new KarmaPrimitive(value));
    }

    /**
     * Set a map value
     *
     * @param key   the value key
     * @param value the elements to add
     */
    @Override
    public void put(final String key, final Character value) {
        put(key, new KarmaPrimitive(value));
    }

    /**
     * Set a map value
     *
     * @param key   the value key
     * @param value the elements to add
     */
    @Override
    public void put(final String key, final Byte value) {
        put(key, new KarmaPrimitive(value));
    }

    /**
     * Set a map value that can be retrieved by its value
     *
     * @param key   the value key
     * @param value the elements to add
     */
    @Override
    public void putRecursive(final String key, final PrimitiveType<?> value) {
        putRecursive(key, new KarmaPrimitive(value));
    }

    /**
     * Set a map value that can be retrieved by its value
     *
     * @param key   the value key
     * @param value the elements to add
     */
    @Override
    public void putRecursive(final String key, final ElementPrimitive value) {
        writeLock.lock();
        try {
            values.put(key, value.onMap(this));
            reverse.put(value.onMap(this), key);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Set a map value that can be retrieved by its value
     *
     * @param key   the value key
     * @param value the elements to add
     */
    @Override
    public void putRecursive(final String key, final String value) {
        putRecursive(key, new KarmaPrimitive(value));
    }

    /**
     * Set a map value that can be retrieved by its value
     *
     * @param key   the value key
     * @param value the elements to add
     */
    @Override
    public void putRecursive(final String key, final Number value) {
        putRecursive(key, new KarmaPrimitive(value));
    }

    /**
     * Set a map value that can be retrieved by its value
     *
     * @param key   the value key
     * @param value the elements to add
     */
    @Override
    public void putRecursive(final String key, final Boolean value) {
        putRecursive(key, new KarmaPrimitive(value));
    }

    /**
     * Set a map value that can be retrieved by its value
     *
     * @param key   the value key
     * @param value the elements to add
     */
    @Override
    public void putRecursive(final String key, final Character value) {
        putRecursive(key, new KarmaPrimitive(value));
    }

    /**
     * Set a map value that can be retrieved by its value
     *
     * @param key   the value key
     * @param value the elements to add
     */
    @Override
    public void putRecursive(final String key, final Byte value) {
        putRecursive(key, new KarmaPrimitive(value));
    }

    /**
     * Remove a value
     *
     * @param key the element key to remove
     */
    @Override
    public void remove(final String key) {
        writeLock.lock();
        try {
            ElementPrimitive value = values.remove(key);
            if (value != null) reverse.remove(value);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Check if the map contains the specified key
     *
     * @param key the key
     * @return if the map contains the key
     */
    @Override
    public boolean containsKey(final String key) {
        readLock.lock();
        try {
            return values.containsKey(key);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Get if the map contains the element
     *
     * @param element the element
     * @return if the map contains the element
     */
    @Override
    public boolean containsValue(final Element<?> element) {
        readLock.lock();
        try {
            Set<Object> objects = values.values().stream().map((primitive) -> primitive.getValue().get()).collect(Collectors.toSet());
            if (element != null && element.isPrimitive()) {
                ElementPrimitive primitive = element.getAsPrimitive();
                return objects.contains(primitive.getValue().get());
            }

            return false;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Check if the key exists and if its value is the same
     * as the one provided
     *
     * @param key     the element key
     * @param element the element
     * @return if the key exists and its value is the one provided
     */
    @Override
    public boolean contains(final String key, final Element<?> element) {
        readLock.lock();
        try {
            ElementPrimitive stored = values.getOrDefault(key, null);
            if (stored != null && element != null && element.isPrimitive()) {
                ElementPrimitive primitive = element.getAsPrimitive();
                if (primitive.isElementNull()) {
                    return stored.isElementNull();
                }

                return primitive.getValue().get().equals(stored.getValue().get());
            }

            return false;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Check if the key is bidirectional
     *
     * @param key the key
     * @return if the key is bidirectional
     */
    @Override
    public boolean isRecursive(final String key) {
        readLock.lock();
        try {
            return reverse.containsValue(key);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Get if the element is bidirectional
     *
     * @param element the element
     * @return if the element is bidirectional
     */
    @Override
    public boolean isRecursive(final Element<?> element) {
        readLock.lock();
        try {
            Set<Object> objects = reverse.keySet().stream().map((primitive) -> primitive.getValue().get()).collect(Collectors.toSet());
            if (element != null && element.isPrimitive()) {
                ElementPrimitive primitive = element.getAsPrimitive();
                return objects.contains(primitive.getValue().get());
            }

            return false;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Get an element at the specified key
     *
     * @param key the element key
     * @return the element
     */
    @Override
    public ElementPrimitive get(final String key) {
        readLock.lock();
        try {
            return values.getOrDefault(key, null);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Get a key by its value
     *
     * @param value the key value
     * @return the key
     */
    @Override
    public String get(final ElementPrimitive value) {
        readLock.lock();
        try {
            if (value == null) return null;

            Map<Object, String> literal = new HashMap<>();
            reverse.keySet().forEach((primitive) -> {
                if (!primitive.isElementNull()) {
                    literal.put(primitive.getValue().get(), reverse.get(primitive));
                }
            });
            if (value.isElementNull()) {
                return null;
            }

            return literal.getOrDefault(value, null);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Get this map but with its contents lower case
     *
     * @return the map lower case
     */
    @Override
    public ElementMap<ElementPrimitive> contentsToLowerCase() {
        readLock.lock();
        try {
            ElementMap<ElementPrimitive> new_map = new KarmaMap();
            for (String key : values.keySet()) {
                ElementPrimitive value = values.get(key);
                switch (value.getValue().type()) {
                    case TEXT:
                        String text = value.asString().toLowerCase();
                        value = new KarmaPrimitive(new StringPrimitive(text));
                        break;
                    case CHARACTER:
                        char character = Character.toLowerCase(value.asCharacter());
                        value = new KarmaPrimitive(new CharacterPrimitive(character));
                        break;
                    case BYTE:
                        byte b = (byte) Character.toLowerCase(value.asByte());
                        value = new KarmaPrimitive(new BytePrimitive(b));
                        break;
                }

                if (reverse.containsKey(value)) {
                    new_map.putRecursive(key, value);
                } else {
                    new_map.put(key, value);
                }
            }

            return new_map;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Get this map but with its contents upper case
     *
     * @return the map upper case
     */
    @Override
    public ElementMap<ElementPrimitive> contentsToUpperCase() {
        readLock.lock();
        try {
            ElementMap<ElementPrimitive> new_map = new KarmaMap();
            for (String key : values.keySet()) {
                ElementPrimitive value = values.get(key);
                switch (value.getValue().type()) {
                    case TEXT:
                        String text = value.asString().toUpperCase();
                        value = new KarmaPrimitive(new StringPrimitive(text));
                        break;
                    case CHARACTER:
                        char character = Character.toUpperCase(value.asCharacter());
                        value = new KarmaPrimitive(new CharacterPrimitive(character));
                        break;
                    case BYTE:
                        byte b = (byte) Character.toUpperCase(value.asByte());
                        value = new KarmaPrimitive(new BytePrimitive(b));
                        break;
                }

                if (reverse.containsKey(value)) {
                    new_map.putRecursive(key, value.onMap(this));
                } else {
                    new_map.put(key, value.onMap(this));
                }
            }

            return new_map;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Run an action for each map key
     *
     * @param consumer the action to perform
     */
    @Override
    public void forEachKey(final Consumer<String> consumer) {
        values.keySet().forEach(consumer);
    }

    /**
     * Get the map size
     *
     * @return the map size
     */
    @Override
    public int getSize() {
        return values.size();
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @NotNull
    @Override
    public Iterator<ElementPrimitive> iterator() {
        return values.values().iterator();
    }

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        readLock.lock();
        try {
            StringBuilder builder = new StringBuilder("'map' -> {").append("\n");
            for (String key : values.keySet()) {
                ElementPrimitive primitive = values.get(key);

                if (primitive.isString()) {
                    builder.append('\t').append("\"").append(key).append("\"").append((reverse.containsKey(primitive) ? " <-> " : " -> ")).append("'").append(primitive.getValue().get()).append("'").append("\n");
                } else {
                    builder.append('\t').append("\"").append(key).append("\"").append((reverse.containsKey(primitive) ? " <-> " : " -> ")).append(primitive.getValue().get()).append("\n");
                }
            }

            return builder.append("}").toString();
        } finally {
            readLock.unlock();
        }
    }
}
