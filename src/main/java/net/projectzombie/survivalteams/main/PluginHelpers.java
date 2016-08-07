package net.projectzombie.survivalteams.main;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by maxgr on 7/22/2016.
 */
public class PluginHelpers
{
    private static JavaPlugin plugin;
    private static BukkitScheduler scheduler;
    private static Set<PotionEffectType> bannedPVPPotionEffects;

    public static void loadInHelpers(JavaPlugin pluginLoad, BukkitScheduler schedulerLoad)
    {
        plugin = pluginLoad;
        scheduler = schedulerLoad;

        bannedPVPPotionEffects = new HashSet<>();
        bannedPVPPotionEffects.add(PotionEffectType.BLINDNESS);
        bannedPVPPotionEffects.add(PotionEffectType.CONFUSION);
        bannedPVPPotionEffects.add(PotionEffectType.HARM);
        bannedPVPPotionEffects.add(PotionEffectType.HUNGER);
        bannedPVPPotionEffects.add(PotionEffectType.POISON);
        bannedPVPPotionEffects.add(PotionEffectType.SLOW);
        bannedPVPPotionEffects.add(PotionEffectType.SLOW_DIGGING);
        bannedPVPPotionEffects.add(PotionEffectType.WEAKNESS);
        bannedPVPPotionEffects.add(PotionEffectType.WITHER);
    }

    public static JavaPlugin getPlugin()
    {
        return plugin;
    }

    public static BukkitScheduler getScheduler()
    {
        return scheduler;
    }

    public static Set<PotionEffectType> getBannedPVPPotionEffects()
    {
        return bannedPVPPotionEffects;
    }

    public static boolean isBannedPVPPotionEffect(PotionEffectType effect)
    {
        return bannedPVPPotionEffects.contains(effect);
    }
}
