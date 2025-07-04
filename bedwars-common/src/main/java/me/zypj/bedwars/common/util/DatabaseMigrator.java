package me.zypj.bedwars.common.util;

import me.zypj.bedwars.api.database.DatabaseManager;
import me.zypj.bedwars.common.logger.Debug;

import java.io.File;
import java.sql.*;

public class DatabaseMigrator {

    private DatabaseMigrator() {}

    public static void migratePreferences(File sqliteFile,
                                          DatabaseManager sqliteManager,
                                          DatabaseManager mysqlManager) {
        if (!sqliteFile.exists()) return;

        Debug.log("§eMigrating SQLite data to MySQL...", true);
        String selectSql = "SELECT player_id, language_iso FROM bw_player_preferences";
        String insertSql = "REPLACE INTO bw_player_preferences(player_id, language_iso) VALUES (?,?)";

        try (Connection sqliteConn = sqliteManager.getConnection();
             Connection mysqlConn = mysqlManager.getConnection();
             PreparedStatement selectPs = sqliteConn.prepareStatement(selectSql);
             PreparedStatement insertPs = mysqlConn.prepareStatement(insertSql);
             ResultSet rs = selectPs.executeQuery()
        ) {
            mysqlConn.setAutoCommit(false);

            while (rs.next()) {
                insertPs.setString(1, rs.getString("player_id"));
                insertPs.setString(2, rs.getString("language_iso"));
                insertPs.addBatch();
            }
            insertPs.executeBatch();
            mysqlConn.commit();

            Debug.log("§aMigration completed, deleting SQLite file...", true);
            if (!sqliteFile.delete()) Debug.log("§cCould not delete " + sqliteFile.getAbsolutePath(), false);
        } catch (Exception e) {
            Debug.log("&cError migrating preferences: " + e.getMessage(), false);
        }
    }
}
