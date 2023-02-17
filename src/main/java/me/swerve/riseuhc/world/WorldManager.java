package me.swerve.riseuhc.world;

import com.wimbli.WorldBorder.Events.WorldBorderFillFinishedEvent;
import com.wimbli.WorldBorder.Events.WorldBorderFillStartEvent;
import com.wimbli.WorldBorder.WorldFillTask;
import lombok.Getter;
import me.swerve.riseuhc.RiseUHC;
import me.swerve.riseuhc.attribute.MatchAttribute;
import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.util.BlockUtil;
import me.swerve.riseuhc.util.BorderUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Getter
public class WorldManager implements Listener {
    public enum UHCWorldType { UHC_WORLD, UHC_NETHER }

    @Getter private static WorldManager instance;

    private World uhcWorld;
    private World uhcNetherWorld;

    private UHCWorldType currentlyGeneratingWorldType;

    private boolean fullyGeneratedUHCWorld = false;
    private boolean fullyGeneratedNetherWorld = false;

    private WorldFillTask fillTask;
    protected long startTime;

    public WorldManager() {
        instance = this;

        String path = RiseUHC.getInstance().getServer().getWorldContainer().getAbsolutePath();

        File uhcWorldFile = new File(path, "uhc_world");
        File uhcNetherWorldFile = new File(path, "uhc_world_nether");

        if (uhcWorldFile.exists()) {
            uhcWorld = new WorldCreator("uhc_world").createWorld();
            uhcWorld.getPopulators().add(new CanePopulator());

            fullyGeneratedUHCWorld = true;
        }

        if (uhcNetherWorldFile.exists()) {
            uhcNetherWorld = new WorldCreator("uhc_world_nether").createWorld();
            fullyGeneratedNetherWorld = true;
        }
    }

    public void generate(boolean override) {
        if (override) {
            //try { setTerrainControl(); } catch (IOException ignored) {};
            gen();
            return;
        }

        //try { setTerrainControl(); } catch (IOException e) { e.printStackTrace(); };
        if (!fullyGeneratedUHCWorld) gen();
    }

    public void setTerrainControl() throws IOException {
        File biomeFolder = new File(RiseUHC.getInstance().getDataFolder().getParentFile() + "/TerrainControl/worlds/uhc_world/WorldBiomes");

        if (biomeFolder.listFiles() == null) {
            System.out.println(biomeFolder.getAbsolutePath());
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cError, could not find the biome files to change ore rates."));
            return;
        }

        for (File file : biomeFolder.listFiles()) {
            FileWriter writer = new FileWriter(file);

            System.out.println(Paths.get(file.getAbsolutePath()).getFileName());
            List<String> fileLines = Files.readAllLines(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8);
            new BufferedReader(new FileReader(file)).lines().forEach(System.out::println);
            System.out.println(file.getPath());
            System.out.println(fileLines.size());

/*            for (String str : fileLines) {
                if (str.contains("GOLD")) str = getGoldLine();
                if (str.contains("DIAMOND")) str = getDiamondLine();
                writer.append(str).append("\n");
            }

            writer.flush();
            writer.close();*/
        }
    }

    private void gen() {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fGenerating worlds..."));
        generateUHCWorld();
    }

    private void generateUHCWorld() {
        currentlyGeneratingWorldType = UHCWorldType.UHC_WORLD;
        UHCManager.getInstance().setCurrentGameState(UHCManager.GameState.GENERATING);

        uhcWorld = Bukkit.createWorld(new WorldCreator("uhc_world").environment(World.Environment.NORMAL).type(WorldType.NORMAL));
        uhcWorld.getPopulators().add(new CanePopulator());

        preGenerate();
    }

    private void generateNetherWorld() {
        currentlyGeneratingWorldType = UHCWorldType.UHC_NETHER;
        uhcWorld = Bukkit.createWorld(new WorldCreator("uhc_world_nether").environment(World.Environment.NETHER).type(WorldType.NORMAL));

        UHCManager.getInstance().setCurrentGameState(UHCManager.GameState.GENERATING);
        preGenerate();
    }

