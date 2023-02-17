package me.swerve.riseuhc.util;

import net.minecraft.server.v1_8_R3.EntityChicken;
import net.minecraft.server.v1_8_R3.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import java.util.HashMap;

public class SitUtil {

    private static final HashMap<String, Integer> seatData = new HashMap<>();

    public static void sitPlayer(Player p) {
        EntityChicken chicken = new EntityChicken(((CraftWorld) p.getWorld()).getHandle());
        CraftPlayer craftPlayer = (CraftPlayer) p;

        Location loc = p.getLocation();
        chicken.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0.0f, 0.0f);
        chicken.setInvisible(true);

        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(chicken);
        craftPlayer.getHandle().playerConnection.sendPacket(packet);

        PacketPlayOutAttachEntity sit = new PacketPlayOutAttachEntity(0, craftPlayer.getHandle(), chicken);
        craftPlayer.getHandle().playerConnection.sendPacket(sit);

        seatData.put(p.getName(), chicken.getId());
    }

    public static void unSitPlayer(Player p) {
        if(seatData.get(p.getName()) == null) return;

        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(seatData.get(p.getName()));
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }
}
