package me.zypj.bedwars.services.language;

import me.zypj.bedwars.api.language.LanguagePreferenceService;
import me.zypj.bedwars.common.services.language.PlayerLanguageCacheService;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class LanguagePreferenceServiceImpl implements LanguagePreferenceService {

    private final PlayerLanguageCacheService cacheService;

    public LanguagePreferenceServiceImpl(PlayerLanguageCacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public Optional<String> getLanguage(UUID playerId) {
        return cacheService.get(playerId);
    }

    @Override
    public Optional<String> getLanguage(Player player) {
        return getLanguage(player.getUniqueId());
    }

    @Override
    public void setLanguage(UUID playerId, String iso) {
        cacheService.put(playerId, iso.toLowerCase());
    }

    @Override
    public void setLanguage(Player player, String iso) {
        setLanguage(player.getUniqueId(), iso);
    }

    @Override
    public void saveAll() throws Exception {
        cacheService.saveAll();
    }

    @Override
    public void shutdown() throws Exception {
        cacheService.shutdown();
    }
}
