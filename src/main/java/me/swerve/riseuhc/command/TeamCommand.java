package me.swerve.riseuhc.command;

import me.swerve.riseuhc.attribute.MatchAttribute;
import me.swerve.riseuhc.manager.TeamManager;
import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamCommand implements CommandExecutor {
    String[] usageMessage = new String[] {
            ChatColor.translateAlternateColorCodes('&', "&cTeam Commands:"),
            ChatColor.translateAlternateColorCodes('&', "&c/team create &7- Creates a team"),
            ChatColor.translateAlternateColorCodes('&', "&c/team invite &7- Invites a player to the team"),
            ChatColor.translateAlternateColorCodes('&', "&c/team accept &7- Accepts invitations from teams"),
            ChatColor.translateAlternateColorCodes('&', "&c/team leave &7- Leave your team"),
            ChatColor.translateAlternateColorCodes('&', "&c/team kick &7- Kick a player from your team"),
            ChatColor.translateAlternateColorCodes('&', "&c/team list &7- Check a players team")
    };

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fYou must be a player to use this command."));
            return false;
        }

        Player p = (Player) sender;

        if (((int) MatchAttribute.getAttributeFromName("Team Size").getCurrentSelection().getValue()) == 1) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cIt's an FFA! You can't use team commands!"));
            return false;
        }

        if (label.equalsIgnoreCase("sc") || label.equalsIgnoreCase("sendcoords") || label.equalsIgnoreCase("pmcoords")) {
            sendCoords(p);
            return false;
        }

        if (args.length < 1) {
            p.sendMessage(usageMessage);
            return false;
        }

        if (label.equalsIgnoreCase("tl")) {
            String[] newArgs = new String[] { "list" };
            if (args.length == 1) newArgs = new String[] { "list", args[0] };
            args = newArgs;

            teamList(p, args);
            return false;
        }


        switch (args[0]) {
            case "create":
                createTeam(p);
                return false;
            case "invite":
                teamInvite(p, args);
                return false;
            case "accept":
            case "join":
                teamAccept(p, args);
                return false;
            case "leave":
                teamLeave(p);
                return false;
            case "kick":
                teamKick(p, args);
                return false;
            case "list":
                teamList(p, args);
                return false;
            default:
                p.sendMessage(usageMessage);
                return false;
        }
    }

    private void createTeam(Player p) {
        boolean isInTeam = false;
        for (Team team : TeamManager.getInstance().getTeamsList()) if (team.isMember(p.getUniqueId())) {
            isInTeam = true;
            break;
        }

        if (isInTeam) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou're already in a team! You must leave your team to create a new team."));
            return;
        }

        Team team = new Team(p.getUniqueId());
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have successfully create a team! Your team is #" + team.getTeamNumber()));
    }

    private void teamInvite(Player p, String[] args) {
        if (args.length != 2) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /team invite <name>"));
            return;
        }

        Team currentTeam = null;
        for (Team team : TeamManager.getInstance().getTeamsList()) if (team.isMember(p.getUniqueId())) {
            currentTeam = team;
            break;
        }

        if (currentTeam == null) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCreating a team since you don't have one!"));
            currentTeam = new Team(p.getUniqueId());
        }

        if (currentTeam.getTeamLeader() != p.getUniqueId()) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&You can't invite people if you're not the team leader!"));
            return;
        }

        if (currentTeam.getTeamMembers().size() >= (int) MatchAttribute.getAttributeFromName("Team Size").getCurrentSelection().getValue()) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou're already at the max team size!"));
            return;
        }

        UUID toInvite = null;
        if (Bukkit.getPlayer(args[1]) != null) toInvite = Bukkit.getPlayer(args[1]).getUniqueId();

        if (toInvite == null) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCould not find the player: " + args[1]));
            return;
        }

        if (toInvite == p.getUniqueId()) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can't invite yourself to a team!"));
            return;
        }

        currentTeam.sendInvite(toInvite);
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have invited " + Bukkit.getPlayer(args[1]).getDisplayName() + " to the team."));
    }

    private void teamAccept(Player p, String[] args) {
        if (args.length != 2) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /team accept <name>"));
            return;
        }

        Team currentTeam = null;
        for (Team team : TeamManager.getInstance().getTeamsList()) if (team.isMember(p.getUniqueId())) {
            currentTeam = team;
            break;
        }

        if (currentTeam != null) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can't join other teams til you've left your team!"));
            return;
        }

        UUID toJoin = null;
        if (Bukkit.getPlayer(args[1]) != null) toJoin = Bukkit.getPlayer(args[1]).getUniqueId();

        if (toJoin == null) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCould not find the player: " + args[1]));
            return;
        }

        Team attemptingToJoin = null;
        for (Team team : TeamManager.getInstance().getTeamsList()) if (team.isMember(toJoin)) {
            attemptingToJoin = team;
            break;
        }

        if (attemptingToJoin == null) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCould not find their team! Did they disband it?"));
            return;
        }

        boolean invited = false;
        for (UUID uuid : attemptingToJoin.getInvitedPlayers()) if (uuid == p.getUniqueId()) {
            invited = true;
            break;
        }

        if (!invited) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou're not invited to that team!"));
            return;
        }

        if (attemptingToJoin.getTeamMembers().size() >= (int) MatchAttribute.getAttributeFromName("Team Size").getCurrentSelection().getValue()) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThat team is already at the max team size!"));
            return;
        }

        attemptingToJoin.addMember(p.getUniqueId());
    }

    private void teamLeave(Player p) {
        if (UHCManager.getInstance().getCurrentGameState() == UHCManager.GameState.SCATTERING || UHCManager.getInstance().getCurrentGameState() == UHCManager.GameState.ENDING) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can't leave your team in the games current state!"));
            return;
        }

        Team currentTeam = null;
        for (Team team : TeamManager.getInstance().getTeamsList()) if (team.isMember(p.getUniqueId())) {
            currentTeam = team;
            break;
        }

        if (currentTeam == null) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can't leave a team since you're not in one!"));
            return;
        }

        currentTeam.removeMember(p.getUniqueId(), false);
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have successfully left your team!"));
    }

    private void teamKick(Player p, String[] args) {
        if (UHCManager.getInstance().getCurrentGameState() == UHCManager.GameState.SCATTERING || UHCManager.getInstance().getCurrentGameState() == UHCManager.GameState.ENDING) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can't kick people from your team in the games current state!"));
            return;
        }

        if (args.length != 2) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /team kick <name>"));
            return;
        }

        Team currentTeam = null;
        for (Team team : TeamManager.getInstance().getTeamsList()) if (team.isMember(p.getUniqueId())) {
            currentTeam = team;
            break;
        }

        if (currentTeam == null) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can't kick people from a team since you're not in one!"));
            return;
        }

        if (currentTeam.getTeamLeader() != p.getUniqueId()) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can't kick people if you're not the team leader!"));
            return;
        }

        UUID toKick = null;
        if (Bukkit.getPlayer(args[1]) != null) toKick = Bukkit.getPlayer(args[1]).getUniqueId();
        else if (Bukkit.getOfflinePlayer(args[1]) != null) toKick = Bukkit.getOfflinePlayer(args[1]).getUniqueId();

        if (toKick == null) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCould not find the player: " + args[1]));
            return;
        }

        currentTeam.removeMember(toKick, true);
    }

    private void teamList(Player p, String[] args) {
        boolean specific = args.length == 2;

        if (specific) {
            UUID toCheck = null;
            if (Bukkit.getPlayer(args[1]) != null) toCheck = Bukkit.getPlayer(args[1]).getUniqueId();
            if (Bukkit.getOfflinePlayer(args[1]) != null) toCheck = Bukkit.getOfflinePlayer(args[1]).getUniqueId();

            if (toCheck == null) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCould not find the player: " + args[1]));
                return;
            }

            Team attemptingToView = null;
            for (Team team : TeamManager.getInstance().getTeamsList()) if (team.isMember(toCheck)) {
                attemptingToView = team;
                break;
            }

            if (attemptingToView == null) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCould not find their team! Did they disband it?"));
                return;
            }

            StringBuilder builder = new StringBuilder();
            List<String> teamListMessage = attemptingToView.getTeamListMessage();
            builder.append(ChatColor.translateAlternateColorCodes('&', teamListMessage.get(0))).append("\n").append(ChatColor.translateAlternateColorCodes('&', teamListMessage.get(1)));
            p.sendMessage(builder.toString());

            return;
        }

        TeamManager.getInstance().sortTeams();

        List<List<String>> teamListString = new ArrayList<>();
        for (Team team : TeamManager.getInstance().getTeamsList()) teamListString.add(team.getTeamListMessage());

        StringBuilder builder = new StringBuilder();
        for (List<String> teamMessage : teamListString) builder.append(ChatColor.translateAlternateColorCodes('&', teamMessage.get(0))).append("\n").append(ChatColor.translateAlternateColorCodes('&', teamMessage.get(1))).append("\n");
        if (builder.length() > 2) builder.deleteCharAt(builder.length() - 1);
        else {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThere are no teams to display!"));
            return;
        }

        p.sendMessage(builder.toString());
    }

    private void sendCoords(Player p) {
        if (UHCManager.getInstance().getCurrentGameState() != UHCManager.GameState.PLAYING) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can't send coords if you're not in game!"));
            return;
        }

        Team currentTeam = null;
        for (Team team : TeamManager.getInstance().getTeamsList()) if (team.isMember(p.getUniqueId())) {
            currentTeam = team;
            break;
        }

        if (currentTeam == null) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can't send coords when you don't have a team"));
            return;
        }

        currentTeam.sendCoords(p);
    }
}
