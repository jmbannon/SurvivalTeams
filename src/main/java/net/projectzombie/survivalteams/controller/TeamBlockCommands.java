package net.projectzombie.survivalteams.controller;

import net.projectzombie.survivalteams.file.FileWrite;
import static net.projectzombie.survivalteams.controller.CMDText.*;

import net.projectzombie.survivalteams.file.WorldCoordinate;
import net.projectzombie.survivalteams.file.buffers.HitToolBuffer;
import net.projectzombie.survivalteams.file.buffers.BlockBuffer;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Conveyance commands. Although a couple have to be set up to make server work properly.
 *
 */
public class TeamBlockCommands implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length >= 1)
        {
            if (args[0].equals("add") && sender.hasPermission(ADD_PERM))
            {
                boolean isInt = true;
                int health = -1;
                if (args.length == 3)
                {
                    try { health = Integer.parseInt(args[2]); }
                    catch (NumberFormatException e) { isInt = false; }
                    if (isInt && health != -1)
                    {
                        FileWrite.writeSurvivalBlockHealth(args[1], health);
                        sender.sendMessage(ADD_FINALIZE);
                    }
                    else
                        sender.sendMessage(ADD_USAGE);
                }
                else
                    sender.sendMessage(ADD_USAGE);
                return true;
            }
            else if (args[0].equals("br") && sender.hasPermission(BR_PERM))
            {
                boolean isInt = true;
                int radius = -1;
                if (args.length == 2)
                {
                    try { radius = Integer.parseInt(args[1]); }
                    catch (NumberFormatException e) { isInt = false; }
                    if (isInt && radius != -1)
                    {
                        FileWrite.writeDefaultBuildRadius(radius);
                        sender.sendMessage(BR_FINALIZE + radius);
                    }
                    else
                        sender.sendMessage(BR_USAGE);
                }
                else
                    sender.sendMessage(BR_USAGE);
                return true;
            }
            else if (args[0].equals("bn") && sender.hasPermission(BN_PERM))
            {
                boolean bN;
                if (args.length == 2)
                {
                    bN = Boolean.valueOf(args[1]);
                    FileWrite.writeBreakNaturally(bN);
                    sender.sendMessage(BN_FINALIZE + bN);
                }
                else
                    sender.sendMessage(BN_USAGE);
                return true;
            }
            else if (args[0].equals("delay") && sender.hasPermission(DELAY_PERM))
            {
                boolean isInt = true;
                int delay = -1;
                if (args.length == 2)
                {
                    try { delay = Integer.parseInt(args[1]); }
                    catch (NumberFormatException e) { isInt = false; }
                    if (isInt && delay != -1)
                    {
                        FileWrite.writeAttackDelay(delay);
                        sender.sendMessage(DELAY_FINALIZE + delay);
                    }
                    else
                        sender.sendMessage(DELAY_USAGE);
                }
                else
                    sender.sendMessage(DELAY_USAGE);
                return true;
            }
            else if (args[0].equals("tool") && sender.hasPermission(TOOL_PERM))
            {
                if (args.length == 2)
                {
                    Material material = Material.valueOf(args[1]);
                    FileWrite.writeTeamBlockCheckerTool(material);
                    sender.sendMessage(TOOL_FINALIZE + material);
                }
                else
                    sender.sendMessage(TOOL_USAGE);
                return true;
            }
            else if (args[0].equals("hitpoints") && sender.hasPermission(HIT_POINTS_PERM))
            {
                boolean isInt = true;
                int damage = -1;
                if (args.length == 2)
                {
                    try { damage = Integer.parseInt(args[1]); }
                    catch (NumberFormatException e) { isInt = false; }
                    if (isInt && damage != -1)
                    {
                        FileWrite.writeDefaultHitToolDamage(damage);
                        sender.sendMessage(HIT_POINTS_FINALIZE + damage);
                    }
                    else
                        sender.sendMessage(HIT_POINTS_USAGE);
                }
                else
                    sender.sendMessage(HIT_POINTS_USAGE);
                return true;
            }
            else if (args[0].equals("hittool") && sender.hasPermission(HIT_TOOL_PERM))
            {
                boolean isInt = true;
                int damage = 0;
                int durability = 0;
                if (args.length == 4)
                {
                    try
                    {
                        damage = Integer.parseInt(args[2]);
                        durability = Integer.parseInt(args[3]);
                    }
                    catch (NumberFormatException e) { isInt = false; }
                    Material material = Material.valueOf(args[1]);
                    if (isInt)
                    {
                        FileWrite.writeHitTool(material, damage, durability);
                        sender.sendMessage(HIT_TOOL_FINALIZE);
                    }
                    else
                        sender.sendMessage(HIT_TOOL_USAGE);
                }
                else
                    sender.sendMessage(HIT_TOOL_USAGE);
                return true;
            }
            else if (args[0].equals("reload") && sender.hasPermission(RELOAD_PERM))
            {
                BlockBuffer.readInDefaults();
                BlockBuffer.readInPlacedTeamBlocks();
                HitToolBuffer.readInDefaults();
                sender.sendMessage(RELOAD_FINALIZE);
                return true;
            }
            else if (args[0].equals("durability") && sender.hasPermission(DURABILITY_PERM))
            {
                boolean isInt = true;
                int durability = -1;
                if (args.length == 2)
                {
                    try { durability = Integer.parseInt(args[1]); }
                    catch (NumberFormatException e) { isInt = false; }
                    if (isInt && durability != -1)
                    {
                        FileWrite.writeDefaultHitToolDurability(durability);
                        sender.sendMessage(DURABILITY_FINALIZE + durability);
                    }
                    else
                        sender.sendMessage(DURABILITY_USAGE);
                }
                else
                    sender.sendMessage(DURABILITY_USAGE);
                return true;
            }
            else if (args[0].equals("rsb") && sender.hasPermission(RSB_PERM))
            {
                if (args.length == 2)
                {
                    Material material = Material.valueOf(args[1]);
                    FileWrite.wipeSurvivalBlock(material);
                    BlockBuffer.removeSurvivalBlock(material);
                    sender.sendMessage(RSB_FINALIZE);
                }
                else
                    sender.sendMessage(RSB_USAGE);
                return true;
            }
            else if (args[0].equals("rtb") && sender.hasPermission(RTB_PERM))
            {
                if (args.length == 2)
                {
                    FileWrite.wipeTeamBlock(args[1]);
                    BlockBuffer.removeTeamBlock(WorldCoordinate.toLocation(args[1]));
                    BlockBuffer.remove(WorldCoordinate.toLocation(args[1]));
                    sender.sendMessage(RTB_FINALIZE);
                }
                else
                    sender.sendMessage(RTB_USAGE);
                return true;
            }
            else if (args[0].equals("rht") && sender.hasPermission(R_HIT_TOOL_PERM))
            {
                if (args.length == 2)
                {
                    Material material = Material.valueOf(args[1]);
                    FileWrite.wipeHitTool(material);
                    HitToolBuffer.remove(material);
                    sender.sendMessage(RHT_FINALIZE);
                }
                else
                    sender.sendMessage(RHT_USAGE);
                return true;
            }
            else if (args[0].equals("rall") && sender.hasPermission(RALL_PERM))
            {
                FileWrite.wipeTeamBlocks();
                BlockBuffer.clearBuffer();
                sender.sendMessage(RALL_FINALIZE);
                return true;
            }
            else if (args[0].equals("help") && sender.hasPermission(TBHELP_PERM))
            {
                helpForward(sender, TEAM_BLOCK_HELP);
                return true;
            }
        }
        return false;
    }

    public void helpForward(CommandSender sender, String[] helps)
    {
        for (String help : helps)
        {
            sender.sendMessage(help);
        }
    }
}
