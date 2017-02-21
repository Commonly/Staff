package me.jdog.staff;

import me.jdog.murapi.api.Color;
import me.jdog.murapi.api.cmd.CMD;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Muricans on 2/17/17.
 */
public class TPHere implements CMD {

    private Core core;

    public TPHere(Core core) {
        this.core = core;
    }

    @Override
    public String getName() {
        return "teleporthere";
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        if (args.length == 0) {
            sender.sendMessage(Color.addColor("teleport.arguments-tphere", core));
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(Color.addColor("teleport.offline", core).replace("$target", args[0]));
            return true;
        }
        Player player = (Player) sender;
        Location loc = player.getLocation();
        target.teleport(loc);
        sender.sendMessage(Color.addColor("teleport.tphere", core).replace("$target", target.getName()));
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (pl.hasPermission("staff.staff")) {
                pl.sendMessage(Color.addColor("teleport.tphere-everyone", core).replace("$sender", sender.getName()).replace("$target", target.getName()));
            }
        }
        return false;
    }
}
