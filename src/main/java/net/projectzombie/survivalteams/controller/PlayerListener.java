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
package net.projectzombie.survivalteams.controller;

import java.util.UUID;
import net.projectzombie.survivalteams.file.FileRead;
import net.projectzombie.survivalteams.file.buffers.PlayerBuffer;
import net.projectzombie.survivalteams.file.buffers.TeamBuffer;
import net.projectzombie.survivalteams.player.TeamPlayer;
import net.projectzombie.survivalteams.team.Team;
import net.projectzombie.survivalteams.team.TeamRank;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author jb
 */
public class PlayerListener implements Listener
{
    @EventHandler(priority = EventPriority.MONITOR)
    public void AssignTeamPlayerOnLoogin(final PlayerLoginEvent event)
    {
        initializePlayer(event.getPlayer());
    }
    
    public void removeTeamPlayerOnLogout(final PlayerQuitEvent event)
    {
        PlayerBuffer.remove(event.getPlayer().getUniqueId());
    }
    
    /**
     * To be called when a player logs into the server.
     * @param player
     * @return The player's initialized TeamPlayer object.
     */
    static public TeamPlayer initializePlayer(final Player player)
    {
        final UUID uuid = player.getUniqueId();
        final String teamName;
        final TeamPlayer teamPlayer;

        if ((teamName = FileRead.getTeamName(uuid)) != null)
        {
            Team team = getTeam(teamName);
            if (FileRead.getLeaderUUID(teamName).equals(uuid))
                teamPlayer = new TeamPlayer(player, team, TeamRank.LEADER);
            else
                teamPlayer = new TeamPlayer(player, team, FileRead.getMemberRank(teamName, uuid));
        }
        else
            teamPlayer = new TeamPlayer(player);
        
        PlayerBuffer.add(teamPlayer);
        return teamPlayer;
    }
    
    /**
     * Gets the team by name if one of its players are online and the team is
     * within ONLINE_TEAMS.
     * 
     * @param teamName Name of the team.
     * @return  Team of the given name if one of the players is online.
     */
    static private Team getTeam(final String teamName)
    {
        final Team team;
        if (TeamBuffer.contains(teamName))
            team = TeamBuffer.get(teamName);
        else
            team = new Team(teamName, FileRead.getLeaderUUID(teamName), FileRead.getMemberUUIDs(teamName), FileRead.getSpawn(teamName));
        
        TeamBuffer.add(team);
        return team;
    }
    
}
