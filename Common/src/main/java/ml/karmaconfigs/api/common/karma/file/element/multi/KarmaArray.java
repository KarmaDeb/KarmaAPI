package ml.karmaconfigs.api.common.karma.file.element.multi;

import ml.karmaconfigs.api.common.karma.file.element.KarmaPrimitive;
import ml.karmaconfigs.api.common.karma.file.element.types.Element;
import ml.karmaconfigs.api.common.karma.file.element.types.ElementArray;
import ml.karmaconfigs.api.common.karma.file.element.types.ElementPrimitive;
import ml.karmaconfigs.api.common.karma.file.element.types.primitive.BytePrimitive;
import ml.karmaconfigs.api.common.karma.file.element.types.primitive.CharacterPrimitive;
import ml.karmaconfigs.api.common.karma.file.element.types.primitive.PrimitiveType;
import ml.karmaconfigs.api.common.karma.file.element.types.primitive.StringPrimitive;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class KarmaArray implements ElementArray<ElementPrimitive> {

    private final List<ElementPrimitive> contents = new ArrayList<>();

    /**
     * Initialize the array
     *
     * @param elements the elements to start with
     */
    public KarmaArray(final ElementPrimitive... elements) {
        for (ElementPrimitive element : elements) {
            contents.add(element.onArray(this));
        }
    }

    /**
     * Load a java array into the karma array
     *
     * @param array the array
     * @return the karma array
     */
    public static KarmaArray fromJavaArray(final Object[] array, final boolean sort) {
        List<ElementPrimitive> primitives = new ArrayList<>();
        List<Object> elements = Arrays.asList(array);
        if (sort) {
            elements = Arrays.stream(array).sorted().collect(Collectors.toList());
        }

        for (Object elm : elements) {
            if (elm instanceof String) {
                primitives.add(new KarmaPrimitive((String) elm));
                continue;
            }
            if (elm instanceof Boolean) {
                primitives.add(new KarmaPrimitive((Boolean) elm));
                continue;
            }
            if (elm instanceof Character) {
                primitives.add(new KarmaPrimitive((Character) elm));
                continue;
            }
            if (elm instanceof Byte) {
                primitives.add(new KarmaPrimitive((Byte) elm));
                continue;
            }
            if (elm instanceof Number) {
                primitives.add(new KarmaPrimitive((Number) elm));
                continue;
            }
            if (elm instanceof Byte[]) {
                Byte[] data = (Byte[]) elm;
                for (byte b : data) {
                    primitives.add(new KarmaPrimitive(b));
                }
                continue;
            }
            if (elm instanceof PrimitiveType) {
                primitives.add(new KarmaPrimitive((PrimitiveType<?>) elm));
                continue;
            }
            if (elm == null) {
                primitives.add(KarmaPrimitive.forNull());
                continue;
            }
            try {
                primitives.add(new KarmaPrimitive(elm.toString()));
            } catch (Throwable ignored) {
                primitives.add(new KarmaPrimitive(String.valueOf(elm)));
            }
        }

        return new KarmaArray(primitives.toArray(new ElementPrimitive[0]));
    }

    /**
     * Add elements to the array
     *
     * @param elements the elements to add
     */
    @Override
    public void add(final PrimitiveType<?>... elements) {
        add(Arrays.stream(elements).map(KarmaPrimitive::new).toArray(KarmaPrimitive[]::new));
    }

    /**
     * Add elements to the array
     *
     * @param elements the elements to add
     */
    @Override
    public void add(final ElementPrimitive... elements) {
        for (ElementPrimitive element : elements) {
            if (element != null && !element.isElementNull()) {
                contents.add(element.onArray(this));
            }
        }
    }

    /**
     * Add elements to the array
     *
     * @param elements the elements to add
     */
    @Override
    public void add(final String... elements) {
        add(Arrays.stream(elements).map(KarmaPrimitive::new).toArray(KarmaPrimitive[]::new));
    }

    /**
     * Add elements to the array
     *
     * @param elements the elements to add
     */
    @Override
    public void add(final Number... elements) {
        add(Arrays.stream(elements).map(KarmaPrimitive::new).toArray(KarmaPrimitive[]::new));
    }

    /**
     * Add elements to the array
     *
     * @param elements the elements to add
     */
    @Override
    public void add(final Boolean... elements) {
        add(Arrays.stream(elements).map(KarmaPrimitive::new).toArray(KarmaPrimitive[]::new));
    }

    /**
     * Add elements to the array
     *
     * @param elements the elements to add
     */
    @Override
    public void add(final Character... elements) {
        add(Arrays.stream(elements).map(KarmaPrimitive::new).toArray(KarmaPrimitive[]::new));
    }

    /**
     * Add elements to the array
     *
     * @param elements the elements to add
     */
    @Override
    public void add(final Byte... elements) {
        add(Arrays.stream(elements).map(KarmaPrimitive::new).toArray(KarmaPrimitive[]::new));
    }

    /**
     * Remove elements from the array
     *
     * @param elements the elements to remove
     */
    @Override
    public void remove(final PrimitiveType<?>... elements) {
        remove(Arrays.stream(elements).map(KarmaPrimitive::new).toArray(KarmaPrimitive[]::new));
    }

    /**
     * Add elements to the array
     *
     * @param elements the elements to add
     */
    @Override
    public void remove(final ElementPrimitive... elements) {
        Set<Object> objects = Arrays.stream(elements).map((element) -> element.getValue().get()).collect(Collectors.toSet());

        List<ElementPrimitive> remove = new ArrayList<>();
        for (ElementPrimitive element : contents) {
            if (element != null && objects.contains(element.getValue().get())) {
                remove.add(element.onArray(this));
            }
        }
        contents.removeAll(remove);
    }

    /**
     * Remove elements from the array
     *
     * @param elements the elements to remove
     */
    @Override
    public void remove(final String... elements) {
        remove(Arrays.stream(elements).map(KarmaPrimitive::new).toArray(KarmaPrimitive[]::new));
    }

    /**
     * Remove elements from the array
     *
     * @param elements the elements to remove
     */
    @Override
    public void remove(final Number... elements) {
        remove(Arrays.stream(elements).map(KarmaPrimitive::new).toArray(KarmaPrimitive[]::new));
    }

    /**
     * Remove elements from the array
     *
     * @param elements the elements to remove
     */
    @Override
    public void remove(final Boolean... elements) {
        remove(Arrays.stream(elements).map(KarmaPrimitive::new).toArray(KarmaPrimitive[]::new));
    }

    /**
     * Remove elements from the array
     *
     * @param elements the elements to remove
     */
    @Override
    public void remove(final Character... elements) {
        remove(Arrays.stream(elements).map(KarmaPrimitive::new).toArray(KarmaPrimitive[]::new));
    }

    /**
     * Remove elements from the array
     *
     * @param elements the elements to remove
     */
    @Override
    public void remove(Byte... elements) {
        remove(Arrays.stream(elements).map(KarmaPrimitive::new).toArray(KarmaPrimitive[]::new));
    }

    /**
     * Check if the array contains the specified element
     *
     * @param element the element
     * @return if the array contains the element
     */
    @Override
    public boolean contains(final Element<?> element) {
        Set<Object> objects = contents.stream().map((e) -> e.getValue().get()).collect(Collectors.toSet());

        if (element.isPrimitive()) {
            ElementPrimitive primitive = element.getAsPrimitive();
            return objects.contains(primitive.getValue().get());
        }

        return false;
    }

    /**
     * Check if the array contains all the specified elements
     *
     * @param elements the elements
     * @return if the array contains all the elements
     */
    @Override
    public boolean containsAll(final Element<?>... elements) {
        Set<Object> objects = contents.stream().map((element) -> element.getValue().get()).collect(Collectors.toSet());

        for (Element<?> element : elements) {
            if (element != null && element.isPrimitive()) {
                ElementPrimitive primitive = element.getAsPrimitive();
                if (!objects.contains(primitive.getValue().get())) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Get an element at the specified index
     *
     * @param index the element index
     * @return the element
     */
    @Override
    public ElementPrimitive get(final int index) {
        return contents.get(index);
    }

    /**
     * Get this array but with its contents lower case
     *
     * @return the array lower case
     */
    @Override
    public ElementArray<ElementPrimitive> contentsToLowerCase() {
        ElementPrimitive[] new_array = new ElementPrimitive[contents.size()];
        for (int i = 0; i < contents.size(); i++) {
            ElementPrimitive element = contents.get(i);
            switch (element.getValue().type()) {
                case TEXT:
                    String text = element.asString().toLowerCase();
                    element = new KarmaPrimitive(new StringPrimitive(text));
                    break;
                case CHARACTER:
                    char character = Character.toLowerCase(element.asCharacter());
                    element = new KarmaPrimitive(new CharacterPrimitive(character));
                    break;
                case BYTE:
                    byte b = (byte) Character.toLowerCase(element.asByte());
                    element = new KarmaPrimitive(new BytePrimitive(b));
                    break;
            }

            new_array[i] = element;
        }

        return new KarmaArray(new_array);
    }

    /**
     * Get this array but with its contents upper case
     *
     * @return the array upper case
     */
    @Override
    public ElementArray<ElementPrimitive> contentsToUpperCase() {
        ElementPrimitive[] new_array = new ElementPrimitive[contents.size()];
        for (int i = 0; i < contents.size(); i++) {
            ElementPrimitive element = contents.get(i);
            switch (element.getValue().type()) {
                case TEXT:
                    String text = element.asString().toUpperCase();
                    element = new KarmaPrimitive(new StringPrimitive(text));
                    break;
                case CHARACTER:
                    char character = Character.toUpperCase(element.asCharacter());
                    element = new KarmaPrimitive(new CharacterPrimitive(character));
                    break;
                case BYTE:
                    byte b = (byte) Character.toUpperCase(element.asByte());
                    element = new KarmaPrimitive(new BytePrimitive(b));
                    break;
            }

            new_array[i] = element;
        }

        return new KarmaArray(new_array);
    }

    /**
     * Get the array size
     *
     * @return the array size
     */
    @Override
    public int getSize() {
        return contents.size();
    }

    /**
     * Get the element
     *
     * @return the element
     */
    @Override
    public ElementPrimitive[] getValue() {
        return contents.toArray(new ElementPrimitive[0]).clone();
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @NotNull
    @Override
    public Iterator<ElementPrimitive> iterator() {
        return contents.iterator();
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
        StringBuilder builder = new StringBuilder("'array' -> {").append("\n");
        for (ElementPrimitive primitive : contents) {
            if (primitive.isString()) {
                builder.append('\t').append("'").append(primitive.getValue().get()).append("'").append("\n");
            } else {
                builder.append('\t').append(primitive.getValue().get()).append("\n");
            }
        }

        return builder.append("}").toString();
    }
}
