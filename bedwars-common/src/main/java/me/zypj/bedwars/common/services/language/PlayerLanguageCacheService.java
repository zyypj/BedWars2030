package me.zypj.bedwars.common.services.language;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import me.zypj.bedwars.api.cache.CacheService;
import me.zypj.bedwars.api.database.DatabaseManager;
import me.zypj.bedwars.common.logger.Debug;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.sql.*;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerLanguageCacheService implements CacheService<UUID, String> {

    private final Cache<UUID, String> cache;
    private final DatabaseManager databaseManager;
    private final String defaultLanguage;
    private final BukkitTask saveTask;

    public PlayerLanguageCacheService(JavaPlugin plugin,
                                      DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.defaultLanguage = plugin.getConfig().getString("language", "en");

        this.cache = CacheBuilder.newBuilder()
                .maximumSize(10_000)
                .build();

        ensureTableExists();

        long ticks10m = TimeUnit.MINUTES.toSeconds(10) * 20L;
        this.saveTask = plugin.getServer()
                .getScheduler()
                .runTaskTimerAsynchronously(plugin, this::saveAllUnchecked, ticks10m, ticks10m);
    }

    private void ensureTableExists() {
        try (Connection conn = databaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS bw_player_preferences (" +
                            "player_id VARCHAR(36) PRIMARY KEY, " +
                            "language_iso VARCHAR(5) NOT NULL" +
                            ")"
            );
        } catch (SQLException e) {
            Debug.log("&cError creating preferences table: " + e.getMessage(), false);
        }
    }

    @Override
    public void put(UUID key, String value) {
        cache.put(key, value.toLowerCase());
    }

    @Override
    public Optional<String> get(UUID key) {
        String lang = cache.getIfPresent(key);
        if (lang != null) return Optional.of(lang);

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT language_iso FROM bw_player_preferences WHERE player_id = ?"
             )) {
            ps.setString(1, key.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    lang = rs.getString("language_iso");
                    cache.put(key, lang);
                    return Optional.of(lang);
                }
            }
        } catch (Exception e) {
            Debug.log("&cError loading language for " + key + ": " + e.getMessage(), false);
        }

        cache.put(key, defaultLanguage);
        return Optional.of(defaultLanguage);
    }

    @Override
    public void remove(UUID key) {
        cache.invalidate(key);
    }

    @Override
    public void saveAll() throws Exception {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "REPLACE INTO bw_player_preferences(player_id, language_iso) VALUES (?,?)"
             )) {
            for (Map.Entry<UUID, String> entry : cache.asMap().entrySet()) {
                ps.setString(1, entry.getKey().toString());
                ps.setString(2, entry.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    @Override
    public void shutdown() throws Exception {
        if (saveTask != null) saveTask.cancel();
        saveAll();
    }

    private void saveAllUnchecked() {
        try {
            saveAll();
        } catch (Exception e) {
            Debug.log("&cError saving player languages: " + e.getMessage(), false);
        }
    }
}
