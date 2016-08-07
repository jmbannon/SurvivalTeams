/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.projectzombie.survivalteams.file;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author com.gmail.jbann1994
 */
public class FileContents
{
    protected static File TEAM_FILE;
    protected static FileConfiguration TEAM_YAML;
    protected static Plugin PLUGIN;
    
    public static boolean initialize(final Plugin plugin)
    {
        PLUGIN = plugin;
        loadConfig();
        return saveConfig();
    }
    
    public static boolean onDisable()
    {
        return saveConfig();
    }
    
    /**
     * Loads file from plugin folder.
     */
    protected static void loadConfig()
    {
        if (TEAM_FILE == null)
            TEAM_FILE = new File(PLUGIN.getDataFolder(), "survival_teams.yml");

        TEAM_YAML = new YamlConfiguration();
        TEAM_YAML = YamlConfiguration.loadConfiguration(TEAM_FILE);
        saveConfig();
    }

    /**
     * Saves all changes to file and file configuration.
     * @return 
     */
    protected static boolean saveConfig()
    {
        if (TEAM_FILE == null || TEAM_YAML == null)
            return false;
        
        try
        {
            TEAM_YAML.save(TEAM_FILE);
            return true;
        } catch (IOException e)
        {
            PLUGIN.getLogger().log(Level.SEVERE, "Could not save config to " + TEAM_YAML.getName(), e);
            return false;
        }
    }
}
