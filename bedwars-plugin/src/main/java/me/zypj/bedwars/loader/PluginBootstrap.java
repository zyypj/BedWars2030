package me.zypj.bedwars.loader;

import com.google.common.base.Stopwatch;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.zypj.bedwars.BedWarsPlugin;
import me.zypj.bedwars.systems.projectil.eggbridge.service.EggBridgeService;
import me.zypj.bedwars.systems.visual.hologram.service.HologramService;
import me.zypj.bedwars.common.file.service.ConfigService;
import me.zypj.bedwars.common.logger.Debug;
import me.zypj.bedwars.command.manager.SubInstanceManager;
import me.zypj.bedwars.systems.explosive.tnt.service.TntService;
import me.zypj.bedwars.systems.explosive.fireball.service.FireballService;

@Getter
@RequiredArgsConstructor
public class PluginBootstrap {

    private final BedWarsPlugin plugin;

    private ConfigService configService;
    private HologramService hologramService;

    private TntService tntService;
    private FireballService fireballService;

    private EggBridgeService eggBridgeService;

    private SubInstanceManager subInstanceManager;

    public void init() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Debug.log("", true);
        Debug.log("&eLoading instances...", true);

        plugin.saveDefaultConfig();
        loadFiles();
        loadServices();
        loadSubInstances();

        Debug.log("&aInstances loaded in " + stopwatch.stop() + "!", true);
        Debug.log("", true);
    }

    private void loadFiles() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Debug.log("", true);
        Debug.log("&eLoading files ...", true);

        plugin.saveDefaultConfig();

        Debug.log("&aFiles loaded in " + stopwatch.stop() + "!", true);
        Debug.log("", true);
    }

    private void loadServices() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Debug.log("", true);
        Debug.log("&eLoading API services...", true);

        configService = new ConfigService(plugin);
        hologramService = new HologramService();

        tntService = new TntService(plugin);
        fireballService = new FireballService(plugin);

        eggBridgeService = new EggBridgeService(plugin);

        Debug.log("&aAPI services loaded in " + stopwatch.stop() + "!", true);
        Debug.log("", true);
    }

    private void loadSubInstances() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Debug.log("", true);
        Debug.log("&eLoading SubCommands instances...", true);

        subInstanceManager = new SubInstanceManager(plugin);
        subInstanceManager.init();

        Debug.log("&aSubCommands instances loaded in " + stopwatch.stop() + "!", true);
        Debug.log("", true);
    }
}
