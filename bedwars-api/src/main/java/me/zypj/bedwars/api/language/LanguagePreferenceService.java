package me.zypj.bedwars.api.language;

import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public interface LanguagePreferenceService {
    Optional<String> getLanguage(UUID playerId);
    Optional<String> getLanguage(Player player);

    void setLanguage(UUID playerId, String iso);
    void setLanguage(Player player, String iso);

    void saveAll() throws Exception;

    void shutdown() throws Exception;
}
