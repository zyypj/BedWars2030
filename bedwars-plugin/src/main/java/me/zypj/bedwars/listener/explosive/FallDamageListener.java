package me.zypj.bedwars.listener.explosive;

import me.zypj.bedwars.BedWarsPlugin;
import me.zypj.bedwars.common.file.path.ConfigPath;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class FallDamageListener implements Listener {

    private final BedWarsPlugin plugin;

    public FallDamageListener(BedWarsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;

        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) return;
        Player player = (Player) entity;

        String meta =
                player.hasMetadata("fall-fireball") ? "fall-fireball" :
                player.hasMetadata("fall-tnt") ? "fall-tnt" : null;
        if (meta == null) return;

        double multiplier = meta.equals("fall-fireball")
                ? plugin.getPluginBootstrap().getFireballService()
                .getConfig().getConfigDouble(ConfigPath.FIREBALL_FALL_DAMAGE_MULTIPLIER)
                : plugin.getPluginBootstrap().getTntService()
                .getConfig().getConfigDouble(ConfigPath.TNT_FALL_DAMAGE_MULTIPLIER);

        event.setDamage(event.getDamage() * multiplier);
        player.removeMetadata(meta, plugin);
    }
}
