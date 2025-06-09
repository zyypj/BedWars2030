package me.zypj.bedwars.api.file.service;

import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@RequiredArgsConstructor
public class ConfigService {

    private final JavaPlugin plugin;

    public String getConfigString(String path) {
        return plugin.getConfig().getString(path);
    }

    public List<String> getConfigStringList(String path) {
        return plugin.getConfig().getStringList(path);
    }

    public double getConfigDouble(String path) {
        return plugin.getConfig().getDouble(path);
    }

    public boolean getConfigBoolean(String path) {
        return plugin.getConfig().getBoolean(path);
    }

    public int getConfigInt(String path) {
        return plugin.getConfig().getInt(path);
    }
}
