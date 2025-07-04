package me.zypj.bedwars.services;

import me.zypj.bedwars.api.database.DatabaseManager;
import me.zypj.bedwars.api.language.LanguagePreferenceService;
import me.zypj.bedwars.api.language.MessageService;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BedWarsServices {

    private static JavaPlugin plugin;
    private static ServicesManager services;

    private BedWarsServices() {
    }

    public static void init(JavaPlugin javaPlugin) {
        plugin = javaPlugin;
        services = plugin.getServer().getServicesManager();
    }

    public static <T> void register(Class<T> clazz, T instance) {
        services.register(clazz, instance, plugin, ServicePriority.High);
    }

    public static DatabaseManager getDatabaseManager() {
        return services.load(DatabaseManager.class);
    }

    public static LanguagePreferenceService getLanguagePreferenceService() {
        return services.load(LanguagePreferenceService.class);
    }

    public static MessageService getMessageService() {
        return services.load(MessageService.class);
    }
}
