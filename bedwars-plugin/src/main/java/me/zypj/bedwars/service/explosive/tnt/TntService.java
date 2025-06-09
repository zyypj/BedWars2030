package me.zypj.bedwars.service.explosive.tnt;

import lombok.Getter;
import me.zypj.bedwars.BedWarsPlugin;
import me.zypj.bedwars.api.factory.explosive.tnt.adapter.TntAdapter;
import me.zypj.bedwars.api.factory.explosive.tnt.adapter.provider.TntAdapterProvider;
import me.zypj.bedwars.api.file.path.ConfigPath;
import me.zypj.bedwars.api.file.service.ConfigService;
import me.zypj.bedwars.api.logger.Debug;
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
        double kbHorizontal = config.getConfigDouble(ConfigPath.TNT_KNOCKBACK_HORIZONTAL);
        double kbVertical = config.getConfigDouble(ConfigPath.TNT_KNOCKBACK_VERTICAL);
        float power = (float) config.getConfigDouble(ConfigPath.TNT_EXPLOSION_POWER);
        boolean breakBlocks = config.getConfigBoolean(ConfigPath.TNT_EXPLOSION_BREAK_BLOCKS);
        double dmgSelf = config.getConfigDouble(ConfigPath.TNT_DAMAGE_SELF);
        double dmgOthers = config.getConfigDouble(ConfigPath.TNT_DAMAGE_OTHERS);
        int fuse = config.getConfigInt(ConfigPath.TNT_FUSE_TICK);

        TNTPrimed tnt = adapter.spawnTnt(
                shooter, loc,
                kbHorizontal, kbVertical,
                power, breakBlocks,
                dmgSelf, dmgOthers,
                fuse
        );

        Debug.log("&e[TNTService] spawned TNT at " + loc, true);
        return tnt;
    }
}
