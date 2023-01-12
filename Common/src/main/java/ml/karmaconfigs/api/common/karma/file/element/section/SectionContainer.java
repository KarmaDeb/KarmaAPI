package ml.karmaconfigs.api.common.karma.file.element.section;

import ml.karmaconfigs.api.common.karma.file.KarmaMain;
import ml.karmaconfigs.api.common.karma.file.element.KarmaSection;
import ml.karmaconfigs.api.common.karma.file.element.types.Element;

import java.util.HashSet;
import java.util.Set;

/**
 * Karma section implementation
 */
public class SectionContainer extends KarmaSection {

    private final KarmaMain main;
    private final String path;
    private final String name;

    /**
     * Initialize the section container
     *
     * @param source the source file
     * @param p the section path
     * @param n the section name
     */
    public SectionContainer(final KarmaMain source, final String p, final String n) {
        main = source;
        path = p;
        name = n;
    }

    /**
     * Get the main that handles this section
     *
     * @return the section main
     */
    @Override
    public KarmaMain getMain() {
        return main;
    }

    /**
     * Get the section parent
     *
     * @return the parent section
     */
    @Override
    public String getParent() {
        if (path.contains(".")) {
            String[] data = path.split("\\.");
            return data[data.length - 1];
        } else {
            return path;
        }
    }

    /**
     * Get the section path
     *
     * @return the section path
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * Get the section name
     *
     * @return the section name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Get the section children if any
     *
     * @param concurrent iterate also with children child
     * @return the section children
     */
    @Override
    public Set<String> getChildren(final boolean concurrent) {
        Set<String> found_keys = new HashSet<>();

        main.getKeys().forEach((key) -> {
            if (key.startsWith(path + "." + name)) {
                String[] original_keyData = key.split("\\.");
                String tmp_key = key.replaceFirst(path + "\\.", "");
                String[] keyData = tmp_key.split("\\.");

                if (keyData.length > 2 && concurrent) {
                    if (tmp_key.startsWith(name + ".")) {
                        if (keyData.length == 3) {
                            KarmaSection section = new SectionContainer(main, key.replaceFirst("\\." + original_keyData[original_keyData.length - 2] + "\\." + original_keyData[original_keyData.length - 1], ""), original_keyData[original_keyData.length - 2]);
                            section.getChildren(true).forEach((subKey) -> {
                                found_keys.add(key.replaceFirst(path + "\\.", ""));
                            });
                        } else {
                            found_keys.add(tmp_key);
                        }
                    }
                } else {
                    if (keyData.length == 2) {
                        found_keys.add(tmp_key);
                    }
                }
            }
        });

        return found_keys;
    }

    /**
     * Get a section children
     *
     * @param childName the child section name
     * @return the child section
     */
    @Override
    public KarmaSection getChildren(final String childName) {
        String tmpKey = childName;
        if (childName.startsWith(name + "\\.")) {
            tmpKey = childName.replaceFirst(name + "\\.", "");
        }

        String tmp_path = path + "." + name + "." + tmpKey;
        return main.getSection(tmp_path);
    }

    /**
     * Get a value
     *
     * @param key the value key
     * @return the value
     */
    @Override
    public Element<?> get(final String key) {
        String tmpKey = key;
        if (key.startsWith(name + "\\.")) {
            tmpKey = key.replaceFirst(name + "\\.", "");
        }

        String tmp_path = path + "." + name + "." + tmpKey;
        return main.get(tmp_path);
    }

    /**
     * Get a value
     *
     * @param key the value key
     * @param def the default value
     * @return the value
     */
    @Override
    public Element<?> get(final String key, final Element<?> def) {
        String tmpKey = key;
        if (key.startsWith(name + "\\.")) {
            tmpKey = key.replaceFirst(name + "\\.", "");
        }

        String tmp_path = path + "." + name + "." + tmpKey;
        return main.get(tmp_path, def);
    }

    /**
     * Get a key
     *
     * @param element the key value
     * @return the key
     */
    @Override
    public String get(final Element<?> element) {
        String tmp_path = "main." + main.get(element);
        if (tmp_path.startsWith(path + "." + name)) {
            return tmp_path;
        }

        return null;
    }

    /**
     * Get a key
     *
     * @param element the key value
     * @param def     the default key
     * @return the key
     */
    @Override
    public String get(final Element<?> element, final String def) {
        String tmp_path = "main." + main.get(element);
        if (tmp_path.startsWith(path + "." + name)) {
            return tmp_path;
        }

        return def;
    }

    /**
     * Get if a key element is recursive
     *
     * @param key the key
     * @return if the key element is recursive
     */
    @Override
    public boolean isRecursive(final String key) {
        String tmpKey = key;
        if (key.startsWith(name + "\\.")) {
            tmpKey = key.replaceFirst(name + "\\.", "");
        }

        String tmp_path = path + "." + name + "." + tmpKey;
        return main.isRecursive(key);
    }

    /**
     * Get if a element is recursive
     *
     * @param element the element
     * @return if the element is recursive
     */
    @Override
    public boolean isRecursive(final Element<?> element) {
        String tmp_path = "main." + main.get(element);
        if (tmp_path.startsWith(path + "." + name)) {
            return main.isRecursive(tmp_path);
        }

        return false;
    }

    /**
     * Get if a key is set
     *
     * @param key the key to find
     * @return if the key is set
     */
    @Override
    public boolean isSet(final String key) {
        String tmpKey = key;
        if (key.startsWith(name + "\\.")) {
            tmpKey = key.replaceFirst(name + "\\.", "");
        }

        String tmp_path = path + "." + name + "." + tmpKey;

        Element<?> res = main.get(tmp_path);
        return res != null && !res.isElementNull();
    }
}
