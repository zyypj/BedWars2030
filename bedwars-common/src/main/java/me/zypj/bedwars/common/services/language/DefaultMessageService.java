package me.zypj.bedwars.common.services.language;

import me.zypj.bedwars.api.adapter.ConfigAdapterAPI;
import me.zypj.bedwars.api.language.LanguagePreferenceService;
import me.zypj.bedwars.api.language.MessageService;
import me.zypj.bedwars.common.adapter.LanguageFileAdapter;
import me.zypj.bedwars.common.file.YAML;
import me.zypj.bedwars.common.logger.Debug;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultMessageService implements MessageService {

    private final Map<String, YAML> languages;
    private final String defaultIso;
    private final LanguagePreferenceService languagePreferenceService;

    public DefaultMessageService(JavaPlugin plugin,
                                 ConfigAdapterAPI configAdapter,
                                 LanguageFileAdapter languageFileAdapter,
                                 LanguagePreferenceService languagePreferenceService) {
        this.languagePreferenceService = languagePreferenceService;

        languageFileAdapter.createFiles();

        String configDefault = Optional.ofNullable(configAdapter.getDefaultLanguage())
                .orElse("en")
                .toLowerCase();

        Set<String> disabled = configAdapter.getDisabledLanguages()
                .stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        File folder = new File(plugin.getDataFolder(), "Languages");
        File[] files = folder.listFiles((d, n) -> n.startsWith("messages_") && n.endsWith(".yml"));

        this.languages = Optional.ofNullable(files)
                .map(Arrays::stream).orElseGet(Stream::empty)
                .map(f -> {
                    String iso = f.getName()
                            .substring("messages_".length(), f.getName().length() - ".yml".length())
                            .toLowerCase();
                    if (disabled.contains(iso)) return null;
                    try {
                        YAML yaml = new YAML("messages_" + iso, plugin, folder);
                        yaml.reload();
                        return new SimpleEntry<>(iso, yaml);
                    } catch (IOException e) {
                        Debug.log("§cFailed to load messages_" + iso + ".yml: " + e.getMessage(), false);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));

        String fallback = languages.keySet().stream().findFirst().orElse(configDefault);
        this.defaultIso = Stream.of(configDefault, "en", fallback)
                .filter(languages::containsKey)
                .findFirst()
                .orElse(configDefault);
    }

    @Override
    public String getMessage(Player player, String path) {
        String iso = languagePreferenceService.getLanguage(player.getUniqueId())
                .orElse(defaultIso)
                .toLowerCase();
        return getMessage(iso, path);
    }

    @Override
    public String getMessage(String iso, String path) {
        YAML yaml = languages.getOrDefault(iso.toLowerCase(), languages.get(defaultIso));
        return yaml.getString(path, true);
    }
}
