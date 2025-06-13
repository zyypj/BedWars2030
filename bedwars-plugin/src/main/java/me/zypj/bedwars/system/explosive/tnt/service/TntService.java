package me.zypj.bedwars.system.explosive.tnt.service;

import lombok.Getter;
import me.zypj.bedwars.BedWarsPlugin;
import me.zypj.bedwars.api.event.explosive.TntUseEvent;
import me.zypj.bedwars.common.file.path.ConfigPath;
import me.zypj.bedwars.common.file.service.ConfigService;
import me.zypj.bedwars.common.logger.Debug;
import me.zypj.bedwars.system.explosive.tnt.adapter.TntAdapter;
import me.zypj.bedwars.system.explosive.tnt.adapter.provider.TntAdapterProvider;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;

@Getter
public class TntService {

    private final BedWarsPlugin plugin;
    private final ConfigService config;
    private final TntAdapter adapter;

    public TntService(BedWarsPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getPluginBootstrap().getConfigService();
        this.adapter = TntAdapterProvider.getAdapter();
    }

    public TNTPrimed spawnTnt(Player shooter, Location loc) {
        Settings s = loadSettings();

        TNTPrimed tnt = adapter.spawnTnt(
                shooter, loc,
                s.kbHorizontal,
                s.kbVertical,
                s.power,
                s.breakBlocks,
                s.dmgSelf,
                s.dmgOthers,
                s.fuseTicks
        );

        TntUseEvent event = new TntUseEvent(shooter, loc, tnt);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            tnt.remove();
            return null;
        }

        Debug.log(
                String.format("[TntService] spawned TNT by %s at %s", shooter.getName(), loc),
                true
        );
        return tnt;
    }

    private Settings loadSettings() {
        return new Settings(
                config.getConfigDouble(ConfigPath.TNT_KNOCKBACK_HORIZONTAL),
                config.getConfigDouble(ConfigPath.TNT_KNOCKBACK_VERTICAL),
                (float) config.getConfigDouble(ConfigPath.TNT_EXPLOSION_POWER),
                config.getConfigBoolean(ConfigPath.TNT_EXPLOSION_BREAK_BLOCKS),
                config.getConfigDouble(ConfigPath.TNT_DAMAGE_SELF),
                config.getConfigDouble(ConfigPath.TNT_DAMAGE_OTHERS),
                config.getConfigInt(ConfigPath.TNT_FUSE_TICK)
        );
    }

    private static class Settings {
        final double kbHorizontal;
        final double kbVertical;
        final float power;
        final boolean breakBlocks;
        final double dmgSelf;
        final double dmgOthers;
        final int fuseTicks;

        Settings(double kbHorizontal,
                 double kbVertical,
                 float power,
                 boolean breakBlocks,
                 double dmgSelf,
                 double dmgOthers,
                 int fuseTicks) {
            this.kbHorizontal = kbHorizontal;
            this.kbVertical = kbVertical;
            this.power = power;
            this.breakBlocks = breakBlocks;
            this.dmgSelf = dmgSelf;
            this.dmgOthers = dmgOthers;
            this.fuseTicks = fuseTicks;
        }
    }
}
