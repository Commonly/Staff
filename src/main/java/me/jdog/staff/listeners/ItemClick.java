package me.jdog.staff.listeners;

import me.jdog.murapi.api.Color;
import me.jdog.murapi.api.config.Config;
import me.jdog.staff.Core;
import me.jdog.staff.SS;
import me.jdog.staff.Vanish;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Muricans on 2/16/17.
 */
public class ItemClick implements Listener {

    private static ArrayList<String> players = new ArrayList<>();
    private Core core;

    public ItemClick(Core core) {
        this.core = core;
    }

    static ArrayList<String> getPlayers() {
        return players;
    }

    @EventHandler
    public void event(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (getStaffList().contains(e.getPlayer().getName())) {
                if (e.getMaterial().equals(Material.WATCH)) {
                    for (Player pl : Bukkit.getOnlinePlayers()) {
                        if (!players.contains(pl.getName())) {
                            players.add(pl.getName());
                        }
                    }

                    Player x = Bukkit.getPlayer(players.get(new Random().nextInt(players.size())));
                    Location loc = x.getLocation();
                    e.getPlayer().teleport(loc);
                    e.getPlayer().sendMessage(Color.addColor("staffitems.randomteleport", core).replace("$player", x.getName()));
                }

                if (e.getMaterial().equals(Material.FEATHER)) {
                    e.getPlayer().performCommand("vanish");
                }

            }
        }

        if(!e.getPlayer().isSneaking() && (e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            if (getStaffList().contains(e.getPlayer().getName())) {
                if(core.getConfig().getBoolean("silent-chest-viewing.enabled")
                        && e.getPlayer().hasPermission(core.getConfig().getString("silent-chest-viewing.permission"))) {
                    Inventory inven = null;
                    switch (e.getClickedBlock().getType()) {
                        case CHEST:
                            Chest chest = (Chest) e.getClickedBlock().getState();
                            inven = core.getServer().createInventory(e.getPlayer(), chest.getInventory().getSize());
                            inven.setContents(chest.getInventory().getContents());
                            e.setCancelled(true);
                            e.getPlayer().openInventory(inven);
                            //chest.getBlockInventory().setContents(inven.getContents());
                            break;
                        case TRAPPED_CHEST:
                            Chest trapChest = (Chest) e.getClickedBlock().getState();
                            inven = core.getServer().createInventory(e.getPlayer(), trapChest.getInventory().getSize());
                            inven.setContents(trapChest.getInventory().getContents());
                            e.setCancelled(true);
                            e.getPlayer().openInventory(inven);
                            //trapChest.getBlockInventory().setContents(inven.getContents());
                            break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void event2(PlayerInteractEntityEvent e) {
        if (getStaffList().contains(e.getPlayer().getName())) {
            if (e.getRightClicked() instanceof Player) {
                if (e.getPlayer().getItemInHand().getType().equals(Material.BOOK)) {
                    Player clicked = (Player) e.getRightClicked();
                    Inventory playerInventory = clicked.getInventory();
                    e.getPlayer().openInventory(playerInventory);
                    e.getPlayer().sendMessage(Color.addColor("staffitems.invsee", core).replace("$clicked", clicked.getName()));
                    clicked.getInventory().setContents(playerInventory.getContents());
                }
                if (e.getPlayer().getItemInHand().getType().equals(Material.PACKED_ICE)) {
                    Player clicked = (Player) e.getRightClicked();
                    e.getPlayer().performCommand("screenshare " + clicked.getName());
                }
            }
        }
    }

    @EventHandler
    public void event3(BlockBreakEvent e) {
        if (getStaffList().contains(e.getPlayer().getName()) &&!e.getPlayer().hasPermission("staff.edit")) {
            e.getPlayer().sendMessage(Color.addColor("staffother.editterrain", core));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void event4(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (getStaffList().contains(player.getName()) && !player.hasPermission("staff.edit")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void event5(BlockPlaceEvent e) {
        if (getStaffList().contains(e.getPlayer().getName()) &&!e.getPlayer().hasPermission("staff.edit")) {
            e.getPlayer().sendMessage(Color.addColor("staffother.editterrain", core));
            e.setCancelled(true);
        }

        if (getStaffList().contains(e.getPlayer().getName()) &&e.getBlock().getType().equals(Material.PACKED_ICE)) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void event6(PlayerDropItemEvent e) {
        if (getStaffList().contains(e.getPlayer().getName())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void event7(PlayerPickupItemEvent e) {
        if (getStaffList().contains(e.getPlayer().getName())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void event8(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player player = (Player) e.getDamager();
            if (getStaffList().contains(player.getName())) {
                e.setCancelled(true);
            }
            if(SS.getFrozen().contains(player.getName())) {
                e.setCancelled(true);
            }
        }
    }

    private List<String> getStaffList() {
        Config staff = new Config(core, "staff.yml");
        List<String> staffList = staff.getFile().getStringList("toggled");
        return staffList;
    }

}
