package me.jdog.staff;

import me.jdog.murapi.api.Color;
import me.jdog.murapi.api.cmd.CMD;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by Muricans on 2/16/17.
 */
public class Vanish implements CMD {
    private static ArrayList<String> vanished = new ArrayList<>();

    private Core core;

    public Vanish(Core core) {
        this.core = core;
    }

    public static ArrayList<String> getVanished() {
        return vanished;
    }

    @Override
    public String getName() {
        return "vanish";
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        if (args.length == 0) {
            if (vanished.contains(sender.getName())) {
                vanished.remove(sender.getName());
                sender.sendMessage(Color.addColor("vanish.unvanished", core));
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    if (pl.hasPermission("staff.staff")) {
                        pl.sendMessage(Color.addColor("vanish.unvanish-everyone", core).replace("$sender", sender.getName()));
                    }
                }
                Player player = (Player) sender;
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    pl.showPlayer(player);
                }
                return true;
            } else if (!vanished.contains(sender.getName())) {
                vanished.add(sender.getName());
                sender.sendMessage(Color.addColor("vanish.vanished", core));
                Player player = (Player) sender;
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    if (pl.hasPermission("staff.staff")) {
                        pl.showPlayer(player);
                    } else if (!pl.hasPermission("staff.staff")) {
                        pl.hidePlayer(player);
                    }
                }
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    if (pl.hasPermission("staff.staff")) {
                        pl.sendMessage(Color.addColor("vanish.vanish-everyone", core).replace("$sender", sender.getName()));
                    }
                }
            }
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(Color.addColor("vanish.offline", core).replace("$target", args[0]));
            return true;
        }

        if (vanished.contains(target.getName())) {
            vanished.remove(target.getName());
            target.sendMessage(Color.addColor("vanish.unvanished", core));
            sender.sendMessage(Color.addColor("vanish.unvanished-other", core).replace("$target", target.getName()));
            for (Player pl : Bukkit.getOnlinePlayers()) {
                pl.showPlayer(target);
            }
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (pl.hasPermission("staff.staff")) {
                    pl.sendMessage(Color.addColor("vanish.unvanished-other-everyone", core).replace("$sender", sender.getName()).replace("$target", target.getName()));
                }
            }
            return true;
        } else if (!vanished.contains(target.getName())) {
            vanished.add(target.getName());
            target.sendMessage(Color.addColor("vanish.vanished", core));
            sender.sendMessage(Color.addColor("vanish.vanished-other", core).replace("$target", target.getName()));
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (pl.hasPermission("staff.staff")) {
                    pl.showPlayer(target);
                } else if (!pl.hasPermission("staff.staff")) {
                    pl.hidePlayer(target);
                }
            }
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (pl.hasPermission("staff.staff")) {
                    pl.sendMessage(Color.addColor("vanish.vanished-other-everyone", core).replace("$sender", sender.getName()).replace("$target", target.getName()));
                }
            }
            return true;
        }
        return false;
    }
}
