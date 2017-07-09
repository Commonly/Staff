package me.jdog.staff;

import me.jdog.murapi.api.Color;
import me.jdog.murapi.api.cmd.CMD;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Muricans on 2/16/17.
 */
public class Help implements CMD {
    private Core core;

    private HashMap<String, Integer> cooldown = new HashMap<>();
    private HashMap<String, BukkitRunnable> cooldownTask = new HashMap<>();

    public Help(Core core) {
        this.core = core;
    }

    @Override
    public String getName() {
        return "helpop";
    }

    @Override
    public boolean execute(CommandSender cSender, Command command, String label, String[] args) {
        final CommandSender sender = cSender;
        if (cooldown.containsKey(sender.getName())) {
            sender.sendMessage(Color.addColor("help.cooldown-message", core).replace("$cooldown", cooldown.get(sender.getName()).toString()));
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(Color.addColor("help.arguments", core));
            return true;
        }
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (pl.hasPermission("staff.staff")) {
                String message = StringUtils.join(args, ' ', 0, args.length);
                String msg = Color.strip(message);
                List<String> received = core.getConfig().getStringList("help.received");
                for (String receive : received) {
                    String rCon = receive.replace("$sender", sender.getName()).replace("$reason", msg);
                    pl.sendMessage(Color.addColor(rCon));
                }

            }
        }
        sender.sendMessage(Color.addColor("help.sent", core).replace("$reason", StringUtils.join(args, ' ', 0, args.length)));
        cooldown.put(sender.getName(), core.getConfig().getInt("help.cooldown"));
        cooldownTask.put(sender.getName(), new BukkitRunnable() {
            @Override
            public void run() {
                cooldown.put(sender.getName(), cooldown.get(sender.getName()) - 1);
                if (cooldown.get(sender.getName()) == 0) {
                    cooldown.remove(sender.getName());
                    cooldownTask.remove(sender.getName());
                    cancel();
                }
            }
        });

        cooldownTask.get(sender.getName()).runTaskTimer(core, 20, 20);
        return false;
    }
}
