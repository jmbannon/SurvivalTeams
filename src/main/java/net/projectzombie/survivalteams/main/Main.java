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

import net.projectzombie.survivalteams.controller.PlayerCommands;
import net.projectzombie.survivalteams.controller.PlayerListener;
import net.projectzombie.survivalteams.file.FileContents;
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
        this.getCommand("party").setExecutor(new PlayerCommands());
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getLogger().info("SurvivalParties Enabled.");
        
    }

    @Override
    public void onDisable()
    {
        FileContents.onDisable();
        this.getLogger().info("SurvivalParties disabled.");
    }
}
