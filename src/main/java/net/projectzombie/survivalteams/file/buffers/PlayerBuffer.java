/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.projectzombie.survivalteams.file.buffers;

import java.util.HashMap;
import java.util.UUID;
import net.projectzombie.survivalteams.player.TeamPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author com.gmail.jbann1994
 */
public class PlayerBuffer
{

    private static final HashMap<UUID, TeamPlayer> ONLINE_PLAYERS   = new HashMap<>();
    
    private PlayerBuffer() { /* Do not use. */ }
    
    public static void add(final TeamPlayer player)
    {
        ONLINE_PLAYERS.put(player.getUUID(), player);
    }

    public static void remove(final UUID uuid)
    {
        ONLINE_PLAYERS.remove(uuid);
    }

    public static TeamPlayer get(final UUID uuid)
    {
        final TeamPlayer tp = ONLINE_PLAYERS.get(uuid);
        return tp.isOnline() ? tp : null;
    }
    
    public static boolean contains(final UUID uuid)
    {
        return ONLINE_PLAYERS.containsKey(uuid);
    }
    
    static public Location getSpawnLocation(final Player player)
    {
        final TeamPlayer tp = get(player.getUniqueId());
        if (tp != null && tp.hasTeam())
            return tp.getTeam().getSpawn();
        else
            return null;
    }
    
}
