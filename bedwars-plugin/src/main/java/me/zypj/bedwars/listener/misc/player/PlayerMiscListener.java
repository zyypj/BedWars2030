package me.zypj.bedwars.listener.misc.player;

import lombok.RequiredArgsConstructor;
import me.zypj.bedwars.BedWarsPlugin;
import me.zypj.bedwars.common.file.path.ConfigPath;
import me.zypj.bedwars.common.logger.Debug;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

@RequiredArgsConstructor
public class PlayerMiscListener implements Listener {

    private final BedWarsPlugin plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!e.getPlayer().hasPermission("bw.setup")) return;
        if (plugin.getServer().getSpawnRadius() == 0) return;

        Debug.log("§cChange the §lspawn-protection §csetting §lin server.properties §cto 0!", false);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> e.getPlayer().sendMessage("§cChange the §lspawn-protection §csetting §lin server.properties §cto 0!"), 10L);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (!plugin.getConfig().getBoolean(ConfigPath.PLAYER_HUNGRY))  {
            e.setFoodLevel(20);
            e.setCancelled(true);
        }
    }
}
