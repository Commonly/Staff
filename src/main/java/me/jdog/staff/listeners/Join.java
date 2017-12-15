package me.jdog.staff.listeners;

import me.jdog.murapi.api.Color;
import me.jdog.murapi.api.config.Config;
import me.jdog.murapi.api.fetch.UUIDFetcher;
import me.jdog.staff.Core;
import me.jdog.staff.Vanish;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;

/**
 * Created by Muricans on 2/17/17.
 */
public class Join implements Listener {

    private Core core;

    public Join(Core core) {
        this.core = core;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Config staff = new Config(core, "staff.yml");
        UUIDFetcher fetch = new UUIDFetcher(e.getPlayer().getName());
        if (e.getPlayer().hasPermission("staff.staff") && staff.getBoolean("data." + fetch.getUUIDAsString() + ".vanishonjoin")) {
            Vanish.getVanished().add(e.getPlayer().getName());
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (!pl.hasPermission("staff.staff")) {
                    pl.hidePlayer(e.getPlayer());
                }
            }
            e.getPlayer().sendMessage(Color.addColor("&bYou joined vanished."));
        }
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (Vanish.getVanished().contains(pl.getName())) {
                if (!e.getPlayer().hasPermission("staff.staff")) {
                    e.getPlayer().hidePlayer(pl);
                }
            }
        }
    }

}
