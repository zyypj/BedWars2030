package me.zypj.bedwars.common.services.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import lombok.var;
import me.zypj.bedwars.api.cache.CacheService;
import me.zypj.bedwars.api.database.DatabaseManager;
import me.zypj.bedwars.common.logger.Debug;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.sql.Connection;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Getter
public abstract class AbstractCacheService<K, V> implements CacheService<K, V> {

    protected final Cache<K, V> cache;

    private final JavaPlugin plugin;
    private final DatabaseManager databaseManager;
    private BukkitTask saveTask;

    public AbstractCacheService(JavaPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;

        this.cache = CacheBuilder.newBuilder()
                .maximumSize(10_000)
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .build();

        startAutoSave();
    }

    private void startAutoSave() {
        long ticks10m = 20L * 60 * 10;
        saveTask = plugin.getServer()
                .getScheduler()
                .runTaskTimerAsynchronously(plugin, () -> {
                    try {
                        saveAll();
                    } catch (Exception e) {
                        Debug.log("&cError saving cache: " + e.getMessage(), false);
                    }
                }, ticks10m, ticks10m);
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public Optional<V> get(K key) {
        return Optional.ofNullable(cache.getIfPresent(key));
    }

    @Override
    public void remove(K key) {
        cache.invalidate(key);
    }

    @Override
    public void saveAll() throws Exception {
        try (Connection conn = databaseManager.getConnection()) {
            for (var entry : cache.asMap().entrySet()) {
                persist(entry.getKey(), entry.getValue(), conn);
            }
        }
    }

    @Override
    public void shutdown() throws Exception {
        if (saveTask != null) {
            saveTask.cancel();
        }
        saveAll();
    }

    protected abstract void persist(K key, V value, Connection conn) throws Exception;
}
