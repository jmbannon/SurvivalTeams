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
package net.projectzombie.survivalteams.controller;

/**
 *
 * @author jb
 */
public class CMDText
{
    public static String[]
    COMMAND = new String[] { "party", "p"},
    
    ARG_INVITE = new String[]  { "invite",  "inv",    "i" },
    ARG_KICK   = new String[]  { "kick",    "boot",   "k" },
    ARG_CREATE = new String[]  { "create",  "new",    "c" },
    ARG_ACCEPT = new String[]  { "accept",            "a" },
    ARG_PROMOTE = new String[] { "promote", "raise",  "p" },
    ARG_DEMOTE = new String[]  { "demote",  "d" },
    ARG_LEAVE  = new String[]  { "leave",   "quit",   "q", "l" },
    ARG_DISBAND = new String[] { "disband" },
    ARG_ONLINE = new String[]  { "online", "o" },
    ARG_INFO   = new String[]  { "info" },
    ARG_SETBASE = new String[] { "setbase", "set" },
    ARG_BASE    = new String[] { "base", "spawn" },
    ARG_LIST   = new String[]  { "list" },
    ARG_HELP   = new String[]  { "help",    "commands" },
            
    ARG_REMOVE_SPAWNS   = new String[]  { "removeAllSpawns" };

    public static final String
            ADD_USAGE = "/tb add <Type> <Health>",
            BR_USAGE = "/tb br <radius size>",
            BN_USAGE = "/tb bn <true:false>",
            DELAY_USAGE = "/tb delay <time>",
            TOOL_USAGE = "/tb tool <Material>",
            HIT_TOOL_USAGE = "/tb hittool <material> <damage> <durability>",
            HIT_POINTS_USAGE = "/tb damage <damage>",
            DURABILITY_USAGE = "/tb durability <durability>",
            RSB_USAGE = "/tb sb <Material>",
            RTB_USAGE = "/tb rtb <ID>",
            RHT_USAGE = "/tb rht <Material",

            ADD_FINALIZE = "Default type added.",
            BR_FINALIZE = "Build radius set to ",
            BN_FINALIZE = "Break naturally set to ",
            DELAY_FINALIZE = "Delay set to ",
            TOOL_FINALIZE = "tb tool set to ",
            HIT_TOOL_FINALIZE = "Hit tool added.",
            HIT_POINTS_FINALIZE = "Default hit points set to ",
            RELOAD_FINALIZE = "Survival Blocks and Survival Weapons reloaded.",
            DURABILITY_FINALIZE = "Default durability set to ",
            RSB_FINALIZE = "Removed default tb.",
            RTB_FINALIZE = "Removed tb.",
            RHT_FINALIZE = "Removed SW.",
            RALL_FINALIZE = "All tb have been removed.",

            CHECKER_PERM = "TB.checker",
            ADD_PERM = "TB.add",
            BR_PERM = "TB.br",
            BN_PERM = "TB.bn",
            DELAY_PERM = "TB.delay",
            TOOL_PERM = "TB.tool",
            HIT_TOOL_PERM = "TB.ht",
            HIT_POINTS_PERM = "TB.hitpoints",
            RELOAD_PERM = "TB.reload",
            DURABILITY_PERM = "TB.durability",
            RSB_PERM = "TB.rdtp",
            RTB_PERM = "TB.rtb",
            R_HIT_TOOL_PERM = "TB.rht",
            RALL_PERM = "TB.rall",
            TBHELP_PERM = "TB.help";

    public static final String[] TEAM_BLOCK_HELP = {
                "add \t allows adding of default block types.",
                "br \t Build radius, how far players can build from spawn flag. (default 0)",
                "bn \t Break naturally, if drops occur. (default false)",
                "delay \t How often zombies damage blocks when chasing a human. (default 10)",
                "tool \t Sets the tool for checking block stats. (no default)",
                "hitpoints \t Default damage. (default 0)",
                "hittool \t Allows specification of a blocks damage from the item and the ",
                "\tdurability decrease of a hit.",
                "durability \t Default item durability decrease on hit. (default 0)",
                "rsb \t Removes a default tb from disc.",
                "rtb \t Removes a tb from disc.",
                "rht \t Removes a S weapon.",
                "rall \t Removes all tbs."};
            
}
