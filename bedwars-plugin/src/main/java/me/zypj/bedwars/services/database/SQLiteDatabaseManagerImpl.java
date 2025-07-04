package me.zypj.bedwars.services.database;

import me.zypj.bedwars.api.database.DatabaseManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDatabaseManagerImpl implements DatabaseManager {
    private final String url;

    public SQLiteDatabaseManagerImpl(String filePath) {
        this.url = "jdbc:sqlite:" + filePath;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    @Override
    public void close() {}
}
