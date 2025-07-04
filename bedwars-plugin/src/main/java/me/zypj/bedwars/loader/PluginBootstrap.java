package me.zypj.bedwars.loader;

import com.google.common.base.Stopwatch;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.zypj.bedwars.BedWarsPlugin;
import me.zypj.bedwars.api.language.MessageService;
import me.zypj.bedwars.common.services.language.DefaultMessageService;
import me.zypj.bedwars.common.util.CooldownUtil;
import me.zypj.bedwars.common.util.DatabaseMigrator;
import me.zypj.bedwars.services.BedWarsServices;
import me.zypj.bedwars.api.language.LanguagePreferenceService;
import me.zypj.bedwars.common.adapter.ConfigAdapter;
import me.zypj.bedwars.api.database.DatabaseManager;
import me.zypj.bedwars.common.adapter.LanguageFileAdapter;
import me.zypj.bedwars.common.enums.DatabaseType;
import me.zypj.bedwars.common.model.config.DatabaseConfig;
import me.zypj.bedwars.common.services.language.PlayerLanguageCacheService;
import me.zypj.bedwars.services.database.MySQLDatabaseManagerImpl;
import me.zypj.bedwars.services.database.SQLiteDatabaseManagerImpl;
import me.zypj.bedwars.services.language.LanguagePreferenceServiceImpl;
import me.zypj.bedwars.systems.projectil.eggbridge.service.EggBridgeService;
import me.zypj.bedwars.systems.visual.hologram.service.HologramService;
import me.zypj.bedwars.common.file.service.ConfigService;
import me.zypj.bedwars.common.logger.Debug;
import me.zypj.bedwars.command.manager.SubInstanceManager;
import me.zypj.bedwars.systems.explosive.tnt.service.TntService;
import me.zypj.bedwars.systems.explosive.fireball.service.FireballService;

import java.io.File;
import java.sql.SQLException;

@Getter
@RequiredArgsConstructor
public class PluginBootstrap {

    private final BedWarsPlugin plugin;

    private ConfigAdapter configAdapter;
    private LanguageFileAdapter languageFileAdapter;

    private ConfigService configService;
    private HologramService hologramService;

    private TntService tntService;
    private FireballService fireballService;
    private EggBridgeService eggBridgeService;

    private PlayerLanguageCacheService playerLanguageCacheService;
    private LanguagePreferenceService languagePreferenceService;

    private MessageService messageService;

    private SubInstanceManager subInstanceManager;

    private DatabaseManager databaseManager;

    public void init() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Debug.log("", true);
        Debug.log("&eLoading instances...", true);

        loadFiles();

        loadDatabase();

        loadServices();
        loadSubInstances();
        registerServices();

        setupUtils();

        Debug.log("&aInstances loaded in " + stopwatch.stop() + "!", true);
        Debug.log("", true);
    }

    private void loadFiles() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Debug.log("", true);
        Debug.log("&eLoading files ...", true);

        plugin.saveDefaultConfig();
        configAdapter = new ConfigAdapter(plugin);

        languageFileAdapter = new LanguageFileAdapter(plugin);
        languageFileAdapter.createFiles();

        Debug.log("&aFiles loaded in " + stopwatch.stop() + "!", true);
        Debug.log("", true);
    }

    private void loadDatabase() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Debug.log("", true);
        Debug.log("&eLoading database...", true);

        DatabaseConfig databaseConfig = configAdapter.getDatabaseConfig();
        try {
            if (databaseConfig.getType() == DatabaseType.MYSQL) {
                databaseManager = new MySQLDatabaseManagerImpl(databaseConfig.getMysql());

                File sqLiteFile = new File(plugin.getDataFolder(), "bedwars.db");
                if (sqLiteFile.exists()) {
                    DatabaseManager sqliteMgr = new SQLiteDatabaseManagerImpl(sqLiteFile.getAbsolutePath());
                    DatabaseMigrator.migratePreferences(sqLiteFile, sqliteMgr, databaseManager);
                    sqliteMgr.getConnection().close();
                }
            } else {
                String file = plugin.getDataFolder() + "/bedwars.db";
                databaseManager = new SQLiteDatabaseManagerImpl(file);
            }
            databaseManager.getConnection().close();
        } catch (SQLException ex) {
            Debug.log("&cUnable to connect to DB: " + ex.getMessage(), true);
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

        Debug.log("&aDatabase loaded in " + stopwatch.stop() + "!", true);
        Debug.log("", true);
    }

    private void loadServices() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Debug.log("", true);
        Debug.log("&eLoading API services...", true);

        configService = new ConfigService(plugin);
        hologramService = new HologramService();

        tntService = new TntService(plugin);
        fireballService = new FireballService(plugin);
        eggBridgeService = new EggBridgeService(plugin);

        playerLanguageCacheService = new PlayerLanguageCacheService(plugin, databaseManager);
        languagePreferenceService = new LanguagePreferenceServiceImpl(playerLanguageCacheService);

        messageService = new DefaultMessageService(plugin, configAdapter, languageFileAdapter, languagePreferenceService);

        Debug.log("&aAPI services loaded in " + stopwatch.stop() + "!", true);
        Debug.log("", true);
    }

    private void loadSubInstances() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Debug.log("", true);
        Debug.log("&eLoading SubCommands instances...", true);

        subInstanceManager = new SubInstanceManager(plugin);
        subInstanceManager.init();

        Debug.log("&aSubCommands instances loaded in " + stopwatch.stop() + "!", true);
        Debug.log("", true);
    }

    private void registerServices() {
        BedWarsServices.init(plugin);

        BedWarsServices.register(DatabaseManager.class, databaseManager);
        BedWarsServices.register(LanguagePreferenceService.class, languagePreferenceService);
        BedWarsServices.register(MessageService.class, messageService);
    }

    private void setupUtils() {
        CooldownUtil.init(messageService);
    }

    public void shutdown() {
        try {
            languagePreferenceService.shutdown();
            databaseManager.close();
            Debug.log("&aServices closed", true);
        } catch (Exception ex) {
            Debug.log("&cError closing services: " + ex.getMessage(), true);
        }
    }
}
