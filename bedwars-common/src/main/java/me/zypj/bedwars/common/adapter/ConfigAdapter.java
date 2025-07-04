package me.zypj.bedwars.common.adapter;

import me.zypj.bedwars.api.adapter.ConfigAdapterAPI;
import me.zypj.bedwars.common.enums.DatabaseType;
import me.zypj.bedwars.common.model.config.DatabaseConfig;
import me.zypj.bedwars.common.model.config.MySQLConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.stream.Collectors;

public class ConfigAdapter implements ConfigAdapterAPI {

    private final JavaPlugin plugin;
    private final FileConfiguration configuration;

    public ConfigAdapter(JavaPlugin plugin) {
        this.plugin = plugin;
        this.configuration = plugin.getConfig();
    }

    public String getDefaultLanguage() {
        return plugin.getConfig()
                .getString("language");
    }

    public List<String> getDisabledLanguages() {
        return plugin.getConfig()
                .getStringList("disabled-languages")
                .stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    public DatabaseConfig getDatabaseConfig() {
        String raw = configuration.getString("database.type", "SQLite");
        DatabaseType type = DatabaseType.valueOf(raw.toUpperCase());

        MySQLConfig mysql = new MySQLConfig(
                configuration.getString("database.mysql.host"),
                configuration.getInt("database.mysql.port"),
                configuration.getString("database.mysql.database"),
                configuration.getString("database.mysql.username"),
                configuration.getString("database.mysql.password")
        );

        return new DatabaseConfig(type, mysql);
    }
}
