package me.swerve.riseuhc.scenario.scen;

import me.swerve.riseuhc.player.UHCPlayer;
import me.swerve.riseuhc.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class Timber extends Scenario {
    public Timber() {
        super("Timber", Material.LOG, Collections.singletonList(
                "&fWhen you break a tree, each log on the tree will break"
        ));
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e) {
        UHCPlayer player = UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId());
        if(player.getCurrentState() != UHCPlayer.PlayerState.PLAYING) return;

        if (e.getBlock().getType() == Material.LOG || e.getBlock().getType() == Material.LOG_2) this.loop(e.getBlock(), e.getPlayer());
    }

    private void loop(Block block, Player player) {
        for (BlockFace blockface : BlockFace.values()) {
            Block relative = block.getRelative(blockface);
            if (relative.getType() != Material.LOG && relative.getType() != Material.LOG_2) continue;

            player.getInventory().addItem(new ItemStack(relative.getType()));

            relative.setType(Material.AIR);
            relative.getState().update(true, false);

            this.loop(relative, player);
        }
    }
}
