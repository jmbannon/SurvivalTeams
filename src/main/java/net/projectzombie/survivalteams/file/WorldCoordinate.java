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
package net.projectzombie.survivalteams.file;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 *
 * @author jb
 */
public class WorldCoordinate
{
    static public String toString(final Block location)
    {
        final StringBuilder stb = new StringBuilder();
        stb.append(location.getWorld().getUID().toString());
        stb.append(",");
        stb.append(location.getX());
        stb.append(",");
        stb.append(location.getY());
        stb.append(",");
        stb.append(location.getZ());
        return stb.toString();
    }
    
    static public Location toLocation(final String worldCoordinate)
    {
        final String split[] = worldCoordinate.split(",");
        return split.length == 4 ? new Location(Bukkit.getWorld(split[0]), Double.valueOf(split[1]), Double.valueOf(split[2]),  Double.valueOf(split[3])) : null;
    }
}