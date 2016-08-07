package net.projectzombie.survivalteams.file.buffers;

import net.projectzombie.survivalteams.block.SurvivalBlock;
import net.projectzombie.survivalteams.block.TeamBlock;
import net.projectzombie.survivalteams.file.FileRead;
import net.projectzombie.survivalteams.file.FileWrite;
import net.projectzombie.survivalteams.file.WorldCoordinate;
import net.projectzombie.survivalteams.player.TeamPlayer;
import net.projectzombie.survivalteams.team.Team;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Buffer to hold default types and all Team blocks placed.
 */
public class BlockBuffer
{

    private static Map<Material, SurvivalBlock> defaultBlocks;
    private static Map<Location, TeamBlock> teamBlocks;
    private static int buildRadius;
    private static boolean breakNaturally;
    private static int attackDelay;
    private static Material tool;

    public static boolean isBreakNaturally()
    {
        return breakNaturally;
    }

    public static Material getTool()
    {
        return tool;
    }

    public static int getAttackDelay()
    {
        return attackDelay;
    }

    public static void removeSurvivalBlock(Material material)
    {
        defaultBlocks.remove(material);
    }

    /**
     * Gets all types of blocks tracked, so blocks able to be broken and placed.
     * @return
     */
    public static Set<Material> getPlaceableBlocks()
    {
        return defaultBlocks.keySet();
    }

    public static int getBuildRadius()
    {
        return buildRadius;
    }

    /**
     * @param material Type of block needed from defaults.
     * @return The SurvivalBlocks with default Team block info. Null if not a saved type.
     */
    public static SurvivalBlock getServivalBlock(Material material) {
        return defaultBlocks.get(material);
    }

    /**
     * Reads in all defaults for blocks and tools.
     */
    public static void readInDefaults()
    {
        buildRadius = FileRead.getBuildRadius();
        breakNaturally = FileRead.getBreakNaturally();
        attackDelay = FileRead.getAttackDelay();
        tool = FileRead.getTeamBlockCheckerTool();

        // Read in default blocks.
        Set<String> blocks = FileRead.getSurvivalBlocks();
        if (defaultBlocks == null)
            defaultBlocks = new HashMap<Material, SurvivalBlock>();
        if (blocks != null)
        {
            for (String block : blocks)
            {
                int health = FileRead.getSurvivalBlockHealth(block);
                defaultBlocks.put(Material.valueOf(block), new SurvivalBlock(health));
            }
        }
    }

    /**
     * Use readInDefaults before using this method. It reads in already placed blocks.
     */
    public static void readInPlacedTeamBlocks()
    {
        Set<String> blocks = FileRead.getTeamBlocks();

        if (teamBlocks == null)
        {
            teamBlocks = new HashMap<Location, TeamBlock>();
        }

        if (blocks != null)
        {
            for (String iD : blocks)
            {
                int health = FileRead.getTeamBlockHealth(iD);
                String teamName = WorldCoordinate.toTeamName(iD);
                Location loc = WorldCoordinate.toLocation(iD);
                TeamBlock teamBlock = new TeamBlock(health, teamName, loc);
                teamBlocks.put(loc, teamBlock);
            }
        }
    }

    /**
     * Receives a TeamBlock from the buffer.
     * @param loc Location of TeamBlock.
     * @return TeamBlock at location, null if not found.
     */
    public static TeamBlock getTeamBlock(Location loc) {
        return teamBlocks.get(loc);
    }

    /**
     * TeamBlock to be added by its location.
     * @param teamBlock tB to be added.
     * @param loc Location to find it in buffer.
     */
    public static void add(TeamBlock teamBlock, Location loc)
    {
        teamBlocks.put(loc, teamBlock);
    }

    /**
     * @param loc TeamBlock to be removed from buffer.
     */
    public static void remove(Location loc)
    {
        teamBlocks.remove(loc);
    }

    /**
     * Remove blocks belonging to a team.
     * @param teamName Team that owns the blocks.
     */
    public static void removeTeamBlocks(String teamName)
    {
        Map<Location, TeamBlock> tempTeamBlocks = new HashMap<>();
        for (Location loc : teamBlocks.keySet())
        {
            if (teamBlocks.get(loc).getTeamName().equals(teamName))
            {
                removeTeamBlock(loc);
            }
            else
                tempTeamBlocks.put(loc, teamBlocks.get(loc));
        }
        teamBlocks = tempTeamBlocks;
    }

    /**
     * Removes blocks out of range from the base.
     * @param teamName Team that owns the blocks.
     */
    public static void removeTeamBlocksFar(String teamName)
    {
        Team team = TeamBuffer.get(teamName);
        Location spawn = team.getSpawn();
        Map<Location, TeamBlock> tempTeamBlocks = new HashMap<>();
        for (Location loc : teamBlocks.keySet())
        {
            if (teamBlocks.get(loc).getTeamName().equals(teamName) && spawn != null &&
                Math.abs(spawn.distance(loc)) > BlockBuffer.getBuildRadius())
            {
                removeTeamBlock(loc);
            }
            else
                tempTeamBlocks.put(loc, teamBlocks.get(loc));
        }
        teamBlocks = tempTeamBlocks;
    }

    /**
     * Wipes all disc saved blocks, and saves what is in memory.
     */
    public static void saveTeamBlocks()
    {
        FileWrite.wipeTeamBlocks(); // Wipe before save of all blocks in memory.
        for (TeamBlock teamBlock : teamBlocks.values())
            if (teamBlock.getLocation().getBlock().getType() != Material.AIR)
                teamBlock.saveTeamBlock();
    }

    /**
     * Removes all blocks from world and buffer.
     */
    public static void clearBuffer()
    {
        for (Location loc : teamBlocks.keySet())
        {
            removeTeamBlock(loc);
        }

        teamBlocks.clear();
    }

    /**
     * Removes the TeamBlock.
     * @param loc TeamBlock's location.
     */
    public static void removeTeamBlock(Location loc)
    {

        FileWrite.wipeTeamBlock(teamBlocks.get(loc).getID());
        loc.getBlock().setType(Material.AIR);
    }

    public static boolean isInTeamBase(Player player)
    {
        Location loc = player.getLocation();
        TeamPlayer tP = PlayerBuffer.get(player.getUniqueId());

        if (tP != null)
        {
            Team team = tP.getTeam();
            if (team != null)
            {
                Location spawn = team.getSpawn();
                if (spawn != null && spawn.getWorld() == loc.getWorld())
                    return spawn.distance(loc) <= BlockBuffer.getBuildRadius();
            }
        }

        return false;
    }

    public static boolean isInEnemyBase(Player player)
    {
        return getMinDistanceFromEnemyBase(player) <= BlockBuffer.getBuildRadius();
    }

    public static double getMinDistanceFromEnemyBase(Player player)
    {
        double shortestDistance = -1;
        Location loc = player.getLocation();
        TeamPlayer tP = PlayerBuffer.get(player.getUniqueId());

        Set<Location> spawns = new HashSet<>();
        spawns.addAll(TeamBuffer.getSpawns());
        if (tP != null)
        {
            Team team = tP.getTeam();
            if (team != null)
            {
                spawns.remove(team.getSpawn());
                if (spawns.isEmpty())
                    return BlockBuffer.getBuildRadius() * 4;
                for (Location spawn : spawns)
                {
                    if (spawn.getWorld() == loc.getWorld())
                    {
                        double distance = spawn.distance(loc);
                        if (shortestDistance == -1)
                            shortestDistance = distance;
                        if (distance < shortestDistance)
                            shortestDistance = distance;
                    }
                }
            }
        }

        return shortestDistance;
    }
}
