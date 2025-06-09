package me.zypj.bedwars.api.logger;

import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Debug {

    @Setter
    private static JavaPlugin plugin;

    public static void log(String message, boolean debug) {
        message = ChatColor.translateAlternateColorCodes('&', message);

        if (debug) {
            if (plugin.getConfig().getBoolean("debug", false)) {
                Bukkit.getServer().getConsoleSender().sendMessage("§f§l[BedWars2030]§8§l [DEBUG]§f " + message);
            }

            return;
        }

        Bukkit.getServer().getConsoleSender().sendMessage("§f§l[BedWars2030]§f " + message);
    }
}
