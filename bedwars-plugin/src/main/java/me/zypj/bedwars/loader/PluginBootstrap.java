package me.zypj.bedwars.loader;

import com.google.common.base.Stopwatch;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.zypj.bedwars.BedWarsPlugin;
import me.zypj.bedwars.api.factory.visual.hologram.service.HologramService;
import me.zypj.bedwars.api.file.service.ConfigService;
import me.zypj.bedwars.api.logger.Debug;

@Getter
@RequiredArgsConstructor
public class PluginBootstrap {

    private final BedWarsPlugin plugin;
    private ConfigService configService;
    private HologramService hologramService;

    public void init() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Debug.log("", true);
        Debug.log("&eLoading instances...", true);

        plugin.saveDefaultConfig();
        loadServices();

        Debug.log("&aInstances loaded in " + stopwatch.stop() + "!", true);
        Debug.log("", true);
    }

    public void loadServices() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Debug.log("", true);
        Debug.log("&eLoading API services...", true);

        configService = new ConfigService(plugin);
        hologramService = new HologramService();

        Debug.log("&aAPI services loaded in " + stopwatch.stop() + "!", true);
        Debug.log("", true);

    }
}
