package me.jdog.staff.listeners;

import me.jdog.staff.Vanish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Muricans on 2/16/17.
 */
public class Leave implements Listener {

    @EventHandler
    public void event(PlayerQuitEvent e) {
        ItemClick.getPlayers().remove(e.getPlayer().getName());
        if (Vanish.getVanished().contains(e.getPlayer().getName())) {
            Vanish.getVanished().remove(e.getPlayer().getName());
        }
    }

}
