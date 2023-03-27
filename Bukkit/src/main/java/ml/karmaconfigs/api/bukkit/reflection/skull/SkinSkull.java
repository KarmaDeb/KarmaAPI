package ml.karmaconfigs.api.bukkit.reflection.skull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

public final class SkinSkull {

    private static Class<?> property;
    static {
        try {
            property = Class.forName("org.spongepowered.api.profile.property.ProfileProperty");
        } catch (Exception e) {
            try {
                property = Class.forName("com.mojang.authlib.properties.Property");
            } catch (Exception ex) {
                try {
                    property = Class.forName("net.md_5.bungee.connection.LoginResult$Property");
                } catch (Exception exe) {
                    try {
                        property = Class.forName("net.md_5.bungee.protocol.Property");
                    } catch (Exception exec) {
                        try {
                            property = Class.forName("net.minecraft.util.com.mojang.authlib.properties.Property");
                        } catch (Exception excep) {
                            try {
                                property = Class.forName("com.velocitypowered.api.util.GameProfile$Property");
                            } catch (Exception except) {
                                except.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Create a texture property
     *
     * @param value the texture value
     * @param signature the texture signature
     * @return the property
     * @throws NoSuchMethodException as part of {@link Class#getConstructor(Class[])}
     * @throws InvocationTargetException as part of {@link Constructor#newInstance(Object...)}
     * @throws InstantiationException as part of {@link Constructor#newInstance(Object...)}
     * @throws IllegalAccessException as part of {@link Constructor#newInstance(Object...)}
     */
    private static Object createProperty(final String value, final String signature) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?> propertyConstructor = property.getConstructor(String.class, String.class, String.class);
        propertyConstructor.setAccessible(true);
        return propertyConstructor.newInstance("textures", value, signature);
    }

    /**
     * Create a game profile
     *
     * @param properties the game profile properties
     * @return the game profile
     * @throws NoSuchMethodException as part of {@link Class#getConstructor(Class[])}
     * @throws InvocationTargetException as part of {@link Constructor#newInstance(Object...)} and {@link Method#invoke(Object, Object...)}
     * @throws InstantiationException as part of {@link Constructor#newInstance(Object...)}
     * @throws IllegalAccessException as part of {@link Constructor#newInstance(Object...)} and {@link Method#invoke(Object, Object...)}
     */
    private static Object createGameProfile(final Object properties) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> gameProfile = null;
        try {
            gameProfile = Class.forName("com.mojang.authlib.GameProfile");
        } catch (Throwable ex) {
            try {
                gameProfile = Class.forName("net.minecraft.util.com.mojang.authlib.GameProfile");
            } catch (Throwable ignored) {}
        }

        Constructor<?> profileConstructor = gameProfile.getConstructor(UUID.class, String.class);
        profileConstructor.setAccessible(true);

        Object profile = profileConstructor.newInstance(UUID.randomUUID(), null);

        Method getProperties = profile.getClass().getMethod("getProperties");
        getProperties.setAccessible(true);

        Object propMap = getProperties.invoke(profile);

        Method clear = propMap.getClass().getMethod("clear");
        Method put = propMap.getClass().getMethod("put", Object.class, Object.class);

        clear.setAccessible(true);
        put.setAccessible(true);

        clear.invoke(propMap);
        put.invoke(propMap, "textures", properties);

        return profile;
    }

    /**
     * Update the meta game profile
     *
     * @param value the game profile value
     * @param signature the game profile signature
     * @return the skull
     */
    public static ItemStack createSkull(final String value, final String signature) {
        try {
            return createSkull(createGameProfile(createProperty(value, signature)), null);
        } catch (Throwable ex) {
            return null;
        }
    }

    /**
     * Update the meta game profile
     *
     * @param profile the game profile
     * @return the skull
     */
    public static ItemStack createSkull(final Object profile) {
        return createSkull(profile, null);
    }

    /**
     * Update the meta game profile
     *
     * @param value the game profile value
     * @param signature the game profile signature
     * @param custom_meta the custom meta
     * @return the skull
     */
    public static ItemStack createSkull(final String value, final String signature, final Function<SkullMeta, SkullMeta> custom_meta) {
        try {
            return createSkull(createGameProfile(createProperty(value, signature)), custom_meta);
        } catch (Throwable ex) {
            return null;
        }
    }

    /**
     * Update the meta game profile
     *
     * @param profile the game profile
     * @param custom_meta the custom item meta
     * @return the skull
     */
    public static ItemStack createSkull(final Object profile, final Function<SkullMeta, SkullMeta> custom_meta) {
        ItemStack item;
        try {
            item = new ItemStack(Material.PLAYER_HEAD, 1);
        } catch (Throwable ex) {
            try {
                item = new ItemStack(Objects.requireNonNull(Material.matchMaterial("SKULL_ITEM", true)), 1, (short) 3);
            } catch (Throwable ex2) {
                item = new ItemStack(Objects.requireNonNull(Material.matchMaterial("SKULL_ITEM")), 1, (short) 3);
            }
        }

        SkullMeta meta = (SkullMeta) item.getItemMeta();
        assert meta != null;

        if (custom_meta != null) {
            SkullMeta clone = meta.clone();
            clone = custom_meta.apply(clone);
            if (clone != null) meta = clone;
        }

        try {
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (Throwable ignored) {}

        item.setItemMeta(meta);
        return item;
    }
}
