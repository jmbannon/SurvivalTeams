/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.projectzombie.survivalteams.file;

import net.projectzombie.survivalteams.block.TeamBlock;
import net.projectzombie.survivalteams.file.buffers.TeamBuffer;
import static net.projectzombie.survivalteams.file.FileContents.*;
import net.projectzombie.survivalteams.file.buffers.PlayerBuffer;
import net.projectzombie.survivalteams.player.TeamPlayer;
import net.projectzombie.survivalteams.team.Team;
import net.projectzombie.survivalteams.team.TeamRank;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

/**
 *
 * @author com.gmail.jbann1994
 */
public class FileWrite
{

    ////////////////////////////////////////////////////////////////////////////
    // Write functions
    //
    public static boolean writeHitTool(Material material, int damage, int durability)
    {
        TEAM_YAML.set(FilePath.hitToolHitPoints(material), damage);
        TEAM_YAML.set(FilePath.hitToolDurability(material), durability);
        return saveConfig();
    }

    public static boolean wipeHitTool(Material material)
    {
        TEAM_YAML.set(FilePath.hitTool(material), null);
        return saveConfig();
    }

    public static boolean writeDefaultHitToolDurability(int durability)
    {
        TEAM_YAML.set(FilePath.hitToolDefaultDurability(), durability);
        return saveConfig();
    }

    public static boolean writeDefaultHitToolDamage(int damage)
    {
        TEAM_YAML.set(FilePath.hitToolDefaultHitPoints(), damage);
        return saveConfig();
    }

    public static boolean writeTeamBlockCheckerTool(Material tool)
    {
        TEAM_YAML.set(FilePath.teamBlockCheckerTool(), tool.toString());
        return saveConfig();
    }

    public static boolean writeBreakNaturally(boolean natural)
    {
        TEAM_YAML.set(FilePath.breakNaturally(), natural);
        return saveConfig();
    }

    public static boolean writeAttackDelay(int delay)
    {
        TEAM_YAML.set(FilePath.attackDelay(), delay);
        return saveConfig();
    }

    public static boolean writeTeamBlockHealth(final String teamName, final Location loc, final int health)
    {
        TEAM_YAML.set(FilePath.teamBlockHealth(teamName, loc), health);
        return saveConfig();
    }

    public static boolean writeDefaultBuildRadius(final int buildRadius)
    {
        TEAM_YAML.set(FilePath.buildRadius(), buildRadius);
        return saveConfig();
    }

    public static boolean writeSurvivalBlockHealth(final String material, final int health)
    {
        TEAM_YAML.set(FilePath.survivalBlockHealth(material), health);
        return saveConfig();
    }

    /**
     * Used for destroying block for disc.
     * @param material
     * @return
     */
    public static boolean wipeSurvivalBlock(final Material material)
    {
        TEAM_YAML.set(FilePath.survivalBlock(material.toString()), null);
        return saveConfig();
    }

    /**
     * Used for destroying block from disc.
     * @param ID
     * @return
     */
    public static boolean wipeTeamBlock(String ID)
    {
        TEAM_YAML.set(FilePath.teamBlock(ID), null);
        return saveConfig();
    }

    public static boolean wipeTeamBlocks()
    {
        TEAM_YAML.set(FilePath.teamBlocks(), null);
        return saveConfig();
    }

    public static boolean writeTeamBlockHealth(final TeamBlock sB) {
        return writeTeamBlockHealth(sB.getTeamName(), sB.getLocation(), sB.getHealth());
    }

    public static boolean writeTeam(final TeamPlayer creator, final Team team)
    {
        final String teamName = team.getName();
        final boolean savedSuccessfully;
        TEAM_YAML.set(FilePath.leader(teamName) + "." + creator.getUUID().toString(), creator.getPlayerName());
        savedSuccessfully = saveConfig();
        if (savedSuccessfully)
        {
            TeamBuffer.add(team);
        }
        return savedSuccessfully;
    }

    public static boolean writePlayerToTeam(final Team team,
                                            final TeamPlayer reciever,
                                            final TeamRank rank)
    {
        if (!reciever.isLeader())
        {
            TEAM_YAML.set(FilePath.memberRank(team, reciever), rank.toFileName());
            TEAM_YAML.set(FilePath.memberName(team, reciever), reciever.getPlayerName());
            return saveConfig();
        } else
        {
            return false;
        }
    }

    public static boolean writeSpawn(final Team team,
                                     final Location location)
    {
        TEAM_YAML.set(FilePath.spawn(team), WorldCoordinate.toString(location.getBlock()));
        return saveConfig();
    }

    public static TeamPlayer getPlayer(final String name)
    {
        final Player player = Bukkit.getPlayer(name);
        return player != null ? PlayerBuffer.get(player.getUniqueId()) : null;
    }
    
    public static boolean removePlayerFromTeam(final Team team,
                                               final TeamPlayer player)
    {
        TEAM_YAML.set(FilePath.member(team, player), null);
        return saveConfig();
    }
    
    static public boolean removeTeam(final Team team)
    {
        TEAM_YAML.set(FilePath.team(team), null);
        return saveConfig();
    }
    
    
    static public boolean removeSpawn(final Team team)
    {
        TEAM_YAML.set(FilePath.spawn(team), null);
        return saveConfig();
    }
    
    static public boolean removeAllTeamSpawns()
    {
        Location spawnLocation;
        String spawnPath;
        Block bannerBlock;
        if (TEAM_YAML.contains(FilePath.teams()))
        {
            for (String teamName : TEAM_YAML.getConfigurationSection(FilePath.teams()).getKeys(false))
            {
                spawnPath = FilePath.team(teamName) + ".spawn";
                spawnLocation = WorldCoordinate.toLocation(TEAM_YAML.getString(spawnPath));
                if (spawnLocation != null)
                {
                    bannerBlock = spawnLocation.getBlock().getRelative(BlockFace.DOWN);
                    if (bannerBlock.getType().equals(Material.STANDING_BANNER))
                        bannerBlock.setType(Material.AIR);
                }
                    
                TEAM_YAML.set(spawnPath, null);
            }
        }
        return saveConfig();
    }
    
}
