package net.projectzombie.survivalteams.block;

import org.bukkit.inventory.ItemStack;

/**
 * For creating types, does not keep track of actual weapons, just allows unifying block
 *  damage and durability decreases. Types that can not take durability damage like stone
 *  can have specified hitPoints, but durability decrease will have no effect.
 */
public class TeamHitTool
{
    private int hitToDurabilityDecrement;
    private int hitPoints;

    /**
     * Used to make new types of SW.
     * @param hitToDurabilityRatio = Amount that type of weapon is hurt on hit.
     * @param hitPoints Amount of damage does to block.
     */
    public TeamHitTool(int hitToDurabilityRatio, int hitPoints)
    {
        this.hitToDurabilityDecrement = hitToDurabilityRatio;
        this.hitPoints = hitPoints;
    }

    public int getHitToDurabilityDecrement()
    {
        return hitToDurabilityDecrement;
    }

    public int getHitPoints()
    {
        return hitPoints;
    }

    /**
     * Gives item damage.
     * @param item Decreases durability.
     */
    public void itemHit(ItemStack item)
    {
        short itemHealth = (short) (item.getDurability() + hitToDurabilityDecrement);
        item.setDurability(itemHealth);
    }
}
