package ml.karmaconfigs.api.bukkit.legacy.skull;

import net.minecraft.server.v1_7_R4.*;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;

public class NMSSkull extends EntityWitherSkull {

    private boolean lockTick;

    public NMSSkull(World world) {
        super(((CraftWorld) world).getHandle());
        this.motX = 0.0D;
        this.motY = 0.0D;
        this.motZ = 0.0D;
        this.dirX = 0.0D;
        this.dirY = 0.0D;
        this.dirZ = 0.0D;
        this.boundingBox.a = 0.0D;
        this.boundingBox.b = 0.0D;
        this.boundingBox.c = 0.0D;
        this.boundingBox.d = 0.0D;
        this.boundingBox.e = 0.0D;
        this.boundingBox.f = 0.0D;
        a(0.0F, 0.0F);
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

    public int getId() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        if (elements.length > 2 && elements[2] != null && elements[2].getFileName().equals("EntityTrackerEntry.java") && elements[2].getLineNumber() > 134 && elements[2].getLineNumber() < 144)
            return -1;
        return super.getId();
    }

    public void h() {
        if (!lockTick)
            super.h();
    }

    public void makeSound(String sound, float f1, float f2) {}

    public void setLockTick(boolean lock) {
        lockTick = lock;
    }

    public void setLocationNMS(double x, double y, double z) {
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
}
