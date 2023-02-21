package me.swerve.riseuhc;

import assemble.Assemble;
import lombok.Getter;
import me.swerve.riseuhc.attribute.MatchAttribute;
import me.swerve.riseuhc.bot.DiscordBot;
import me.swerve.riseuhc.bot.TwitterBot;
import me.swerve.riseuhc.command.*;
import me.swerve.riseuhc.listener.*;
import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.runnable.SchedulerRunnable;
import me.swerve.riseuhc.runnable.TimeRunnable;
import me.swerve.riseuhc.scenario.Scenario;
import me.swerve.riseuhc.scoreboard.ScoreBoardManager;
import me.swerve.riseuhc.world.WorldManager;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;

public final class RiseUHC extends JavaPlugin {
    @Getter private static RiseUHC instance;

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fRiseUHC has been enabled.."));

        Scenario.instantiateScenarios();
        MatchAttribute.instantiateAttributes();

        registerListeners();
        registerCommands();

        new Assemble(this, new ScoreBoardManager());

        new UHCManager();

        Bukkit.createWorld(new WorldCreator("uhc_practice").environment(World.Environment.NORMAL).type(WorldType.NORMAL));

        try {
            new DiscordBot();
            new TwitterBot();
        } catch (Exception e) { e.printStackTrace(); }

        new TimeRunnable().runTaskTimer(RiseUHC.getInstance(), 0, 200);
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fRiseUHC has been disabled.."));
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new FoodListener(), this);
        Bukkit.getPluginManager().registerEvents(new ConnectionListener(), this);
        Bukkit.getPluginManager().registerEvents(new ItemListener(), this);
        Bukkit.getPluginManager().registerEvents(new SpawnListener(), this);
        Bukkit.getPluginManager().registerEvents(new WeatherListener(), this);
        Bukkit.getPluginManager().registerEvents(new EnchantListener(), this);
        Bukkit.getPluginManager().registerEvents(new HealListener(), this);
        Bukkit.getPluginManager().registerEvents(new DamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
        Bukkit.getPluginManager().registerEvents(new InteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new HealListener(), this);
        Bukkit.getPluginManager().registerEvents(new PortalListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChunkListener(), this);
        Bukkit.getPluginManager().registerEvents(new DeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new CraftListener(), this);
        Bukkit.getPluginManager().registerEvents(new MoveListener(), this);
        Bukkit.getPluginManager().registerEvents(new WorldManager(), this);
        Bukkit.getPluginManager().registerEvents(new DurabilityListener(), this);
    }

    private void registerCommands() {
        Bukkit.getPluginCommand("manage").setExecutor(new ManageCommand());
        Bukkit.getPluginCommand("scenario").setExecutor(new ScenarioCommand());
        Bukkit.getPluginCommand("spectate").setExecutor(new SpectateCommand());
        Bukkit.getPluginCommand("mutechat").setExecutor(new MuteChatCommand());
        Bukkit.getPluginCommand("config").setExecutor(new ConfigCommand());
        Bukkit.getPluginCommand("practice").setExecutor(new PracticeCommand());
        Bukkit.getPluginCommand("cpractice").setExecutor(new PracticeManageCommand());
        Bukkit.getPluginCommand("updateterrain").setExecutor(new UpdateTerrainCommand());
        Bukkit.getPluginCommand("killtop").setExecutor(new KillTopCommand());
        Bukkit.getPluginCommand("health").setExecutor(new HealthCommand());
        Bukkit.getPluginCommand("kills").setExecutor(new KillsCommand());
        Bukkit.getPluginCommand("respawn").setExecutor(new RespawnCommand());
        Bukkit.getPluginCommand("helpop").setExecutor(new HelpopCommand());
        Bukkit.getPluginCommand("helpopreply").setExecutor(new HelpopReplyCommand());
        Bukkit.getPluginCommand("whitelisttoggle").setExecutor(new WhitelistCommand());
        Bukkit.getPluginCommand("discord").setExecutor(new DiscordCommand());
        Bukkit.getPluginCommand("twitter").setExecutor(new TwitterCommand());
        Bukkit.getPluginCommand("schedule").setExecutor(new ScheduleCommand());
        Bukkit.getPluginCommand("cancel").setExecutor(new CancelScheduleCommand());
        Bukkit.getPluginCommand("heal").setExecutor(new HealCommand());
        Bukkit.getPluginCommand("clearchat").setExecutor(new ClearChatCommand());
        Bukkit.getPluginCommand("latescatter").setExecutor(new LateScatterCommand());
    }
}