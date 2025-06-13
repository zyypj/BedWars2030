package me.zypj.bedwars.system.explosive.fireball.service;

import lombok.Getter;
import me.zypj.bedwars.BedWarsPlugin;
import me.zypj.bedwars.api.event.explosive.FireballUseEvent;
import me.zypj.bedwars.common.file.path.ConfigPath;
import me.zypj.bedwars.common.file.service.ConfigService;
import me.zypj.bedwars.common.logger.Debug;
import me.zypj.bedwars.system.explosive.fireball.adapter.FireballAdapter;
import me.zypj.bedwars.system.explosive.fireball.adapter.provider.FireballAdapterProvider;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
public class FireballService {

    private final BedWarsPlugin plugin;
    private final ConfigService config;
    private final FireballAdapter adapter;
    private final Map<Player, Long> lastUse = new HashMap<>();

    public FireballService(BedWarsPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getPluginBootstrap().getConfigService();
        this.adapter = FireballAdapterProvider.getAdapter();
    }

    public boolean isOnCooldown(Player p) {
        double cdSec = config.getConfigDouble(ConfigPath.FIREBALL_COOLDOWN);
        return lastUse.containsKey(p) && (System.currentTimeMillis() - lastUse.get(p)) < (long) (cdSec * 1000);
    }

    public Fireball launchFireball(Player shooter, Location loc) {
        if (isOnCooldown(shooter)) return null;
        lastUse.put(shooter, System.currentTimeMillis());

        Settings s = loadSettings();

        Fireball fb = adapter.spawnFireball(shooter,
                loc,
                s.kbHorizontal,
                s.kbVertical,
                s.power,
                s.breakBlocks,
                s.makeFire,
                s.dmgSelf,
                s.dmgOthers,
                s.speed);

        FireballUseEvent event = new FireballUseEvent(shooter, loc, fb);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            fb.remove();
            return null;
        }

        Debug.log(String.format("[FireballService] launched fireball by %s at %s", shooter.getName(), loc), true);
        return fb;
    }

    private Settings loadSettings() {
        return new Settings(
                config.getConfigDouble(ConfigPath.FIREBALL_KNOCKBACK_HORIZONTAL),
                config.getConfigDouble(ConfigPath.FIREBALL_KNOCKBACK_VERTICAL),
                (float) config.getConfigDouble(ConfigPath.FIREBALL_EXPLOSION_POWER),
                config.getConfigBoolean(ConfigPath.FIREBALL_EXPLOSION_BREAK_BLOCKS),
                config.getConfigBoolean(ConfigPath.FIREBALL_EXPLOSION_MAKE_FIRE),
                config.getConfigDouble(ConfigPath.FIREBALL_DAMAGE_SELF),
                config.getConfigDouble(ConfigPath.FIREBALL_DAMAGE_OTHERS), 
                config.getConfigDouble(ConfigPath.FIREBALL_SPEED_MULTIPLIER));
    }

    private static class Settings {
        final double kbHorizontal;
        final double kbVertical;
        final float power;
        final boolean breakBlocks;
        final boolean makeFire;
        final double dmgSelf;
        final double dmgOthers;
        final double speed;

        Settings(double kbHorizontal, double kbVertical, float power, boolean breakBlocks, boolean makeFire, double dmgSelf, double dmgOthers, double speed) {
            this.kbHorizontal = kbHorizontal;
            this.kbVertical = kbVertical;
            this.power = power;
            this.breakBlocks = breakBlocks;
            this.makeFire = makeFire;
            this.dmgSelf = dmgSelf;
            this.dmgOthers = dmgOthers;
            this.speed = speed;
        }
    }
}
