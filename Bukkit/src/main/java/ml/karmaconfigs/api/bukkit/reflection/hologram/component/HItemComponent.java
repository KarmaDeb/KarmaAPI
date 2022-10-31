package ml.karmaconfigs.api.bukkit.reflection.hologram.component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

/**
 * Hologram item component
 */
public abstract class HItemComponent implements HologramComponent {

    /**
     * Get the item
     *
     * @return the item
     */
    public abstract ItemStack getItem();

    /**
     * Update the item
     *
     * @param item the item
     */
    public abstract void updateItem(final ItemStack item);

    /**
     * Update the item material and try to preserve the
     * original item data
     *
     * @param itemMaterial the new item material
     */
    public abstract void updateItem(final Material itemMaterial);

    /**
     * Get the component raw text
     *
     * @return the component raw text
     */
    @Override
    public String getRaw() {
        Gson gson = new GsonBuilder().setPrettyPrinting().setLenient().create();
        JsonObject main = new JsonObject();

        ItemStack item = getItem();
        Type gsonType = new TypeToken(){}.getType();
        String gsonString = gson.toJson(item.serialize(), gsonType);
        JsonObject itemData = gson.fromJson(gsonString, JsonObject.class);

        main.addProperty("type", "item");
        main.add("item", itemData);

        return gson.toJson(main);
    }
}
