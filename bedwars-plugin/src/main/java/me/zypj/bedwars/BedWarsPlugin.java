package me.zypj.bedwars;

import com.google.common.base.Stopwatch;
import lombok.Getter;
import me.zypj.bedwars.command.LanguageCommand;
import me.zypj.bedwars.common.logger.Debug;
import me.zypj.bedwars.command.MainCommand;
import me.zypj.bedwars.listener.explosive.FireballListener;
import me.zypj.bedwars.listener.explosive.TntListener;
import me.zypj.bedwars.listener.misc.player.PlayerMiscListener;
import me.zypj.bedwars.listener.misc.server.ServerMiscListener;
import me.zypj.bedwars.listener.projectil.EggBridgeListener;
import me.zypj.bedwars.loader.PluginBootstrap;
import org.bukkit.event.Listener;
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

        loadListeners();
        loadCommands();

        Debug.log("&2BedWars2030 started in " + stopwatch.stop() + "!", false);
        Debug.log("", false);
    }

    @Override
    public void onDisable() {
        pluginBootstrap.shutdown();
        Debug.log("&aPlugin off, thanks for the use! Made with great affection by tadeu (@zypj)", false);
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

    private void loadListeners() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Debug.log("", true);
        Debug.log("&eLoading listeners...", true);

        registerListeners(
                new TntListener(this),
                new FireballListener(this),
                new EggBridgeListener(this),
                new PlayerMiscListener(this),
                new ServerMiscListener()
        );

        Debug.log("&aListeners loaded in " + stopwatch.stop() + "!", true);
    }

    private void loadCommands() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Debug.log("", true);
        Debug.log("&eLoading commands...", true);

        getCommand("bedwars").setExecutor(new MainCommand(this));
        getCommand("language").setExecutor(new LanguageCommand(this));

        Debug.log("&aCommands loaded in " + stopwatch.stop() + "!", true);
        Debug.log("", true);
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}
