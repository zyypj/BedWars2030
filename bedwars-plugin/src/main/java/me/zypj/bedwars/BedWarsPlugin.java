package me.zypj.bedwars;

import com.google.common.base.Stopwatch;
import lombok.Getter;
import me.zypj.bedwars.api.logger.Debug;
import me.zypj.bedwars.command.MainCommand;
import me.zypj.bedwars.loader.PluginBootstrap;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class BedWarsPlugin extends JavaPlugin {

    private PluginBootstrap pluginBootstrap;

    @Override
    public void onEnable() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Debug.log("", false);
        Debug.log("&aStarting BedWars2030...", false);

        loadServices();

        Debug.log("&2BedWars2030 started in " + stopwatch.stop() + "!", false);
        Debug.log("", false);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void loadServices() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Debug.log("", true);
        Debug.log("&eLoading services...", true);

        Debug.setPlugin(this);
        pluginBootstrap = new PluginBootstrap(this);
        pluginBootstrap.init();

        Debug.log("&aServices loaded in " + stopwatch.stop() + "!", true);
    }
    private void loadCommands() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Debug.log("", true);
        Debug.log("&eLoading commands...", true);

        getCommand("bedwars").setExecutor(new MainCommand(this));

        Debug.log("&aCommands loaded in " + stopwatch.stop() + "!", true);
        Debug.log("", true);
    }
}
