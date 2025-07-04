package me.zypj.bedwars.common.adapter;

import me.zypj.bedwars.common.file.YAML;
import me.zypj.bedwars.common.logger.Debug;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class LanguageFileAdapter {

    private static final List<String> DEFAULT_LANGUAGES = Arrays.asList("en", "pt");

    private final JavaPlugin plugin;
    private final File folder;

    public LanguageFileAdapter(JavaPlugin plugin) {
        this.plugin = plugin;
        this.folder = new File(plugin.getDataFolder(), "Languages");
    }

    public void createFiles() {
        if (!folder.exists() && !folder.mkdirs()) {
            Debug.log("&cCould not create 'Languages' folder at " + folder.getAbsolutePath(), false);
            return;
        }
        for (String iso : DEFAULT_LANGUAGES) {
            try {
                YAML yaml = new YAML("messages_" + iso, plugin, folder);
                yaml.saveDefaultConfig();
            } catch (IOException e) {
                Debug.log("§cFailed to create messages_" + iso + ".yml: " + e.getMessage(), false);
            }
        }
    }

    public List<String> getAvailableLanguages() {
        File[] files = folder.listFiles((d, n) -> n.startsWith("messages_") && n.endsWith(".yml"));
        if (files == null) return Collections.emptyList();
        return Arrays.stream(files)
                .map(f -> f.getName()
                        .substring("messages_".length(), f.getName().length() - ".yml".length())
                        .toLowerCase()
                )
                .collect(Collectors.toList());
    }

    public void reload() {
        File[] files = folder.listFiles((d, n) -> n.startsWith("messages_") && n.endsWith(".yml"));
        if (files == null) return;

        for (File f : files) {
            String name = f.getName();
            String iso = name.substring("messages_".length(), name.length() - ".yml".length()).toLowerCase();
            try {
                YAML yaml = new YAML("messages_" + iso, plugin, folder);
                yaml.reload();
            } catch (IOException e) {
                Debug.log("§cFailed to reload " + name + ": " + e.getMessage(), false);
            }
        }
    }
}
