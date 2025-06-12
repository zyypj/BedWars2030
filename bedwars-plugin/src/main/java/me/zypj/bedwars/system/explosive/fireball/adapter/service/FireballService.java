package me.zypj.bedwars.system.explosive.fireball.service;

import lombok.Getter;
import me.zypj.bedwars.BedWarsPlugin;
import me.zypj.bedwars.common.file.path.ConfigPath;
import me.zypj.bedwars.common.file.service.ConfigService;
import me.zypj.bedwars.common.logger.Debug;
import me.zypj.bedwars.system.explosive.fireball.adapter.FireballAdapter;
import me.zypj.bedwars.system.explosive.fireball.adapter.provider.FireballAdapterProvider;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.Location;

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
        return lastUse.containsKey(p)
                && (System.currentTimeMillis() - lastUse.get(p)) < (long) (cdSec * 1000);
    }

    public Fireball launchFireball(Player shooter, Location loc) {
        if (isOnCooldown(shooter)) return null;
        lastUse.put(shooter, System.currentTimeMillis());

        double kbH = config.getConfigDouble(ConfigPath.FIREBALL_KNOCKBACK_HORIZONTAL);
        double kbV = config.getConfigDouble(ConfigPath.FIREBALL_KNOCKBACK_VERTICAL);
        float power = (float) config.getConfigDouble(ConfigPath.FIREBALL_EXPLOSION_POWER);
        boolean breakBlocks = config.getConfigBoolean(ConfigPath.FIREBALL_EXPLOSION_BREAK_BLOCKS);
        boolean makeFire = config.getConfigBoolean(ConfigPath.FIREBALL_EXPLOSION_MAKE_FIRE);
        double dmgSelf = config.getConfigDouble(ConfigPath.FIREBALL_DAMAGE_SELF);
        double dmgOthers = config.getConfigDouble(ConfigPath.FIREBALL_DAMAGE_OTHERS);
        double speed = config.getConfigDouble(ConfigPath.FIREBALL_SPEED_MULTIPLIER);

        Fireball fb = adapter.spawnFireball(
                shooter, loc,
                kbH, kbV,
                power, breakBlocks,
                makeFire,
                dmgSelf, dmgOthers,
                speed
        );
        Debug.log(
                "&e[FireballService] launched fireball by " + shooter.getName(),
                config.getConfigBoolean("debug")
        );
        return fb;
    }
}
