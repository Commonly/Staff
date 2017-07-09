package me.jdog.staff;

import me.jdog.murapi.api.Color;
import me.jdog.murapi.api.cmd.CMD;
import me.jdog.murapi.api.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * Created by Muricans on 2/16/17.
 */
public class StaffMode implements CMD {
    private static List<String> staffList;
    private Core core;

    public StaffMode(Core core) {
        this.core = core;
    }

    public static List<String> getStaffList() {
        return staffList;
    }

    @Override
    public String getName() {
        return "staffmode";
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        Config staff = new Config(core, "staff.yml");
        staffList = staff.getStringList("toggled");
        if (!(sender instanceof Player)) return true;
        if (!staffList.contains(sender.getName())) {
            staffList.add(sender.getName());
            staff.set("toggled", staffList);

            // init staffmode
            Player player = (Player) sender;
            staff.set("inventories." + player.getName() + ".items", player.getInventory().getContents());
            staff.set("inventories." + player.getName() + ".armor", player.getInventory().getArmorContents());
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            staff.set("data." + player.getUniqueId().toString() + ".gamemode", player.getGameMode().toString().toUpperCase());
            player.setGameMode(GameMode.CREATIVE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 50000, 13));
            if (!Vanish.getVanished().contains(player.getName())) {
                player.performCommand("vanish");
            }

            ItemStack wand = new ItemStack(Material.WOOD_AXE);
            ItemMeta wandMeta = wand.getItemMeta();
            if (!player.hasPermission("worldedit.*")) {
                wandMeta.setDisplayName(Color.addColor("&dWorldEdit Wand : &4&lNO PERMISSION"));
            } else {
                wandMeta.setDisplayName(Color.addColor("&dWorldEdit Wand"));
            }
            wand.setItemMeta(wandMeta);

            ItemStack compass = new ItemStack(Material.COMPASS);
            ItemMeta compassMeta = compass.getItemMeta();
            if (!player.hasPermission("worldedit.*")) {
                compassMeta.setDisplayName(Color.addColor("&dTeleporter : &4&lNO PERMISSION"));
            } else {
                compassMeta.setDisplayName(Color.addColor("&dTeleporter"));
            }
            compass.setItemMeta(compassMeta);

            ItemStack book = new ItemStack(Material.BOOK);
            ItemMeta bookMeta = book.getItemMeta();
            bookMeta.setDisplayName(Color.addColor("&7View Player Inventory"));
            book.setItemMeta(bookMeta);

            ItemStack feather = new ItemStack(Material.FEATHER);
            ItemMeta featherMeta = feather.getItemMeta();
            featherMeta.setDisplayName(Color.addColor("&7Toggle Vanish"));
            feather.setItemMeta(featherMeta);

            ItemStack clock = new ItemStack(Material.WATCH);
            ItemMeta clockMeta = clock.getItemMeta();
            clockMeta.setDisplayName(Color.addColor("&6Random Teleport"));
            clock.setItemMeta(clockMeta);

            ItemStack ice = new ItemStack(Material.PACKED_ICE);
            ItemMeta iceMeta = ice.getItemMeta();
            iceMeta.setDisplayName(Color.addColor("&bFreeze Player"));
            ice.setItemMeta(iceMeta);

            player.getInventory().setItem(3, clock);
            player.getInventory().setItem(8, ice);
            player.getInventory().setItem(0, compass);
            player.getInventory().setItem(1, wand);
            player.getInventory().setItem(7, book);
            player.getInventory().setItem(6, feather);

            player.sendMessage(Color.addColor("staffmode.enable", core));
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (pl.hasPermission("staff.staff")) {
                    pl.sendMessage(Color.addColor("staffmode.enable-everyone", core).replace("$sender", sender.getName()));
                }
            }
            staff.save();
            return true;
        } else if (staffList.contains(sender.getName())) {
            staffList.remove(sender.getName());
            staff.set("toggled", staffList);
            staff.save();

            // disable staffmode
            List<ItemStack> inventory = (List<ItemStack>) staff.get("inventories." + sender.getName() + ".items");
            List<ItemStack> armor = (List<ItemStack>) staff.get("inventories." + sender.getName() + ".armor");
            Player player = (Player) sender;
            player.getInventory().clear();
            player.getInventory().setContents(inventory.toArray(new ItemStack[inventory.size()]));
            player.getInventory().setArmorContents(armor.toArray(new ItemStack[armor.size()]));
            player.sendMessage(Color.addColor("staffmode.disable", core));
            player.setGameMode(GameMode.valueOf(staff.getString("data." + player.getUniqueId().toString() + ".gamemode")));
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            if (Vanish.getVanished().contains(player.getName())) {
                player.performCommand("vanish");
            }
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (pl.hasPermission("staff.staff")) {
                    pl.sendMessage(Color.addColor("staffmode.disable-everyone", core).replace("$sender", sender.getName()));
                }
            }
            staff.save();
            return true;
        }
        return false;
    }
}
