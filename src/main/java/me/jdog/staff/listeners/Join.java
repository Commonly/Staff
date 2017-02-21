package me.jdog.staff.listeners;

import me.jdog.murapi.api.Color;
import me.jdog.staff.Vanish;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Muricans on 2/17/17.
 */
public class Join implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (e.getPlayer().hasPermission("staff.staff")) {
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
