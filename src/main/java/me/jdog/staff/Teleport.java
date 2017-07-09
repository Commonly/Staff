package me.jdog.staff;

import me.jdog.murapi.api.Color;
import me.jdog.murapi.api.cmd.CMD;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Muricans on 2/16/17.
 */
public class Teleport implements CMD {

    private Core core;

    public Teleport(Core core) {
        this.core = core;
    }

    @Override
    public String getName() {
        return "teleport";
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        if (args.length == 0) {
            sender.sendMessage(Color.addColor("teleport.arguments-tp", core));
            return true;
        }
        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(Color.addColor("teleport.offline", core).replace("$target", args[0]));
                return true;
            }
            Player player = (Player) sender;
            player.teleport(target.getLocation());
            player.sendMessage(Color.addColor("teleport.tp", core).replace("$target", target.getName()));
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (pl.hasPermission("staff.staff")) {
                    pl.sendMessage(Color.addColor("teleport.tp-everyone", core).replace("$sender", sender.getName()).replace("$target", target.getName()));
                }
            }
            return true;
        }
        if (!sender.hasPermission("staff.tpother")) {
            sender.sendMessage(Color.addColor("&cERROR:&r You don't have permission to teleport players to other players!"));
        }
        Player target = Bukkit.getPlayer(args[0]);
        Player othertarget = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(Color.addColor("teleport.offline", core).replace("$target", args[0]));
            return true;
        }
        if (othertarget == null) {
            sender.sendMessage(Color.addColor("teleport.offline", core).replace("$target", args[1]));
            return true;
        }
        target.teleport(othertarget.getLocation());
        sender.sendMessage(Color.addColor("teleport.tp-other", core).replace("$target", target.getName()).replace("$othertarget", othertarget.getName()));
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (pl.hasPermission("staff.staff")) {
                pl.sendMessage(Color.addColor("teleport.tp-other-everyone", core).replace("$sender", sender.getName()).replace("$target", target.getName()).replace("$othertarget", othertarget.getName()));
            }
        }
        return false;
    }
}
