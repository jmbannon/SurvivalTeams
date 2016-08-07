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
package net.projectzombie.survivalteams.file;

import java.util.UUID;
import net.projectzombie.survivalteams.player.TeamPlayer;
import net.projectzombie.survivalteams.team.Team;

/**
 *
 * @author jb
 */
public class FilePath
{
    private static final String ROOT_PATH      = "teams";
    
    private FilePath() { /* Do nothing */ }

    static protected String teams()
    {
        return ROOT_PATH;
    }
    
    static protected String team(final String teamName)
    {
        return ROOT_PATH + "." + teamName;
    }
    
    static protected String team(final Team team)
    {
        return ROOT_PATH + "." + team.getName();
    }

    static protected String member(final Team team,
                                   final TeamPlayer player)
    {
        return members(team.getName()) + "." + player.getUUID().toString();
    }
    
    static protected String member(final Team team,
                                   final UUID uuid)
    {
        return members(team.getName()) + "." + uuid.toString();
    }
    
    static protected String member(final String teamName,
                                   final UUID uuid)
    {
        return members(teamName) + "." + uuid.toString();
    }
    
    static protected String memberRank(final Team team,
                                       final TeamPlayer player)
    {
        return members(team.getName()) + "." + player.getUUID().toString() + ".rank";
    }
    
    static protected String memberName(final Team team,
                                       final TeamPlayer player)
    {
        return members(team.getName()) + "." + player.getUUID().toString() + ".name";
    }

    static protected String members(final String teamName)
    {
        return team(teamName) + ".members";
    }

    static protected String leader(final String teamName)
    {
        return team(teamName) + ".leader";
    }
    
    static protected String leaderName(final String teamName,
                                       final UUID uuid)
    {
        return team(teamName) + ".leader." + uuid.toString();
    }
    
    static protected String spawn(final String team)
    {
        return team(team) + ".spawn";
    }
    
    static protected String spawn(final Team team)
    {
        return team(team) + ".spawn";
    }
}
