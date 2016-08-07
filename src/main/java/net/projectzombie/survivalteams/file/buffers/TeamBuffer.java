/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.projectzombie.survivalteams.file.buffers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import net.projectzombie.survivalteams.team.Team;

/**
 *
 * @author com.gmail.jbann1994
 */
public class TeamBuffer
{
    protected static final HashMap<String, Team> ONLINE_TEAMS = new HashMap<>();
    
    
    public static ArrayList<Team> getAll()
    {
        final ArrayList<Team> sortedTeams = new ArrayList<>();
        final Collection<Team> teams = ONLINE_TEAMS.values();
        
        for (Team team : teams)
            sortedTeams.add(team);
        
        Collections.sort(sortedTeams);
        return sortedTeams;
    }
    
    
    public static Team get(final String teamName)
    {
        return ONLINE_TEAMS.get(teamName);
    }
    
    static public void add(final Team team)
    {
        ONLINE_TEAMS.put(team.getName(), team);
    }
    
    static public void remove(final Team team)
    {
        ONLINE_TEAMS.remove(team.getName());
    }
    
    static public boolean contains(final String teamName)
    {
        return ONLINE_TEAMS.containsKey(teamName);
    }
}
