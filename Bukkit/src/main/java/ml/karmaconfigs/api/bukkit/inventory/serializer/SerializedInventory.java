package ml.karmaconfigs.api.bukkit.inventory.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import ml.karmaconfigs.api.bukkit.KarmaPlugin;
import ml.karmaconfigs.api.common.karma.file.KarmaMain;
import ml.karmaconfigs.api.common.karma.file.element.KarmaObject;
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
    public ItemStack[] getItems() {
        Gson gson = new GsonBuilder().create();
        String stored = new String(Base64.getDecoder().decode(container.get("items").getObjet().getString()));

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

        return new ItemStack[0];
    }

    /**
     * Get the inventory title
     *
     * @return the inventory title
     */
    public String getTitle() {
        return container.get("title", new KarmaObject("")).getObjet().getString();
    }

    /**
     * Get the inventory size
     *
     * @return the inventory size
     */
    public int getSize() {
        return container.get("size", new KarmaObject(9)).getObjet().getNumber().intValue();
    }

    /**
     * Get the inventory type
     *
     * @return the inventory type
     */
    public InventoryType getType() {
        String type = container.get("type", new KarmaObject(InventoryType.CHEST.name())).getObjet().getString();
        try {
            return InventoryType.valueOf(type);
        } catch (Throwable ex) {
            return InventoryType.CHEST;
        }
    }
}
