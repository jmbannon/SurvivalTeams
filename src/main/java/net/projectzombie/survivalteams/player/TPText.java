/*
 * SurvivalTeams
 *
 * Version:     0.5
 * MC Build:    1.8.3
 * Date:        09-22-2015
 *
 * Author:      Jesse Bannon
 * Email:       jbann1994@gmail.com
 * Server:      Project Zombie
 * Website:     www.projectzombie.net
 *
 * Allows players to create rank-based Teams. Includes features such as no
 * team PVP and a group spawn.
 *
 */
package net.projectzombie.survivalteams.player;

import java.util.ArrayList;
import java.util.UUID;
import net.projectzombie.consistentchatapi.PluginChat;
import net.projectzombie.survivalteams.file.FileRead;
import net.projectzombie.survivalteams.team.Team;
import net.projectzombie.survivalteams.team.TeamRank;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author jb
 */
public class TPText
{
    private static final PluginChat chat = new PluginChat("Party",         // Tag
                                                          ChatColor.GREEN, // Tag Color
                                                          ChatColor.GRAY,  // Plain Text
                                                          ChatColor.RED,   // Keyword 1
                                                          ChatColor.AQUA); // Keyword 2
    
    private static final String tag = chat.getTag();
    
    static protected String
    
    TEAM_CREATE_NAME_TAKEN  = tag+"A team with this name already exists.",
    RANK_NO_OPERATION       = tag+"You are not a high enough rank to perform this action.",
    INVITE_INVALID_EXPIRED  = tag+"This invite has expired; ask for a resend.",
    INVITE_INVALID_HAS_TEAM = tag+"You cannot invite a player who is already on a team.",
    INVITE_INVALID_ON_TEAM  = tag+"You cannot accept a team invite when you are on a team.",  
    LEADER_CANT_QUIT        = tag+"As the leader you must disband your team to quit it.",
    NOT_LEADER              = tag+"You must be a leader to perform this action.",
    NO_TEAM                 = tag+"You are not on a team.",
    ON_TEAM                 = tag+"You cannot do this while on a team.",
    NOT_ON_TEAM             = tag+"This player is not on your team.",
    FILE_ERROR              = tag+"A file error has occured with this operation. Please consult an admin",
    NEW_SPAWN               = tag+"A new team base has been set.",
    PLAYER_NOT_FOUND        = tag+"Could find a player with that name.",
    RANK_NOT_FOUND          = tag+"That rank does not exist.",
    TEAM_NOT_ONLINE         = tag+"None of your team is online.",
    INVALID_BASE_SET        = tag+"You cannot set your base here.",
    INVALID_BASE            = tag+"Your team base point is invalid. It must be reset by the party leader.",
    SELF_ERROR              = tag+"You cannot perform this action on yourself.";
    
    private TPText() { /* Return nothing. */ }
    
    static public String sendInvite(final TeamPlayer invited)
    {
        return tag+"You have invited " + chat.toK1(invited.getPlayerName()) + ".";
    }
    
    static public String kickedPlayer(final TeamPlayer player)
    {
        return tag+"You have kicked " + chat.toK1(player.getPlayerName()) + ".";
    }
    
    static public String kickedFromTeam(final Team team)
    {
        return tag+"You have been kicked from " + chat.toK1(team.getName()) + ".";
    }
    
    static public String recieveTeamInvite(final TeamPlayer sender)
    {
        return tag+chat.toK1(sender.getPlayerName()) + " has invited you to " + chat.toK2(sender.getTeam().getName()) + ".";
    }
    
    static public String recievePromotion(final TeamPlayer sender,
                                          final TeamRank newRank)
    {
        return tag+chat.toK1(sender.getPlayerName()) + " has promoted you to " + chat.toK2(newRank.getTitle()) + ".";
    }
    
    static public String recieveDemotion(final TeamPlayer sender,
                                            final TeamRank newRank)
    {
        return tag+chat.toK1(sender.getPlayerName()) + " has demoted you to " + chat.toK2(newRank.getTitle()) + ".";
    }
    
    static public String acceptTeamInvite(final Team team)
    {
        return tag+"You have joined " + chat.toK1(team.getName()) + " starting as a " + chat.toK2(TeamRank.FOLLOWER.getTitle()) + ".";
    }
    
    static public String createdNewTeam(final Team team)
    {
        return tag+"You have created " + chat.toK1(team.getName()) + ".";
    }
    
    static public String disbandedTeam(final Team team)
    {
        return tag+chat.toK1(team.getName()) + " has been disbanded.";
    }
    
    static public String hasQuitTeam(final TeamPlayer quitter)
    {
        return tag+chat.toK1(quitter.getPlayerName()) + " has quit your team.";
    }
    
    static public String hasJoinedTeam(final TeamPlayer joiner)
    {
        return tag+chat.toK1(joiner.getPlayerName()) + " has joined your team.";
    }
    
    static public String quitTeam(final Team team)
    {
        return tag+"You have quit " + chat.toK1(team.getName()) + ".";
    }
    
    static public String promoted(final TeamPlayer reciever,
                                  final TeamRank rank)
    {
        return tag+"You have promoted " + chat.toK1(reciever.getPlayerName()) + " to " + chat.toK2(rank.getTitle()) + ".";
    }
    
    static public String demoted(final TeamPlayer reciever,
                                 final TeamRank rank)
    {
        return tag+"You have demoted " + chat.toK1(reciever.getPlayerName()) + " to " + chat.toK2(rank.getTitle()) + ".";
    }
    
    static public void printTeamInfo(final Team team,
                                     final TeamPlayer teamPlayer)
    {
        final Player player = teamPlayer.getPlayer();
        
        if (team != null)
        {
            final String teamName = team.getName();
            final ArrayList<String> officers = new ArrayList<>();
            final ArrayList<String> followers = new ArrayList<>();
            
            TeamRank rank;
            String name;
            for (UUID uuid : team.getMemberUUIDs())
            {
                name = FileRead.getMemberName(teamName, uuid);
                rank = FileRead.getMemberRank(teamName, uuid);
                if (name != null)
                {
                    if (rank.equals(TeamRank.OFFICER))
                        officers.add(name);
                    else if (rank.equals(TeamRank.FOLLOWER))
                        followers.add(name);
                }
            }
            
            player.sendMessage(tag+chat.toK1(teamName));
            player.sendMessage(chat.toK1("Leader: ") + FileRead.getLeaderName(teamName, team.getLeaderUUID()));
            player.sendMessage(chat.toK1("Officers: "));
            for (String officerName : officers)
                player.sendMessage(chat.toPT(officerName));
            player.sendMessage(chat.toK1("Followers: "));
            for (String followerName : followers)
                player.sendMessage(chat.toPT(followerName));
            
        }
        else
            player.getPlayer().sendMessage(NO_TEAM);
    }
}
