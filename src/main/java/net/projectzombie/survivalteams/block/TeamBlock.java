package net.projectzombie.survivalteams.block;

import net.projectzombie.survivalteams.file.FileWrite;
import net.projectzombie.survivalteams.file.WorldCoordinate;
import net.projectzombie.survivalteams.file.buffers.HitToolBuffer;
import net.projectzombie.survivalteams.file.buffers.BlockBuffer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Created by maxgr on 7/17/2016.
 */
public class TeamBlock extends SurvivalBlock {
    private String teamName;
    private Location loc;

    /**
     * Main constructor, meant for all blocks that are not default.
     * @param health Block's health.
     * @param teamName Team that placed the block.
     * @param loc Location of block.
     */
    public TeamBlock(final int health, final String teamName, final Location loc)
    {
        super(health);
        this.teamName = teamName;
        this.loc = loc;
    }

    /**
     * @param teamName New team the block belongs to. Only should be used when migrating
     *                      from default.
     */
    public void setTeamName(final String teamName)
    {
        this.teamName = teamName;
    }

    /**
     * @return Name of team block belongs to.
     */
    public String getTeamName()
    {
        return teamName;
    }

    /**
     * @return Location of block.
     */
    public Location getLocation()
    {
        return loc;
    }

    /**
     * Given the item in hand the block will take damage, item will also take damage,
     *  if damageable.
     * @param pI Player's inventory that is doing the attack.
     * @Return True if the block broke because of the hit and did so successfully.
     */
    public boolean takeHit(PlayerInventory pI) {
        // Tool damage if damageable hitTool.
        ItemStack item = pI.getItemInHand();
        TeamHitTool weapon = HitToolBuffer.getHitTool(item.getType());
        if (item.getType().getMaxDurability() != 0)
        {
            if (weapon != null)
                weapon.itemHit(item);
            else
            {
                short itemHealth = (short) (item.getDurability() +
                                    HitToolBuffer.getDefaultDurability());
                item.setDurability(itemHealth);
            }
            if (item.getDurability() >= item.getType().getMaxDurability())
                pI.remove(pI.getItemInHand());
        }

        return takeHit(item);
    }

    /**
     * Zombie specific damage event.
     * @param eE Zombie's inventory.
     * @Return True if the block broke because of the hit and did so successfully.
     */
    public boolean takeHit(EntityEquipment eE) {
        // Can be changed to specify zombie's as a whole damage.
        ItemStack weapon = eE.getItemInHand();
        weapon.setDurability(weapon.getDurability());
        return takeHit(weapon);
    }

    /**
     * Unifying damage event, given item gives block damage.
     * @param weapon Damage specifier.
     * @Return True if the block broke because of the hit and did so successfully.
     */
    private boolean takeHit(ItemStack weapon)
    {
        if (HitToolBuffer.getHitTools().contains(weapon.getType()))
        {
            int hitValue = HitToolBuffer.getHitToolHitPoints(weapon.getType()).getHitPoints();
            hit(hitValue);
        }
        else
        {
            hit(HitToolBuffer.getDefaultHitPoints());
        }

        if (isDead())
        {
            return kill();
        }

        return false;
    }

    /**
     * Subtracts or adds to health depending on sign of hitValue.
     * @param hitValue amount to add or subtract from health.
     */
    private void hit(final int hitValue)
    {
        if (hitValue >= 0)
            safeAddToHealth(hitValue, loc.getBlock().getType());
        else
            decrementHealth(hitValue);
    }

    /**
     * Gives a hitValue value to the block, negative numbers will hurt the block, and
     *  positive numbers will heal it. Returns true if the block broke from the hitValue.
     * @param hitValue Hit value on block, negative for block damage and positive for healing.
     * @return True if the block broke because of the hit and did so successfully.
     */
    public boolean takeHit(final int hitValue)
    {
        hit(hitValue);

        if (isDead())
        {
            return kill();
        }

        return false;
    }

    /**
     * @return True if dead.
     */
    public boolean isDead()
    {
        return getHealth() <= 0;
    }

    public String getID()
    {
        return WorldCoordinate.toStringLocID(teamName, loc.getBlock());
    }

    /**
     * Kills the block.
     * @Return True if the disc deletion of the block was successful.
     */
    public boolean kill()
    {
        if (BlockBuffer.isBreakNaturally())
            loc.getBlock().breakNaturally();
        else
            loc.getBlock().setType(Material.AIR);

        BlockBuffer.remove(loc);

        return FileWrite.wipeTeamBlock(getID());
    }

    /**
     * Saves the block.
     * @return True if save was successful.
     */
    public boolean saveTeamBlock() {
        return FileWrite.writeTeamBlockHealth(teamName, loc, getHealth());
    }

    /**
     * Creates a SB and adds it to buffer.
     * @param block
     * @param teamNameN
     * @return If write of new block is successful.
     */
    public static boolean createTeamBlock(Block block, String teamNameN) {
        SurvivalBlock sBT = BlockBuffer.getServivalBlock(block.getType());
        TeamBlock sB = new TeamBlock(sBT.getHealth(), teamNameN, block.getLocation());

        BlockBuffer.add(sB, block.getLocation());
        return FileWrite.writeTeamBlockHealth(sB);
    }
}
