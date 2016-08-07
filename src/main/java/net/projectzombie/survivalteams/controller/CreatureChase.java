package net.projectzombie.survivalteams.controller;

import net.projectzombie.survivalteams.block.TeamBlock;
import net.projectzombie.survivalteams.file.buffers.BlockBuffer;
import net.projectzombie.survivalteams.main.PluginHelpers;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Zombie block hitting scheduler, if a monster is chasing a person, it will check if a block
 *  is in between them and try to break it.
 */
public class CreatureChase implements Runnable
{
    // Magic number, can be changed, just allows a valid loc.
    public static final int ZOMBIE_Hit_RANGE = 15;

    private Monster monster;

    /**
     * Basic constructor to get things needed to track reoccurring monster block breaks.
     * @param monster The monster chasing a player.
     */
    public CreatureChase(Monster monster)
    {
        this.monster = monster;
    }

    /**
     * Checks if a monster is targeting a player, if so keeps a delayed hit event and tries to
     *  damage the blocks at eye level, above head, and at the monster's feet.
     */
    @Override
    public void run()
    {
        if (monster.getTarget() instanceof Player && !monster.isDead())
        {
            PluginHelpers.getScheduler().scheduleSyncDelayedTask(
                PluginHelpers.getPlugin(), this, BlockBuffer.getAttackDelay());

            Location monsterFeetLoc = monster.getLocation();
            hitBlock(monsterFeetLoc, 0); // Foot level block
            hitBlock(monsterFeetLoc, 1); // Eye level block
            hitBlock(monsterFeetLoc, 2); // One above head block
        }
    }

    /**
     * Will hit the block one in front of direction. If yMovement above 0, block hit will move up.
     * @param mobLoc Specifies the direction of zombie, and reference point for hit.
     * @param yMovement The number of blocks above that location that will be hit.
     */
    public void hitBlock(Location mobLoc, int yMovement)
    {
        Vector direction = mobLoc.getDirection();
        int xChange = 0;
        int zChange = 0;

        if (Math.abs(direction.getX()) > Math.abs(direction.getZ()))
        {
            if (direction.getX() > 0.0)
                xChange = 1;
            else
                xChange = -1;
        }
        else
        {
            if (direction.getY() > 0.0)
                zChange = 1;
            else
                zChange = -1;
        }

        Block middle = mobLoc.getBlock().getRelative(xChange, yMovement, zChange);

        TeamBlock middleTB = BlockBuffer.getTeamBlock(middle.getLocation());
        if (middleTB != null)
        {
            middleTB.takeHit(monster.getEquipment());
        }

    }

    /**
     * Block hit, it the block is there and not air.
     * @param main Reference block, the one at monster's body.
     * @param x Determines direction of E or W if not 0.
     * @param z Determines direction of N or S if not 0.
     */
    @Deprecated
    public void hitBlock(Block main, int x, int z)
    {
        BlockFace mainDirection = null;
        if (x != 0)
        {
            if (x > 0)
                mainDirection = BlockFace.WEST;
            else
                mainDirection = BlockFace.EAST;
        }
        else if (z != 0)
        {
            if (z > 0)
                mainDirection = BlockFace.NORTH;
            else
                mainDirection = BlockFace.SOUTH;
        }

        if (mainDirection != null)
        {
            Block middle = main.getRelative(mainDirection, 1);

            TeamBlock middleTB = BlockBuffer.getTeamBlock(middle.getLocation());
            if (middleTB != null)
            {
                middleTB.takeHit(monster.getEquipment());
            }
        }
    }
}