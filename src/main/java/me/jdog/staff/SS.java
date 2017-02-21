package me.jdog.staff;

import me.jdog.murapi.api.Color;
import me.jdog.murapi.api.cmd.CMD;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by Muricans on 2/16/17.
 */
public class SS implements CMD {
    private static ArrayList<String> frozen = new ArrayList<>();

    private Core core;

    public SS(Core core) {
        this.core = core;
    }

    public static ArrayList<String> getFrozen() {
        return frozen;
    }

    @Override
    public String getName() {
        return "screenshare";
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Color.addColor("frozen.arguments", core));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(Color.addColor("frozen.offline", core).replace("$target", args[0]));
            return true;
        }

        if (frozen.contains(target.getName())) {
            frozen.remove(target.getName());
            target.setGameMode(GameMode.SURVIVAL);
            target.sendMessage(Color.addColor("frozen.unfreezed", core));
            sender.sendMessage(Color.addColor("frozen.unfreezed-sender", core).replace("$target", target.getName()));
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (pl.hasPermission("staff.staff")) {
                    pl.sendMessage(Color.addColor("frozen.unfreezed-everyone", core).replace("$sender", sender.getName()).replace("$target", target.getName()));
                }
            }
            return true;
        } else if (!frozen.contains(target.getName())) {
            frozen.add(target.getName());
            target.setGameMode(GameMode.ADVENTURE);
            target.sendMessage(Color.addColor("frozen.freezed", core));
            sender.sendMessage(Color.addColor("frozen.freezed-sender", core).replace("$target", target.getName()));
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (pl.hasPermission("staff.staff")) {
                    pl.sendMessage(Color.addColor("frozen.freezed-everyone", core).replace("$sender", sender.getName()).replace("$target", target.getName()));
                }
            }
            return true;
        }

        return false;
    }
}
