package me.zypj.bedwars.api.file;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class YAML extends YamlConfiguration {

    private final File configFile;
    private final JavaPlugin plugin;

    public YAML(String name, JavaPlugin plugin, File folder) throws IOException {
        this.plugin = plugin;
        File dir = folder != null ? folder : plugin.getDataFolder();
        if (!dir.exists() && !dir.mkdirs()) throw new IOException("Could not create folder " + dir.getAbsolutePath());

        String fileName = name.endsWith(".yml") ? name : name + ".yml";
        this.configFile = new File(dir, fileName);
    }

    public YAML(String name, JavaPlugin plugin) throws IOException {
        this(name, plugin, null);
    }

    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            String folderName = configFile.getParentFile().getName();
            String resourcePath = folderName + "/" + configFile.getName();
            InputStream resource = plugin.getResource(resourcePath);

            if (resource == null) {
                resource = plugin.getResource(configFile.getName());
            }

            if (resource != null) {
                try {
                    copyResource(resource, configFile);
                } catch (IOException e) {
                    logError(
                            "Error saving default resource to file " + configFile.getName(),
                            e);
                }
            } else {
                try {
                    File parent = configFile.getParentFile();
                    if (!parent.exists() && !parent.mkdirs()) {
                        logError(
                                "Could not create directory for file "
                                        + configFile.getName(),
                                new IOException("mkdirs return false"));
                    }
                    if (!configFile.createNewFile()) {
                        logError(
                                "Could not create file " + configFile.getName(),
                                new IOException("createNewFile return false"));
                    }
                } catch (IOException e) {
                    logError("Could not create file " + configFile.getName(), e);
                }
            }
        }
    }

    public void backup(String suffix) {
        File backup =
                new File(configFile.getParent(), configFile.getName() + "." + suffix + ".backup");
        try {
            Files.copy(configFile.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            plugin.getLogger()
                    .warning("Unable to create backup of: " + configFile.getName());
        }
    }

    @Override
    public void save(File file) throws IOException {
        super.save(configFile);
    }

    public void save() {
        try {
            super.save(configFile);
        } catch (IOException e) {
            logError("Error saving file " + configFile.getName(), e);
        }
    }

    public void reload() {
        try {
            loadConfig();
        } catch (Exception e) {
            logError("Error reloading file " + configFile.getName(), e);
        }
    }

    public void set(String path, Object value, boolean save) {
        super.set(path, value);
        if (save) {
            save();
        }
    }

    public void setDefault(String path, Object value) {
        if (!contains(path)) {
            set(path, value);
            save();
        }
    }

    public void createDefaults() {
        saveDefaultConfig();
    }

    public boolean exists() {
        return configFile.exists();
    }

    public boolean delete() {
        return configFile.delete();
    }

    public String getString(String path, boolean translateColors) {
        String raw = super.getString(path);
        if (raw == null) return null;
        return translateColors ? ChatColor.translateAlternateColorCodes('&', raw) : raw;
    }

    public List<String> getStringList(String path, boolean translateColors) {
        List<String> list = super.getStringList(path);
        if (!translateColors) return list;
        return list.stream()
                .map(s -> ChatColor.translateAlternateColorCodes('&', s))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public <T> T getOrSet(String path, T defaultValue) {
        if (!contains(path)) {
            set(path, defaultValue);
            save();
        }
        return (T) get(path);
    }

    public String getFormatted(String path, Object... args) {
        String raw = getString(path, true);
        return raw != null ? String.format(raw, args) : null;
    }

    public Set<String> getKeysRecursive(String path) {
        if (getConfigurationSection(path) == null) {
            return Collections.emptySet();
        }
        return getConfigurationSection(path).getKeys(true);
    }

    private void loadConfig() throws IOException, InvalidConfigurationException {
        if (!configFile.exists()) {
            String folderName = configFile.getParentFile().getName();
            String resourcePath = folderName + "/" + configFile.getName();
            InputStream resource = plugin.getResource(resourcePath);

            if (resource == null) resource = plugin.getResource(configFile.getName());

            if (resource != null) {
                copyResource(resource, configFile);
            } else if (!configFile.createNewFile()) {
                throw new IOException(
                        "Could not create config file " + configFile.getAbsolutePath());
            }
        }

        load(configFile);
    }

    private void copyResource(InputStream in, File dest) throws IOException {
        Files.copy(in, dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        in.close();
    }

    private void logError(String message, Throwable throwable) {
        plugin.getServer().getConsoleSender().sendMessage("§c[YAML] " + message);
        throwable.printStackTrace();
    }
}
