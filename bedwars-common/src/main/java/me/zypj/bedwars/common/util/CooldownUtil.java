package me.zypj.bedwars.common.util;

import me.zypj.bedwars.api.language.MessageService;
import me.zypj.bedwars.common.file.path.MessagePath;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public final class CooldownUtil {

    private static MessageService messageService;
    private static final Map<UUID, Map<String, Long>> expirations = new ConcurrentHashMap<>();

    private CooldownUtil() {}

    public static void init(MessageService service) {
        if (service == null) throw new IllegalArgumentException("MessageService cannot be null");

        messageService = service;
    }

    public static boolean tryCooldown(Player player, String key, long durationSeconds) {
        ensureInitialized();
        UUID id = player.getUniqueId();
        long now = System.currentTimeMillis();

        Map<String, Long> userMap = expirations.computeIfAbsent(id, k -> new ConcurrentHashMap<>());

        Long expiresAt = userMap.get(key);
        if (expiresAt != null && now < expiresAt) {
            long remaining = TimeUnit.MILLISECONDS.toSeconds(expiresAt - now);
            String msg = messageService.getMessage(player, MessagePath.YOU_ARE_COOLDOWN)
                    .replace("{TIME}", String.valueOf(remaining));
            player.sendMessage(msg);
            return false;
        }

        userMap.put(key, now + TimeUnit.SECONDS.toMillis(durationSeconds));
        return true;
    }

    public static long getRemaining(Player player, String key) {
        ensureInitialized();
        UUID id = player.getUniqueId();
        Map<String, Long> userMap = expirations.get(id);
        if (userMap == null) return 0;

        Long expiresAt = userMap.get(key);
        if (expiresAt == null) return 0;

        long rem = TimeUnit.MILLISECONDS.toSeconds(expiresAt - System.currentTimeMillis());
        if (rem <= 0) {
            userMap.remove(key);
            return 0;
        }
        return rem;
    }

    public static void clear(Player player, String key) {
        ensureInitialized();
        Map<String, Long> userMap = expirations.get(player.getUniqueId());
        if (userMap != null) {
            userMap.remove(key);
        }
    }

    public static void clearAll(Player player) {
        ensureInitialized();
        expirations.remove(player.getUniqueId());
    }

    private static void ensureInitialized() {
        if (messageService == null)
            throw new IllegalStateException("CooldownUtil not initialized. Call init() first.");
    }
}
