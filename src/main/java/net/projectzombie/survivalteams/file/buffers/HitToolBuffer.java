package net.projectzombie.survivalteams.file.buffers;

import net.projectzombie.survivalteams.block.TeamHitTool;
import net.projectzombie.survivalteams.file.FileRead;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Holds all weapon types.
 */
public class HitToolBuffer
{
    private static Map<Material, TeamHitTool> hitTools;
    private static int defaultDamage;
    private static int defaultDurability;

    public static TeamHitTool getHitTool(Material material)
    {
        return hitTools.get(material);
    }

    public static TeamHitTool getHitToolHitPoints(Material material)
    {
        return hitTools.get(material);
    }

    public static int getDefaultHitPoints()
    {
        return defaultDamage;
    }

    public static int getDefaultDurability()
    {
        return defaultDurability;
    }

    public static Set<Material> getHitTools()
    {
        return hitTools.keySet();
    }

    public static void remove(Material material)
    {
        hitTools.remove(material);
    }

    /**
     * Reads in all types and defaults, and stores in buffer.
     */
    public static void readInDefaults()
    {
        defaultDamage = FileRead.getDefaultHitToolDamage();
        defaultDurability = FileRead.getHitToolDefaultDurability();

        //Read in hitTools.
        if (hitTools == null)
            hitTools = new HashMap<Material, TeamHitTool>();
        Set<String> tWeapons = FileRead.getHitTools();
        if (tWeapons != null)
        {
            for (String tWeapon : tWeapons)
            {
                int damage = FileRead.getHitToolHitPoints(Material.valueOf(tWeapon));
                int durability = FileRead.getHitToolDurability(Material.valueOf(tWeapon));
                hitTools.put(Material.valueOf(tWeapon), new TeamHitTool(durability, damage));
            }
        }
    }
}
