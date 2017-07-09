package me.jdog.staff.listeners;

import me.jdog.murapi.api.Color;
import me.jdog.staff.Core;
import me.jdog.staff.SS;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

/**
 * Created by Muricans on 2/16/17.
 */
public class Frozen implements Listener {

    private Core core;

    public Frozen(Core core) {
        this.core = core;
    }

    @EventHandler
    public void event(PlayerMoveEvent e) {
        if (getFrozen().contains(e.getPlayer().getName())) {
            e.setTo(e.getFrom());
            e.getPlayer().sendMessage(Color.addColor("frozen.moved", core));
        }
    }

    @EventHandler
    public void event2(PlayerQuitEvent e) {
        if (getFrozen().contains(e.getPlayer().getName())) {
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (pl.hasPermission("staff.staff")) {
                    for(String lm : core.getConfig().getStringList("frozen.logger")) {
                        pl.sendMessage(Color.addColor(lm).replace("$logger", e.getPlayer().getName()));
                    }
                }
            }
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), core.getConfig().getString("frozen.command-logger").replace("$logger", e.getPlayer().getName()));
            getFrozen().remove(e.getPlayer().getName());
        }
    }

    private List<String> getFrozen() {
        return SS.getFrozen();
    }

}
