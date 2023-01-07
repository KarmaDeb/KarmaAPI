package ml.karmaconfigs.api.bukkit.legacy.horse;

import net.minecraft.server.v1_7_R4.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;

import java.lang.reflect.Field;

public class NMSHorse extends EntityHorse {

    private boolean lockTick;

    public NMSHorse(World world, final Location location) {
        super(((CraftWorld) world).getHandle());
        setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        this.ageLocked = true;
        this.persistent = true;
        this.boundingBox.a = 0d;
        this.boundingBox.b = 0d;
        this.boundingBox.c = 0d;
        this.boundingBox.d = 0d;
        this.boundingBox.e = 0d;
        this.boundingBox.f = 0d;
        a(0f, 0f);
        setAge(-1700000);
    }

    public void h() {
        if (this.ticksLived % 20 == 0)
            if (this.vehicle == null)
                die();

        if (!lockTick)
            super.h();
    }

    public void setLockTick(final boolean flag) {
        lockTick = flag;
    }

    public void b(NBTTagCompound nbttagcompound) {}

    public boolean c(NBTTagCompound nbttagcompound) {
        return false;
    }

    public boolean d(NBTTagCompound nbttagcompound) {
        return false;
    }

    public void e(NBTTagCompound nbttagcompound) {}

    public boolean isInvulnerable() {
        return true;
    }
    public void setCustomName(String customName) {}

    public void setCustomNameVisible(boolean visible) {}

    public void makeSound(String sound, float volume, float pitch) {}

    public void setText(String name) {
        if (name != null && name.length() > 300)
            name = name.substring(0, 300);

        super.setCustomName(name);
        super.setCustomNameVisible((name != null && !name.isEmpty()));
    }

    public void setRiding(Entity vehicleBase) {
        try {
            setPrivateField(Entity.class, this, "g", 0d);
            setPrivateField(Entity.class, this, "h", 0d);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (vehicle != null)
            vehicle.passenger = null;

        vehicle = vehicleBase;
        vehicleBase.passenger = (Entity)this;
    }

    public void teleport(double x, double y, double z) {
        setPosition(x, y, z);
        PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(
                getId(),
                MathHelper.floor(this.locX * 32.0D),
                MathHelper.floor(this.locY * 32.0D),
                MathHelper.floor(this.locZ * 32.0D),
                (byte)(int)(this.yaw * 256.0F / 360.0F),
                (byte)(int)(this.pitch * 256.0F / 360.0F),
                false, true);

        for (Object obj : this.world.players) {
            if (obj instanceof EntityPlayer) {
                EntityPlayer nmsPlayer = (EntityPlayer)obj;
                nmsPlayer.playerConnection.sendPacket(teleportPacket);
            }
        }
    }

    @Override
    public boolean canSpawn() {
        return true; //Always can spawn
    }

    static void setPrivateField(Class<?> clazz, Object handle, String fieldName, Object value) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(handle, value);
    }
}
