/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.projectzombie.survivalteams.file;

import java.util.ArrayList;
import java.util.UUID;
import static net.projectzombie.survivalteams.file.FileContents.*;
import static net.projectzombie.survivalteams.file.FilePath.*;
import net.projectzombie.survivalteams.team.Team;
import net.projectzombie.survivalteams.team.TeamRank;
import org.bukkit.Location;

/**
 *
 * @author com.gmail.jbann1994
 */
public class FileRead
{
    static public TeamRank getMemberRank(final String teamName,
                                         final UUID uuid)
    {
        final String memberPath = FilePath.members(teamName) + "." + uuid.toString() + ".rank";
        return TEAM_YAML.contains(memberPath) ? TeamRank.getIntRank(TEAM_YAML.getString(memberPath)) : TeamRank.NULL;
    }
    
    static public String getMemberName(final String teamName,
                                       final UUID uuid)
    {
        final String memberPath = FilePath.members(teamName) + "." + uuid.toString() + ".name";
        return TEAM_YAML.contains(memberPath) ? TEAM_YAML.getString(memberPath) : null;
    }
    
    static public String getLeaderName(final String teamName,
                                       final UUID uuid)
    {
        final String leaderPath = FilePath.leader(teamName) + "." + uuid.toString();
        return TEAM_YAML.contains(leaderPath) ? TEAM_YAML.getString(leaderPath) : null;
    }
    
    /**
     * Gets a list of member UUIDs from an existing team.
     * @param teamName Existing team name within TEAM_YAML.
     * @return 
     */
    public static ArrayList<UUID> getMemberUUIDs(final String teamName)
    {
        final ArrayList<UUID> uuids = new ArrayList<>();
        final String memberPath = FilePath.members(teamName);
        
        if (TEAM_YAML.contains(memberPath))
        {
            for (String uuid : TEAM_YAML.getConfigurationSection(memberPath).getKeys(false))
                uuids.add(UUID.fromString(uuid));
        }
        return uuids;
    }
    
    static protected boolean containsMemberUUID(final String teamName,
                                                final UUID uuid)
    {
        return TEAM_YAML.contains(FilePath.member(teamName, uuid));
    }
        
    public static String getTeamName(final UUID uuid)
    {
        final String teamsPath = FilePath.teams();
        if (TEAM_YAML.contains(teamsPath))
        {
            for (String teamName : TEAM_YAML.getConfigurationSection(teamsPath).getKeys(false))
            {
                 if (containsMemberUUID(teamName, uuid) || getLeaderUUID(teamName).equals(uuid))
                     return teamName;
            }
        }
        return null;
    }
    
    public static UUID getLeaderUUID(final String teamName)
    {
        if (containsTeam(teamName))
        {
            for (String leaderUUID : TEAM_YAML.getConfigurationSection(FilePath.leader(teamName)).getKeys(false))
                return UUID.fromString(leaderUUID);
        }
        return null;
    }
    
    public static Location getSpawn(final String teamName)
    {
        final String spawnPath = FilePath.team(teamName) + ".spawn";
        return TEAM_YAML.contains(spawnPath) ? WorldCoordinate.toLocation(TEAM_YAML.getString(spawnPath)) : null;
    }
    
    public static boolean containsTeam(final String teamName)
    {
        return TEAM_YAML.contains(FilePath.team(teamName));
    }
}
