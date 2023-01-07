package ml.karmaconfigs.api.bukkit.inventory.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import ml.karmaconfigs.api.bukkit.KarmaPlugin;
import ml.karmaconfigs.api.common.karma.file.KarmaMain;
import ml.karmaconfigs.api.common.karma.file.element.KarmaElement;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Base64;
import java.util.Map;
import java.util.UUID;

/**
 * Inventory serializer
 */
public final class InventorySerializer {

    private final KarmaPlugin plugin;

    /**
     * Initialize the inventory serializer
     *
     * @param owner the serializer owner
     */
    public InventorySerializer(final KarmaPlugin owner) {
        plugin = owner;
    }

    /**
     * Serialize the inventory
     *
     * @param inventory the inventory to serialize
     * @param name the inventory name, as spigot deprecated the Inventory#getTitle method
     * @return the serialized inventory
     */
    public SerializedInventory serialize(final Inventory inventory, final String name) {
        UUID random = UUID.randomUUID();
        String fl_name = random.toString().replace("-", "") + ".kf";

        KarmaMain container = new KarmaMain(plugin, fl_name, "cache", "inventory");
        if (!container.exists())
            container.create();

        container.set("title", KarmaElement.from(name));
        container.set("size", KarmaElement.from(inventory.getSize()));
        container.set("type", KarmaElement.from(inventory.getType().name()));

        Gson gson = new GsonBuilder().create();
        JsonArray array = new JsonArray();

        for (int i  = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null) {
                item = new ItemStack(Material.AIR);
            }

            Map<String, Object> serialized = item.serialize();
            String as_json = gson.toJson(serialized);
            JsonElement json_item = gson.fromJson(as_json, JsonElement.class);
            array.add(json_item);
        }

        container.set("items", KarmaElement.from(Base64.getEncoder().encodeToString(gson.toJson(array).getBytes())));
        if (container.save()) {
            return new SerializedInventory(random, plugin);
        } else {
            return null;
        }
    }
}
