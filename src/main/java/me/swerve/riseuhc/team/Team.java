package me.swerve.riseuhc.team;

import lombok.Getter;
import lombok.Setter;
import me.swerve.riseuhc.manager.TeamManager;
import me.swerve.riseuhc.player.UHCPlayer;
import me.swerve.riseuhc.util.KillTopPlayer;
import me.swerve.riseuhc.util.ScatterLocation;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Team {
    @Getter private final List<UUID> teamMembers = new ArrayList<>();
    @Getter private UUID teamLeader;
    @Getter @Setter private int teamNumber;

    @Getter private final List<UUID> invitedPlayers = new ArrayList<>();

    @Getter private final Inventory teamInventory = Bukkit.createInventory(null, 27, "Team Inventory");

    @Getter @Setter private ScatterLocation teamScatterLoc = null;

    public Team(UUID teamLeader) {
        this.teamLeader = teamLeader;
        addMember(teamLeader);

        int nextTeamNumber = 1;
        for (Team team : TeamManager.getInstance().getTeamsList()) if (team.getTeamNumber() > nextTeamNumber) nextTeamNumber = team.getTeamNumber();
        this.teamNumber = nextTeamNumber + 1;

        TeamManager.getInstance().getTeamsList().add(this);
    }

    public void addMember(UUID uuid) {
        teamMembers.add(uuid);

        String joiningMember = "";
        if (Bukkit.getPlayer(uuid) != null) joiningMember = Bukkit.getPlayer(uuid).getDisplayName();
        else if (Bukkit.getOfflinePlayer(uuid) != null) joiningMember = Bukkit.getOfflinePlayer(uuid).getName();

        for (UUID u : teamMembers) if (Bukkit.getPlayer(u) != null || u == uuid) Bukkit.getPlayer(u).sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + joiningMember + " has joined the team!"));
    }

    public void removeMember(UUID uuid, boolean removed) {
        if (teamMembers.size() == 1) {
            if (Bukkit.getPlayer(teamLeader) != null) Bukkit.getPlayer(teamLeader).sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThe team has been disbanded!"));

            TeamManager.getInstance().getTeamsList().remove(this);
            return;
        }

        if (teamLeader == uuid) {
            teamMembers.remove(uuid);
            teamLeader = teamMembers.get(0);

            String teamLeaderName = "";
            if (Bukkit.getPlayer(teamLeader) != null) teamLeaderName = Bukkit.getPlayer(teamLeader).getDisplayName();
            else if (Bukkit.getOfflinePlayer(teamLeader) != null) teamLeaderName = Bukkit.getOfflinePlayer(teamLeader).getName();

            if (teamLeaderName.equalsIgnoreCase("")) {
                System.out.println("ERROR");
                return;
            }

            for (UUID u : teamMembers) if (Bukkit.getPlayer(u) != null) Bukkit.getPlayer(u).sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + teamLeaderName + " is the new team leader!"));
        }

        String leavingMember = "";
        if (Bukkit.getPlayer(uuid) != null) leavingMember = Bukkit.getPlayer(uuid).getDisplayName();
        else if (Bukkit.getOfflinePlayer(uuid) != null) leavingMember = Bukkit.getOfflinePlayer(uuid).getName();

        teamMembers.remove(uuid);
        for (UUID u : teamMembers) if (Bukkit.getPlayer(u) != null) {
            if (removed) {
                Bukkit.getPlayer(u).sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + leavingMember + " has been kicked from the team!"));
                return;
            }

            Bukkit.getPlayer(u).sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + leavingMember + " has left the team!"));
        }

    }

    public void sendInvite(UUID uuid) {
        invitedPlayers.add(uuid);

        String teamLeaderName = "";
        if (Bukkit.getPlayer(teamLeader) != null) teamLeaderName = Bukkit.getPlayer(teamLeader).getDisplayName();
        else if (Bukkit.getOfflinePlayer(teamLeader) != null) teamLeaderName = Bukkit.getOfflinePlayer(teamLeader).getName();

        TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&cYou have been invited by " + teamLeaderName + " to team #" + getTeamNumber()));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', "&cClick to accept")).create()));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/team accept " + teamLeaderName));
        if (Bukkit.getPlayer(uuid) != null) Bukkit.getPlayer(uuid).spigot().sendMessage(component);
    }

    public void sendCoords(Player p) {
        for (UUID u : teamMembers) if (Bukkit.getPlayer(u) != null) Bukkit.getPlayer(u).sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + p.getDisplayName() + "&7[&f" + UHCPlayer.getUhcPlayers().get(p.getUniqueId()).getCurrentKills() + "&7] is at &6X: " + p.getLocation().getBlockX() + " Y: " + p.getLocation().getBlockY() + " Z: " + p.getLocation().getBlockZ()));
    }

    public boolean isMember(UUID uuid) {
        return teamMembers.contains(uuid);
    }

    public List<String> getTeamListMessage() {
        List<String> toReturn = new ArrayList<>();
        toReturn.add(ChatColor.translateAlternateColorCodes('&', "&cTeam #" + getTeamNumber()));

        StringBuilder memberLine = new StringBuilder();

        for (UUID uuid : teamMembers) {
            String member = "";
            if (Bukkit.getPlayer(uuid) != null) member = Bukkit.getPlayer(uuid).getDisplayName();
            else if (Bukkit.getOfflinePlayer(uuid) != null) member = Bukkit.getOfflinePlayer(uuid).getName();

            int kills = 0;
            for (KillTopPlayer p : KillTopPlayer.getKillers()) if (p.getName().equalsIgnoreCase(member)) {
                kills = p.getKills();
                break;
            }

            memberLine.append("&a").append(member).append("&7[&f").append(kills).append("&7]&c").append(" ");
        }

        memberLine.deleteCharAt(memberLine.length() - 1);

        toReturn.add(memberLine.toString());

        return toReturn;
    }
}
