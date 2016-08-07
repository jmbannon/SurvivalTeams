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
package net.projectzombie.survivalteams.main;

import static net.projectzombie.survivalteams.controller.CMDText.*;
import net.projectzombie.survivalteams.controller.PlayerCommands;
import net.projectzombie.survivalteams.controller.PlayerListener;
import net.projectzombie.survivalteams.controller.TeamBlockCommands;
import net.projectzombie.survivalteams.file.FileContents;
import net.projectzombie.survivalteams.file.buffers.HitToolBuffer;
import net.projectzombie.survivalteams.file.buffers.BlockBuffer;
import net.projectzombie.survivalteams.file.buffers.TeamBuffer;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Jesse Bannon
 * 
 * Main is used for enabling and disable the plugin on server startup/stop.
 */
public class Main extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        FileContents.initialize(this);
        BlockBuffer.readInDefaults();
        BlockBuffer.readInPlacedTeamBlocks();
        HitToolBuffer.readInDefaults();
        PluginHelpers.loadInHelpers(this, this.getServer().getScheduler());
        TeamBuffer.loadUpSpawns();
        this.getCommand("tb").setExecutor(new TeamBlockCommands());
        this.getCommand("party").setExecutor(new PlayerCommands());
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getLogger().info("SurvivalParties Enabled.");

        // Adding perms
        String[] perms = {CHECKER_PERM, ADD_PERM, BR_PERM, BN_PERM, DELAY_PERM, TOOL_PERM,
                            HIT_TOOL_PERM, HIT_POINTS_PERM, RELOAD_PERM, DURABILITY_PERM,
                            RSB_PERM, RTB_PERM, R_HIT_TOOL_PERM, RALL_PERM, TBHELP_PERM};
        PluginManager pM = getServer().getPluginManager();
        for (String perm : perms)
            pM.addPermission(new Permission(perm));
    }

    @Override
    public void onDisable()
    {
        BlockBuffer.saveTeamBlocks();
        FileContents.onDisable();
        this.getLogger().info("SurvivalParties disabled.");
    }
}
