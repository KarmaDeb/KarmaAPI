package ml.karmaconfigs.api.bukkit.inventory.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import ml.karmaconfigs.api.bukkit.KarmaPlugin;
import ml.karmaconfigs.api.common.karma.file.KarmaMain;
import ml.karmaconfigs.api.common.karma.file.element.KarmaPrimitive;
import ml.karmaconfigs.api.common.karma.file.element.types.Element;
import ml.karmaconfigs.api.common.karma.file.element.types.ElementPrimitive;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Serialized inventory
 */
public final class SerializedInventory {

    private final UUID identifier;
    private final KarmaMain container;

    /**
     * Initialize the serialized inventory
     *
     * @param i the inventory identifier
     * @param owner the inventory owner
     */
    public SerializedInventory(final UUID i, final KarmaPlugin owner) {
        identifier = i;
        String fl_name = i.toString().replace("-", "") + ".kf";
        container = owner.loadFile(fl_name, "cache", "inventory");
    }

    /**
     * Get if the serialized inventory exists
     *
     * @return if the inventory exists
     */
    public boolean exists() {
        return container.exists();
    }

    /**
     * Get the inventory identifier
     *
     * @return the inventory identifier
     */
    public UUID getIdentifier() {
        return identifier;
    }

    /**
     * Get the inventory items
     *
     * @return the inventory items
     */
    @SuppressWarnings("unchecked")
    public ItemStack[] getItems() {
        Gson gson = new GsonBuilder().create();
        Element<?> encoded = container.get("items");
        if (encoded.isPrimitive()) {
            ElementPrimitive primitive = (ElementPrimitive) encoded;
            if (primitive.isString()) {
                String stored = new String(Base64.getDecoder().decode(primitive.asString()));

                JsonElement element = gson.fromJson(stored, JsonElement.class);
                if (element != null && element.isJsonArray()) {
                    List<ItemStack> loaded = new ArrayList<>();

                    JsonArray items = element.getAsJsonArray();
                    for (JsonElement sub : items) {
                        Map<String, Object> item_map = gson.fromJson(sub, Map.class);
                        loaded.add(ItemStack.deserialize(item_map));
                    }

                    return loaded.toArray(new ItemStack[0]);
                }
            }
        }

        return new ItemStack[0];
    }

    /**
     * Get the inventory title
     *
     * @return the inventory title
     */
    public String getTitle() {
        Element<?> element = container.get("title", new KarmaPrimitive(""));
        if (element.isPrimitive()) {
            ElementPrimitive primitive = element.getAsPrimitive();
            if (primitive.isString()) {
                return primitive.asString();
            }
        }

        return "";
    }

    /**
     * Get the inventory size
     *
     * @return the inventory size
     */
    public int getSize() {
        Element<?> element = container.get("title", new KarmaPrimitive(9));
        if (element.isPrimitive()) {
            ElementPrimitive primitive = element.getAsPrimitive();
            if (primitive.isNumber()) {
                return primitive.asInteger();
            }
        }

        return 9;
    }

    /**
     * Get the inventory type
     *
     * @return the inventory type
     */
    public InventoryType getType() {
        Element<?> element = container.get("type", new KarmaPrimitive(InventoryType.CHEST.name()));
        try {
            ElementPrimitive primitive = element.getAsPrimitive();
            return InventoryType.valueOf(primitive.asString());
        } catch (Throwable ex) {
            return InventoryType.CHEST;
        }
    }
}
