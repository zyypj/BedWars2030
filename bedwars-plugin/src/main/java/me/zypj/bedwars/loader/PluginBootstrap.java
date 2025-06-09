package me.zypj.bedwars.loader;

import com.google.common.base.Stopwatch;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.zypj.bedwars.BedWarsPlugin;
import me.zypj.bedwars.api.factory.visual.hologram.service.HologramService;
import me.zypj.bedwars.api.file.service.ConfigService;
import me.zypj.bedwars.api.logger.Debug;
import me.zypj.bedwars.command.manager.SubInstanceManager;
import me.zypj.bedwars.service.explosive.tnt.TntService;

@Getter
@RequiredArgsConstructor
public class PluginBootstrap {

    private final BedWarsPlugin plugin;

    private ConfigService configService;
    private HologramService hologramService;

    private TntService tntService;

    private SubInstanceManager subInstanceManager;

    public void init() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Debug.log("", true);
        Debug.log("&eLoading instances...", true);

        plugin.saveDefaultConfig();
        loadServices();
        loadSubInstances();

        Debug.log("&aInstances loaded in " + stopwatch.stop() + "!", true);
        Debug.log("", true);
    }

    private void loadServices() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Debug.log("", true);
        Debug.log("&eLoading API services...", true);

        configService = new ConfigService(plugin);
        hologramService = new HologramService();

        tntService = new TntService(plugin);

        Debug.log("&aAPI services loaded in " + stopwatch.stop() + "!", true);
        Debug.log("", true);
    }

    private void loadSubInstances() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Debug.log("", true);
        Debug.log("&eLoading SubCommands instances...", true);

        subInstanceManager = new SubInstanceManager(plugin);

        Debug.log("&aSubCommands instances loaded in " + stopwatch.stop() + "!", true);
        Debug.log("", true);
    }
}