    private void preGenerate() {
        String worldName = "uhc_world";
        if (currentlyGeneratingWorldType == UHCWorldType.UHC_NETHER) worldName  = "uhc_world_nether";

        int borderSize = (int) MatchAttribute.getAttributeFromName("Border Size").getCurrentSelection().getValue();
        if (currentlyGeneratingWorldType == UHCWorldType.UHC_NETHER) borderSize = borderSize / 4;

        Arrays.asList(
                "wb fillautosave 0",
                "wb shape square",
                "wb " + worldName +  " set "  + borderSize + " " + borderSize + " 0 0",
                "wb " + worldName + " fill 1000",
                "wb " + worldName + " fill confirm"
        ).forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
    }

    @EventHandler
    public void onFillStart(WorldBorderFillStartEvent e) {
        startTime = System.currentTimeMillis();
        fillTask = e.getFillTask();
    }

    @EventHandler
    public void onFillFinish(WorldBorderFillFinishedEvent e) {
        new BukkitRunnable() {
            public void run() {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fThe " + currentlyGeneratingWorldType.toString().toLowerCase() + " has finished generating in " + (System.currentTimeMillis() - startTime) / 1000 + " seconds."));

                if (currentlyGeneratingWorldType == UHCWorldType.UHC_WORLD) {
                    BorderUtil.createBedrockWall((int) MatchAttribute.getAttributeFromName("Border Size").getCurrentSelection().getValue());
                    for (int i = 0; i < 5; i++) Bukkit.getWorld("uhc_world").getEntities().forEach(entity -> {
                        if (entity.getType() == EntityType.DROPPED_ITEM) entity.remove();
                        if (entity.getType() == EntityType.RABBIT) entity.remove();
                    });

                    fullyGeneratedUHCWorld = true;
                    generateNetherWorld();
                } else {
                    fullyGeneratedNetherWorld = true;
                    UHCManager.getInstance().setCurrentGameState(UHCManager.GameState.WAITING);

                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb uhc_world set 3500 3500 0 0");
                    Bukkit.shutdown();
                }
            }
        }.runTaskLater(RiseUHC.getInstance(), 20);
    }

    private String getDiamondLine() {
        switch ((int) MatchAttribute.getAttributeFromName("Diamond Rate").getCurrentSelection().getValue()) {
            case 6:
                return "Ore(DIAMOND_ORE,8,3,70.0,0,20,STONE)";
            case 5:
                return "Ore(DIAMOND_ORE,8,2,50.0,0,16,STONE)";
            case 4:
                return "Ore(DIAMOND_ORE,7,2,40.0,0,16,STONE)";
        }


        return "Ore(DIAMOND_ORE,7,1,40.0,0,16,STONE)";
    }

    private String getGoldLine() {
        switch ((int) MatchAttribute.getAttributeFromName("Gold Rate").getCurrentSelection().getValue()) {
            case 6:
                return "Ore(GOLD_ORE,8,3,85.0,0,32,STONE)";
            case 5:
                return "Ore(GOLD_ORE,8,3,75.0,0,32,STONE)";
            case 4:
                return "Ore(GOLD_ORE,8,2,65.0,0,32,STONE)";
        }


        return "Ore(GOLD_ORE,8,2,65.0,0,32,STONE)";
    }
}


class CanePopulator extends BlockPopulator {
    @Override
    public void populate(World world, Random random, Chunk chunk) {
        if (random.nextInt(100) < 50) return;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                Block block = getHighestBlock(chunk, i, j);
                if (block == null) return;
                if (block.getType() == Material.SAND || block.getType() == Material.GRASS) tryPlaceCane(block, random);
            }
        }
    }

    private void tryPlaceCane(Block block, Random rand) {
        boolean validPosition = false;

        for (BlockFace face : BlockUtil.getFaces()) {
            if (face == BlockFace.UP || face == BlockFace.DOWN) continue;
            if (block.getRelative(face).getType().toString().toLowerCase().contains("water")) validPosition = true;
        }

        if (!validPosition || (rand.nextInt(100) < 75)) return;
        for (int i = 1; i < rand.nextInt(5) + 1; i++) {
            block.getRelative(0, i, 0).setType(Material.SUGAR_CANE_BLOCK);
        }
    }

    private Block getHighestBlock(Chunk chunk, int x, int z) {
        if (!chunk.isLoaded()) chunk.load();

        Block block = null;
        for (int i = 125; i >= 0; i--) if ((block = chunk.getBlock(x, i, z)).getTypeId() != 0) return block;

        return block;
    }

}
