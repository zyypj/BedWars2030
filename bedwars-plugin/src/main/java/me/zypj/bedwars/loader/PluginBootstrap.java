package me.zypj.bedwars.loader;

import com.google.common.base.Stopwatch;
import lombok.Getter;
import me.zypj.bedwars.api.factory.visual.hologram.service.HologramService;
import me.zypj.bedwars.api.logger.Debug;

@Getter
public class PluginBootstrap {

    private HologramService hologramService;

    public void init() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Debug.log("", true);
        Debug.log("&eLoading instances...", true);

        loadServices();

        Debug.log("&aInstances loaded in " + stopwatch.stop() + "!", true);
        Debug.log("", true);
    }

    public void loadServices() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Debug.log("", true);
        Debug.log("&eLoading API services...", true);

        hologramService = new HologramService();

        Debug.log("&aAPI services loaded in " + stopwatch.stop() + "!", true);
        Debug.log("", true);

    }
}
