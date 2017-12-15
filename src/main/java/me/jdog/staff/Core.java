package me.jdog.staff;

import me.jdog.murapi.api.Color;
import me.jdog.murapi.api.cmd.CMDManager;
import me.jdog.murapi.api.config.Config;
import me.jdog.murapi.api.logger.LogType;
import me.jdog.murapi.api.logger.Logger;
import me.jdog.staff.listeners.Frozen;
import me.jdog.staff.listeners.ItemClick;
import me.jdog.staff.listeners.Join;
import me.jdog.staff.listeners.Leave;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public final class Core extends JavaPlugin {

    @Override
    public void onEnable() {
        // debug (if any errors happen)
        PluginDescriptionFile pdf = getDescription();
        getServer().getConsoleSender().sendMessage(Color.addColor("&aStaff v" + pdf.getVersion() + " has been enabled"));
        Config staff = new Config(this,"staff.yml");
        staff.create();
        CMDManager.registerCommand(new StaffMode(this), this);
        CMDManager.registerCommand(new Vanish(this), this);
        CMDManager.registerCommand(new Teleport(this), this);
        CMDManager.registerCommand(new SS(this), this);
        CMDManager.registerCommand(new Help(this), this);
        CMDManager.registerCommand(new TPHere(this), this);
        getServer().getPluginManager().registerEvents(new ItemClick(this), this);
        getServer().getPluginManager().registerEvents(new Leave(), this);
        getServer().getPluginManager().registerEvents(new Frozen(this), this);
        getServer().getPluginManager().registerEvents(new Join(this), this);

        if (!isMurApiInstalled()) {
            getServer().getPluginManager().disablePlugin(this);
        } else {
            Logger logger = Logger.getLogger();
            logger.log(LogType.DEBUG, "Successfully hooked into staffLib (Staff)");
        }

        //config
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // debug (if any errors happen)
        PluginDescriptionFile pdf = getDescription();
        getServer().getConsoleSender().sendMessage(Color.addColor("&cStaff v" + pdf.getVersion() + " has been disabled"));
    }

    private boolean isMurApiInstalled() {
        boolean found = false;
        if (getServer().getPluginManager().getPlugin("murAPI") == null) {
            found = false;
        } else if (getServer().getPluginManager().getPlugin("murAPI") != null) {
            found = true;
        }
        return found;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("stafflib")) {
            if (args.length == 0) {
                sender.sendMessage(Color.addColor("&cERROR:&r Not enough arguments. /stafflib <reload|version|spigot>"));
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                Config staff = new Config(this, "staff.yml");
                staff.reload();
                reloadConfig();
                sender.sendMessage(Color.addColor("&6core:&r reload completed &cplease note this might not have reloaded everything if you didn't save!"));
                return true;
            } else if (args[0].equalsIgnoreCase("version")) {
                sender.sendMessage(Color.addColor("&6staffLib (Staff) version:&r " + getDescription().getVersion()));
                return true;
            } else if (args[0].equalsIgnoreCase("spigot")) {
                sender.sendMessage(Color.addColor("&6core:&r Spigot link: &dhttps://www.spigotmc.org/resources/staff.43580/"));
                return true;
            } else {
                sender.sendMessage(Color.addColor("&cERROR:&r Invalid arguments. /stafflib <reload|version|spigot>"));
            }

        }
        return false;
    }
}
