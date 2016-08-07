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

import net.projectzombie.survivalteams.block.TeamBlock;
import net.projectzombie.survivalteams.file.FileRead;
import net.projectzombie.survivalteams.file.buffers.PlayerBuffer;
import net.projectzombie.survivalteams.file.buffers.BlockBuffer;
import net.projectzombie.survivalteams.file.buffers.TeamBuffer;
import net.projectzombie.survivalteams.main.PluginHelpers;
import net.projectzombie.survivalteams.player.TeamPlayer;
import net.projectzombie.survivalteams.team.Team;
import net.projectzombie.survivalteams.team.TeamRank;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author jb
 */
public class PlayerListener implements Listener
{
    /**
     * Allows the player to check block health on interact.
     * @param event Player clicking on a block to check its health stats.
     */
    @EventHandler
    public void showTeamBlockInfo(final PlayerInteractEvent event)
    {
        if (event.getMaterial() == BlockBuffer.getTool())
        {
            if (event.getPlayer().hasPermission(CMDText.CHECKER_PERM))
            {
                Player player = event.getPlayer();
                Set<Material> transparentB = null;
                Block block = player.getTargetBlock(transparentB, 4);

                if ((BlockBuffer.getPlaceableBlocks()).contains(block.getType()))
                {
                    TeamBlock sB = BlockBuffer.getTeamBlock(block.getLocation());
                    if (sB != null)
                    {
                        player.sendMessage("[" + ChatColor.BLUE + "Block"  + ChatColor.RESET + "]: ");
                        player.sendMessage("    Health: " + sB.getHealth());
                        player.sendMessage("    Team: " + sB.getTeamName());
                    }
                    else
                        player.sendMessage("This is not a survival block.");
                }
            }
        }
    }

    /**
     * Allows players to place certain blocks near their base, and then tracks them in a buffer.
     * @param event Player's place event.
     */
    @EventHandler
    public void trackTeamBlocks(final BlockPlaceEvent event)
    {
        Player player = event.getPlayer();
        if (BlockBuffer.isInTeamBase(player))
        {
            // Will not do null pointer, isInTeamBase grantees they have a team.
            Team team = PlayerBuffer.get(player.getUniqueId()).getTeam();
            event.setCancelled(false);
            TeamBlock.createTeamBlock(event.getBlock(), team.getName());
        }
    }

    /**
     * Tracks player's hitting blocks, and damages Survival Blocks if the block is a SB.
     * @param event Event of block being damaged.
     */
    @EventHandler
    public void trackTeamBlockPlayerDamage(final BlockDamageEvent event)
    {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if ((BlockBuffer.getPlaceableBlocks()).contains(block.getType()))
        {
            TeamBlock sB = BlockBuffer.getTeamBlock(block.getLocation());
            if (sB != null)
            {
                sB.takeHit(player.getInventory());
            }
        }
    }

    /**
     * Tracks Zombies targeting players, so to allow breaking blocks which block the
     *  zombie from the player.
     * @param event Zombie targeting the player.
     */
    @EventHandler
    public void trackTeamBlockMonsterDamage(final EntityTargetEvent event)
    {
        if (event.getEntity() instanceof Monster)
        {
            Monster monster = (Monster) event.getEntity();
            PluginHelpers.getScheduler().scheduleSyncDelayedTask(PluginHelpers.getPlugin(),
                         new CreatureChase(monster),
                         BlockBuffer.getAttackDelay());
        }
    }

    /**
     * Tracks when players get damaged, so team members don't damage each other.
     * @param event Entity attacking an entity.
     */
    @EventHandler
    public void correctPVPHit(final EntityDamageByEntityEvent event)
    {
        //TODO Once updated to 1.10, area effect clouds will need to be taken into account.
        if (event.getEntity() instanceof Player)
        {
            // Handles all pvp with direct harm.
            TeamPlayer p2 = PlayerBuffer.get(((Player) event.getEntity()).getUniqueId());

            boolean p1Valid;
            boolean playerSameTeam;

            if (event.getDamager() instanceof Player)
            {
                TeamPlayer p1 = PlayerBuffer.get(((Player) event.getDamager()).getUniqueId());
                p1Valid = p1 != null;

                // Check if event should be cancelled, must be in if,
                // or will always cancel or always enable.
                // Allows other plugins to change event state.
                if (p1Valid && p1.getTeam().getName().equals(p2.getTeam().getName()))
                {
                    event.setCancelled(true);
                }
            }
            else if (event.getDamager() instanceof Projectile ||
                     event.getDamager() instanceof ThrownPotion)
            { // Handles non-direct pvp.

                if (((Projectile) event.getDamager()).getShooter() instanceof Player)
                {
                    TeamPlayer p1 = PlayerBuffer.get(((Player) ((Projectile) event.getDamager())
                                    .getShooter()).getUniqueId());
                    p1Valid = p1 != null;
                    playerSameTeam = p1.getTeam().getName().equals(p2.getTeam().getName());

                    if (event.getDamager() instanceof ThrownPotion && p1Valid)
                    {
                        Collection<PotionEffect> effects = ((ThrownPotion) event.getDamager()).getEffects();
                        for (PotionEffect effect : effects)
                        {
                            if (PluginHelpers.isBannedPVPPotionEffect(effect.getType()) &&
                                 playerSameTeam)
                            {
                                event.setCancelled(true);
                                return;
                            }
                        }
                    }

                    if (p1Valid && p1.getTeam() == p2.getTeam())
                    {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    /**
     * If player's team has a usable base, it will spawn them there.
     * @param event Player's death.
     */
    @EventHandler
    public void spawnToCorrectBase(final PlayerRespawnEvent event)
    {
        Player player = event.getPlayer();
        Location loc = PlayerBuffer.getSpawnLocation(player);
        if (loc != null)
        {
            event.setRespawnLocation(loc);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void AssignTeamPlayerOnLoogin(final PlayerLoginEvent event)
    {
        initializePlayer(event.getPlayer());
    }

    @EventHandler
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
